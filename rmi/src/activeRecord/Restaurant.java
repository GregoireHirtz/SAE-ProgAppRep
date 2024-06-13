package activeRecord;

import bd.Bd;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Restaurant implements ActiveRecord{

    private int numrestau;
    private String nom;
    private Double latitude, longitude;

    public Restaurant(String nom, Double latitude, Double longitude) {
        if (nom==null || nom.equals(""))
            throw new IllegalArgumentException("Le nom ne peut etre null ou vide \"\"");
        if (latitude==null || longitude==null)
            throw new IllegalArgumentException("La latitude, ni la longitude ne peut etre null");

        this.numrestau = 0;
        this.nom = nom;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    protected Restaurant(int numrestau, String nom, Double latitude, Double longitude) {
        this.numrestau = numrestau;
        this.nom = nom;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Restaurant(Bd bd, int numrestau) throws SQLException {
        if (bd==null)
            throw new IllegalArgumentException("Le bd ne peut pas etre null");
        if (numrestau<=0)
            throw new IllegalArgumentException("Le numrestau ne peut pas etre <=0");

        String requete = "SELECT * FROM restaurant WHERE numrestau = ?";

        ResultSet result = bd.executeQuery(requete, numrestau);
        if (result.next()) {
            this.numrestau = result.getInt("numrestau");
            this.nom = result.getString("nom");
            this.latitude = result.getDouble("latitude");
            this.longitude = result.getDouble("longitude");
        }else{
            throw new IllegalArgumentException("Le numero du restau founrie n'est pas trouvé en bd");
        }
    }


    @Override
    public void save(Bd bd) {
        if (bd == null) throw new IllegalArgumentException("La connexion ne peut pas être null");

        // insertion nouveau restaurant
        if (this.numrestau == 0) {
            String sql = "INSERT INTO restaurant (nom, latitude, longitude) VALUES ( ?, ?, ?)";
            try{
                bd.executeQuery(sql, this.nom, this.latitude, this.longitude);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        // mise a jour
        else{
            String sql = "UPDATE restaurant SET nom = ?, latitude = ?, longitude = ? WHERE numrestau = ?";
            try{
                bd.executeQuery(sql, this.nom, this.latitude, this.longitude, this.numrestau);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void delete(Bd bd) {
        if (this.numrestau <= 0)
            throw new IllegalArgumentException("l'objet actuel n'est pas sauvegardé sur le bd");

        String requete2 = "DELETE FROM menu WHERE numrestau=?";
        String requete = "DELETE FROM reservation WHERE numrestau = ?";
        try{
            ResultSet r = bd.executeQuery(requete, this.numrestau);
            if (r.next()){
                bd.executeQuery(requete, numrestau);
                bd.executeQuery(requete2, numrestau);
            }
            requete = "DELETE FROM restaurant WHERE numrestau = ?";
            bd.executeQuery(requete, this.numrestau);
        }catch (SQLException e){}
    }

    public String toString(){
        return "Restaurant : "+this.nom + "-" + this.latitude + "-" + this.longitude;
    }


    public static ArrayList<Restaurant> getAll(Bd bd) throws SQLException{

        if (bd == null)
            throw new IllegalArgumentException("bd == null");

        String requete = "SELECT * FROM restaurant";

        ResultSet r = bd.executeQuery(requete);
        ArrayList<Restaurant> restaurants= new ArrayList<>();
        while (r.next()){
            restaurants.add(new Restaurant(r.getInt("numrestau"), r.getString("nom"), r.getDouble("latitude"), r.getDouble("longitude")));
        }
        return  restaurants;
    }

    public int getNumrestau() {
        return numrestau;
    }

    public void setNumrestau(int numrestau) {
        this.numrestau = numrestau;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }


    public List<Plat> getMenu(Bd bd)throws SQLException {
        if (bd == null)
            throw new IllegalArgumentException("bd == null");

        String requete = "SELECT * FROM plats p inner join commande c on p.numplat=c.numplat where "; // A finir

        ResultSet r = bd.executeQuery(requete);
        ArrayList<Plat> plats= new ArrayList<Plat>();
        while (r.next()){
            plats.add(new Plat(r.getString("libelle"), r.getString("type"), r.getDouble("prixunit")));
        }
        return  plats;
    }

    public List<Tabl> getTables(Bd bd)throws SQLException {
        if (bd == null)
            throw new IllegalArgumentException("bd == null");

        String requete = "SELECT * FROM tabl WHERE numrestau == ?";

        ResultSet r = bd.executeQuery(requete, numrestau);
        ArrayList<Tabl> Tables= new ArrayList<Tabl>();
        while (r.next()){
            Tables.add(new Tabl(r.getInt("numtab"), r.getInt("nbplace"), r.getInt("numRestau")));
        }
        return  Tables;
    }

    public List<Tabl> getTablesLibre(Bd bd, Date date) throws SQLException {
        if (bd == null)
            throw new IllegalArgumentException("bd == null");

        String requete = "SELECT * FROM tabl WHERE numrestau == ? AND numtab not IN " +
                "(SELECT numtab FROM reservation WHERE numrestau == ? AND date == ?)";

        ResultSet r = bd.executeQuery(requete, numrestau, numrestau, date);
        ArrayList<Tabl> Tables= new ArrayList<Tabl>();
        while (r.next()){
            Tables.add(new Tabl(r.getInt("numtab"), r.getInt("nbplace"), r.getInt("numRestau")));
        }
        return  Tables;
    }

    public Object getPlats(Bd bd) {
        return null;
    }
}
