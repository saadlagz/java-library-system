package service;

import model.Book;
import model.Loan;
import model.Member;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service de statistiques pour la bibliotheque.
 * Fournit des analyses et des rapports sur l'utilisation
 * de la bibliotheque : emprunts, membres actifs, genres, etc.
 *
 * @author Saad LAGZIRI
 * @version 1.0
 */
public class StatisticsService {

    /** Reference vers le service de la bibliotheque */
    private final LibraryService libraryService;

    /**
     * Constructeur du service de statistiques.
     *
     * @param libraryService le service de bibliotheque a analyser
     */
    public StatisticsService(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    /**
     * Retourne les livres les plus empruntes, tries par nombre d'emprunts decroissant.
     *
     * @param limit le nombre maximum de livres a retourner
     * @return une map ordonnee (livre -> nombre d'emprunts)
     */
    public LinkedHashMap<Book, Long> getMostBorrowedBooks(int limit) {
        List<Loan> allLoans = libraryService.getAllLoans();

        // Compter le nombre d'emprunts par livre
        Map<Book, Long> borrowCounts = allLoans.stream()
                .collect(Collectors.groupingBy(Loan::getBook, Collectors.counting()));

        // Trier par nombre d'emprunts decroissant et limiter
        return borrowCounts.entrySet().stream()
                .sorted(Map.Entry.<Book, Long>comparingByValue().reversed())
                .limit(limit)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new));
    }

    /**
     * Retourne les livres les plus empruntes (top 10 par defaut).
     *
     * @return une map ordonnee (livre -> nombre d'emprunts)
     */
    public LinkedHashMap<Book, Long> getMostBorrowedBooks() {
        return getMostBorrowedBooks(10);
    }

    /**
     * Retourne les membres les plus actifs, tries par nombre d'emprunts decroissant.
     *
     * @param limit le nombre maximum de membres a retourner
     * @return une map ordonnee (membre -> nombre d'emprunts)
     */
    public LinkedHashMap<Member, Long> getMostActiveMembers(int limit) {
        List<Loan> allLoans = libraryService.getAllLoans();

        // Compter le nombre d'emprunts par membre
        Map<Member, Long> memberCounts = allLoans.stream()
                .collect(Collectors.groupingBy(Loan::getMember, Collectors.counting()));

        // Trier par nombre d'emprunts decroissant
        return memberCounts.entrySet().stream()
                .sorted(Map.Entry.<Member, Long>comparingByValue().reversed())
                .limit(limit)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new));
    }

    /**
     * Retourne les membres les plus actifs (top 10 par defaut).
     *
     * @return une map ordonnee (membre -> nombre d'emprunts)
     */
    public LinkedHashMap<Member, Long> getMostActiveMembers() {
        return getMostActiveMembers(10);
    }

    /**
     * Retourne les statistiques mensuelles d'emprunts.
     * Pour chaque mois, donne le nombre d'emprunts effectues.
     *
     * @return une map ordonnee (mois -> nombre d'emprunts)
     */
    public LinkedHashMap<YearMonth, Long> getMonthlyLoanStats() {
        List<Loan> allLoans = libraryService.getAllLoans();

        // Grouper les emprunts par mois
        Map<YearMonth, Long> monthlyStats = allLoans.stream()
                .collect(Collectors.groupingBy(
                        loan -> YearMonth.from(loan.getLoanDate()),
                        Collectors.counting()));

        // Trier par mois chronologiquement
        return monthlyStats.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new));
    }

    /**
     * Retourne la distribution des livres par genre.
     *
     * @return une map (genre -> nombre de livres)
     */
    public LinkedHashMap<String, Long> getGenreDistribution() {
        List<Book> allBooks = libraryService.getAllBooks();

        // Compter le nombre de livres par genre
        Map<String, Long> genreCounts = allBooks.stream()
                .collect(Collectors.groupingBy(Book::getGenre, Collectors.counting()));

        // Trier par nombre de livres decroissant
        return genreCounts.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new));
    }

    /**
     * Calcule le taux d'occupation de la bibliotheque.
     * C'est le pourcentage de livres actuellement empruntes.
     *
     * @return le taux d'occupation en pourcentage (0.0 a 100.0)
     */
    public double getOccupancyRate() {
        int totalBooks = libraryService.getTotalBooks();
        if (totalBooks == 0) {
            return 0.0;
        }
        int borrowedBooks = totalBooks - libraryService.getAvailableBooks().size();
        return (double) borrowedBooks / totalBooks * 100.0;
    }

    /**
     * Calcule la duree moyenne des emprunts (en jours).
     * Ne prend en compte que les emprunts retournes.
     *
     * @return la duree moyenne en jours, ou 0 si aucun emprunt retourne
     */
    public double getAverageLoanDuration() {
        List<Loan> returnedLoans = libraryService.getAllLoans().stream()
                .filter(Loan::isReturned)
                .collect(Collectors.toList());

        if (returnedLoans.isEmpty()) {
            return 0.0;
        }

        long totalDays = returnedLoans.stream()
                .mapToLong(loan -> java.time.temporal.ChronoUnit.DAYS.between(
                        loan.getLoanDate(), loan.getReturnDate()))
                .sum();

        return (double) totalDays / returnedLoans.size();
    }

    /**
     * Retourne le taux de retard (pourcentage d'emprunts en retard).
     *
     * @return le taux de retard en pourcentage
     */
    public double getOverdueRate() {
        List<Loan> activeLoans = libraryService.getActiveLoans();
        if (activeLoans.isEmpty()) {
            return 0.0;
        }
        long overdueCount = activeLoans.stream()
                .filter(Loan::isOverdue)
                .count();
        return (double) overdueCount / activeLoans.size() * 100.0;
    }

    /**
     * Genere un rapport complet de statistiques sous forme de chaine formatee.
     *
     * @return le rapport de statistiques
     */
    public String generateReport() {
        StringBuilder report = new StringBuilder();
        String separator = "=".repeat(50);

        report.append(separator).append("\n");
        report.append("       RAPPORT STATISTIQUES BIBLIOTHEQUE\n");
        report.append(separator).append("\n\n");

        // Statistiques generales
        report.append("--- Statistiques generales ---\n");
        report.append(String.format("  Nombre total de livres       : %d%n", libraryService.getTotalBooks()));
        report.append(String.format("  Livres disponibles           : %d%n", libraryService.getAvailableBooks().size()));
        report.append(String.format("  Nombre total de membres      : %d%n", libraryService.getTotalMembers()));
        report.append(String.format("  Emprunts en cours            : %d%n", libraryService.getActiveLoansCount()));
        report.append(String.format("  Taux d'occupation            : %.1f%%%n", getOccupancyRate()));
        report.append(String.format("  Duree moyenne d'emprunt      : %.1f jours%n", getAverageLoanDuration()));
        report.append(String.format("  Taux de retard               : %.1f%%%n", getOverdueRate()));
        report.append("\n");

        // Top livres
        report.append("--- Top 5 livres les plus empruntes ---\n");
        LinkedHashMap<Book, Long> topBooks = getMostBorrowedBooks(5);
        if (topBooks.isEmpty()) {
            report.append("  Aucun emprunt enregistre.\n");
        } else {
            int rank = 1;
            for (Map.Entry<Book, Long> entry : topBooks.entrySet()) {
                report.append(String.format("  %d. \"%s\" - %d emprunt(s)%n",
                        rank++, entry.getKey().getTitle(), entry.getValue()));
            }
        }
        report.append("\n");

        // Top membres
        report.append("--- Top 5 membres les plus actifs ---\n");
        LinkedHashMap<Member, Long> topMembers = getMostActiveMembers(5);
        if (topMembers.isEmpty()) {
            report.append("  Aucun emprunt enregistre.\n");
        } else {
            int rank = 1;
            for (Map.Entry<Member, Long> entry : topMembers.entrySet()) {
                report.append(String.format("  %d. %s - %d emprunt(s)%n",
                        rank++, entry.getKey().getName(), entry.getValue()));
            }
        }
        report.append("\n");

        // Distribution par genre
        report.append("--- Distribution par genre ---\n");
        LinkedHashMap<String, Long> genres = getGenreDistribution();
        if (genres.isEmpty()) {
            report.append("  Aucun livre dans la bibliotheque.\n");
        } else {
            for (Map.Entry<String, Long> entry : genres.entrySet()) {
                report.append(String.format("  %-20s : %d livre(s)%n",
                        entry.getKey(), entry.getValue()));
            }
        }

        report.append("\n").append(separator).append("\n");
        return report.toString();
    }
}
