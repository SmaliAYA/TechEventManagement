package exceptions;

/**
 * Exception personnalisée utilisée lorsque
 * un stand demandé n’est pas trouvé dans le système.
 *
 * Elle permet de gérer proprement les erreurs liées aux stands.
 */
public class StandNotFoundException extends Exception {

    /**
     * Constructeur de l’exception.
     * Il reçoit un message d’erreur qui sera affiché
     * lorsqu’on lance l’exception.
     */
    public StandNotFoundException(String message) {
        super(message);
    }
}