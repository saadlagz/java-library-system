package service;

import model.Book;
import model.Member;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Moteur de recherche avance pour la bibliotheque.
 * Permet d'effectuer des recherches multi-criteres sur les livres
 * et les membres avec des filtres combinables.
 *
 * @author Saad LAGZIRI
 * @version 1.0
 */
public class SearchEngine {

    /** Reference vers le service de la bibliotheque */
    private final LibraryService libraryService;

    /**
     * Constructeur du moteur de recherche.
     *
     * @param libraryService le service de bibliotheque a interroger
     */
    public SearchEngine(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    /**
     * Recherche avancee de livres avec filtres multiples.
     * Tous les filtres non-null sont appliques en combinaison (ET logique).
     *
     * @param title         filtre sur le titre (contient, insensible a la casse)
     * @param author        filtre sur l'auteur (contient, insensible a la casse)
     * @param genre         filtre sur le genre (egal, insensible a la casse)
     * @param yearFrom      annee minimum de publication (incluse), ou null
     * @param yearTo        annee maximum de publication (incluse), ou null
     * @param availableOnly si true, ne retourne que les livres disponibles
     * @return la liste des livres correspondant a tous les criteres
     */
    public List<Book> searchBooks(String title, String author, String genre,
                                   Integer yearFrom, Integer yearTo,
                                   boolean availableOnly) {
        List<Book> results = new ArrayList<>(libraryService.getAllBooks());

        // Filtre par titre
        if (title != null && !title.trim().isEmpty()) {
            String lowerTitle = title.toLowerCase();
            results = results.stream()
                    .filter(book -> book.getTitle().toLowerCase().contains(lowerTitle))
                    .collect(Collectors.toList());
        }

        // Filtre par auteur
        if (author != null && !author.trim().isEmpty()) {
            String lowerAuthor = author.toLowerCase();
            results = results.stream()
                    .filter(book -> book.getAuthor().toLowerCase().contains(lowerAuthor))
                    .collect(Collectors.toList());
        }

        // Filtre par genre
        if (genre != null && !genre.trim().isEmpty()) {
            String lowerGenre = genre.toLowerCase();
            results = results.stream()
                    .filter(book -> book.getGenre().toLowerCase().equals(lowerGenre))
                    .collect(Collectors.toList());
        }

        // Filtre par annee minimum
        if (yearFrom != null) {
            results = results.stream()
                    .filter(book -> book.getYear() >= yearFrom)
                    .collect(Collectors.toList());
        }

        // Filtre par annee maximum
        if (yearTo != null) {
            results = results.stream()
                    .filter(book -> book.getYear() <= yearTo)
                    .collect(Collectors.toList());
        }

        // Filtre par disponibilite
        if (availableOnly) {
            results = results.stream()
                    .filter(Book::isAvailable)
                    .collect(Collectors.toList());
        }

        return results;
    }

    /**
     * Recherche de livres par genre.
     *
     * @param genre le genre recherche
     * @return la liste des livres du genre specifie
     */
    public List<Book> searchByGenre(String genre) {
        return searchBooks(null, null, genre, null, null, false);
    }

    /**
     * Recherche de livres par auteur.
     *
     * @param author le nom de l'auteur
     * @return la liste des livres de l'auteur
     */
    public List<Book> searchByAuthor(String author) {
        return searchBooks(null, author, null, null, null, false);
    }

    /**
     * Recherche de livres par plage d'annees.
     *
     * @param yearFrom l'annee de debut (incluse)
     * @param yearTo   l'annee de fin (incluse)
     * @return la liste des livres publies dans la plage
     */
    public List<Book> searchByYearRange(int yearFrom, int yearTo) {
        return searchBooks(null, null, null, yearFrom, yearTo, false);
    }

    /**
     * Recherche de livres disponibles par mot-cle.
     *
     * @param keyword le mot-cle de recherche
     * @return la liste des livres disponibles correspondants
     */
    public List<Book> searchAvailableBooks(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return libraryService.getAvailableBooks();
        }
        return searchBooks(keyword, null, null, null, null, true);
    }

    /**
     * Recherche avancee de membres avec filtres.
     *
     * @param name            filtre sur le nom (contient, insensible a la casse)
     * @param email           filtre sur l'email (contient, insensible a la casse)
     * @param hasActiveLoans  si non-null, filtre sur l'existence d'emprunts actifs
     * @return la liste des membres correspondants
     */
    public List<Member> searchMembers(String name, String email, Boolean hasActiveLoans) {
        List<Member> results = new ArrayList<>(libraryService.getAllMembers());

        // Filtre par nom
        if (name != null && !name.trim().isEmpty()) {
            String lowerName = name.toLowerCase();
            results = results.stream()
                    .filter(member -> member.getName().toLowerCase().contains(lowerName))
                    .collect(Collectors.toList());
        }

        // Filtre par email
        if (email != null && !email.trim().isEmpty()) {
            String lowerEmail = email.toLowerCase();
            results = results.stream()
                    .filter(member -> member.getEmail().toLowerCase().contains(lowerEmail))
                    .collect(Collectors.toList());
        }

        // Filtre par emprunts actifs
        if (hasActiveLoans != null) {
            if (hasActiveLoans) {
                results = results.stream()
                        .filter(member -> member.getBorrowedCount() > 0)
                        .collect(Collectors.toList());
            } else {
                results = results.stream()
                        .filter(member -> member.getBorrowedCount() == 0)
                        .collect(Collectors.toList());
            }
        }

        return results;
    }

    /**
     * Recherche globale (livres et membres) par mot-cle.
     * Retourne les resultats sous forme de chaine formatee.
     *
     * @param keyword le mot-cle de recherche
     * @return les resultats de la recherche formates
     */
    public String globalSearch(String keyword) {
        StringBuilder results = new StringBuilder();
        results.append("=== Resultats de la recherche pour : \"").append(keyword).append("\" ===\n\n");

        // Recherche dans les livres
        List<Book> books = libraryService.searchBook(keyword);
        results.append("--- Livres (").append(books.size()).append(" resultat(s)) ---\n");
        if (books.isEmpty()) {
            results.append("  Aucun livre trouve.\n");
        } else {
            for (int i = 0; i < books.size(); i++) {
                results.append("  ").append(i + 1).append(". ").append(books.get(i)).append("\n");
            }
        }
        results.append("\n");

        // Recherche dans les membres
        List<Member> members = libraryService.searchMember(keyword);
        results.append("--- Membres (").append(members.size()).append(" resultat(s)) ---\n");
        if (members.isEmpty()) {
            results.append("  Aucun membre trouve.\n");
        } else {
            for (int i = 0; i < members.size(); i++) {
                results.append("  ").append(i + 1).append(". ").append(members.get(i)).append("\n");
            }
        }

        return results.toString();
    }

    /**
     * Retourne les genres disponibles dans la bibliotheque.
     *
     * @return la liste des genres distincts
     */
    public List<String> getAvailableGenres() {
        return libraryService.getAllBooks().stream()
                .map(Book::getGenre)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    /**
     * Retourne les auteurs disponibles dans la bibliotheque.
     *
     * @return la liste des auteurs distincts
     */
    public List<String> getAvailableAuthors() {
        return libraryService.getAllBooks().stream()
                .map(Book::getAuthor)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }
}
