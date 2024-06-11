import activeRecord.Restaurant;
import bd.Bd;

public class Main {

    public static void main(String[] args) throws Exception {

        Bd bd = new Bd("jdbc:mariadb://localhost:3306/miaam", "user", "password");

        Restaurant r = new Restaurant(bd, 2);
        System.out.println(r);
        //r.delete(bd);
    }
}
