package model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

/**
 * Classe representant un emprunt de livre dans la bibliotheque.
 * Un emprunt lie un livre a un membre avec des dates de pret et de retour.
 *
 * @author Saad LAGZIRI
 * @version 1.0
 */
public class Loan implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Duree par defaut d'un emprunt en jours */
    public static final int DEFAULT_LOAN_DURATION = 21;

    /** Livre emprunte */
    private Book book;

    /** Membre emprunteur */
    private Member member;

    /** Date de l'emprunt */
    private LocalDate loanDate;

    /** Date limite de retour */
    private LocalDate dueDate;

    /** Date effective de retour (null si pas encore retourne) */
    private LocalDate returnDate;

    /** Indique si le livre a ete retourne */
    private boolean isReturned;

    /**
     * Constructeur d'un nouvel emprunt.
     * La date d'emprunt est fixee a aujourd'hui et la date limite
     * est calculee automatiquement (21 jours par defaut).
     *
     * @param book   le livre emprunte
     * @param member le membre emprunteur
     */
    public Loan(Book book, Member member) {
        this.book = book;
        this.member = member;
        this.loanDate = LocalDate.now();
        this.dueDate = loanDate.plusDays(DEFAULT_LOAN_DURATION);
        this.returnDate = null;
        this.isReturned = false;
    }

    /**
     * Constructeur avec dates personnalisees.
     *
     * @param book     le livre emprunte
     * @param member   le membre emprunteur
     * @param loanDate la date de l'emprunt
     * @param dueDate  la date limite de retour
     */
    public Loan(Book book, Member member, LocalDate loanDate, LocalDate dueDate) {
        this.book = book;
        this.member = member;
        this.loanDate = loanDate;
        this.dueDate = dueDate;
        this.returnDate = null;
        this.isReturned = false;
    }

    // ==================== Getters ====================

    /**
     * @return le livre emprunte
     */
    public Book getBook() {
        return book;
    }

    /**
     * @return le membre emprunteur
     */
    public Member getMember() {
        return member;
    }

    /**
     * @return la date de l'emprunt
     */
    public LocalDate getLoanDate() {
        return loanDate;
    }

    /**
     * @return la date limite de retour
     */
    public LocalDate getDueDate() {
        return dueDate;
    }

    /**
     * @return la date effective de retour, ou null si pas encore retourne
     */
    public LocalDate getReturnDate() {
        return returnDate;
    }

    /**
     * @return true si le livre a ete retourne
     */
    public boolean isReturned() {
        return isReturned;
    }

    // ==================== Methodes metier ====================

    /**
     * Effectue le retour du livre.
     * Met a jour la date de retour et le statut.
     */
    public void returnBook() {
        this.returnDate = LocalDate.now();
        this.isReturned = true;
    }

    /**
     * Effectue le retour du livre a une date donnee.
     *
     * @param date la date de retour
     */
    public void returnBook(LocalDate date) {
        this.returnDate = date;
        this.isReturned = true;
    }

    /**
     * Verifie si l'emprunt est en retard.
     * Un emprunt est en retard si la date limite est depassee
     * et le livre n'a pas encore ete retourne.
     *
     * @return true si l'emprunt est en retard
     */
    public boolean isOverdue() {
        if (isReturned) {
            return returnDate.isAfter(dueDate);
        }
        return LocalDate.now().isAfter(dueDate);
    }

    /**
     * Calcule le nombre de jours de retard.
     *
     * @return le nombre de jours de retard (0 si pas en retard)
     */
    public long getOverdueDays() {
        if (!isOverdue()) {
            return 0;
        }
        LocalDate referenceDate = isReturned ? returnDate : LocalDate.now();
        return ChronoUnit.DAYS.between(dueDate, referenceDate);
    }

    /**
     * Calcule le nombre de jours restants avant la date limite.
     *
     * @return le nombre de jours restants (negatif si en retard)
     */
    public long getDaysRemaining() {
        if (isReturned) {
            return 0;
        }
        return ChronoUnit.DAYS.between(LocalDate.now(), dueDate);
    }

    // ==================== Methodes utilitaires ====================

    /**
     * Egalite basee sur le livre, le membre et la date d'emprunt.
     *
     * @param o l'objet a comparer
     * @return true si les emprunts sont identiques
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Loan loan = (Loan) o;
        return Objects.equals(book, loan.book) &&
               Objects.equals(member, loan.member) &&
               Objects.equals(loanDate, loan.loanDate);
    }

    /**
     * @return le hashCode base sur le livre, le membre et la date
     */
    @Override
    public int hashCode() {
        return Objects.hash(book, member, loanDate);
    }

    /**
     * Representation textuelle de l'emprunt.
     * @return les informations de l'emprunt sous forme de chaine
     */
    @Override
    public String toString() {
        String status;
        if (isReturned) {
            status = "Retourne le " + returnDate;
        } else if (isOverdue()) {
            status = "EN RETARD (" + getOverdueDays() + " jours)";
        } else {
            status = "En cours (reste " + getDaysRemaining() + " jours)";
        }

        return String.format("Emprunt: \"%s\" par %s | Du %s au %s | %s",
                book.getTitle(), member.getName(), loanDate, dueDate, status);
    }
}
