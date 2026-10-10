package cipher;

public class TextSanitizer {

    // sanitize (remove lang mga sapces, nums, special chars and all, then convert
    // to uppercase.)
    public static String sanitize(String text) {
        if (text == null)
            return "";
        return text.toUpperCase().replaceAll("[^A-Z]", "");
    }
}
