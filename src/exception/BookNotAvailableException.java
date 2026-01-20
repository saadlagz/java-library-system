package exception;

/**
 * Exception levee lorsqu'un livre n'est pas disponible a l'emprunt.
 * Cette exception est utilisee quand un membre tente d'emprunter
 * un livre deja emprunte par un autre membre.
 *
 * @author Saad LAGZIRI
 * @version 1.0
 */
public class BookNotAvailableException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * Constructeur avec message par defaut.
     */
    public BookNotAvailableException() {
        super("Ce livre n'est pas disponible actuellement.");
    }

    /**
     * Constructeur avec message personnalise.
     *
     * @param message le message d'erreur
     */
    public BookNotAvailableException(String message) {
        super(message);
    }

    /**
     * Constructeur avec message et cause.
     *
     * @param message le message d'erreur
     * @param cause   la cause de l'exception
     */
    public BookNotAvailableException(String message, Throwable cause) {
        super(message, cause);
    }
}
