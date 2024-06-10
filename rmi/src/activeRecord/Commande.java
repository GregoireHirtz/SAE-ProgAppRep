package activeRecord;

import bd.Bd;

import java.sql.ResultSet;

public class Commande implements ActiveRecord{

    private int numres;
    private int numplat;
    private int quantite;

    public Commande(int numres, int numplat, int quantite){
        if (numres < 0 || numplat < 0 || quantite < 0 ) {
            throw new IllegalArgumentException("Les paramètres ne peuvent pas être négatif ou null");
        }

        this.numres = numres;
        this.numplat = numplat;
        this.quantite = quantite;
    }

    @Override
    public void save(Bd bd) {
        if (bd == null) throw new IllegalArgumentException("La connexion ne peut pas être null");

        Commande commande = Commande.findByNums(bd, this.numres, this.numplat);

        // si nouvelle commande alors on l'ajoute
        if (commande == null){
            String sql = "INSERT INTO commande (numres, numplat, quantite) VALUES (?, ?, ?)";
            try{
                bd.executeQuery(sql, this.numres, this.numplat, this.quantite);
            }catch (Exception e){
                e.printStackTrace();
            }
        // sinon update
        }else{
            String sql = "UPDATE commande SET quantite = ? WHERE numres = ? AND numplat = ?";
            try{
                bd.executeQuery(sql, this.quantite, this.numres, this.numplat);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public static Commande findByNums(Bd bd, int numres, int numplat) {
        if (bd == null) throw new IllegalArgumentException("La connexion ne peut pas être null");

        String sql = "SELECT * FROM commande WHERE numres = ? AND numplat = ?";
        try {
            ResultSet rs = bd.executeQuery(sql, numres, numplat);
            if (rs.next()) {
                return new Commande(rs.getInt("numres"), rs.getInt("numplat"), rs.getInt("quantite"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
