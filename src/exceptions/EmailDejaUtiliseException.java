package exceptions;

/**
 * Exception levée quand on tente d'enregistrer un email déjà utilisé.
 */
public class EmailDejaUtiliseException extends Exception {
    public EmailDejaUtiliseException(String email) {
        super("L'email '" + email + "' est déjà utilisé par un autre utilisateur.");
    }
}