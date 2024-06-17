package activeRecord;

import bd.Bd;

import java.sql.*;
import java.util.ArrayList;

public class Reservation implements ActiveRecord {

    private int numres;
    private String nom, prenom;
    private int nbpers;
    private String telephone;
    private int numrestau;
    private Date date;
    private Date dateajout;
    private int numtab;

    public Reservation() {}

    public Reservation(String nom, String prenom, int nbpers, String telephone, int numrestau, Date date, Date dateajout, int numtab) {
        if (nom==null || prenom==null || telephone==null || date==null || dateajout==null)
            throw new IllegalArgumentException("nom("+nom+"), prenom("+prenom+"), telephone("+telephone+") ou date("+date+") sont null");
        if (nbpers<=0||numtab<=0)
            throw new IllegalArgumentException("nbpers <= 0");

        this.numres = 0;
        this.nom = nom;
        this.prenom = prenom;
        this.nbpers = nbpers;
        this.telephone = telephone;
        this.numrestau = numrestau;
        this.date = date;
        this.dateajout = dateajout;
        this.numtab = numtab;
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
            this.date = result.getDate("date");
            this.dateajout = result.getDate("dateajout");
        }else{
            throw new IllegalArgumentException("Le numero de reservation founrie n'est pas trouvé en bd");
        }
    }

    @Override
    public void save(Bd bd) throws SQLException {
        if (bd == null) throw new IllegalArgumentException("La connexion ne peut pas être null");

        if (this.numres == 0) {
            // verifie si reserv pas deja dans la bd

            String sql = "INSERT INTO reservation (nom, prenom, nbpers, telephone, numrestau, date, dateajout, numtab) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = bd.getConnection().prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            ResultSet resultSet = bd.executeQuery(statement, sql, this.nom, this.prenom, this.nbpers, this.telephone, this.numrestau, this.date, this.dateajout, this.numtab);

            if (resultSet.next()) {
                this.numres = resultSet.getInt(1);
            }
        }

        else{
            String sql = "UPDATE reservation SET nom=?, prenom=?, nbpers=?, telephone=?, numrestau=?, date=?, dateajout=?, numtab=? WHERE numres = ?";
            bd.executeQuery(sql, this.nom, this.prenom, this.nbpers, this.telephone, this.numrestau, this.date, this.dateajout, this.numtab, this.numres);
        }
    }

    @Override
    public void delete(Bd bd) throws SQLException{
        if (this.numres <= 0)
            throw new IllegalArgumentException("l'objet actuel n'est pas sauvegardé sur le bd");

        String requete = "DELETE FROM reservation WHERE numres = ?";
        ResultSet r = bd.executeQuery(requete, this.numres);
    }

    static public void nettoyerTickets(Bd bd) throws SQLException{
        String sql = "DELETE FROM reservation WHERE dateajout < NOW() - INTERVAL 15 MINUTE";
        try (Statement stmt = bd.getConnection().createStatement()) {
            stmt.execute(sql);
        }
    }

    public static ArrayList<Reservation> getAll(Bd bd) throws SQLException{
        if (bd == null)
            throw new IllegalArgumentException("bd == null");

        String requete = "SELECT * FROM reservation";

        ResultSet r = bd.executeQuery(requete);
        ArrayList<Reservation> reservations= new ArrayList<>();
        while (r.next()){
            reservations.add(new Reservation(bd, r.getInt("numres")));
        }
        return  reservations;
    }


    public String toString(){
        return "Réservation n°"+numres+" ("+this.nom+" "+this.prenom+" : "+this.nbpers+" "+this.telephone+" : "+this.numrestau+")";
    }

    public boolean equals(Reservation other) {
        if(other.numres != numres) return false;
        if(other.nom != nom) return false;
        if(other.prenom != prenom) return false;
        if(other.nbpers != nbpers) return false;
        if(other.telephone != telephone) return false;
        if(other.numrestau != numrestau) return false;
        if(other.date != date) return false;
        if(other.dateajout != dateajout) return false;
        if(other.numtab != numtab) return false;
        return true;
    }

    public int getNumres() {
        return numres;
    }

    public void setNumres(int numres) {
        this.numres = numres;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public int getNbpers() {
        return nbpers;
    }

    public void setNbpers(int nbpers) {
        this.nbpers = nbpers;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public int getNumrestau() {
        return numrestau;
    }

    public void setNumrestau(int numrestau) {
        this.numrestau = numrestau;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getDateajout() {
        return dateajout;
    }

    public void setDateajout(Date dateajout) {
        this.dateajout = dateajout;
    }

    public int getNumtab() {
        return numtab;
    }

    public void setNumtab(int numtab) {
        this.numtab = numtab;
    }
}
