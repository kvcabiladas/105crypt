package cipher;

public class TextSanitizer {

    /**
     * Sanitizes input text by removing spaces, numbers, punctuation,
     * and converting all letters to uppercase A-Z.
     */
    public static String sanitize(String text) {
        if (text == null) return "";
        return text.toUpperCase().replaceAll("[^A-Z]", "");
    }
}
