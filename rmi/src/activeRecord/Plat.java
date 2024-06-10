package activeRecord;

import bd.Bd;

import java.sql.Array;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Plat implements ActiveRecord{

    private int numplat;
    private String libelle;
    private String type;
    private double prixunit;
    private int qtservie;

    public Plat(String libelle, String type, double prixunit, int qtservie){
        if (libelle == null || type == null || prixunit < 0 || qtservie < 0) {
            throw new IllegalArgumentException("Les paramètres ne peuvent pas être null");
        }

        this.numplat = 0;
        this.libelle = libelle;
        this.type = type;
        this.prixunit = prixunit;
        this.qtservie = qtservie;
    }

    public Plat(Bd bd, int numplat){
        if (bd == null)
            throw new IllegalArgumentException("La connexion ne peut pas être null");

        if (numplat <= 0)
            throw new IllegalArgumentException("Le paramètre ne peuvent pas négatif ou égale à 0");


        String requete = "SELECT * FROM plat WHERE numplat = ?";
        try{
            ResultSet result = bd.executeQuery(requete, numplat);
            if (result.next()) {
                this.numplat = result.getInt("numplat");
                this.libelle = result.getString("libelle");
                this.type = result.getString("type");
                this.prixunit = result.getDouble("prixunit");
                this.qtservie = result.getInt("qtservie");
            }
        }catch (SQLException e){
            throw new IllegalArgumentException("L'id fournie n'est pas trouvé en bd");
        }


    }

    @Override
    public void save(Bd bd) {
        if (bd == null) throw new IllegalArgumentException("La connexion ne peut pas être null");

        System.out.println("numplat : " + this.numplat);

        // si plat sans id = nouveau plat donc insertion
        if (this.numplat == 0) {
            String sql = "INSERT INTO plat (libelle, type, prixunit, qtservie) VALUES (?, ?, ?, ?)";
            try{
                bd.executeQuery(sql, this.libelle, this.type, this.prixunit, this.qtservie);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        // sinon update du plat
        else{
            String sql = "UPDATE plat SET libelle = ?, type = ?, prixunit = ?, qteservie = ? WHERE numplat = ?";
            try{
                bd.executeQuery(sql, this.libelle, this.type, this.prixunit, this.qtservie, this.numplat);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public String toString(){
        return "- ["+this.numplat+"] " +this.libelle + " (" + this.type + ") - " + this.prixunit + "€" + " - " + this.qtservie + " restants";
    }

    public int getQtservie(){
        return this.qtservie;
    }

    public void setQtservie(int qtservie){
        this.qtservie = qtservie;
    }
}
