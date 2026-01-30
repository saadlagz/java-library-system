package util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;

/**
 * Classe utilitaire pour la manipulation et le formatage des dates.
 * Fournit des methodes statiques pour formater, parser et calculer
 * des differences entre dates.
 *
 * @author Saad LAGZIRI
 * @version 1.0
 */
public final class DateUtils {

    /** Format de date francais (jj/mm/aaaa) */
    private static final DateTimeFormatter FR_FORMATTER =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /** Format de date ISO (aaaa-mm-jj) */
    private static final DateTimeFormatter ISO_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /** Format de date court (jj/mm/aa) */
    private static final DateTimeFormatter SHORT_FORMATTER =
            DateTimeFormatter.ofPattern("dd/MM/yy");

    /**
     * Constructeur prive pour empecher l'instanciation.
     */
    private DateUtils() {
        throw new UnsupportedOperationException("Classe utilitaire non instanciable.");
    }

    /**
     * Formate une date au format francais (jj/mm/aaaa).
     *
     * @param date la date a formater
     * @return la date formatee en chaine
     */
    public static String formatFrench(LocalDate date) {
        if (date == null) {
            return "N/A";
        }
        return date.format(FR_FORMATTER);
    }

    /**
     * Formate une date au format ISO (aaaa-mm-jj).
     *
     * @param date la date a formater
     * @return la date formatee en chaine
     */
    public static String formatISO(LocalDate date) {
        if (date == null) {
            return "N/A";
        }
        return date.format(ISO_FORMATTER);
    }

    /**
     * Formate une date au format court (jj/mm/aa).
     *
     * @param date la date a formater
     * @return la date formatee en chaine
     */
    public static String formatShort(LocalDate date) {
        if (date == null) {
            return "N/A";
        }
        return date.format(SHORT_FORMATTER);
    }

    /**
     * Parse une chaine au format francais (jj/mm/aaaa) en LocalDate.
     *
     * @param dateString la chaine a parser
     * @return la date parsee
     * @throws DateTimeParseException si la chaine n'est pas au bon format
     */
    public static LocalDate parseFrench(String dateString) throws DateTimeParseException {
        return LocalDate.parse(dateString, FR_FORMATTER);
    }

    /**
     * Parse une chaine au format ISO (aaaa-mm-jj) en LocalDate.
     *
     * @param dateString la chaine a parser
     * @return la date parsee
     * @throws DateTimeParseException si la chaine n'est pas au bon format
     */
    public static LocalDate parseISO(String dateString) throws DateTimeParseException {
        return LocalDate.parse(dateString, ISO_FORMATTER);
    }

    /**
     * Calcule le nombre de jours entre deux dates.
     *
     * @param start la date de debut
     * @param end   la date de fin
     * @return le nombre de jours entre les deux dates
     */
    public static long daysBetween(LocalDate start, LocalDate end) {
        if (start == null || end == null) {
            return 0;
        }
        return ChronoUnit.DAYS.between(start, end);
    }

    /**
     * Verifie si une date est dans le passe.
     *
     * @param date la date a verifier
     * @return true si la date est avant aujourd'hui
     */
    public static boolean isPast(LocalDate date) {
        if (date == null) {
            return false;
        }
        return date.isBefore(LocalDate.now());
    }

    /**
     * Verifie si une date est aujourd'hui.
     *
     * @param date la date a verifier
     * @return true si la date est aujourd'hui
     */
    public static boolean isToday(LocalDate date) {
        if (date == null) {
            return false;
        }
        return date.isEqual(LocalDate.now());
    }

    /**
     * Retourne une representation textuelle relative de la date.
     * Par exemple : "il y a 3 jours", "dans 5 jours", "aujourd'hui".
     *
     * @param date la date a decrire
     * @return la description relative de la date
     */
    public static String getRelativeDescription(LocalDate date) {
        if (date == null) {
            return "N/A";
        }
        long days = daysBetween(LocalDate.now(), date);
        if (days == 0) {
            return "aujourd'hui";
        } else if (days == 1) {
            return "demain";
        } else if (days == -1) {
            return "hier";
        } else if (days > 0) {
            return "dans " + days + " jours";
        } else {
            return "il y a " + Math.abs(days) + " jours";
        }
    }
}
