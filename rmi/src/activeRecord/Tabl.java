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

    public Tabl(Bd bd, int nbplace) {
        if (bd == null)
            throw new IllegalArgumentException("La connexion ne peut pas être null");

        if (nbplace < 0)
            throw new IllegalArgumentException("Le nombre de place ne peut pas être négatif");

        String requete = "SELECT * FROM tabl WHERE numtab= ?";
        try{
            ResultSet result = bd.executeQuery(requete, numtab);
            if (result.next()) {
                this.numtab = result.getInt("numtab");
                this.nbplace = result.getInt("nbplace");
            }

        }catch (SQLException e){
            throw new IllegalArgumentException("Le numero de reservatio  founrie n'est pas trouvé en bd");
        }
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
}
