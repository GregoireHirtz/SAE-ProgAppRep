package activeRecord;

import bd.Bd;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Tabl implements ActiveRecord {

    private int numtab;
    private int nbplace;

    public Tabl(int numtab, int nbplace) {
        if (nbplace < 0) throw new IllegalArgumentException("Le nombre de place ne peut pas être négatif");

        this.numtab = numtab;
        this.nbplace = nbplace;
    }

    public Tabl(int nbplace) {
        if (nbplace < 0) throw new IllegalArgumentException("Le nombre de place ne peut pas être négatif");

        this.numtab = 0;
        this.nbplace = nbplace;
    }

    @Override
    public void save(Bd bd){
        if (bd == null) throw new IllegalArgumentException("La connexion ne peut pas être null");

        if (this.numtab == 0) {
            String sql = "INSERT INTO tabl (nbplace) VALUES (?)";
            try{
                bd.executeQuery(sql, this.nbplace);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        else{
            String sql = "UPDATE tabl SET nbplace = ? WHERE numtab = ?";
            try{
                bd.executeQuery(sql, this.nbplace, this.numtab);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public String toString(){
        return "Table n°" + this.numtab + " (" + this.nbplace + " places)";
    }

    public int getNbplace() {
        return nbplace;
    }





    public static Tabl findByNum(Bd bd, int numtab) throws SQLException {
        if (bd == null) throw new IllegalArgumentException("La connexion ne peut pas être null");

        ResultSet rs = bd.executeQuery("SELECT * FROM tabl WHERE numtab = ?", numtab);
        if (rs.next()){
            return new Tabl(rs.getInt("numtab"), rs.getInt("nbplace"));
        }
        return null;
    }

    public static List<Tabl> getTableLibre(Bd bd, Date date) throws SQLException {
        if (bd == null) throw new IllegalArgumentException("La connexion ne peut pas être null");
        if (date == null) throw new IllegalArgumentException("La date ne peut pas être null");

        ResultSet rs = bd.executeQuery("SELECT * FROM tabl WHERE numtab NOT IN (SELECT numtab FROM reservation WHERE DATE_SUB(datres, INTERVAL 2 HOUR) < ? AND ((datpaie IS NULL AND DATE_ADD(datres, INTERVAL 2 HOUR) > ?) OR (datpaie IS NOT NULL AND datpaie > ?)))", date, date, date);
        ArrayList<Tabl> tables = new ArrayList<>();
        while (rs.next()){
            tables.add(new Tabl(rs.getInt("numtab"), rs.getInt("nbplace")));
        }
        return tables;
    }

    public static List<Tabl> getAll(Bd bd) throws SQLException {
        if (bd == null) throw new IllegalArgumentException("La connexion ne peut pas être null");

        ResultSet rs = bd.executeQuery("SELECT * FROM tabl");
        ArrayList<Tabl> tables = new ArrayList<>();
        while (rs.next()){
            tables.add(new Tabl(rs.getInt("numtab"), rs.getInt("nbplace")));
        }
        return tables;
    }
}
