import service.DataManager;
import service.LibraryService;
import ui.ConsoleMenu;

import java.io.IOException;

/**
 * Point d'entree de l'application de gestion de bibliotheque.
 * Initialise les services et lance l'interface console.
 *
 * @author Saad LAGZIRI
 * @version 1.0
 */
public class Main {

    /** Nom du fichier de sauvegarde par defaut */
    private static final String DATA_FILE = "library_data.ser";

    /**
     * Methode principale de l'application.
     * Tente de charger les donnees existantes, puis demarre le menu console.
     *
     * @param args les arguments de ligne de commande (non utilises)
     */
    public static void main(String[] args) {
        System.out.println("Demarrage du systeme de gestion de bibliotheque...");
        System.out.println();

        // Initialiser le gestionnaire de donnees
        DataManager dataManager = new DataManager();
        LibraryService libraryService;

        // Tenter de charger les donnees existantes
        if (dataManager.saveFileExists(DATA_FILE)) {
            try {
                libraryService = dataManager.loadLibrary(DATA_FILE);
                System.out.println("Donnees precedentes chargees avec succes.");
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Impossible de charger les donnees : " + e.getMessage());
                System.out.println("Creation d'une nouvelle bibliotheque...");
                libraryService = new LibraryService();
                initializeSampleData(libraryService);
            }
        } else {
            System.out.println("Premiere utilisation : creation de la bibliotheque...");
            libraryService = new LibraryService();
            initializeSampleData(libraryService);
        }

        // Demarrer l'interface console
        ConsoleMenu menu = new ConsoleMenu(libraryService);
        menu.start();
    }

    /**
     * Initialise la bibliotheque avec des donnees d'exemple.
     * Ajoute quelques livres et membres pour faciliter les tests.
     *
     * @param service le service de bibliotheque a initialiser
     */
    private static void initializeSampleData(LibraryService service) {
        // Ajout de livres d'exemple
        service.addBook(new model.Book(
                "Les Miserables", "Victor Hugo",
                "978-2-07-040850-4", 1862, "Roman"));

        service.addBook(new model.Book(
                "Le Petit Prince", "Antoine de Saint-Exupery",
                "978-2-07-061275-8", 1943, "Conte"));

        service.addBook(new model.Book(
                "Germinal", "Emile Zola",
                "978-2-07-036024-6", 1885, "Roman"));

        service.addBook(new model.Book(
                "Introduction aux algorithmes", "Thomas Cormen",
                "978-2-10-003922-7", 2009, "Informatique"));

        service.addBook(new model.Book(
                "Design Patterns", "Erich Gamma",
                "978-0-20-163361-0", 1994, "Informatique"));

        service.addBook(new model.Book(
                "L'Etranger", "Albert Camus",
                "978-2-07-036002-4", 1942, "Roman"));

        service.addBook(new model.Book(
                "Dune", "Frank Herbert",
                "978-2-266-32008-8", 1965, "Science-Fiction"));

        service.addBook(new model.Book(
                "Fondation", "Isaac Asimov",
                "978-2-07-046357-2", 1951, "Science-Fiction"));

        // Ajout de membres d'exemple
        service.addMember("Jean Dupont", "jean.dupont@email.fr");
        service.addMember("Marie Martin", "marie.martin@email.fr");
        service.addMember("Ahmed Benali", "ahmed.benali@email.fr");

        System.out.println("Donnees d'exemple chargees : " +
                service.getTotalBooks() + " livres et " +
                service.getTotalMembers() + " membres.");
    }
}
