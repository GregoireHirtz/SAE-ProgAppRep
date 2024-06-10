package activeRecord;

import bd.Bd;

public interface ActiveRecord {

    public void save(Bd bd);
    public void delete(Bd bd);

}
