package model;

import java.io.Serializable;
import java.util.Objects;

/**
 * Classe representant un livre dans la bibliotheque.
 * Implemente Serializable pour la persistance des donnees.
 *
 * @author Saad LAGZIRI
 * @version 1.0
 */
public class Book implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Titre du livre */
    private String title;

    /** Auteur du livre */
    private String author;

    /** Numero ISBN unique */
    private String isbn;

    /** Annee de publication */
    private int year;

    /** Indique si le livre est disponible a l'emprunt */
    private boolean available;

    /** Genre du livre (Roman, Science-Fiction, Informatique, etc.) */
    private String genre;

    /**
     * Constructeur complet d'un livre.
     *
     * @param title     le titre du livre
     * @param author    l'auteur du livre
     * @param isbn      le numero ISBN
     * @param year      l'annee de publication
     * @param genre     le genre du livre
     */
    public Book(String title, String author, String isbn, int year, String genre) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.year = year;
        this.available = true;
        this.genre = genre;
    }

    /**
     * Constructeur simplifie (genre par defaut : "Non classe").
     *
     * @param title     le titre du livre
     * @param author    l'auteur du livre
     * @param isbn      le numero ISBN
     * @param year      l'annee de publication
     */
    public Book(String title, String author, String isbn, int year) {
        this(title, author, isbn, year, "Non classe");
    }

    // ==================== Getters ====================

    /**
     * @return le titre du livre
     */
    public String getTitle() {
        return title;
    }

    /**
     * @return l'auteur du livre
     */
    public String getAuthor() {
        return author;
    }

    /**
     * @return le numero ISBN du livre
     */
    public String getIsbn() {
        return isbn;
    }

    /**
     * @return l'annee de publication
     */
    public int getYear() {
        return year;
    }

    /**
     * @return true si le livre est disponible, false sinon
     */
    public boolean isAvailable() {
        return available;
    }

    /**
     * @return le genre du livre
     */
    public String getGenre() {
        return genre;
    }

    // ==================== Setters ====================

    /**
     * Modifie le titre du livre.
     * @param title le nouveau titre
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Modifie l'auteur du livre.
     * @param author le nouvel auteur
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * Modifie la disponibilite du livre.
     * @param available true si disponible, false sinon
     */
    public void setAvailable(boolean available) {
        this.available = available;
    }

    /**
     * Modifie le genre du livre.
     * @param genre le nouveau genre
     */
    public void setGenre(String genre) {
        this.genre = genre;
    }

    /**
     * Modifie l'annee de publication.
     * @param year la nouvelle annee
     */
    public void setYear(int year) {
        this.year = year;
    }

    // ==================== Methodes utilitaires ====================

    /**
     * Verifie l'egalite entre deux livres basee sur l'ISBN.
     * Deux livres sont consideres egaux s'ils ont le meme ISBN.
     *
     * @param o l'objet a comparer
     * @return true si les ISBN sont identiques
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(isbn, book.isbn);
    }

    /**
     * Calcule le hashCode base sur l'ISBN.
     * @return le hashCode du livre
     */
    @Override
    public int hashCode() {
        return Objects.hash(isbn);
    }

    /**
     * Representation textuelle du livre.
     * @return une chaine contenant les informations du livre
     */
    @Override
    public String toString() {
        return String.format("[%s] \"%s\" par %s (%d) - Genre: %s - %s",
                isbn, title, author, year, genre,
                available ? "Disponible" : "Emprunte");
    }
}
