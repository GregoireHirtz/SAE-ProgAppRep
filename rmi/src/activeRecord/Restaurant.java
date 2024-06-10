package activeRecord;

import bd.Bd;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;



public class Restaurant implements ActiveRecord{

    private int numrestau;
    private String nom, prenom;
    private Double latitude, longitude;

    public Restaurant(String nom, String prenom, Double latitude, Double longitude) {
        if (nom==null || nom=="" || prenom==null || prenom=="")
            throw new IllegalArgumentException("Le prenom nom ne peut etre null ou vide \"\"");
        if (latitude==null || longitude==null)
            throw new IllegalArgumentException("La latitude, ni la longitude ne peut etre null");

        this.numrestau = 0;
        this.nom = nom;
        this.prenom = prenom;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Restaurant(Bd bd, int numrestau) {
        if (bd==null)
            throw new IllegalArgumentException("Le bd ne peut pas etre null");
        if (numrestau<=0)
            throw new IllegalArgumentException("Le numrestau ne peut pas etre <=0");

        String requete = "SELECT * FROM restaurant WHERE numrestau = ?";
        try{
            ResultSet result = bd.executeQuery(requete, numrestau);
            if (result.next()) {
                this.numrestau = result.getInt("numrestau");
                this.nom = result.getString("nom");
                this.prenom = result.getString("prenom");
                this.latitude = result.getDouble("latitude");
                this.longitude = result.getDouble("longitude");
            }

        }catch (SQLException e){
            throw new IllegalArgumentException("Le numero du restau founrie n'est pas trouvé en bd");
        }
    }


    @Override
    public void save(Bd bd) {
        if (bd == null) throw new IllegalArgumentException("La connexion ne peut pas être null");

        // insertion nouveau restaurant
        if (this.numrestau == 0) {
            String sql = "INSERT INTO restaurant (nom, prenom, latitude, longitude) VALUES (?, ?, ?, ?)";
            try{
                bd.executeQuery(sql, this.nom, this.prenom, this.latitude, this.longitude);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        // mise a jour
        else{
            String sql = "UPDATE restaurant SET nom = ?, prenom = ?, latitude = ?, longitude = ? WHERE numrestau = ?";
            try{
                bd.executeQuery(sql, this.nom, this.prenom, this.latitude, this.longitude, this.numrestau);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public String toString(){
        return "Restruant : "+this.nom + "-" + this.prenom + "-" + this.latitude + "-" + this.longitude;
    }
}
