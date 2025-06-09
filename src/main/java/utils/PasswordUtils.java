package utils;

import java.util.Base64;

public class PasswordUtils {

    // Méthode simplifiée pour "hacher" un mot de passe (en réalité, ne fait rien)
    public static String hashPasswordWithSalt(String password) {
        // Retourne le mot de passe tel quel
        return password;
    }

    // Méthode simplifiée pour vérifier un mot de passe
    public static boolean verifyPassword(String password, String storedPassword) {
        // Compare directement les chaînes de caractères
        return password.equals(storedPassword);
    }

    // Vérifier si un mot de passe est valide (au moins 8 caractères et contient au moins un chiffre)
    public static boolean isValidPassword(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }

        // Vérifier qu'il y a au moins un chiffre
        return password.matches(".*\\d.*");
    }

    // Vérifier si une adresse email est valide
    public static boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }

        // Expression régulière simple pour valider le format de l'email
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }
}