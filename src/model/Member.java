package model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Classe representant un membre de la bibliotheque.
 * Un membre peut emprunter des livres et possede un historique d'emprunts.
 *
 * @author Saad LAGZIRI
 * @version 1.0
 */
public class Member implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Nombre maximum d'emprunts simultanes autorises */
    public static final int MAX_LOANS = 5;

    /** Identifiant unique du membre */
    private String id;

    /** Nom complet du membre */
    private String name;

    /** Adresse email du membre */
    private String email;

    /** Liste des livres actuellement empruntes */
    private List<Book> borrowedBooks;

    /** Date d'inscription du membre */
    private LocalDate registrationDate;

    /**
     * Constructeur d'un nouveau membre.
     * La date d'inscription est fixee a la date du jour.
     *
     * @param id    l'identifiant unique du membre
     * @param name  le nom complet du membre
     * @param email l'adresse email du membre
     */
    public Member(String id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.borrowedBooks = new ArrayList<>();
        this.registrationDate = LocalDate.now();
    }

    /**
     * Constructeur avec date d'inscription personnalisee.
     *
     * @param id               l'identifiant unique du membre
     * @param name             le nom complet du membre
     * @param email            l'adresse email du membre
     * @param registrationDate la date d'inscription
     */
    public Member(String id, String name, String email, LocalDate registrationDate) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.borrowedBooks = new ArrayList<>();
        this.registrationDate = registrationDate;
    }

    // ==================== Getters ====================

    /**
     * @return l'identifiant du membre
     */
    public String getId() {
        return id;
    }

    /**
     * @return le nom du membre
     */
    public String getName() {
        return name;
    }

    /**
     * @return l'adresse email du membre
     */
    public String getEmail() {
        return email;
    }

    /**
     * @return la liste des livres empruntes (copie defensive)
     */
    public List<Book> getBorrowedBooks() {
        return new ArrayList<>(borrowedBooks);
    }

    /**
     * @return la date d'inscription du membre
     */
    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    /**
     * @return le nombre de livres actuellement empruntes
     */
    public int getBorrowedCount() {
        return borrowedBooks.size();
    }

    // ==================== Setters ====================

    /**
     * Modifie le nom du membre.
     * @param name le nouveau nom
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Modifie l'adresse email du membre.
     * @param email la nouvelle adresse email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    // ==================== Gestion des emprunts ====================

    /**
     * Ajoute un livre a la liste des emprunts du membre.
     *
     * @param book le livre a emprunter
     * @return true si l'ajout a reussi, false si le maximum est atteint
     */
    public boolean addBorrowedBook(Book book) {
        if (borrowedBooks.size() >= MAX_LOANS) {
            return false;
        }
        return borrowedBooks.add(book);
    }

    /**
     * Retire un livre de la liste des emprunts du membre.
     *
     * @param book le livre a retirer
     * @return true si le livre a ete retire, false s'il n'etait pas emprunte
     */
    public boolean removeBorrowedBook(Book book) {
        return borrowedBooks.remove(book);
    }

    /**
     * Verifie si le membre peut encore emprunter des livres.
     *
     * @return true si le nombre d'emprunts est inferieur au maximum
     */
    public boolean canBorrow() {
        return borrowedBooks.size() < MAX_LOANS;
    }

    /**
     * Verifie si le membre a emprunte un livre specifique.
     *
     * @param book le livre a verifier
     * @return true si le membre a emprunte ce livre
     */
    public boolean hasBorrowed(Book book) {
        return borrowedBooks.contains(book);
    }

    // ==================== Methodes utilitaires ====================

    /**
     * Egalite basee sur l'identifiant du membre.
     *
     * @param o l'objet a comparer
     * @return true si les identifiants sont identiques
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Member member = (Member) o;
        return Objects.equals(id, member.id);
    }

    /**
     * @return le hashCode base sur l'identifiant
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    /**
     * Representation textuelle du membre.
     * @return les informations du membre sous forme de chaine
     */
    @Override
    public String toString() {
        return String.format("[%s] %s (%s) - Emprunts: %d/%d - Inscrit le: %s",
                id, name, email, borrowedBooks.size(), MAX_LOANS, registrationDate);
    }
}
