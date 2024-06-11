package ServiceRestaurant;

import activeRecord.Reservation;
import activeRecord.Restaurant;
import bd.Bd;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.rmi.RemoteException;
import java.rmi.server.RemoteServer;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.TreeMap;

public class ServiceResto extends RemoteServer implements ServiceRestaurant {

    Bd bd;
    private ArrayList<Restaurant> restaurants;
    private HashMap<Integer, Restaurant> restaurantHashMap;

    ServiceResto(Bd bd) {
        this.bd = bd;
    }

    public String getRestaurant(int index) throws RemoteException, RuntimeException {
        if(Restaurant.haveUpdated(bd)) {
            getRestaurants();
        }

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(restaurantHashMap.get(index));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException("Error while processing JSON");
        }
    }
    @Override
    public String getRestaurants() throws RemoteException, RuntimeException {
        try {
            if(Restaurant.haveUpdated(bd)) {
                this.restaurants = Restaurant.getAll(bd);
                this.restaurantHashMap = new HashMap<>();
                for (Restaurant restaurant : restaurants) {
                    restaurantHashMap.put(restaurant.getNumrestau(), restaurant);
                }
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
    public void reserverTable(String nom, String prenom, int nbpers, String telephone, int numrestau, Date date) throws RemoteException, RuntimeException {
        //TODO
        //Date de réservation & vérification de l'unicité des tâches
        //Sûrement à faire directement dans la méthode save
        Reservation reservation = new Reservation(nom, prenom, nbpers, telephone, numrestau, date);
        try {
            reservation.save(bd);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur au moment de réserver la table :" + e.getMessage());
        }

    }
}
