package activeRecord;

import bd.Bd;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Tabl implements ActiveRecord {

    private int numtab;
    private int nbplace;
    private int numrestau;

    public Tabl(int nbplace, int numrestau) {
        if (nbplace<=0 || numrestau<=0)
            throw new IllegalArgumentException("nbplace and numrestau must be greater than 0");


        this.numtab = 0;
        this.nbplace = nbplace;
        this.numrestau = numrestau;
    }

    public Tabl(Bd bd, int numtab) throws SQLException {
        if (bd==null)
            throw new IllegalArgumentException("bd must not be null");
        if (numtab<=0)
            throw new IllegalArgumentException("numtab must be greater than 0");


        String requete = "SELECT * FROM tabl WHERE numtab = ?";

        ResultSet result = bd.executeQuery(requete, numtab);
        if (result.next()) {
            this.numtab = result.getInt("numtab");
            this.nbplace = result.getInt("nbplace");
            this.numrestau = result.getInt("numrestau");
        }else{
            throw new IllegalArgumentException("Le numero de la table fournie n'est pas trouvé en bd");
        }
    }

    public Tabl(int numtab, int nbplace, int numrestau) {
        this.numtab = numtab;
        this.nbplace = nbplace;
        this.numrestau = numrestau;
    }


    @Override
    public void save(Bd bd) throws SQLException {
        if (bd==null)
            throw new IllegalArgumentException("bd must not be null");

        if (this.numtab == 0) {
            String sql = "INSERT INTO tabl (numtab, nbplace, numrestau) VALUES (?, ?, ?)";
            bd.executeQuery(sql);
        }

        else{
            // ne permet de changer la tabled de restaurant
            String sql = "UPDATE reservation SET nbplace=? WHERE numtab=?";
            bd.executeQuery(sql, this.nbplace, this.numtab);
        }
    }

    @Override
    public void delete(Bd bd) throws SQLException {
        if (this.numtab <= 0)
            throw new IllegalArgumentException("l'objet actuel n'est pas sauvegardé sur le bd");

        String requete = "DELETE FROM tabl WHERE numtab = ?";
        ResultSet r = bd.executeQuery(requete, this.numtab);
    }

    @Override
    public String toString() {
        return "Tabl ["+numtab + ": "+nbplace+" ("+numrestau+")]";
    }

    public int getNumtab() {
        return numtab;
    }

    public void setNumtab(int numtab) {
        this.numtab = numtab;
    }

    public int getNbplace() {
        return nbplace;
    }

    public void setNbplace(int nbplace) {
        this.nbplace = nbplace;
    }

    public int getNumrestau() {
        return numrestau;
    }

    public void setNumrestau(int numrestau) {
        this.numrestau = numrestau;
    }
}
