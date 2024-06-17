import activeRecord.Restaurant;
import bd.Bd;

public class Main {

    public static void main(String[] args) throws Exception {

        Bd bd = new Bd("jdbc:mariadb://webetu.iutnc.univ-lorraine.fr:3306/hirtz44u", "hirtz44u", "password");

        Restaurant r = new Restaurant(bd, 2);
        System.out.println(r);
        //r.delete(bd);
    }
}
