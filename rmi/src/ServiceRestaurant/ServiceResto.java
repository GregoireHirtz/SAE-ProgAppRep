package ServiceRestaurant;

import activeRecord.Reservation;
import activeRecord.Restaurant;
import bd.Bd;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.rmi.RemoteException;
import java.rmi.server.RemoteServer;
import java.sql.Timestamp;

public class ServiceResto extends RemoteServer implements ServiceRestaurant {

    Bd bd;

    ServiceResto(Bd bd) {
        this.bd = bd;
    }

    @Override
    public String getRestaurants() throws RemoteException, RuntimeException {

        // TODO
        // FETCH LES DONNEES
        Restaurant[] restaurants = new Restaurant[10]; // à fetch depuis la bd
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            return objectMapper.writeValueAsString(restaurants);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException("Error while processing JSON");
        }
    }

    @Override
    public void reserverTable(String nom, String prenom, int nbpers, String telephone, int numrestau) throws RemoteException, RuntimeException {
        //TODO
        //Date de réservation & vérification de l'unicité des tâches
        //Sûrement à faire directement dans la méthode save
        Reservation reservation = new Reservation(nom, prenom, nbpers, telephone, numrestau);
        reservation.save(bd);
    }
}
