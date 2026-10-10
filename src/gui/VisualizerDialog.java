package gui;

import cipher.PlayfairCipher;
import cipher.RailFenceCipher;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class VisualizerDialog extends JDialog {

    public static void showPlayfairMatrix(JFrame parent, String keyword) {
        JDialog dialog = new JDialog(parent, "Playfair 5x5 Alphabet Key Matrix Visualizer", true);
        dialog.setSize(480, 480);
        dialog.setLocationRelativeTo(parent);
        dialog.setLayout(new BorderLayout(10, 10));

        JPanel headerPanel = new JPanel(new BorderLayout(5, 5));
        headerPanel.setBorder(new EmptyBorder(15, 15, 5, 15));
        JLabel title = new JLabel("Playfair Key Matrix (5x5)");
        title.setFont(new Font("SansSerif", Font.BOLD, 15));
        JLabel subtitle = new JLabel("Constructed from Keyword: \"" + (keyword.isEmpty() ? "<Default>" : keyword.toUpperCase()) + "\" (Note: I & J share slot)");
        subtitle.setForeground(Color.DARK_GRAY);
        headerPanel.add(title, BorderLayout.NORTH);
        headerPanel.add(subtitle, BorderLayout.SOUTH);
        dialog.add(headerPanel, BorderLayout.NORTH);

        PlayfairCipher playfair = new PlayfairCipher(keyword);
        char[][] matrix = playfair.getMatrix();

        JPanel gridPanel = new JPanel(new GridLayout(5, 5, 6, 6));
        gridPanel.setBorder(new EmptyBorder(10, 20, 10, 20));

        for (int r = 0; r < 5; r++) {
            for (int c = 0; c < 5; c++) {
                char ch = matrix[r][c];
                JLabel cell = new JLabel(ch == 'I' ? "I/J" : String.valueOf(ch), SwingConstants.CENTER);
                cell.setFont(new Font("SansSerif", Font.BOLD, 18));
                cell.setOpaque(true);
                cell.setBackground(new Color(235, 240, 250));
                cell.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(170, 190, 220), 1),
                        new EmptyBorder(10, 10, 10, 10)
                ));
                gridPanel.add(cell);
            }
        }
        dialog.add(gridPanel, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        JButton closeBtn = new JButton("Close");
        closeBtn.addActionListener(e -> dialog.dispose());
        btnPanel.add(closeBtn);
        dialog.add(btnPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    public static void showRailFenceZigZag(JFrame parent, String text, int rails) {
        JDialog dialog = new JDialog(parent, "Rail Fence Zig-Zag Transposition Visualizer", true);
        dialog.setSize(750, 480);
        dialog.setLocationRelativeTo(parent);
        dialog.setLayout(new BorderLayout(10, 10));

        JPanel headerPanel = new JPanel(new BorderLayout(5, 5));
        headerPanel.setBorder(new EmptyBorder(15, 15, 5, 15));
        JLabel title = new JLabel("Rail Fence Zig-Zag Pattern (" + rails + " Rails)");
        title.setFont(new Font("SansSerif", Font.BOLD, 15));
        JLabel subtitle = new JLabel("Visual representation of characters traced diagonally along the rails:");
        subtitle.setForeground(Color.DARK_GRAY);
        headerPanel.add(title, BorderLayout.NORTH);
        headerPanel.add(subtitle, BorderLayout.SOUTH);
        dialog.add(headerPanel, BorderLayout.NORTH);

        JTextArea visualArea = new JTextArea();
        visualArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        visualArea.setEditable(false);
        visualArea.setText(RailFenceCipher.generateVisualization(text, rails));
        visualArea.setBackground(new Color(248, 250, 252));
        visualArea.setBorder(new EmptyBorder(10, 10, 10, 10));

        JScrollPane scroll = new JScrollPane(visualArea);
        scroll.setBorder(new EmptyBorder(5, 15, 5, 15));
        dialog.add(scroll, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        JButton closeBtn = new JButton("Close");
        closeBtn.addActionListener(e -> dialog.dispose());
        btnPanel.add(closeBtn);
        dialog.add(btnPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }
}
