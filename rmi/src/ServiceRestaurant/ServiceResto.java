package ServiceRestaurant;

import activeRecord.Reservation;
import activeRecord.Restaurant;
import bd.Bd;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.rmi.RemoteException;
import java.rmi.server.RemoteServer;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class ServiceResto extends RemoteServer implements ServiceRestaurant {

    Bd bd;
    private ArrayList<Restaurant> restaurants;
    private String lastUpdate = "";

    ServiceResto(Bd bd) {
        this.bd = bd;
    }

    @Override
    public String getRestaurants() throws RemoteException, RuntimeException {

        String[] splittedDbName = bd.url.split("/");
        String databaseName = splittedDbName[splittedDbName.length -1];
        String tableName = "restaurant";

        String updateTimeQuery = "SELECT update_time FROM information_schema.tables " +
                "WHERE table_schema = ? " +
                "AND table_name = ?";

        try {
            ResultSet resultSet = bd.executeQuery(updateTimeQuery, databaseName, tableName);
            resultSet.next();
            String lastUpdate = resultSet.getString(1);
            resultSet.close();
            if(lastUpdate == null || !lastUpdate.equals(this.lastUpdate)) {
                this.lastUpdate = lastUpdate;
                this.restaurants = Restaurant.getAll(bd);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error while retrieving data, DB might be down");
        }

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(this.restaurants);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException("Error while processing JSON");
        }
    }

    public static void main (String[] args) throws SQLException, RemoteException {
        String url = "jdbc:mariadb://localhost:3306/miaam";
        Scanner sc = new Scanner(System.in);
        System.out.println("Connexion à la base de donnée :");
        System.out.print("-Utilisateur: ");
        String user = sc.nextLine();
        System.out.print("-Mot de passe: ");
        String password = sc.nextLine();

        //---Connexion à la base de donnée
        Bd bd = new Bd(url, user, password);

        ServiceResto resto = new ServiceResto(bd);
        System.out.println(resto.getRestaurants());
    }

    @Override
    public void reserverTable(String nom, String prenom, int nbpers, String telephone, int numrestau) throws RemoteException, RuntimeException {
        //TODO
        //Date de réservation & vérification de l'unicité des tâches
        //Sûrement à faire directement dans la méthode save
        Reservation reservation = new Reservation(nom, prenom, nbpers, telephone, numrestau);
        try {
            reservation.save(bd);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
