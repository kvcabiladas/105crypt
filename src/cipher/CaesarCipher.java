package cipher;

public class CaesarCipher {

    public static String encrypt(String text, int shift) {
        if (text == null) return "";
        shift = ((shift % 26) + 26) % 26;
        StringBuilder result = new StringBuilder();

        for (char c : text.toCharArray()) {
            if (Character.isUpperCase(c)) {
                char shifted = (char) ('A' + (c - 'A' + shift) % 26);
                result.append(shifted);
            } else if (Character.isLowerCase(c)) {
                char shifted = (char) ('a' + (c - 'a' + shift) % 26);
                result.append(shifted);
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }

    public static String decrypt(String text, int shift) {
        return encrypt(text, 26 - ((shift % 26) + 26) % 26);
    }

    /**
     * Live Brute-Force Cracker: Generates all 25 possible shifts.
     */
    public static String[] crackAllShifts(String text) {
        String[] results = new String[25];
        for (int shift = 1; shift <= 25; shift++) {
            results[shift - 1] = "Shift " + shift + ": " + decrypt(text, shift);
        }
        return results;
    }
}
