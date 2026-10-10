package cipher;

import java.util.Arrays;

public class RailFenceCipher {

    public static String encrypt(String text, int rails) {
        if (text == null || text.isEmpty() || rails <= 1 || rails >= text.length()) {
            return text == null ? "" : text;
        }

        char[][] railMatrix = new char[rails][text.length()];
        for (char[] row : railMatrix) {
            Arrays.fill(row, '\n');
        }

        boolean down = false;
        int row = 0;

        for (int col = 0; col < text.length(); col++) {
            if (row == 0 || row == rails - 1) {
                down = !down;
            }
            railMatrix[row][col] = text.charAt(col);
            row += down ? 1 : -1;
        }

        StringBuilder result = new StringBuilder();
        for (int r = 0; r < rails; r++) {
            for (int c = 0; c < text.length(); c++) {
                if (railMatrix[r][c] != '\n') {
                    result.append(railMatrix[r][c]);
                }
            }
        }
        return result.toString();
    }

    public static String decrypt(String text, int rails) {
        if (text == null || text.isEmpty() || rails <= 1 || rails >= text.length()) {
            return text == null ? "" : text;
        }

        char[][] railMatrix = new char[rails][text.length()];
        for (char[] row : railMatrix) {
            Arrays.fill(row, '\n');
        }

        // Mark the zig-zag path with '*'
        boolean down = false;
        int row = 0;
        for (int col = 0; col < text.length(); col++) {
            if (row == 0 || row == rails - 1) {
                down = !down;
            }
            railMatrix[row][col] = '*';
            row += down ? 1 : -1;
        }

        // Fill characters into the marked slots
        int index = 0;
        for (int r = 0; r < rails; r++) {
            for (int c = 0; c < text.length(); c++) {
                if (railMatrix[r][c] == '*' && index < text.length()) {
                    railMatrix[r][c] = text.charAt(index++);
                }
            }
        }

        // Read out in zig-zag order
        StringBuilder result = new StringBuilder();
        down = false;
        row = 0;
        for (int col = 0; col < text.length(); col++) {
            if (row == 0 || row == rails - 1) {
                down = !down;
            }
            if (railMatrix[row][col] != '\n') {
                result.append(railMatrix[row][col]);
            }
            row += down ? 1 : -1;
        }

        return result.toString();
    }

    /**
     * Generates a 2D grid representation for visualization in Phase 4.
     */
    public static String generateVisualization(String text, int rails) {
        if (text == null || text.isEmpty()) return "No text provided.";
        if (rails <= 1) return text;

        char[][] railMatrix = new char[rails][text.length()];
        for (char[] row : railMatrix) {
            Arrays.fill(row, '·');
        }

        boolean down = false;
        int row = 0;
        for (int col = 0; col < text.length(); col++) {
            if (row == 0 || row == rails - 1) {
                down = !down;
            }
            railMatrix[row][col] = text.charAt(col);
            row += down ? 1 : -1;
        }

        StringBuilder sb = new StringBuilder();
        for (int r = 0; r < rails; r++) {
            sb.append(String.format("Rail %2d: ", r + 1));
            for (int c = 0; c < text.length(); c++) {
                sb.append(railMatrix[r][c]).append(' ');
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
