package ui;

import exception.BookNotAvailableException;
import exception.MaxLoansExceededException;
import exception.MemberNotFoundException;
import model.Book;
import model.Loan;
import model.Member;
import service.DataManager;
import service.LibraryService;
import util.DateUtils;
import util.InputValidator;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

/**
 * Interface console interactive pour le systeme de gestion de bibliotheque.
 * Gere l'affichage des menus, la saisie utilisateur et l'appel
 * aux services metier.
 *
 * @author Saad LAGZIRI
 * @version 1.0
 */
public class ConsoleMenu {

    /** Largeur de l'affichage console */
    private static final int CONSOLE_WIDTH = 60;

    /** Caractere pour les bordures horizontales */
    private static final char BORDER_CHAR = '=';

    /** Caractere pour les separateurs */
    private static final char SEPARATOR_CHAR = '-';

    /** Service principal de la bibliotheque */
    private LibraryService libraryService;

    /** Gestionnaire de donnees */
    private DataManager dataManager;

    /** Scanner pour la saisie utilisateur */
    private Scanner scanner;

    /** Nom du fichier de sauvegarde */
    private static final String SAVE_FILE = "library_data.ser";

    /**
     * Constructeur du menu console.
     *
     * @param libraryService le service de bibliotheque
     */
    public ConsoleMenu(LibraryService libraryService) {
        this.libraryService = libraryService;
        this.dataManager = new DataManager();
        this.scanner = new Scanner(System.in);
    }

    // ==================== Affichage et formatage ====================

    /**
     * Affiche une bordure horizontale.
     */
    private void printBorder() {
        System.out.println(String.valueOf(BORDER_CHAR).repeat(CONSOLE_WIDTH));
    }

    /**
     * Affiche un separateur leger.
     */
    private void printSeparator() {
        System.out.println(String.valueOf(SEPARATOR_CHAR).repeat(CONSOLE_WIDTH));
    }

    /**
     * Affiche un titre centre avec des bordures.
     *
     * @param title le titre a afficher
     */
    private void printTitle(String title) {
        printBorder();
        int padding = (CONSOLE_WIDTH - title.length() - 2) / 2;
        String paddingStr = " ".repeat(Math.max(0, padding));
        System.out.println("|" + paddingStr + title +
                paddingStr + (title.length() % 2 == 0 ? " |" : "|"));
        printBorder();
    }

    /**
     * Affiche un message d'information.
     *
     * @param message le message a afficher
     */
    private void printInfo(String message) {
        System.out.println("  [INFO] " + message);
    }

    /**
     * Affiche un message d'erreur.
     *
     * @param message le message d'erreur
     */
    private void printError(String message) {
        System.out.println("  [ERREUR] " + message);
    }

    /**
     * Affiche un message de succes.
     *
     * @param message le message de succes
     */
    private void printSuccess(String message) {
        System.out.println("  [OK] " + message);
    }

    /**
     * Lit une saisie utilisateur avec un message d'invite.
     *
     * @param prompt le message d'invite
     * @return la saisie de l'utilisateur
     */
    private String readInput(String prompt) {
        System.out.print("  > " + prompt + " : ");
        return scanner.nextLine().trim();
    }

