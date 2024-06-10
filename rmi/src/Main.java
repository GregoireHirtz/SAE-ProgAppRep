import activeRecord.Reservation;
import activeRecord.Restaurant;
import bd.Bd;

import java.rmi.RemoteException;

public class Main {

    public static void main(String[] args) throws Exception {

        Bd bd = new Bd("jdbc:mariadb://localhost:3306/miaam", "user", "password");

        Restaurant r = new Restaurant(bd, 1);
        System.out.println(r);
        r.delete(bd);
    }
}
