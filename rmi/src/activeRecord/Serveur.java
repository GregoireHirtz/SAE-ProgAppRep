package activeRecord;

import bd.Bd;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

public class Serveur implements ActiveRecord{

    private int numserv;
    private String email;
    private String passwd;
    private String nomserv;
    private String grade;


    public Serveur(String email, String passwd, String nomserv, String grade) {
        if (email == null || passwd == null || nomserv == null || grade == null) {
            throw new IllegalArgumentException("Les paramètres ne peuvent pas être null");
        }
        this.numserv = 0;
        this.email = email;
        this.passwd = passwd;
        this.nomserv = nomserv;
        this.grade = grade;
    }

    public Serveur(Bd bd, int numserv) {
        if (bd == null)
            throw new IllegalArgumentException("La connexion ne peut pas être null");

        if (numserv <= 0)
            throw new IllegalArgumentException("Les paramètres ne peuvent pas négatif ou égale à 0");

        String requete = "SELECT * FROM serveur WHERE numserv = ?";
        try{
            ResultSet result = bd.executeQuery(requete, numserv);
            if (result.next()) {
                this.numserv = result.getInt("numserv");
                this.email = result.getString("email");
                this.passwd = result.getString("passwd");
                this.nomserv = result.getString("nomserv");
                this.grade = result.getString("grade");
            }

        }catch (SQLException e){
            throw new IllegalArgumentException("Le numero de reservatio  founrie n'est pas trouvé en bd");
        }
    }


    @Override
    public void save(Bd bd){
        if (bd == null) throw new IllegalArgumentException("La connexion ne peut pas être null");

        if (this.numserv == 0) {
            String sql = "INSERT INTO serveur (email, passwd, nomserv, grade) VALUES (?, ?, ?, ?)";
            try{
                bd.executeQuery(sql, this.email, this.passwd, this.nomserv, this.grade);
            }catch (SQLException e){
                e.printStackTrace();
            }
        }

        else{
            String sql = "UPDATE serveur SET email = ?, passwd = ?, nomserv = ?, grade = ? WHERE numserv = ?";
            try{
                bd.executeQuery(sql, this.email, this.passwd, this.nomserv, this.grade, this.numserv);
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
    }

    public String toString(){
        return "Serveur " + this.numserv + " : " + this.nomserv + " (" + this.grade + ")";
    }
}
