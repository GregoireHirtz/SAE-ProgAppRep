package activeRecord;

import bd.Bd;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Plat implements ActiveRecord{

    private int numplat;
    private String libelle;
    private String type;
    private double prixunit;

    public Plat(String libelle, String type, double prixunit){
        if (libelle == null || type == null || prixunit <= 0) {
            throw new IllegalArgumentException("Les paramètres ne peuvent pas être null ou égale à 0");
        }

        this.numplat = 0;
        this.libelle = libelle;
        this.type = type;
        this.prixunit = prixunit;
    }

    public Plat(Bd bd, int numplat) throws SQLException{
        if (bd == null)
            throw new IllegalArgumentException("bd == null");
        if (numplat <= 0)
            throw new IllegalArgumentException("Les paramètres ne peuvent pas être null");


        String requete = "SELECT * FROM plat WHERE numplat = ?";

        ResultSet result = bd.executeQuery(requete, numplat);
        if (result.next()) {

        }else{
            throw new IllegalArgumentException("Le numero de reservation founrie n'est pas trouvé en bd");
        }
    }

    public int getNumplat() {
        return numplat;
    }

    public void setNumplat(int numplat) {
        this.numplat = numplat;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getPrixunit() {
        return prixunit;
    }

    public void setPrixunit(double prixunit) {
        this.prixunit = prixunit;
    }

    @Override
    public void save(Bd bd) throws SQLException{
        if (bd == null) throw new IllegalArgumentException("La connexion ne peut pas être null");

        System.out.println("numplat : " + this.numplat);
        if (this.numplat == 0) {
            String sql = "INSERT INTO plat (libelle, type, prixunit, qtservie) VALUES (?, ?, ?, ?)";
            try{
                bd.executeQuery(sql, this.libelle, this.type, this.prixunit);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        else{
            String sql = "UPDATE plat SET libelle = ?, type = ?, prixunit = ?, qteservie = ? WHERE numplat = ?";
            try{
                bd.executeQuery(sql, this.libelle, this.type, this.prixunit, this.numplat);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void delete(Bd bd) throws SQLException{
        if (this.numplat <= 0)
            throw new IllegalArgumentException("l'objet actuel n'est pas sauvegardé sur le bd");

        String requete2 = "DELETE FROM menu WHERE numplat=?";
        String requete = "DELETE FROM plat WHERE numplat = ?";
        bd.executeQuery(requete, this.numplat);
        bd.executeQuery(requete2, this.numplat);
    }

    public String toString(){
        return "- ["+this.numplat+"] " +this.libelle + " (" + this.type + ") - " + this.prixunit + "€" + " - " + " restants";
    }
}