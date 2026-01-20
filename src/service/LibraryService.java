package service;

import exception.BookNotAvailableException;
import exception.MaxLoansExceededException;
import exception.MemberNotFoundException;
import model.Book;
import model.Loan;
import model.Member;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service principal de gestion de la bibliotheque.
 * Cette classe centralise toutes les operations sur les livres,
 * les membres et les emprunts.
 *
 * @author Saad LAGZIRI
 * @version 1.0
 */
public class LibraryService implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Liste de tous les livres de la bibliotheque */
    private List<Book> books;

    /** Liste de tous les membres inscrits */
    private List<Member> members;

    /** Liste de tous les emprunts (actifs et retournes) */
    private List<Loan> loans;

    /** Compteur pour generer des identifiants uniques de membres */
    private int memberIdCounter;

    /**
     * Constructeur du service de bibliotheque.
     * Initialise les listes vides et le compteur de membres.
     */
    public LibraryService() {
        this.books = new ArrayList<>();
        this.members = new ArrayList<>();
        this.loans = new ArrayList<>();
        this.memberIdCounter = 1;
    }

    // ==================== Gestion des livres ====================

    /**
     * Ajoute un livre a la bibliotheque.
     * Verifie que le livre n'existe pas deja (par ISBN).
     *
     * @param book le livre a ajouter
     * @return true si le livre a ete ajoute, false si l'ISBN existe deja
     */
    public boolean addBook(Book book) {
        if (book == null) {
            return false;
        }
        // Verifier si un livre avec le meme ISBN existe deja
        for (Book existingBook : books) {
            if (existingBook.getIsbn().equals(book.getIsbn())) {
                return false;
            }
        }
        return books.add(book);
    }

    /**
     * Supprime un livre de la bibliotheque.
     * Le livre ne peut etre supprime que s'il n'est pas actuellement emprunte.
     *
     * @param isbn le numero ISBN du livre a supprimer
     * @return true si le livre a ete supprime, false sinon
     * @throws BookNotAvailableException si le livre est actuellement emprunte
     */
    public boolean removeBook(String isbn) throws BookNotAvailableException {
        Book book = findBookByIsbn(isbn);
        if (book == null) {
            return false;
        }
        if (!book.isAvailable()) {
            throw new BookNotAvailableException(
                "Impossible de supprimer le livre \"" + book.getTitle() +
                "\" : il est actuellement emprunte.");
        }
        return books.remove(book);
    }

    /**
     * Recherche des livres par mot-cle.
     * La recherche s'effectue sur le titre, l'auteur et le genre.
     *
     * @param keyword le mot-cle de recherche (insensible a la casse)
     * @return la liste des livres correspondants
     */
    public List<Book> searchBook(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return new ArrayList<>(books);
        }
        String lowerKeyword = keyword.toLowerCase();
        return books.stream()
                .filter(book ->
                    book.getTitle().toLowerCase().contains(lowerKeyword) ||
                    book.getAuthor().toLowerCase().contains(lowerKeyword) ||
                    book.getGenre().toLowerCase().contains(lowerKeyword) ||
                    book.getIsbn().toLowerCase().contains(lowerKeyword))
                .collect(Collectors.toList());
    }

    /**
     * Recherche un livre par son ISBN.
     *
     * @param isbn le numero ISBN
     * @return le livre trouve ou null s'il n'existe pas
     */
    public Book findBookByIsbn(String isbn) {
        return books.stream()
                .filter(book -> book.getIsbn().equals(isbn))
                .findFirst()
                .orElse(null);
    }

    /**
     * @return la liste de tous les livres disponibles a l'emprunt
     */
    public List<Book> getAvailableBooks() {
        return books.stream()
                .filter(Book::isAvailable)
                .collect(Collectors.toList());
    }

    /**
     * @return la liste de tous les livres de la bibliotheque
     */
    public List<Book> getAllBooks() {
        return new ArrayList<>(books);
    }

    // ==================== Gestion des membres ====================

    /**
     * Ajoute un nouveau membre a la bibliotheque.
     * Un identifiant unique est genere automatiquement.
     *
     * @param name  le nom du membre
     * @param email l'adresse email du membre
     * @return le membre cree
     */
    public Member addMember(String name, String email) {
        String id = String.format("MBR-%04d", memberIdCounter++);
        Member member = new Member(id, name, email);
        members.add(member);
        return member;
    }

    /**
     * Ajoute un membre existant a la bibliotheque.
     *
     * @param member le membre a ajouter
     * @return true si le membre a ete ajoute
     */
    public boolean addMember(Member member) {
        if (member == null) {
            return false;
        }
        for (Member existing : members) {
            if (existing.getId().equals(member.getId())) {
                return false;
            }
        }
        return members.add(member);
    }

    /**
     * Supprime un membre de la bibliotheque.
     * Le membre ne peut etre supprime que s'il n'a aucun emprunt en cours.
     *
     * @param memberId l'identifiant du membre a supprimer
     * @return true si le membre a ete supprime
     * @throws MemberNotFoundException si le membre n'existe pas
     */
    public boolean removeMember(String memberId) throws MemberNotFoundException {
        Member member = findMemberById(memberId);
        if (member == null) {
            throw new MemberNotFoundException(
                "Aucun membre trouve avec l'identifiant : " + memberId);
        }
        if (member.getBorrowedCount() > 0) {
            return false;
        }
        return members.remove(member);
    }

    /**
     * Recherche des membres par mot-cle.
     *
     * @param keyword le mot-cle de recherche
     * @return la liste des membres correspondants
     */
    public List<Member> searchMember(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return new ArrayList<>(members);
        }
        String lowerKeyword = keyword.toLowerCase();
        return members.stream()
                .filter(member ->
                    member.getName().toLowerCase().contains(lowerKeyword) ||
                    member.getEmail().toLowerCase().contains(lowerKeyword) ||
                    member.getId().toLowerCase().contains(lowerKeyword))
                .collect(Collectors.toList());
    }

    /**
     * Recherche un membre par son identifiant.
     *
     * @param id l'identifiant du membre
     * @return le membre trouve ou null
     */
    public Member findMemberById(String id) {
        return members.stream()
                .filter(member -> member.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    /**
     * @return la liste de tous les membres
     */
    public List<Member> getAllMembers() {
        return new ArrayList<>(members);
    }

    // ==================== Gestion des emprunts ====================

    /**
     * Effectue un emprunt de livre par un membre.
     *
     * @param member le membre emprunteur
     * @param book   le livre a emprunter
     * @return l'objet Loan cree
     * @throws BookNotAvailableException   si le livre n'est pas disponible
     * @throws MaxLoansExceededException   si le membre a atteint son maximum d'emprunts
     * @throws MemberNotFoundException     si le membre n'est pas enregistre
     */
    public Loan borrowBook(Member member, Book book)
            throws BookNotAvailableException, MaxLoansExceededException, MemberNotFoundException {

        // Verifier que le membre existe
        if (!members.contains(member)) {
            throw new MemberNotFoundException(
                "Le membre \"" + member.getName() + "\" n'est pas inscrit.");
        }

        // Verifier que le livre est disponible
        if (!book.isAvailable()) {
            throw new BookNotAvailableException(
                "Le livre \"" + book.getTitle() + "\" n'est pas disponible.");
        }

        // Verifier la limite d'emprunts
        if (!member.canBorrow()) {
            throw new MaxLoansExceededException(
                "Le membre \"" + member.getName() + "\" a atteint le maximum de " +
                Member.MAX_LOANS + " emprunts.");
        }

        // Creer l'emprunt
        Loan loan = new Loan(book, member);
        book.setAvailable(false);
        member.addBorrowedBook(book);
        loans.add(loan);

        return loan;
    }

    /**
     * Effectue le retour d'un livre emprunte.
     *
     * @param loan l'emprunt a retourner
     * @return true si le retour a ete effectue
     */
    public boolean returnBook(Loan loan) {
        if (loan == null || loan.isReturned()) {
            return false;
        }

        loan.returnBook();
        loan.getBook().setAvailable(true);
        loan.getMember().removeBorrowedBook(loan.getBook());
        return true;
    }

    /**
     * Retourne la liste des emprunts en retard.
     *
     * @return la liste des emprunts dont la date limite est depassee
     */
    public List<Loan> getOverdueLoans() {
        return loans.stream()
                .filter(loan -> !loan.isReturned() && loan.isOverdue())
                .collect(Collectors.toList());
    }

    /**
     * Retourne l'historique des emprunts d'un membre.
     *
     * @param member le membre dont on veut l'historique
     * @return la liste des emprunts du membre
     */
    public List<Loan> getMemberBorrowHistory(Member member) {
        return loans.stream()
                .filter(loan -> loan.getMember().equals(member))
                .collect(Collectors.toList());
    }

    /**
     * Retourne la liste des emprunts actifs (non retournes).
     *
     * @return la liste des emprunts en cours
     */
    public List<Loan> getActiveLoans() {
        return loans.stream()
                .filter(loan -> !loan.isReturned())
                .collect(Collectors.toList());
    }

    /**
     * @return la liste de tous les emprunts
     */
    public List<Loan> getAllLoans() {
        return new ArrayList<>(loans);
    }

    // ==================== Statistiques rapides ====================

    /**
     * @return le nombre total de livres
     */
    public int getTotalBooks() {
        return books.size();
    }

    /**
     * @return le nombre total de membres
     */
    public int getTotalMembers() {
        return members.size();
    }

    /**
     * @return le nombre total d'emprunts actifs
     */
    public int getActiveLoansCount() {
        return (int) loans.stream().filter(loan -> !loan.isReturned()).count();
    }

    /**
     * @return le nombre total d'emprunts en retard
     */
    public int getOverdueLoansCount() {
        return getOverdueLoans().size();
    }
}
