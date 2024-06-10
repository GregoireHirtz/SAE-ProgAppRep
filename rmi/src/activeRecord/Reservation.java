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
    private int numtab;
    private Date datres;
    private int nbpers;
    private Date datpaie;
    private String modpaie;
    private Double montcom;

    public Reservation(int numtab, Date datres, int nbpers) {
        if (numtab < 0 || datres == null || nbpers < 0) {
            throw new IllegalArgumentException("Les paramètres ne peuvent pas être null");
        }

        this.numres = 0;
        this.numtab = numtab;
        this.datres = datres;
        this.nbpers = nbpers;
        this.datpaie = null;
        this.modpaie = null;
        this.montcom = null;
    }

    public Reservation(int numres){
        if (numres <= 0) {
            throw new IllegalArgumentException("Les paramètres ne peuvent pas être null");
        }

        // recuperer les data sur la bd ou renvoye erreur id non trouve dans bd
    }

    @Override
    public void save(Bd bd) {
        if (bd == null) throw new IllegalArgumentException("La connexion ne peut pas être null");

        if (this.numres == 0) {
            String sql = "INSERT INTO reservation (numtab, datres, nbpers, datpaie, modpaie, montcom) VALUES (?, ?, ?, ?, ?, ?)";
            try{
                bd.executeQuery(sql, this.numtab, this.datres, this.nbpers, this.datpaie, this.modpaie, this.montcom);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        else{
            String sql = "UPDATE reservation SET numtab = ?, datres = ?, nbpers = ?, datpaie = ?, modpaie = ?, montcom = ? WHERE numres = ?";
            try{
                bd.executeQuery(sql, this.numtab, this.datres, this.nbpers, this.datpaie, this.modpaie, this.montcom, this.numres);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public String toString(){
        return "Réservation n°" + this.numres + " pour " + this.nbpers + " personnes" + " à la table n°" + this.numtab + " le " + this.datres + " pour un montant de " + this.montcom + "€";
    }
}
