package gui;

import cipher.CaesarCipher;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class CaesarCrackDialog extends JDialog {

    private final MainFrame parentFrame;
    private final JTable resultsTable;
    private final DefaultTableModel tableModel;

    public CaesarCrackDialog(MainFrame parent, String cipherText) {
        super(parent, "Caesar Cipher - Live Brute-Force Cracker (All 25 Shifts)", true);
        this.parentFrame = parent;

        setSize(700, 480);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout(5, 5));
        headerPanel.setBorder(new EmptyBorder(12, 15, 8, 15));
        JLabel titleLabel = new JLabel("Caesar Brute-Force Key Analysis");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 15));
        JLabel descLabel = new JLabel("<html>Browsing all 25 possible shift combinations. Select the row containing readable English text and click <b>'Apply to Plaintext'</b>.</html>");
        headerPanel.add(titleLabel, BorderLayout.NORTH);
        headerPanel.add(descLabel, BorderLayout.SOUTH);
        add(headerPanel, BorderLayout.NORTH);

        // Table setup
        String[] columnNames = {"Shift Key", "Decrypted Candidate Text"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        resultsTable = new JTable(tableModel);
        resultsTable.setRowHeight(24);
        resultsTable.setFont(new Font("Monospaced", Font.PLAIN, 13));
        resultsTable.getColumnModel().getColumn(0).setPreferredWidth(90);
        resultsTable.getColumnModel().getColumn(0).setMaxWidth(110);
        resultsTable.getColumnModel().getColumn(1).setPreferredWidth(560);
        resultsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(resultsTable);
        scrollPane.setBorder(new EmptyBorder(0, 15, 0, 15));
        add(scrollPane, BorderLayout.CENTER);

        // Populate table with all 25 shifts
        populateShifts(cipherText);

        // Bottom Action Buttons
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 10));
        JButton applyButton = new JButton("Apply to Plaintext");
        applyButton.setFont(new Font("SansSerif", Font.BOLD, 12));
        JButton closeButton = new JButton("Close");

        applyButton.addActionListener(e -> {
            int selectedRow = resultsTable.getSelectedRow();
            if (selectedRow != -1) {
                String candidateText = (String) tableModel.getValueAt(selectedRow, 1);
                int shift = selectedRow + 1;
                parentFrame.getPlainTextArea().setText(candidateText);
                // parentFrame.getCaesarSlider().setValue(shift);
                parentFrame.getStatusLabel().setText("Applied Caesar Shift " + shift + " from Cracker.");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Please select a row from the list.", "No Row Selected", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        closeButton.addActionListener(e -> dispose());

        bottomPanel.add(applyButton);
        bottomPanel.add(closeButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void populateShifts(String text) {
        if (text == null || text.trim().isEmpty()) {
            tableModel.addRow(new Object[]{"N/A", "<No ciphertext supplied to crack>"});
            return;
        }

        String[] shifts = CaesarCipher.crackAllShifts(text);
        for (int i = 0; i < shifts.length; i++) {
            String candidate = CaesarCipher.decrypt(text, i + 1);
            tableModel.addRow(new Object[]{"Shift " + (i + 1), candidate});
        }
        if (tableModel.getRowCount() > 0) {
            resultsTable.setRowSelectionInterval(0, 0);
        }
    }
}
