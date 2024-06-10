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

    public Serveur(int numserv, String email, String passwd, String nomserv, String grade) {
        if (email == null || passwd == null || nomserv == null || grade == null) {
            throw new IllegalArgumentException("Les paramètres ne peuvent pas être null");
        }
        this.numserv = numserv;
        this.email = email;
        this.passwd = passwd;
        this.nomserv = nomserv;
        this.grade = grade;
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

    public String getGrade() {
        return this.grade;
    }





    public static Serveur getByEmail(Bd bd, String email) {
        if (email == null) throw new IllegalArgumentException("L'email ne peut pas être null");

        String sql = "SELECT * FROM serveur WHERE email = ?";
        try{
            ResultSet rs = bd.executeQuery(sql, email);
            if (rs.next()){
                return new Serveur(rs.getInt("numserv"), rs.getString("email"), rs.getString("passwd"), rs.getString("nomserv"), rs.getString("grade"));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public static List<Serveur> getAll(Bd bd){
        if (bd == null) throw new IllegalArgumentException("La connexion ne peut pas être null");

        try{
            ResultSet rs = bd.executeQuery("SELECT * FROM serveur");
            ArrayList<Serveur> serveurs = new ArrayList<>();
            while (rs.next()){
                serveurs.add(new Serveur(rs.getInt("numserv"), rs.getString("email"), rs.getString("passwd"), rs.getString("nomserv"), rs.getString("grade")));
            }
            return serveurs;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public static Serveur findByNum(Bd bd, int numserv){
        if (bd == null) throw new IllegalArgumentException("La connexion ne peut pas être null");

        try{
            ResultSet rs = bd.executeQuery("SELECT * FROM serveur WHERE numserv = ?", numserv);
            if (rs.next()){
                return new Serveur(rs.getInt("numserv"), rs.getString("email"), rs.getString("passwd"), rs.getString("nomserv"), rs.getString("grade"));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }
}
