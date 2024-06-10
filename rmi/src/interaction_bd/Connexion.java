package interaction_bd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Connexion {

    /**
     * Methode pour connecter un utilisateur à l'application
     * @param connection la connexion à la base de donnée
     * @param mail l'email de l'utilisateur
     * @param mdp le mot de passe de l'utilisateur
     * @return Object de type Serveur ou Gestionnaire en fonction des droits de l'utilisateur, ou NULL si aucune correspondance n'a été trouvée
     * @throws SQLException erreur de connexion
     */
    public static Serveur connecterUtilisateur(Connection connection, String mail, String mdp) throws SQLException {
        String query = "SELECT email, passwd, nomserv, grade FROM serveur WHERE email like ? AND passwd like ?";
        PreparedStatement statement = connection.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        statement.setString(1, mail);
        statement.setString(2, mdp);

        ResultSet resultat = statement.executeQuery();
        if(resultat.next()) {
            String grade = resultat.getString(4);
            if(grade.equals("serveur")) {
                return new Serveur(resultat.getString(1), resultat.getString(2), resultat.getString(3));
            } else if(grade.equals("gestionnaire")) {
                return new Gestionnaire(resultat.getString(1), resultat.getString(2), resultat.getString(3));
            } else return null;
        }
        else return null;
    }
}
