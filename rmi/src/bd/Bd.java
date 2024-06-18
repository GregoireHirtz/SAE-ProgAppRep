    package bd;

    import java.sql.*;
    import java.util.HashMap;

    public class Bd {

        private Connection connection;
        public final String url, username;
        private HashMap<String, String> updates = new HashMap<>();

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
            return executeQuery(statement, query, params);
        }

        public ResultSet executeQuery(PreparedStatement statement, String query, Object... params) throws SQLException{
            if (query==null) throw new NullPointerException("la requête ne peut pas être null");

            for (int i = 0; i < params.length; i++) {
                statement.setObject(i+1, params[i]);
            }

            // si UPDATE, INSERT, DELETE, LOCK, UNLOCK
            if (query.toUpperCase().startsWith("UPDATE") || query.toUpperCase().startsWith("INSERT") || query.toUpperCase().startsWith("DELETE") || query.toUpperCase().startsWith("LOCK") || query.toUpperCase().startsWith("UNLOCK")){
                statement.executeUpdate();
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    return generatedKeys;
                } catch (Exception e) {
                    return null;
                }
            }
            else {
                ResultSet rs = statement.executeQuery();
                return rs;
            }
        }

        public void lockTable(String tableName) throws SQLException {
            String query = "LOCK TABLES " + tableName + " WRITE";
            try (Statement stmt = connection.createStatement()) {
                stmt.execute(query);
            }
        }

        public void lockTables(String... tableNames) throws SQLException {
            StringBuilder query = new StringBuilder("LOCK TABLES ");
            for (String tableName : tableNames) {
                query.append(tableName).append(" WRITE, ");
            }
            query.setLength(query.length() - 2);
            try (Statement stmt = connection.createStatement()) {
                stmt.execute(query.toString());
            }
        }

        public void unlockTable() throws SQLException {
            String query = "UNLOCK TABLES";
            try (Statement stmt = connection.createStatement()) {
                stmt.execute(query);
            }
        }

        public boolean haveUpdate(String tableName) {
            String[] splittedDbName = url.split("/");
            String databaseName = splittedDbName[splittedDbName.length -1];

            String updateTimeQuery = "SELECT update_time FROM information_schema.tables " +
                    "WHERE table_schema = ? " +
                    "AND table_name = ?";

            return getUpdate(updateTimeQuery, databaseName, tableName);
        }

        public boolean haveUpdate() {
            String[] splittedDbName = url.split("/");
            String databaseName = splittedDbName[splittedDbName.length -1];

            String updateTimeQuery = "SELECT update_time FROM information_schema.tables " +
                    "WHERE table_schema = ? ";

            return getUpdate(updateTimeQuery, databaseName, "any");
        }

        private boolean getUpdate(String query, String databaseName, String tableName) throws RuntimeException {
            try {
                ResultSet resultSet;
                if(query.chars().filter(ch -> ch == '?').count() == 1) {
                    resultSet = executeQuery(query, databaseName);
                }
                else {
                    resultSet = executeQuery(query, databaseName, tableName);
                }
                resultSet.next();
                String lastUpdate = resultSet.getString(1);
                resultSet.close();

                String myLastUpdate = updates.get(tableName);

                if(myLastUpdate == null || !myLastUpdate.equals(lastUpdate)) {
                    updates.put(tableName, lastUpdate);
                    return true;
                }
                else {
                    return false;
                }
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException("Error while retrieving data, DB might be down");
            }
        }

        public Connection getConnection() {
            return connection;
        }

    }
