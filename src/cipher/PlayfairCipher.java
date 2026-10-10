package cipher;

import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class PlayfairCipher {

    private final char[][] matrix = new char[5][5];

    public PlayfairCipher(String key) {
        generateMatrix(key);
    }

    public void generateMatrix(String key) {
        if (key == null) key = "";
        String cleanKey = key.toUpperCase().replaceAll("[^A-Z]", "").replace('J', 'I');
        Set<Character> charSet = new LinkedHashSet<>();

        for (char c : cleanKey.toCharArray()) {
            charSet.add(c);
        }
        for (char c = 'A'; c <= 'Z'; c++) {
            if (c != 'J') {
                charSet.add(c);
            }
        }

        List<Character> charList = new ArrayList<>(charSet);
        int idx = 0;
        for (int r = 0; r < 5; r++) {
            for (int c = 0; c < 5; c++) {
                matrix[r][c] = charList.get(idx++);
            }
        }
    }

    public char[][] getMatrix() {
        return matrix;
    }

    public String getMatrixFormatted() {
        StringBuilder sb = new StringBuilder();
        for (int r = 0; r < 5; r++) {
            for (int c = 0; c < 5; c++) {
                sb.append(matrix[r][c]).append("  ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    private Point findPosition(char c) {
        if (c == 'J') c = 'I';
        for (int r = 0; r < 5; r++) {
            for (int col = 0; col < 5; col++) {
                if (matrix[r][col] == c) {
                    return new Point(col, r); // x = col, y = row
                }
            }
        }
        return new Point(0, 0);
    }

    public String preparePlaintext(String text) {
        if (text == null) return "";
        String clean = text.toUpperCase().replaceAll("[^A-Z]", "").replace('J', 'I');
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < clean.length(); i++) {
            char c1 = clean.charAt(i);
            sb.append(c1);
            if (i + 1 < clean.length()) {
                char c2 = clean.charAt(i + 1);
                if (c1 == c2) {
                    sb.append(c1 == 'X' ? 'Q' : 'X');
                } else {
                    sb.append(c2);
                    i++;
                }
            }
        }
        if (sb.length() % 2 != 0) {
            char last = sb.charAt(sb.length() - 1);
            sb.append(last == 'X' ? 'Q' : 'X');
        }
        return sb.toString();
    }

    public String encrypt(String text) {
        String prepared = preparePlaintext(text);
        if (prepared.isEmpty()) return "";

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < prepared.length(); i += 2) {
            char a = prepared.charAt(i);
            char b = prepared.charAt(i + 1);
            Point p1 = findPosition(a);
            Point p2 = findPosition(b);

            if (p1.y == p2.y) {
                // Same Row
                result.append(matrix[p1.y][(p1.x + 1) % 5]);
                result.append(matrix[p2.y][(p2.x + 1) % 5]);
            } else if (p1.x == p2.x) {
                // Same Column
                result.append(matrix[(p1.y + 1) % 5][p1.x]);
                result.append(matrix[(p2.y + 1) % 5][p2.x]);
            } else {
                // Rectangle
                result.append(matrix[p1.y][p2.x]);
                result.append(matrix[p2.y][p1.x]);
            }
            if (i + 2 < prepared.length()) {
                result.append(" ");
            }
        }
        return result.toString();
    }

    public String decrypt(String text) {
        if (text == null) return "";
        String clean = text.toUpperCase().replaceAll("[^A-Z]", "").replace('J', 'I');
        if (clean.length() % 2 != 0) {
            clean += "X";
        }

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < clean.length(); i += 2) {
            char a = clean.charAt(i);
            char b = clean.charAt(i + 1);
            Point p1 = findPosition(a);
            Point p2 = findPosition(b);

            if (p1.y == p2.y) {
                // Same Row
                result.append(matrix[p1.y][(p1.x + 4) % 5]);
                result.append(matrix[p2.y][(p2.x + 4) % 5]);
            } else if (p1.x == p2.x) {
                // Same Column
                result.append(matrix[(p1.y + 4) % 5][p1.x]);
                result.append(matrix[(p2.y + 4) % 5][p2.x]);
            } else {
                // Rectangle
                result.append(matrix[p1.y][p2.x]);
                result.append(matrix[p2.y][p1.x]);
            }
        }
        return result.toString();
    }
}
