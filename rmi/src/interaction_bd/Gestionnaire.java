package interaction_bd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Gestionnaire extends Serveur {

    public Gestionnaire(String mail, String mdp, String nom) {
        super(mail, mdp, nom);
    }

    /**
     * Méthode permettant de consulter les affectations de serveurs, vérouille la table des affectations
     * @param connection
     * @throws SQLException
     */
    public void consulterAffectationServeur(Connection connection) throws SQLException {
        String query = "SELECT serveur.numserv, nomserv, dataff, numtab " +
                       "FROM affecter JOIN serveur ON affecter.numserv = serveur.numserv " +
                       "ORDER BY dataff FOR UPDATE";

        ResultSet resultat = connection.createStatement().executeQuery(query);

        while(resultat.next()) {
            System.out.print("numserv : " + resultat.getInt(1) + " ");
            System.out.print("nomserv : " + resultat.getString(2) + " ");
            System.out.print("affecter le : " + resultat.getTimestamp(3) + " ");
            System.out.println("à la table " + resultat.getInt(4) + " ");
        }
    }

    /**
     * Méthode permettant d'affecter un serveur à une table
     * @param connection connexion à la base de donnée
     * @param numtab numéro de table à affecter au serveur
     * @param dataff date à laquel le serveur sera affecté
     * @param numserv le numéro du serveur à affecter à la table
     * @throws SQLException en cas de donnée incohérante
     */
    public void affecterServeur(Connection connection, int numtab, String dataff, int numserv) throws SQLException {
        String query = "INSERT INTO affecter VALUES(?, to_date(?,'dd/mm/yyyy hh24:mi'), ?)";

        PreparedStatement statement = connection.prepareStatement(query);

        statement.setInt(1, numtab);
        statement.setString(2, dataff);
        statement.setInt(3, numserv);

        //Je me suis rendu compte que les erreurs SQL permettent d'effectuer les vérifications à notre place,
        //ce qui rend la méthode beaucoup plus petite
        try {
            statement.execute();
            connection.commit();
            System.out.print("L'affectation à été réalisée avec succès");
        } catch (SQLException e) {
            connection.rollback();
            System.out.println("L'affectation est impossible, annulation");
            System.out.println(e.getMessage());
        }
    }

    /**
     * Méthode permettant de calculer le total d'une commande
     * @param connection connexion à la base de donnée
     * @param numres numéro de la réservation pour laquel il faut calculer le total
     * @throws SQLException erreur de connexion à la base de donnée
     */
    public void calculerTotalCommande(Connection connection, int numres) throws SQLException {

        //Récupération du montant total de la commande
        //Pas de verouillage car avec ou sans, ce calcul sera toujours obsolète après la moindre modification de commande
        String query = "SELECT SUM(prixunit * quantite) " +
                       "FROM commande JOIN plat ON commande.numplat = plat.numplat " +
                       "WHERE numres = ?";

        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, numres);
        ResultSet resultat = statement.executeQuery();

        //Mise à jour du montant de réservation affiché dans la base
        if(resultat.next()) {
            double montantCommande = resultat.getDouble(1);

            query = "UPDATE reservation SET montcom = " + montantCommande + " WHERE numres = ?";

            statement = connection.prepareStatement(query);
            statement.setInt(1, numres);
            statement.execute();
            connection.commit();

            System.out.println("La commande " + numres + " vaut un total de " + montantCommande + "e");
        }

        else System.out.println("Le numéro de réservation saisi est inconnu");
    }
}
