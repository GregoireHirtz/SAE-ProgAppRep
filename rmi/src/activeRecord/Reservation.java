package activeRecord;

import bd.Bd;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Reservation implements ActiveRecord {

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
        this.numres = 0;
        this.nom = nom;
        this.prenom = prenom;
        this.nbpers = nbpers;
        this.telephone = telephone;
        this.numrestau = numrestau;
    }

    private Reservation(int numres, String nom, String prenom, int nbpers, String telephone, int numrestau) {
        if (numres<=0)
            throw new IllegalArgumentException("numres <= 0");
        if (nom==null || prenom==null || telephone==null)
            throw new IllegalArgumentException("nom, prenom, telephone == null");
        if (nbpers<=0)
            throw new IllegalArgumentException("nbpers <= 0");

        this.numres = numres;
        this.nom = nom;
        this.prenom = prenom;
        this.nbpers = nbpers;
        this.telephone = telephone;
        this.numrestau = numrestau;
    }

    public Reservation(Bd bd, int numres) throws SQLException{
        if (bd == null)
            throw new IllegalArgumentException("La connexion ne peut pas être null");

        if (numres <= 0)
            throw new IllegalArgumentException("Les paramètres ne peuvent pas être négatif ou églae à 0");

        String requete = "SELECT * FROM reservation WHERE numres = ?";

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
    }

    @Override
    public void save(Bd bd) throws SQLException {
        if (bd == null) throw new IllegalArgumentException("La connexion ne peut pas être null");

        if (this.numres == 0) {
            String sql = "INSERT INTO reservation (nom, prenom, nbpers, telephone, numrestau) VALUES (?, ?, ?, ?, ?)";
            bd.executeQuery(sql);
        }

        else{
            String sql = "UPDATE reservation SET nom=?, prenom=?, nbpers=?, telephone=?, numrestau=? WHERE numres = ?";
            bd.executeQuery(sql, this.nom, this.prenom, this.nbpers, this.telephone, this.numrestau, this.numres);
        }
    }

    @Override
    public void delete(Bd bd) throws SQLException{
        if (this.numres <= 0)
            throw new IllegalArgumentException("l'objet actuel n'est pas sauvegardé sur le bd");

        String requete = "DELETE FROM reservation WHERE numres = ?";
        ResultSet r = bd.executeQuery(requete, this.numres);
    }

    public String toString(){
        return "Réservation n°"+numres+" ("+this.nom+" "+this.prenom+" : "+this.nbpers+" "+this.telephone+" : "+this.numrestau+")";
    }
}
