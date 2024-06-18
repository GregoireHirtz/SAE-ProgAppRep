package activeRecord;

import bd.Bd;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Menu implements ActiveRecord{

    private int numrestau;
    private int numplat;

    public int getNumrestau() {
        return numrestau;
    }

    public void setNumrestau(int numrestau) {
        this.numrestau = numrestau;
    }

    public int getNumplat() {
        return numplat;
    }

    public void setNumplat(int numplat) {
        this.numplat = numplat;
    }

    public Menu(int numrestau, int numplat) {
        if (numrestau < 0 || numplat < 0)
            throw new IllegalArgumentException("numrestau or numplat are negative");

        this.numrestau = numrestau;
        this.numplat = numplat;
    }


    @Override
    public void save(Bd bd) throws SQLException {
        String sql = "SELECt * FROM menu WHERE numrestau = ? AND numplat = ?";
        ResultSet r = bd.executeQuery(sql, numrestau, numplat);
        if (!r.next()) {
            sql = "INSERT INTO menu (numrestau, numplat) VALUES (?, ?)";
            bd.executeQuery(sql, numrestau, numplat);
        }

    }

    @Override
    public void delete(Bd bd) throws SQLException {
        String sql = "DELETE FROM menu WHERE numrestau = ? AND numplat = ?";
        bd.executeQuery(sql, numrestau, numplat);
    }
}
