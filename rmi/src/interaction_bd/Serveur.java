package interaction_bd;

import java.sql.*;

public class Serveur {

    private String mail, mdp, nom;

    public Serveur(String mail, String mdp, String nom) {
        this.mail = mail;
        this.nom = nom;
        this.mdp = mdp;
    }

    /**
     * Methode pour afficher toutes les tables disponibles a une date et heure donnee
     * @param connection la connexion à la base de donnee
     * @param date la date et l'heure de reservation au format dd/mm/yyyy hh24:mi
     * @throws SQLException en cas d'erreur de connexion
     */
    public void consulterTables(Connection connection, String date) throws SQLException {
        String query = "SELECT * FROM tabl WHERE numtab NOT IN (" +
                "SELECT numtab FROM reservation WHERE datres = to_date(?, 'dd/mm/yyyy hh24:mi')" +
                ")";

        PreparedStatement statement = connection.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        statement.setString(1, date);

        ResultSet resultat = statement.executeQuery();

        //On vérifie qu'il y a bien une entrée
        if(resultat.next()) {
            System.out.println("Liste des tables disponibles pour le " + date + " :");
            //Affichage plutôt que resultat.first() pour s'assurer que le resultset ne se referme pas par inadvertance
            System.out.println("numtab : " + resultat.getInt(1) + " nbplace : " + resultat.getString(2));
        }
        else System.out.println("Il n'y a pas de table disponible pour le " + date + ".");

        //Affichage du reste des résultats
        while(resultat.next()) {
            System.out.println("numtab : " + resultat.getInt(1) + " nbplace : " + resultat.getString(2));
        }
    }

    /**
     * Permet de réserver une table à une date et heure donnée pour un nombre de personne donné
     * @param connection la connexion à la base de donnée
     * @param numtab le numéro de la table à réservé
     * @param dateres la date et l'heure de la réservation au format dd/mm/yyyy hh24:mi
     * @param nbpers le nombre de personne pour la réservation
     * @param datepaie la date et l'heure du paiement au format dd/mm/yyyy hh24:mi
     * @param modepaie le mode de paiement utilisé
     * @throws SQLException erreur de connexion
     */
    public void reserverTable(Connection connection, int numtab, String dateres, int nbpers, String datepaie, String modepaie) throws SQLException {
        //Vérification du nombre de place reservee comparer au nombre de places disponibles
        String query = "SELECT nbplace FROM tabl WHERE numtab = ?";
        PreparedStatement statement = connection.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        statement.setInt(1, numtab);
        ResultSet resultat = statement.executeQuery();
        if(resultat.next()) {
            if(resultat.getInt(1) < nbpers) {
                System.out.println("la table numéro " + numtab + " possède " + resultat.getInt(1) + " places, " + nbpers + " reservee");
                return;
            }
        } else {
            System.out.println("la table numéro " + numtab + " n'existe pas");
            return;
        }

        //Verouillage de la table le temps de l'insertion
        query = "SELECT * FROM reservation for update";
        connection.createStatement().execute(query);

        try {
            //On vérifie que la table n'est pas déjà réservée sur ce crénaux
            query = "SELECT numres FROM reservation WHERE numtab = ? AND datres = to_date(?, 'dd/mm/yyyy hh24:mi')";
            statement = connection.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            statement.setInt(1, numtab);
            statement.setString(2, dateres);
            resultat = statement.executeQuery();
            if(resultat.next()) throw new IllegalArgumentException("La table est déjà réservée à ce crénaux");

            //Création d'un ID de réservation unique
            query = "SELECT max(numres) FROM reservation";
            resultat = connection.createStatement().executeQuery(query);
            int numres = 1;
            if(resultat.next()) {
                numres = resultat.getInt(1) + 1;
            }

            //Insertion de la réservation dans la table
            query = "insert into reservation values(?,?,to_date(?,'dd/mm/yyyy hh24:mi'),?,to_date(?,'dd/mm/yyyy hh24:mi'),?,null)";
            statement = connection.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            statement.setInt(1, numres);
            statement.setInt(2, numtab);
            statement.setString(3, dateres);
            statement.setInt(4, nbpers);
            statement.setString(5, datepaie);
            statement.setString(6, modepaie);

            statement.execute();
            connection.commit();
            System.out.println("La réservation a bien été effectuée");

        } catch (SQLException e) { //Erreur de connexion
            connection.rollback();
            System.out.println("Erreur de connexion, reservation annulée");
            e.printStackTrace();

        } catch (IllegalArgumentException e) { //Table déjà réservée
            connection.rollback();
            System.out.println(e.getMessage());
        }
    }

