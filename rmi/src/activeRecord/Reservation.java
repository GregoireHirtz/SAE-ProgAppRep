package activeRecord;

import bd.Bd;

import java.sql.Array;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Reservation implements ActiveRecord{

    private int numres;
    private String nom, prenom;
    private int nbpers;
    private String telephone;
    private int numrestau;



    public Reservation(String nom, String prenom, int nbpers, String telephone, int numrestau) {
        if (nom==null || prenom==null || telephone==null)
            throw new IllegalArgumentException("nom, prenom, telephone == null");
        if (nbpers<=0)
            throw new IllegalArgumentException("nbpers <= 0");

    // TODO pas de vérification que le numserv bien valide :  a faire

        this.nom = nom;
        this.prenom = prenom;
        this.nbpers = nbpers;
        this.telephone = telephone;
        this.numrestau = numrestau;
    }

    public Reservation(Bd bd, int numres){
        if (bd == null)
            throw new IllegalArgumentException("La connexion ne peut pas être null");

        if (numres <= 0)
            throw new IllegalArgumentException("Les paramètres ne peuvent pas être négatif ou églae à 0");

        String requete = "SELECT * FROM reservation WHERE numres = ?";
        try{
            ResultSet result = bd.executeQuery(requete, numres);
            if (result.next()) {
                this.numres = result.getInt("numres");
                this.nom = result.getString("nom");
                this.prenom = result.getString("prenom");
                this.nbpers = result.getInt("nbpers");
                this.telephone = result.getString("telephone");
                this.numrestau = result.getInt("numrestau");
            }else{
                throw new IllegalArgumentException("Le numero de reservation founrie n'est pas trouvé en bd");
            }

        }catch (SQLException e){}
    }

    @Override
    public void save(Bd bd) {
        if (bd == null) throw new IllegalArgumentException("La connexion ne peut pas être null");

        if (this.numres == 0) {
            String sql = "INSERT INTO reservation (nom, prenom, nbpers, telephone, numrestau) VALUES (?, ?, ?, ?, ?)";
            try{
                bd.executeQuery(sql);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        else{
            String sql = "UPDATE reservation SET nom=?, prenom=?, nbpers=?, telephone=?, numrestau=? WHERE numres = ?";
            try{
                bd.executeQuery(sql, this.nom, this.prenom, this.nbpers, this.telephone, this.numrestau, this.numres);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void delete(Bd bd) {
        if (this.numres <= 0)
            throw new IllegalArgumentException("l'objet actuel n'est pas sauvegardé sur le bd");

        String requete = "DELETE FROM reservation WHERE numres = ?";
        try{
            ResultSet r = bd.executeQuery(requete, this.numres);
        }catch (SQLException e){}
    }

    public String toString(){
        return "Réservation n°"+numres+" ("+this.nom+" "+this.prenom+" : "+this.nbpers+" "+this.telephone+" : "+this.numrestau+")";
    }
}
