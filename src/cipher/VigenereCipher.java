package cipher;

public class VigenereCipher {

    public static String encrypt(String text, String key) {
        if (text == null) return "";
        if (key == null || key.isEmpty()) return text;

        String cleanKey = key.replaceAll("[^a-zA-Z]", "").toUpperCase();
        if (cleanKey.isEmpty()) return text;

        StringBuilder result = new StringBuilder();
        int keyIndex = 0;

        for (char c : text.toCharArray()) {
            if (Character.isLetter(c)) {
                int shift = cleanKey.charAt(keyIndex % cleanKey.length()) - 'A';
                if (Character.isUpperCase(c)) {
                    result.append((char) ('A' + (c - 'A' + shift) % 26));
                } else {
                    result.append((char) ('a' + (c - 'a' + shift) % 26));
                }
                keyIndex++;
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }

    public static String decrypt(String text, String key) {
        if (text == null) return "";
        if (key == null || key.isEmpty()) return text;

        String cleanKey = key.replaceAll("[^a-zA-Z]", "").toUpperCase();
        if (cleanKey.isEmpty()) return text;

        StringBuilder result = new StringBuilder();
        int keyIndex = 0;

        for (char c : text.toCharArray()) {
            if (Character.isLetter(c)) {
                int shift = cleanKey.charAt(keyIndex % cleanKey.length()) - 'A';
                if (Character.isUpperCase(c)) {
                    result.append((char) ('A' + (c - 'A' - shift + 26) % 26));
                } else {
                    result.append((char) ('a' + (c - 'a' - shift + 26) % 26));
                }
                keyIndex++;
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }
}
