package exceptions;

/**
 * Exception levée quand un utilisateur est introuvable.
 */
public class UtilisateurIntrouvableException extends Exception {
    public UtilisateurIntrouvableException(int id) {
        super("Aucun utilisateur trouvé avec l'ID : " + id);
    }
    public UtilisateurIntrouvableException(String email) {
        super("Aucun utilisateur trouvé avec l'email : " + email);
    }
}