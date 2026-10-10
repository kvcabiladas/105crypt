package cipher;

public class AtbashCipher {

    public static String process(String text) {
        if (text == null) return "";
        StringBuilder result = new StringBuilder();

        for (char c : text.toCharArray()) {
            if (Character.isUpperCase(c)) {
                result.append((char) ('Z' - (c - 'A')));
            } else if (Character.isLowerCase(c)) {
                result.append((char) ('z' - (c - 'a')));
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }

    public static String encrypt(String text) {
        return process(text);
    }

    public static String decrypt(String text) {
        return process(text);
    }
}
