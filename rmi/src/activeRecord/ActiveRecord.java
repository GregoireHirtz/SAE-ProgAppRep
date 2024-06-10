package activeRecord;

import bd.Bd;

import java.sql.SQLException;

public interface ActiveRecord {

    public void save(Bd bd) throws SQLException;
    public void delete(Bd bd);

}
