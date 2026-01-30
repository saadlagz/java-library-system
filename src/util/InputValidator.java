package util;

import java.util.regex.Pattern;

/**
 * Classe utilitaire pour la validation des saisies utilisateur.
 * Fournit des methodes statiques pour valider les ISBN, emails,
 * et autres formats de donnees.
 *
 * @author Saad LAGZIRI
 * @version 1.0
 */
public final class InputValidator {

    /** Pattern pour valider un ISBN-13 (avec ou sans tirets) */
    private static final Pattern ISBN_13_PATTERN =
            Pattern.compile("^(?:\\d{3}[-\\s]?)?\\d{1,5}[-\\s]?\\d{1,7}[-\\s]?\\d{1,7}[-\\s]?\\d$");

    /** Pattern pour valider un ISBN-10 */
    private static final Pattern ISBN_10_PATTERN =
            Pattern.compile("^\\d{1,5}[-\\s]?\\d{1,7}[-\\s]?\\d{1,6}[-\\s]?[\\dXx]$");

    /** Pattern pour valider un ISBN simplifie (sequence de chiffres) */
    private static final Pattern ISBN_SIMPLE_PATTERN =
            Pattern.compile("^[\\d-]{10,17}$");

    /** Pattern pour valider une adresse email */
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");

    /** Pattern pour valider un nom (lettres, espaces, tirets, apostrophes) */
    private static final Pattern NAME_PATTERN =
            Pattern.compile("^[a-zA-ZàâäéèêëïîôùûüÿçÀÂÄÉÈÊËÏÎÔÙÛÜŸÇ\\s'-]{2,100}$");

    /**
     * Constructeur prive pour empecher l'instanciation.
     */
    private InputValidator() {
        throw new UnsupportedOperationException("Classe utilitaire non instanciable.");
    }

    /**
     * Valide un numero ISBN (10 ou 13 chiffres).
     *
     * @param isbn le numero ISBN a valider
     * @return true si l'ISBN est valide
     */
    public static boolean isValidISBN(String isbn) {
        if (isbn == null || isbn.trim().isEmpty()) {
            return false;
        }
        String trimmed = isbn.trim();
        return ISBN_SIMPLE_PATTERN.matcher(trimmed).matches() ||
               ISBN_13_PATTERN.matcher(trimmed).matches() ||
               ISBN_10_PATTERN.matcher(trimmed).matches();
    }

    /**
     * Valide une adresse email.
     *
     * @param email l'adresse email a valider
     * @return true si l'email est valide
     */
    public static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    /**
     * Valide un nom de personne.
     *
     * @param name le nom a valider
     * @return true si le nom est valide
     */
    public static boolean isValidName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        return NAME_PATTERN.matcher(name.trim()).matches();
    }

    /**
     * Valide une annee de publication.
     * L'annee doit etre comprise entre 1450 (invention de l'imprimerie)
     * et l'annee courante + 1 (publications futures proches).
     *
     * @param year l'annee a valider
     * @return true si l'annee est valide
     */
    public static boolean isValidYear(int year) {
        int currentYear = java.time.LocalDate.now().getYear();
        return year >= 1450 && year <= currentYear + 1;
    }

    /**
     * Valide une annee de publication sous forme de chaine.
     *
     * @param yearString la chaine representant l'annee
     * @return true si la chaine represente une annee valide
     */
    public static boolean isValidYear(String yearString) {
        if (yearString == null || yearString.trim().isEmpty()) {
            return false;
        }
        try {
            int year = Integer.parseInt(yearString.trim());
            return isValidYear(year);
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Valide qu'une chaine n'est pas nulle ou vide.
     *
     * @param input la chaine a valider
     * @return true si la chaine n'est ni nulle ni vide
     */
    public static boolean isNotEmpty(String input) {
        return input != null && !input.trim().isEmpty();
    }

    /**
     * Valide qu'un entier est positif.
     *
     * @param value la valeur a valider
     * @return true si la valeur est strictement positive
     */
    public static boolean isPositive(int value) {
        return value > 0;
    }

    /**
     * Nettoie un ISBN en retirant les tirets et espaces.
     *
     * @param isbn l'ISBN a nettoyer
     * @return l'ISBN nettoye
     */
    public static String cleanISBN(String isbn) {
        if (isbn == null) {
            return "";
        }
        return isbn.replaceAll("[\\s-]", "");
    }

    /**
     * Nettoie et normalise une chaine (trim et suppression des espaces multiples).
     *
     * @param input la chaine a nettoyer
     * @return la chaine nettoyee
     */
    public static String cleanInput(String input) {
        if (input == null) {
            return "";
        }
        return input.trim().replaceAll("\\s+", " ");
    }
}
