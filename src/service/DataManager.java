package service;

import model.Book;
import model.Loan;
import model.Member;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Gestionnaire de persistance des donnees de la bibliotheque.
 * Permet de sauvegarder et charger l'etat complet de la bibliotheque
 * via la serialisation Java, et d'exporter les donnees au format CSV.
 *
 * @author Saad LAGZIRI
 * @version 1.0
 */
public class DataManager {

    /** Nom du fichier de sauvegarde par defaut */
    private static final String DEFAULT_FILENAME = "library_data.ser";

    /**
     * Sauvegarde l'etat complet du service de bibliotheque dans un fichier.
     * Utilise la serialisation Java pour persister toutes les donnees.
     *
     * @param libraryService le service a sauvegarder
     * @param filename       le nom du fichier de sortie
     * @throws IOException en cas d'erreur d'ecriture
     */
    public void saveLibrary(LibraryService libraryService, String filename) throws IOException {
        if (filename == null || filename.isEmpty()) {
            filename = DEFAULT_FILENAME;
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(filename))) {
            oos.writeObject(libraryService);
            System.out.println("[INFO] Bibliotheque sauvegardee dans : " + filename);
        }
    }

    /**
     * Charge l'etat d'une bibliotheque depuis un fichier serialise.
     *
     * @param filename le nom du fichier a charger
     * @return le service de bibliotheque charge
     * @throws IOException            en cas d'erreur de lecture
     * @throws ClassNotFoundException si la classe serialisee n'est pas trouvee
     */
    public LibraryService loadLibrary(String filename) throws IOException, ClassNotFoundException {
        if (filename == null || filename.isEmpty()) {
            filename = DEFAULT_FILENAME;
        }

        Path path = Paths.get(filename);
        if (!Files.exists(path)) {
            System.out.println("[INFO] Aucun fichier de sauvegarde trouve. Creation d'une nouvelle bibliotheque.");
            return new LibraryService();
        }

        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(filename))) {
            LibraryService service = (LibraryService) ois.readObject();
            System.out.println("[INFO] Bibliotheque chargee depuis : " + filename);
            return service;
        }
    }

    /**
     * Exporte la liste des livres au format CSV.
     *
     * @param libraryService le service contenant les donnees
     * @param filename       le nom du fichier CSV de sortie
     * @throws IOException en cas d'erreur d'ecriture
     */
    public void exportToCSV(LibraryService libraryService, String filename) throws IOException {
        exportBooksToCSV(libraryService.getAllBooks(), filename);
    }

    /**
     * Exporte la liste des livres au format CSV.
     *
     * @param books    la liste des livres a exporter
     * @param filename le nom du fichier CSV de sortie
     * @throws IOException en cas d'erreur d'ecriture
     */
    public void exportBooksToCSV(List<Book> books, String filename) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            // En-tete CSV
            writer.println("ISBN;Titre;Auteur;Annee;Genre;Disponible");

            // Donnees
            for (Book book : books) {
                writer.printf("%s;%s;%s;%d;%s;%s%n",
                        escapeCsvField(book.getIsbn()),
                        escapeCsvField(book.getTitle()),
                        escapeCsvField(book.getAuthor()),
                        book.getYear(),
                        escapeCsvField(book.getGenre()),
                        book.isAvailable() ? "Oui" : "Non");
            }

            System.out.println("[INFO] Livres exportes dans : " + filename);
        }
    }

    /**
     * Exporte la liste des membres au format CSV.
     *
     * @param members  la liste des membres a exporter
     * @param filename le nom du fichier CSV de sortie
     * @throws IOException en cas d'erreur d'ecriture
     */
    public void exportMembersToCSV(List<Member> members, String filename) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println("ID;Nom;Email;Date Inscription;Emprunts en cours");

            for (Member member : members) {
                writer.printf("%s;%s;%s;%s;%d%n",
                        escapeCsvField(member.getId()),
                        escapeCsvField(member.getName()),
                        escapeCsvField(member.getEmail()),
                        member.getRegistrationDate().toString(),
                        member.getBorrowedCount());
            }

            System.out.println("[INFO] Membres exportes dans : " + filename);
        }
    }

    /**
     * Exporte la liste des emprunts au format CSV.
     *
     * @param loans    la liste des emprunts a exporter
     * @param filename le nom du fichier CSV de sortie
     * @throws IOException en cas d'erreur d'ecriture
     */
    public void exportLoansToCSV(List<Loan> loans, String filename) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println("Livre;Membre;Date Emprunt;Date Limite;Date Retour;Retourne;En Retard");

            for (Loan loan : loans) {
                writer.printf("%s;%s;%s;%s;%s;%s;%s%n",
                        escapeCsvField(loan.getBook().getTitle()),
                        escapeCsvField(loan.getMember().getName()),
                        loan.getLoanDate().toString(),
                        loan.getDueDate().toString(),
                        loan.getReturnDate() != null ? loan.getReturnDate().toString() : "",
                        loan.isReturned() ? "Oui" : "Non",
                        loan.isOverdue() ? "Oui" : "Non");
            }

            System.out.println("[INFO] Emprunts exportes dans : " + filename);
        }
    }

    /**
     * Echappe un champ CSV en gerant les points-virgules et les guillemets.
     *
     * @param field le champ a echapper
     * @return le champ echappe
     */
    private String escapeCsvField(String field) {
        if (field == null) {
            return "";
        }
        if (field.contains(";") || field.contains("\"") || field.contains("\n")) {
            return "\"" + field.replace("\"", "\"\"") + "\"";
        }
        return field;
    }

    /**
     * Verifie si un fichier de sauvegarde existe.
     *
     * @param filename le nom du fichier a verifier
     * @return true si le fichier existe
     */
    public boolean saveFileExists(String filename) {
        if (filename == null || filename.isEmpty()) {
            filename = DEFAULT_FILENAME;
        }
        return Files.exists(Paths.get(filename));
    }
}