    /**
     * Permet de consulter les plats disponibles, bloque la table pour que les chiffres soient exact
     * @param connection la connexion à la table
     * @throws SQLException erreur de connexion
     */
    public void consulterPlatDispo(Connection connection) throws SQLException {
        //Bloque les nouvelles réservations
        String query = "SELECT * FROM reservation for update";
        connection.createStatement().execute(query);

        //Affiche le menu
        query = "SELECT * FROM plat WHERE qteservie > 0 ORDER BY type DESC for update";

        ResultSet resultat = connection.createStatement().executeQuery(query);

        System.out.println("Liste des plats : ");

        while(resultat.next()) {
            System.out.print(resultat.getString("type") + " : ");
            System.out.print("(" + resultat.getInt("qteservie") + ") ");
            System.out.print(resultat.getString("libelle") + " ");
            System.out.println(resultat.getDouble("prixunit") + "e");
        }
    }

    /**
     * Permet de commander une quantité d'un plat donné en paramètre, débloque la table
     * @param connection la connexion à la base de donnée
     * @param numres le numéro de la réservation à laquel assigné la commande
     * @param libelle le nom du plat à commander
     * @param quantite la quantité du plat à commander
     * @throws SQLException erreur de connexion à la base de donnée
     * @throws IllegalArgumentException si le numéro de réservation est mauvais
     */
    public void commanderPlat(Connection connection, int numres, String libelle, int quantite) throws SQLException, IllegalArgumentException {
        //vérification du numéro de réservation
        String query = "SELECT numres FROM reservation WHERE numres = ?";
        PreparedStatement statement = connection.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        statement.setInt(1, numres);
        ResultSet resultat = statement.executeQuery();
        if(!resultat.next()) {
            throw new IllegalArgumentException("Le numéro de réservation " + numres + " est inconnu");
        }

        //récupération du numéro de plat
        query = "SELECT numplat FROM plat WHERE libelle LIKE ? AND qteservie > 0";
        statement = connection.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        statement.setString(1, libelle);
        resultat = statement.executeQuery();

        if(resultat.next()) {
            int numplat = resultat.getInt(1);

            //vérification de la quantité commandée
            query = "SELECT qteservie FROM plat WHERE numplat = ?";
            statement = connection.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            statement.setInt(1, numplat);

            resultat = statement.executeQuery();
            resultat.next();
            int quantiteMax = resultat.getInt(1);
            if(quantite > quantiteMax) {
                System.out.println("Réservation de " + quantiteMax + " plats au lieu de " + quantite + " par manque de stock");
                quantite = quantiteMax;
            }

            //Enregistrement de la commande
            try {
                query = "INSERT INTO commande VALUES(?, ?, ?)";
                statement = connection.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                statement.setInt(1, numres);
                statement.setInt(2, numplat);
                statement.setInt(3, quantite);
                statement.execute();

                query = "UPDATE plat set qteservie = (qteservie - ?) WHERE numplat = ?";
                statement = connection.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                statement.setInt(1, quantite);
                statement.setInt(2, numplat);
                statement.execute();

                connection.commit();
                System.out.println(quantite + " " + libelle + " ont bien été commander");

            } catch (SQLException e) {//Erreur de connexion
                connection.rollback();
                System.out.println("Une erreur est survenue, annulation de la commande");
                e.printStackTrace();
            }
        } else {
            System.out.println("le plat " + libelle + " est inconnu ou en rupture de stock");
        }
    }

    public String getMail() {
        return mail;
    }

    public String getMdp() {
        return mdp;
    }

    public String getNom() {
        return nom;
    }
}