    /**
     * Lit un entier avec gestion d'erreur.
     *
     * @param prompt le message d'invite
     * @return l'entier saisi, ou -1 en cas d'erreur
     */
    private int readInt(String prompt) {
        String input = readInput(prompt);
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            printError("Veuillez entrer un nombre valide.");
            return -1;
        }
    }

    /**
     * Attend que l'utilisateur appuie sur Entree.
     */
    private void waitForEnter() {
        System.out.print("\n  Appuyez sur Entree pour continuer...");
        scanner.nextLine();
    }

    // ==================== Menu principal ====================

    /**
     * Demarre l'interface console et affiche le menu principal.
     * Boucle jusqu'a ce que l'utilisateur choisisse de quitter.
     */
    public void start() {
        printTitle("SYSTEME DE GESTION DE BIBLIOTHEQUE");
        System.out.println("  Bienvenue dans le systeme de gestion de bibliotheque !");
        System.out.println();

        boolean running = true;
        while (running) {
            displayMainMenu();
            int choice = readInt("Votre choix");

            switch (choice) {
                case 1:
                    booksMenu();
                    break;
                case 2:
                    membersMenu();
                    break;
                case 3:
                    loansMenu();
                    break;
                case 4:
                    statisticsMenu();
                    break;
                case 5:
                    dataMenu();
                    break;
                case 0:
                    running = false;
                    handleExit();
                    break;
                default:
                    printError("Choix invalide. Veuillez reessayer.");
            }
        }
    }

    /**
     * Affiche le menu principal.
     */
    private void displayMainMenu() {
        System.out.println();
        printTitle("MENU PRINCIPAL");
        System.out.println("  1. Gestion des livres");
        System.out.println("  2. Gestion des membres");
        System.out.println("  3. Gestion des emprunts");
        System.out.println("  4. Statistiques");
        System.out.println("  5. Sauvegarder / Charger");
        System.out.println("  0. Quitter");
        printSeparator();
    }

    // ==================== Sous-menu Livres ====================

    /**
     * Affiche et gere le sous-menu de gestion des livres.
     */
    private void booksMenu() {
        boolean inMenu = true;
        while (inMenu) {
            System.out.println();
            printTitle("GESTION DES LIVRES");
            System.out.println("  1. Ajouter un livre");
            System.out.println("  2. Supprimer un livre");
            System.out.println("  3. Rechercher un livre");
            System.out.println("  4. Afficher tous les livres");
            System.out.println("  5. Afficher les livres disponibles");
            System.out.println("  0. Retour au menu principal");
            printSeparator();

            int choice = readInt("Votre choix");
            switch (choice) {
                case 1:
                    addBook();
                    break;
                case 2:
                    removeBook();
                    break;
                case 3:
                    searchBooks();
                    break;
                case 4:
                    displayAllBooks();
                    break;
                case 5:
                    displayAvailableBooks();
                    break;
                case 0:
                    inMenu = false;
                    break;
                default:
                    printError("Choix invalide.");
            }
        }
    }

    /**
     * Gere l'ajout d'un nouveau livre.
     */
    private void addBook() {
        System.out.println();
        printTitle("AJOUTER UN LIVRE");

        String title = readInput("Titre");
        if (!InputValidator.isNotEmpty(title)) {
            printError("Le titre ne peut pas etre vide.");
            return;
        }

        String author = readInput("Auteur");
        if (!InputValidator.isValidName(author)) {
            printError("Nom d'auteur invalide.");
            return;
        }

        String isbn = readInput("ISBN");
        if (!InputValidator.isValidISBN(isbn)) {
            printError("Numero ISBN invalide.");
            return;
        }

        int year = readInt("Annee de publication");
        if (!InputValidator.isValidYear(year)) {
            printError("Annee invalide.");
            return;
        }

        String genre = readInput("Genre (Roman, SF, Informatique, Histoire...)");
        if (!InputValidator.isNotEmpty(genre)) {
            genre = "Non classe";
        }

        Book book = new Book(title, author, isbn, year, genre);
        if (libraryService.addBook(book)) {
            printSuccess("Livre ajoute avec succes : " + book);
        } else {
            printError("Un livre avec cet ISBN existe deja.");
        }
    }

    /**
     * Gere la suppression d'un livre.
     */
    private void removeBook() {
        System.out.println();
        printTitle("SUPPRIMER UN LIVRE");

        String isbn = readInput("ISBN du livre a supprimer");
        try {
            if (libraryService.removeBook(isbn)) {
                printSuccess("Livre supprime avec succes.");
            } else {
                printError("Aucun livre trouve avec cet ISBN.");
            }
        } catch (BookNotAvailableException e) {
            printError(e.getMessage());
        }
    }

    /**
     * Gere la recherche de livres.
     */
    private void searchBooks() {
        System.out.println();
        printTitle("RECHERCHER UN LIVRE");

        String keyword = readInput("Mot-cle (titre, auteur, genre ou ISBN)");
        List<Book> results = libraryService.searchBook(keyword);

        if (results.isEmpty()) {
            printInfo("Aucun livre trouve pour : \"" + keyword + "\"");
        } else {
            printInfo(results.size() + " livre(s) trouve(s) :");
            printSeparator();
            for (int i = 0; i < results.size(); i++) {
                System.out.println("  " + (i + 1) + ". " + results.get(i));
            }
        }
        waitForEnter();
    }

    /**
     * Affiche tous les livres de la bibliotheque.
     */
    private void displayAllBooks() {
        System.out.println();
        printTitle("TOUS LES LIVRES");

        List<Book> books = libraryService.getAllBooks();
        if (books.isEmpty()) {
            printInfo("Aucun livre dans la bibliotheque.");
        } else {
            printInfo("Total : " + books.size() + " livre(s)");
            printSeparator();
            for (int i = 0; i < books.size(); i++) {
                System.out.println("  " + (i + 1) + ". " + books.get(i));
            }
        }
        waitForEnter();
    }

    /**
     * Affiche les livres disponibles.
     */
    private void displayAvailableBooks() {
        System.out.println();
        printTitle("LIVRES DISPONIBLES");

        List<Book> books = libraryService.getAvailableBooks();
        if (books.isEmpty()) {
            printInfo("Aucun livre disponible actuellement.");
        } else {
            printInfo(books.size() + " livre(s) disponible(s)");
            printSeparator();
            for (int i = 0; i < books.size(); i++) {
                System.out.println("  " + (i + 1) + ". " + books.get(i));
            }
        }
        waitForEnter();
    }

    // ==================== Sous-menu Membres ====================

    /**
     * Affiche et gere le sous-menu de gestion des membres.
     */
    private void membersMenu() {
        boolean inMenu = true;
        while (inMenu) {
            System.out.println();
            printTitle("GESTION DES MEMBRES");
            System.out.println("  1. Inscrire un membre");
            System.out.println("  2. Supprimer un membre");
            System.out.println("  3. Rechercher un membre");
            System.out.println("  4. Afficher tous les membres");
            System.out.println("  0. Retour au menu principal");
            printSeparator();

            int choice = readInt("Votre choix");
            switch (choice) {
                case 1:
                    addMember();
                    break;
                case 2:
                    removeMember();
                    break;
                case 3:
                    searchMembers();
                    break;
                case 4:
                    displayAllMembers();
                    break;
                case 0:
                    inMenu = false;
                    break;
                default:
                    printError("Choix invalide.");
            }
        }
    }

    /**
     * Gere l'inscription d'un nouveau membre.
     */
    private void addMember() {
        System.out.println();
        printTitle("INSCRIRE UN MEMBRE");

        String name = readInput("Nom complet");
        if (!InputValidator.isValidName(name)) {
            printError("Nom invalide (lettres, espaces et tirets uniquement).");
            return;
        }

        String email = readInput("Email");
        if (!InputValidator.isValidEmail(email)) {
            printError("Adresse email invalide.");
            return;
        }

        Member member = libraryService.addMember(name, email);
        printSuccess("Membre inscrit : " + member);
    }

    /**
     * Gere la suppression d'un membre.
     */
    private void removeMember() {
        System.out.println();
        printTitle("SUPPRIMER UN MEMBRE");

        String memberId = readInput("ID du membre a supprimer");
        try {
            if (libraryService.removeMember(memberId)) {
                printSuccess("Membre supprime avec succes.");
            } else {
                printError("Impossible de supprimer : le membre a des emprunts en cours.");
            }
        } catch (MemberNotFoundException e) {
            printError(e.getMessage());
        }
    }

    /**
     * Gere la recherche de membres.
     */
    private void searchMembers() {
        System.out.println();
        printTitle("RECHERCHER UN MEMBRE");

        String keyword = readInput("Mot-cle (nom, email ou ID)");
        List<Member> results = libraryService.searchMember(keyword);

        if (results.isEmpty()) {
            printInfo("Aucun membre trouve pour : \"" + keyword + "\"");
        } else {
            printInfo(results.size() + " membre(s) trouve(s) :");
            printSeparator();
            for (int i = 0; i < results.size(); i++) {
                System.out.println("  " + (i + 1) + ". " + results.get(i));
            }
        }
        waitForEnter();
    }

    /**
     * Affiche tous les membres.
     */
    private void displayAllMembers() {
        System.out.println();
        printTitle("TOUS LES MEMBRES");

        List<Member> members = libraryService.getAllMembers();
        if (members.isEmpty()) {
            printInfo("Aucun membre inscrit.");
        } else {
            printInfo("Total : " + members.size() + " membre(s)");
            printSeparator();
            for (int i = 0; i < members.size(); i++) {
                System.out.println("  " + (i + 1) + ". " + members.get(i));
            }
        }
        waitForEnter();
    }

    // ==================== Sous-menu Emprunts ====================

    /**
     * Affiche et gere le sous-menu de gestion des emprunts.
     */
    private void loansMenu() {
        boolean inMenu = true;
        while (inMenu) {
            System.out.println();
            printTitle("GESTION DES EMPRUNTS");
            System.out.println("  1. Emprunter un livre");
            System.out.println("  2. Retourner un livre");
            System.out.println("  3. Afficher les emprunts en cours");
            System.out.println("  4. Afficher les retards");
            System.out.println("  5. Historique d'un membre");
            System.out.println("  0. Retour au menu principal");
            printSeparator();

            int choice = readInt("Votre choix");
            switch (choice) {
                case 1:
                    borrowBook();
                    break;
                case 2:
                    returnBook();
                    break;
                case 3:
                    displayActiveLoans();
                    break;
                case 4:
                    displayOverdueLoans();
                    break;
                case 5:
                    displayMemberHistory();
                    break;
                case 0:
                    inMenu = false;
                    break;
                default:
                    printError("Choix invalide.");
            }
        }
    }

    /**
     * Gere l'emprunt d'un livre par un membre.
     */
    private void borrowBook() {
        System.out.println();
        printTitle("EMPRUNTER UN LIVRE");

        String memberId = readInput("ID du membre");
        Member member = libraryService.findMemberById(memberId);
        if (member == null) {
            printError("Aucun membre trouve avec l'ID : " + memberId);
            return;
        }

        String isbn = readInput("ISBN du livre");
        Book book = libraryService.findBookByIsbn(isbn);
        if (book == null) {
            printError("Aucun livre trouve avec l'ISBN : " + isbn);
            return;
        }

        try {
            Loan loan = libraryService.borrowBook(member, book);
            printSuccess("Emprunt effectue avec succes !");
            System.out.println("  " + loan);
        } catch (BookNotAvailableException | MaxLoansExceededException | MemberNotFoundException e) {
            printError(e.getMessage());
        }
    }

    /**
     * Gere le retour d'un livre.
     */
    private void returnBook() {
        System.out.println();
        printTitle("RETOURNER UN LIVRE");

        List<Loan> activeLoans = libraryService.getActiveLoans();
        if (activeLoans.isEmpty()) {
            printInfo("Aucun emprunt en cours.");
            return;
        }

        printInfo("Emprunts en cours :");
        printSeparator();
        for (int i = 0; i < activeLoans.size(); i++) {
            System.out.println("  " + (i + 1) + ". " + activeLoans.get(i));
        }

        int index = readInt("Numero de l'emprunt a retourner");
        if (index < 1 || index > activeLoans.size()) {
            printError("Numero invalide.");
            return;
        }

        Loan loan = activeLoans.get(index - 1);
        if (libraryService.returnBook(loan)) {
            printSuccess("Retour effectue avec succes !");
            if (loan.isOverdue()) {
                printInfo("Attention : retour en retard de " + loan.getOverdueDays() + " jour(s).");
            }
        } else {
            printError("Erreur lors du retour.");
        }
    }

    /**
     * Affiche les emprunts actifs.
     */
    private void displayActiveLoans() {
        System.out.println();
        printTitle("EMPRUNTS EN COURS");

        List<Loan> loans = libraryService.getActiveLoans();
        if (loans.isEmpty()) {
            printInfo("Aucun emprunt en cours.");
        } else {
            printInfo(loans.size() + " emprunt(s) en cours :");
            printSeparator();
            for (int i = 0; i < loans.size(); i++) {
                System.out.println("  " + (i + 1) + ". " + loans.get(i));
            }
        }
        waitForEnter();
    }

    /**
     * Affiche les emprunts en retard.
     */
    private void displayOverdueLoans() {
        System.out.println();
        printTitle("EMPRUNTS EN RETARD");

        List<Loan> overdue = libraryService.getOverdueLoans();
        if (overdue.isEmpty()) {
            printInfo("Aucun emprunt en retard. Tout est en ordre !");
        } else {
            printInfo(overdue.size() + " emprunt(s) en retard :");
            printSeparator();
            for (int i = 0; i < overdue.size(); i++) {
                System.out.println("  " + (i + 1) + ". " + overdue.get(i));
            }
        }
        waitForEnter();
    }

    /**
     * Affiche l'historique d'emprunts d'un membre.
     */
    private void displayMemberHistory() {
        System.out.println();
        printTitle("HISTORIQUE D'UN MEMBRE");

        String memberId = readInput("ID du membre");
        Member member = libraryService.findMemberById(memberId);
        if (member == null) {
            printError("Aucun membre trouve avec l'ID : " + memberId);
            return;
        }

        List<Loan> history = libraryService.getMemberBorrowHistory(member);
        System.out.println("  Membre : " + member.getName());
        printSeparator();

        if (history.isEmpty()) {
            printInfo("Aucun emprunt dans l'historique.");
        } else {
            printInfo(history.size() + " emprunt(s) dans l'historique :");
            for (int i = 0; i < history.size(); i++) {
                System.out.println("  " + (i + 1) + ". " + history.get(i));
            }
        }
        waitForEnter();
    }

    // ==================== Sous-menu Statistiques ====================

    /**
     * Affiche les statistiques de base de la bibliotheque.
     */
    private void statisticsMenu() {
        System.out.println();
        printTitle("STATISTIQUES");

        System.out.println("  Nombre total de livres     : " + libraryService.getTotalBooks());
        System.out.println("  Livres disponibles         : " + libraryService.getAvailableBooks().size());
        System.out.println("  Nombre total de membres    : " + libraryService.getTotalMembers());
        System.out.println("  Emprunts en cours          : " + libraryService.getActiveLoansCount());
        System.out.println("  Emprunts en retard         : " + libraryService.getOverdueLoansCount());

        printSeparator();
        waitForEnter();
    }

    // ==================== Sous-menu Donnees ====================

    /**
     * Affiche et gere le sous-menu de sauvegarde/chargement.
     */
    private void dataMenu() {
        boolean inMenu = true;
        while (inMenu) {
            System.out.println();
            printTitle("SAUVEGARDER / CHARGER");
            System.out.println("  1. Sauvegarder les donnees");
            System.out.println("  2. Charger les donnees");
            System.out.println("  3. Exporter les livres en CSV");
            System.out.println("  4. Exporter les membres en CSV");
            System.out.println("  5. Exporter les emprunts en CSV");
            System.out.println("  0. Retour au menu principal");
            printSeparator();

            int choice = readInt("Votre choix");
            switch (choice) {
                case 1:
                    saveData();
                    break;
                case 2:
                    loadData();
                    break;
                case 3:
                    exportBooksCSV();
                    break;
                case 4:
                    exportMembersCSV();
                    break;
                case 5:
                    exportLoansCSV();
                    break;
                case 0:
                    inMenu = false;
                    break;
                default:
                    printError("Choix invalide.");
            }
        }
    }

    /**
     * Sauvegarde les donnees.
     */
    private void saveData() {
        try {
            dataManager.saveLibrary(libraryService, SAVE_FILE);
            printSuccess("Donnees sauvegardees avec succes !");
        } catch (IOException e) {
            printError("Erreur lors de la sauvegarde : " + e.getMessage());
        }
    }

    /**
     * Charge les donnees.
     */
    private void loadData() {
        try {
            libraryService = dataManager.loadLibrary(SAVE_FILE);
            printSuccess("Donnees chargees avec succes !");
        } catch (IOException | ClassNotFoundException e) {
            printError("Erreur lors du chargement : " + e.getMessage());
        }
    }

    /**
     * Exporte les livres en CSV.
     */
    private void exportBooksCSV() {
        try {
            dataManager.exportBooksToCSV(libraryService.getAllBooks(), "livres.csv");
            printSuccess("Livres exportes dans livres.csv");
        } catch (IOException e) {
            printError("Erreur lors de l'export : " + e.getMessage());
        }
    }

    /**
     * Exporte les membres en CSV.
     */
    private void exportMembersCSV() {
        try {
            dataManager.exportMembersToCSV(libraryService.getAllMembers(), "membres.csv");
            printSuccess("Membres exportes dans membres.csv");
        } catch (IOException e) {
            printError("Erreur lors de l'export : " + e.getMessage());
        }
    }

    /**
     * Exporte les emprunts en CSV.
     */
    private void exportLoansCSV() {
        try {
            dataManager.exportLoansToCSV(libraryService.getAllLoans(), "emprunts.csv");
            printSuccess("Emprunts exportes dans emprunts.csv");
        } catch (IOException e) {
            printError("Erreur lors de l'export : " + e.getMessage());
        }
    }

    // ==================== Sortie ====================

    /**
     * Gere la sortie de l'application.
     * Propose de sauvegarder avant de quitter.
     */
    private void handleExit() {
        System.out.println();
        String save = readInput("Sauvegarder avant de quitter ? (o/n)");
        if (save.equalsIgnoreCase("o") || save.equalsIgnoreCase("oui")) {
            saveData();
        }
        System.out.println();
        printTitle("AU REVOIR !");
        System.out.println("  Merci d'avoir utilise le systeme de bibliotheque.");
        printBorder();
    }

    /**
     * @return le service de bibliotheque (peut etre mis a jour apres chargement)
     */
    public LibraryService getLibraryService() {
        return libraryService;
    }
}
