package exception;

/**
 * Exception levee lorsqu'un membre a atteint le nombre maximum d'emprunts.
 * Chaque membre est limite a un certain nombre d'emprunts simultanes
 * (defini dans Member.MAX_LOANS).
 *
 * @author Saad LAGZIRI
 * @version 1.0
 */
public class MaxLoansExceededException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * Constructeur avec message par defaut.
     */
    public MaxLoansExceededException() {
        super("Le nombre maximum d'emprunts simultanes a ete atteint.");
    }

    /**
     * Constructeur avec message personnalise.
     *
     * @param message le message d'erreur
     */
    public MaxLoansExceededException(String message) {
        super(message);
    }

    /**
     * Constructeur avec message et cause.
     *
     * @param message le message d'erreur
     * @param cause   la cause de l'exception
     */
    public MaxLoansExceededException(String message, Throwable cause) {
        super(message, cause);
    }
}
