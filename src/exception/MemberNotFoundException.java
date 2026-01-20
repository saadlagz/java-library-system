package exception;

/**
 * Exception levee lorsqu'un membre n'est pas trouve dans le systeme.
 * Cette exception est utilisee lors de la recherche d'un membre
 * par identifiant ou par nom.
 *
 * @author Saad LAGZIRI
 * @version 1.0
 */
public class MemberNotFoundException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * Constructeur avec message par defaut.
     */
    public MemberNotFoundException() {
        super("Membre non trouve dans le systeme.");
    }

    /**
     * Constructeur avec message personnalise.
     *
     * @param message le message d'erreur
     */
    public MemberNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructeur avec message et cause.
     *
     * @param message le message d'erreur
     * @param cause   la cause de l'exception
     */
    public MemberNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
