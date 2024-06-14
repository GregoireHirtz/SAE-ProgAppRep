package Services.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.SqlDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Random;

public class Encryption_and_Json {
    public static ObjectMapper objectMapper = new ObjectMapper(); // Objet pour générer les Json

    static { // modification de la mise en forme des dates dans le Json
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        SimpleModule module = new SimpleModule();
        module.addSerializer(Date.class, new SqlDateSerializer());
        objectMapper.registerModule(module);
    }

    private static final int SALT_LENGTH = 16;
    private static final String saltChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789?!_;:$&()";

    /**
     * Méthode pour générer du sel à ajouter aux messages chiffrer pour plus de sécurité
     * @return une chaîne de charactère de taille SALT_LENGTH aléatoire
     */
    private static String generateSalt() {
        Random random = new SecureRandom();
        StringBuilder salt = new StringBuilder(SALT_LENGTH);
        for (int i = 0; i < SALT_LENGTH; i++) {
            salt.append(saltChars.charAt(random.nextInt(saltChars.length())));
        }
        return salt.toString();
    }

    /**
     * Méthode pour transformer un objet en string Json, en gérant les erreurs
     * @param object l'objet à transformer en Json
     * @return l'objet sous forme de Json
     * @throws RuntimeException En cas d'erreur pendant la création du Json
     */
    public static String getJson(Object object) throws RuntimeException {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException("Error while processing JSON");
        }
    }

    /**
     * Méthode pour chiffrer un message à partir d'une clé RSA
     * @param key clé publique de chiffrement
     * @param message le message à chiffrer
     * @return le message chiffré, au format base 64
     * @throws RuntimeException En cas d'erreur de chiffrement (offusqué)
     */
    public static String encryptMessage(PublicKey key, String message) throws RuntimeException {
        try {
            message = message + generateSalt();
            Cipher encryptCipher = Cipher.getInstance("RSA");
            encryptCipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] secretMessageBytes = message.getBytes(StandardCharsets.UTF_8);
            byte[] encryptedMessageBytes = encryptCipher.doFinal(secretMessageBytes);
            return Base64.getEncoder().encodeToString(encryptedMessageBytes);

        } catch (NoSuchPaddingException | IllegalArgumentException | BadPaddingException | InvalidKeyException |
                 IllegalBlockSizeException | NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur");
        }

    }

    /**
     * Méthode pour chiffrer un message à partir d'une clé RSA
     * @param key clé privée de déchiffrement
     * @param message le message à déchiffrer
     * @return le message déchiffré
     * @throws RuntimeException En cas d'erreur de déchiffrement (offusqué)
     */
    public static String decryptMessage(PrivateKey key, String message) throws RuntimeException {
        try{
            Cipher decryptCipher = Cipher.getInstance("RSA");
            decryptCipher.init(Cipher.DECRYPT_MODE, key);
            byte[] encryptedMessageBytes = Base64.getDecoder().decode(message);
            byte[] decryptedMessageBytes = decryptCipher.doFinal(encryptedMessageBytes);
            String res = new String(decryptedMessageBytes, StandardCharsets.UTF_8);
            return res.substring(0, res.length() - SALT_LENGTH);
        } catch (NoSuchPaddingException | IllegalBlockSizeException | NoSuchAlgorithmException | BadPaddingException |
                 InvalidKeyException | IllegalArgumentException e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur");
        }
    }

    public static void main(String[] args) throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        String originalMessage = "Bonjour le monde !";
        System.out.println("Message original: " + originalMessage);

        String encryptedMessage = encryptMessage(keyPair.getPublic(), originalMessage);
        System.out.println("Message chiffré: " + encryptedMessage);

        String decryptedMessage = decryptMessage(keyPair.getPrivate(), encryptedMessage);
        System.out.println("Message déchiffré: " + decryptedMessage);
    }
}
