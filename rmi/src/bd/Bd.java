package bd;

import java.sql.*;

public class Bd {

    private Connection connection;
    public final String url, username;

    public Bd(String url, String username, String password) throws SQLException{
        if (url==null || username==null || password==null) throw new NullPointerException("aucun paramètre ne doit être null");

        this.connection = DriverManager.getConnection(url, username, password);
        this.url = url;
        this.username = username;
    }

    /**
     * Exécute une requête SQL
     * @param query la requête
     * @return le résultat de la requête
     * @throws SQLException
     */
    public ResultSet executeQuery(String query) throws SQLException{
        if (query==null) throw new NullPointerException("la requête ne peut pas être null");

        Statement statement = this.connection.createStatement();
        ResultSet rs = statement.executeQuery(query);
        return rs;
    }

    public ResultSet executeQuery(String query, Object... params) throws SQLException{
        if (query==null) throw new NullPointerException("la requête ne peut pas être null");

        PreparedStatement statement = this.connection.prepareStatement(query);
        for (int i = 0; i < params.length; i++) {
            statement.setObject(i+1, params[i]);
        }

        // si UPDATE, INSERT, DELETE, LOCK, UNLOCK
        if (query.toUpperCase().startsWith("UPDATE") || query.toUpperCase().startsWith("INSERT") || query.toUpperCase().startsWith("DELETE") || query.toUpperCase().startsWith("LOCK") || query.toUpperCase().startsWith("UNLOCK")){
            statement.executeUpdate();
            return null;
        }
        else {
            ResultSet rs = statement.executeQuery();
            return rs;
        }
    }
}
