package gui;

import cipher.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

public class MainFrame extends JFrame {

    // Top Navigation
    private JComboBox<String> cipherComboBox;
    private JLabel cipherDescriptionLabel;

    // Dynamic Control Panel
    private JPanel dynamicControlContainer;
    private CardLayout cardLayout;

    // Cipher-specific Key Controls
    private JSlider caesarSlider;
    private JSpinner caesarSpinner;
    private JTextField vigenereKeyField;
    private JTextField playfairKeyField;
    private JSpinner railFenceSpinner;

    // Advanced Feature Controls
    private JCheckBox autoSanitizeCheckBox;
    private JButton crackCaesarButton;
    private JButton playfairVisualizerButton;
    private JButton railFenceVisualizerButton;

    // Dual-Text Area
    private JTextArea plainTextArea;
    private JTextArea cipherTextArea;
    private JButton encryptButton;
    private JButton decryptButton;
    private JButton swapButton;
    private JButton clearButton;
    private JButton manualSanitizeButton;

    // Status Bar
    private JLabel statusLabel;

    // Cipher Names Constants
    public static final String CIPHER_CAESAR = "Caesar Cipher";
    public static final String CIPHER_VIGENERE = "Vigenère Cipher";
    public static final String CIPHER_ATBASH = "Atbash Cipher";
    public static final String CIPHER_RAIL_FENCE = "Rail Fence Cipher";
    public static final String CIPHER_PLAYFAIR = "Playfair Cipher";

    public MainFrame() {
        setTitle("105crypt - Cryptographic Suite");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(980, 700);
        setMinimumSize(new Dimension(850, 600));
        setLocationRelativeTo(null);

        initMenuBar();
        initUI();
        initLogic();
    }

    private void initMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // // File Menu
        // JMenu fileMenu = new JMenu("File");
        // JMenuItem exitItem = new JMenuItem("Exit");
        // exitItem.addActionListener(e -> System.exit(0));
        // fileMenu.add(exitItem);

        // Tools Menu
        JMenu toolsMenu = new JMenu("Tools");
        JMenuItem crackItem = new JMenuItem("Caesar Brute-Force Cracker");
        JMenuItem pfVisItem = new JMenuItem("Playfair 5x5 Grid Visualizer");
        JMenuItem rfVisItem = new JMenuItem("Rail Fence Zig-Zag Visualizer");
        JMenuItem sanitizeItem = new JMenuItem("Sanitize Plaintext (A-Z only)");

        crackItem.addActionListener(e -> openCaesarCracker());
        pfVisItem.addActionListener(e -> VisualizerDialog.showPlayfairMatrix(this, playfairKeyField.getText().trim()));
        rfVisItem.addActionListener(e -> VisualizerDialog.showRailFenceZigZag(this, plainTextArea.getText(),
                (Integer) railFenceSpinner.getValue()));
        sanitizeItem.addActionListener(e -> {
            plainTextArea.setText(TextSanitizer.sanitize(plainTextArea.getText()));
            statusLabel.setText("Sanitized plaintext to uppercase letters (A-Z).");
        });

        toolsMenu.add(crackItem);
        toolsMenu.add(pfVisItem);
        toolsMenu.add(rfVisItem);
        toolsMenu.addSeparator();
        toolsMenu.add(sanitizeItem);

        // menuBar.add(fileMenu);
        menuBar.add(toolsMenu);
        setJMenuBar(menuBar);
    }

    private void initUI() {
        // Main Container with padding
        JPanel rootPanel = new JPanel(new BorderLayout(10, 10));
        rootPanel.setBorder(new EmptyBorder(12, 14, 12, 14));
        setContentPane(rootPanel);

        // 1. TOP PANEL: Cipher Selection & Dynamic Key Control
        JPanel topSection = new JPanel(new BorderLayout(8, 8));
        topSection.add(createCipherSelectionPanel(), BorderLayout.NORTH);
        topSection.add(createDynamicControlPanel(), BorderLayout.CENTER);
        rootPanel.add(topSection, BorderLayout.NORTH);

        // 2. CENTER PANEL: Dual-Text Interaction Area
        rootPanel.add(createDualTextAreaPanel(), BorderLayout.CENTER);

        // 3. BOTTOM PANEL: Status Bar & Global Sanitizer Toggle
        JPanel bottomBar = new JPanel(new BorderLayout());
        bottomBar.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY));

        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        statusLabel = new JLabel("Ready | Active Cipher: " + cipherComboBox.getSelectedItem());
        statusPanel.add(statusLabel);

        JPanel optionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 4));
        autoSanitizeCheckBox = new JCheckBox("Auto-Sanitize Input (A-Z only before encrypting)", true);
        autoSanitizeCheckBox.setFont(new Font("SansSerif", Font.PLAIN, 12));
        autoSanitizeCheckBox.setToolTipText("Removes non-alphabet characters and converts input to uppercase");
        optionPanel.add(autoSanitizeCheckBox);

        bottomBar.add(statusPanel, BorderLayout.WEST);
        bottomBar.add(optionPanel, BorderLayout.EAST);
        rootPanel.add(bottomBar, BorderLayout.SOUTH);

        // Trigger initial state
        onCipherChanged((String) cipherComboBox.getSelectedItem());
    }

    private JPanel createCipherSelectionPanel() {
        JPanel navPanel = new JPanel(new BorderLayout(0, 4));
        navPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), " Select Cipher ",
                TitledBorder.LEFT, TitledBorder.TOP, new Font("SansSerif", Font.BOLD, 13)));

        JPanel comboRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 2));
        JLabel selectLabel = new JLabel("Active Cipher:");
        selectLabel.setFont(new Font("SansSerif", Font.BOLD, 12));

        String[] ciphers = {
                CIPHER_CAESAR,
                CIPHER_VIGENERE,
                CIPHER_ATBASH,
                CIPHER_RAIL_FENCE,
                CIPHER_PLAYFAIR
        };
        cipherComboBox = new JComboBox<>(ciphers);
        cipherComboBox.setFont(new Font("SansSerif", Font.PLAIN, 13));
        cipherComboBox.setPreferredSize(new Dimension(220, 30));

        comboRow.add(selectLabel);
        comboRow.add(cipherComboBox);

        JPanel descRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 2));
        cipherDescriptionLabel = new JLabel();
        cipherDescriptionLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        descRow.add(cipherDescriptionLabel);

        cipherComboBox.addActionListener(e -> {
            String selected = (String) cipherComboBox.getSelectedItem();
            onCipherChanged(selected);
            statusLabel.setText("Ready | Active Cipher: " + selected);
        });

        navPanel.add(comboRow, BorderLayout.NORTH);
        navPanel.add(descRow, BorderLayout.CENTER);
        return navPanel;
    }

    private JPanel createDynamicControlPanel() {
        cardLayout = new CardLayout();
        dynamicControlContainer = new JPanel(cardLayout);
        dynamicControlContainer.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), " Key Settings & Dedicated Cipher Tools ",
                TitledBorder.LEFT, TitledBorder.TOP, new Font("SansSerif", Font.BOLD, 13)));

        // --- Card 1: Caesar Panel ---
        JPanel caesarPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 6));
        JLabel caesarLabel = new JLabel("Shift Amount (1 - 25):");
        // caesarSlider = new JSlider(JSlider.HORIZONTAL, 1, 25, 3);
        // caesarSlider.setMajorTickSpacing(4);
        // caesarSlider.setMinorTickSpacing(1);
        // caesarSlider.setPaintTicks(true);
        // caesarSlider.setPaintLabels(true);
        // caesarSlider.setPreferredSize(new Dimension(260, 45));

        caesarSpinner = new JSpinner(new SpinnerNumberModel(3, 1, 25, 1));
        caesarSpinner.setPreferredSize(new Dimension(55, 28));

        crackCaesarButton = new JButton("Live Brute-Force Cracker");
        crackCaesarButton.setToolTipText("Displays all 25 possible decrypted shifts simultaneously");
        crackCaesarButton.addActionListener(e -> openCaesarCracker());

        // caesarSlider.addChangeListener(e ->
        // caesarSpinner.setValue(caesarSlider.getValue()));
        // caesarSpinner.addChangeListener(e -> caesarSlider.setValue((Integer)
        // caesarSpinner.getValue()));

        caesarPanel.add(caesarLabel);
        // caesarPanel.add(caesarSlider);
        caesarPanel.add(caesarSpinner);
        caesarPanel.add(crackCaesarButton);

        // --- Card 2: Vigenère Panel ---
        JPanel vigenerePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 6));
        JLabel vigenereLabel = new JLabel("Keyword / Keyphrase:");
        vigenereKeyField = new JTextField(15);
        vigenereKeyField.setText("KEY");
        vigenereKeyField.setPreferredSize(new Dimension(180, 28));
        JLabel vigTip = new JLabel("(Letters only, repeats automatically)");
        vigTip.setForeground(Color.GRAY);
        vigenerePanel.add(vigenereLabel);
        vigenerePanel.add(vigenereKeyField);
        vigenerePanel.add(vigTip);

        // --- Card 3: Atbash (Empty placeholder as panel hides when Atbash is selected)
        // ---
        JPanel atbashPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 6));

        // --- Card 4: Rail Fence Panel ---
        JPanel railFencePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 6));
        JLabel railFenceLabel = new JLabel("Number of Rails (Rows):");
        railFenceSpinner = new JSpinner(new SpinnerNumberModel(3, 2, 20, 1));
        railFenceSpinner.setPreferredSize(new Dimension(60, 28));

        railFenceVisualizerButton = new JButton("View Zig-Zag Pattern");
        railFenceVisualizerButton.setToolTipText("Visualizes how text travels across the rails");
        railFenceVisualizerButton.addActionListener(e -> {
            String text = plainTextArea.getText();
            if (text.isEmpty())
                text = cipherTextArea.getText();
            int rails = (Integer) railFenceSpinner.getValue();
            VisualizerDialog.showRailFenceZigZag(this, text.isEmpty() ? "SAMPLE" : text, rails);
        });

        railFencePanel.add(railFenceLabel);
        railFencePanel.add(railFenceSpinner);
        railFencePanel.add(railFenceVisualizerButton);

        // --- Card 5: Playfair Panel ---
        JPanel playfairPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 6));
        JLabel playfairLabel = new JLabel("Keyword / Passphrase:");
        playfairKeyField = new JTextField(15);
        playfairKeyField.setText("MONARCHY");
        playfairKeyField.setPreferredSize(new Dimension(180, 28));

        playfairVisualizerButton = new JButton("View 5x5 Matrix");
        playfairVisualizerButton.setToolTipText("Display the generated 5x5 alphabet grid");
        playfairVisualizerButton.addActionListener(e -> {
            VisualizerDialog.showPlayfairMatrix(this, playfairKeyField.getText().trim());
        });

        playfairPanel.add(playfairLabel);
        playfairPanel.add(playfairKeyField);
        playfairPanel.add(playfairVisualizerButton);

        // Add cards to container
        dynamicControlContainer.add(caesarPanel, CIPHER_CAESAR);
        dynamicControlContainer.add(vigenerePanel, CIPHER_VIGENERE);
        dynamicControlContainer.add(atbashPanel, CIPHER_ATBASH);
        dynamicControlContainer.add(railFencePanel, CIPHER_RAIL_FENCE);
        dynamicControlContainer.add(playfairPanel, CIPHER_PLAYFAIR);

        return dynamicControlContainer;
    }

    private void onCipherChanged(String selectedCipher) {
        if (selectedCipher != null) {
            updateCipherDescription(selectedCipher);
            if (CIPHER_ATBASH.equals(selectedCipher)) {
                dynamicControlContainer.setVisible(false);
            } else {
                dynamicControlContainer.setVisible(true);
                cardLayout.show(dynamicControlContainer, selectedCipher);
            }
            revalidate();
            repaint();
        }
    }

    private void updateCipherDescription(String cipher) {
        if (cipherDescriptionLabel == null)
            return;
        String desc;
        switch (cipher) {
            case CIPHER_CAESAR:
                desc = "Shift substitution cipher replacing each letter by shifting it a fixed number of positions (key).";
                break;
            case CIPHER_VIGENERE:
                desc = "Polyalphabetic substitution cipher encrypting text using a repeating keyword.";
                break;
            case CIPHER_ATBASH:
                desc = "Monoalphabetic substitution cipher that reverses the alphabet (A \u2194 Z, B \u2194 Y).";
                break;
            case CIPHER_RAIL_FENCE:
                desc = "Transposition cipher writing characters along a zig-zag pattern across multiple rails.";
                break;
            case CIPHER_PLAYFAIR:
                desc = "Digraph substitution cipher encrypting pairs of letters using a 5\u00d75 matrix grid.";
                break;
            default:
                desc = "";
                break;
        }
        cipherDescriptionLabel.setText("<html><span style=' font-weight: bold; color: #000000;'>Description:</span> "
                + desc + "</html>");
    }

    private JPanel createDualTextAreaPanel() {
        JPanel dualPanel = new JPanel(new GridBagLayout());
        dualPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), " Workspace ",
                TitledBorder.LEFT, TitledBorder.TOP, new Font("SansSerif", Font.BOLD, 13)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(6, 6, 6, 6);

        // 1. Plaintext Input Box (Left)
        JPanel leftPanel = new JPanel(new BorderLayout(5, 5));
        JPanel leftHeader = new JPanel(new BorderLayout());
        leftHeader.setPreferredSize(new Dimension(100, 28));
        JLabel plainLabel = new JLabel("Plaintext (Original Message):");
        plainLabel.setFont(new Font("SansSerif", Font.BOLD, 12));

        JPanel leftTools = new JPanel(new FlowLayout(FlowLayout.RIGHT, 4, 0));
        manualSanitizeButton = new JButton("Sanitize");
        manualSanitizeButton.setFont(new Font("SansSerif", Font.PLAIN, 11));
        manualSanitizeButton.setToolTipText("Sanitize current plaintext (A-Z only)");
        manualSanitizeButton.addActionListener(e -> {
            plainTextArea.setText(TextSanitizer.sanitize(plainTextArea.getText()));
            statusLabel.setText("Sanitized plaintext.");
        });

        leftTools.add(manualSanitizeButton);
        leftHeader.add(plainLabel, BorderLayout.WEST);
        leftHeader.add(leftTools, BorderLayout.EAST);

        plainTextArea = new JTextArea();
        plainTextArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        plainTextArea.setLineWrap(true);
        plainTextArea.setWrapStyleWord(true);
        JScrollPane plainScroll = new JScrollPane(plainTextArea);
        leftPanel.add(leftHeader, BorderLayout.NORTH);
        leftPanel.add(plainScroll, BorderLayout.CENTER);
        leftPanel.setPreferredSize(new Dimension(100, 100));
        leftPanel.setMinimumSize(new Dimension(50, 50));

        // 2. Action Buttons Panel (Center)
        JPanel centerButtonPanel = new JPanel(new GridLayout(4, 1, 8, 12));
        encryptButton = new JButton("Encrypt ▶");
        encryptButton.setFont(new Font("SansSerif", Font.BOLD, 12));
        encryptButton.setBackground(new Color(38, 137, 78));
        encryptButton.setForeground(Color.WHITE);
        encryptButton.setOpaque(true);
        encryptButton.setBorderPainted(false);
        encryptButton.setFocusPainted(false);
        encryptButton.setToolTipText("Encrypt Plaintext to Ciphertext");

        decryptButton = new JButton("◀ Decrypt");
        decryptButton.setFont(new Font("SansSerif", Font.BOLD, 12));
        decryptButton.setBackground(new Color(30, 105, 178));
        decryptButton.setForeground(Color.WHITE);
        decryptButton.setOpaque(true);
        decryptButton.setBorderPainted(false);
        decryptButton.setFocusPainted(false);
        decryptButton.setToolTipText("Decrypt Ciphertext to Plaintext");

        swapButton = new JButton("⇄ Swap");
        swapButton.setFont(new Font("SansSerif", Font.PLAIN, 12));
        swapButton.setToolTipText("Swap contents between Plaintext and Ciphertext");

        clearButton = new JButton("Clear All");
        clearButton.setFont(new Font("SansSerif", Font.PLAIN, 12));
        clearButton.setToolTipText("Clear both text areas");

        // Swap action
        swapButton.addActionListener(e -> {
            String temp = plainTextArea.getText();
            plainTextArea.setText(cipherTextArea.getText());
            cipherTextArea.setText(temp);
            statusLabel.setText("Swapped Plaintext and Ciphertext contents.");
        });

        // Clear action
        clearButton.addActionListener(e -> {
            plainTextArea.setText("");
            cipherTextArea.setText("");
            statusLabel.setText("Cleared both text areas.");
        });

        centerButtonPanel.add(encryptButton);
        centerButtonPanel.add(decryptButton);
        centerButtonPanel.add(swapButton);
        centerButtonPanel.add(clearButton);

        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.add(centerButtonPanel);

        // 3. Ciphertext Output Box (Right)
        JPanel rightPanel = new JPanel(new BorderLayout(5, 5));
        JPanel rightHeader = new JPanel(new BorderLayout());
        rightHeader.setPreferredSize(new Dimension(100, 28));
        JLabel cipherLabel = new JLabel("Ciphertext (Encrypted / Output):");
        cipherLabel.setFont(new Font("SansSerif", Font.BOLD, 12));

        JPanel rightTools = new JPanel(new FlowLayout(FlowLayout.RIGHT, 4, 0));
        JButton copyCipherButton = new JButton("Copy");
        copyCipherButton.setFont(new Font("SansSerif", Font.PLAIN, 11));
        copyCipherButton.setToolTipText("Copy ciphertext to clipboard");
        copyCipherButton.addActionListener(e -> {
            String text = cipherTextArea.getText();
            if (text != null && !text.isEmpty()) {
                Toolkit.getDefaultToolkit().getSystemClipboard()
                        .setContents(new StringSelection(text), null);
                statusLabel.setText("Ciphertext copied to clipboard.");
            }
        });
        rightTools.add(copyCipherButton);

        rightHeader.add(cipherLabel, BorderLayout.WEST);
        rightHeader.add(rightTools, BorderLayout.EAST);

        cipherTextArea = new JTextArea();
        cipherTextArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        cipherTextArea.setLineWrap(true);
        cipherTextArea.setWrapStyleWord(true);
        JScrollPane cipherScroll = new JScrollPane(cipherTextArea);
        rightPanel.add(rightHeader, BorderLayout.NORTH);
        rightPanel.add(cipherScroll, BorderLayout.CENTER);
        rightPanel.setPreferredSize(new Dimension(100, 100));
        rightPanel.setMinimumSize(new Dimension(50, 50));

        // Add to GridBagLayout: equal weights (0.5) for text panes, 0.0 for center
        // button column
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.5;
        gbc.weighty = 1.0;
        dualPanel.add(leftPanel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.0;
        gbc.weighty = 1.0;
        dualPanel.add(centerWrapper, gbc);

        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.weightx = 0.5;
        gbc.weighty = 1.0;
        dualPanel.add(rightPanel, gbc);

        return dualPanel;
    }

    private void initLogic() {
        encryptButton.addActionListener(e -> performEncryption());
        decryptButton.addActionListener(e -> performDecryption());
    }

    private void performEncryption() {
        String input = plainTextArea.getText();
        if (input == null || input.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter plaintext in the left box to encrypt.", "Empty Input",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (autoSanitizeCheckBox.isSelected()) {
            input = TextSanitizer.sanitize(input);
            plainTextArea.setText(input);
        }

        String selectedCipher = (String) cipherComboBox.getSelectedItem();
        String result = "";

        try {
            switch (selectedCipher) {
                case CIPHER_CAESAR:
                    int caesarShift = (Integer) caesarSpinner.getValue();
                    result = CaesarCipher.encrypt(input, caesarShift);
                    statusLabel.setText("Encrypted using Caesar Cipher (Shift: " + caesarShift + ").");
                    break;

                case CIPHER_VIGENERE:
                    String vKey = vigenereKeyField.getText().trim();
                    if (vKey.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Please provide a valid keyword for Vigenère Cipher.",
                                "Missing Key", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    result = VigenereCipher.encrypt(input, vKey);
                    statusLabel.setText("Encrypted using Vigenère Cipher (Key: " + vKey + ").");
                    break;

                case CIPHER_ATBASH:
                    result = AtbashCipher.encrypt(input);
                    statusLabel.setText("Encrypted using Atbash Cipher.");
                    break;

                case CIPHER_RAIL_FENCE:
                    int rails = (Integer) railFenceSpinner.getValue();
                    result = RailFenceCipher.encrypt(input, rails);
                    statusLabel.setText("Encrypted using Rail Fence Cipher (" + rails + " rails).");
                    break;

                case CIPHER_PLAYFAIR:
                    String pKey = playfairKeyField.getText().trim();
                    if (pKey.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Please provide a keyword for Playfair Cipher.",
                                "Missing Key", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    PlayfairCipher playfair = new PlayfairCipher(pKey);
                    result = playfair.encrypt(input);
                    statusLabel.setText("Encrypted using Playfair Cipher (Key: " + pKey + ").");
                    break;

                default:
                    JOptionPane.showMessageDialog(this, "Unknown cipher selected.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
            }

            cipherTextArea.setText(result);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Encryption Error: " + ex.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void performDecryption() {
        String input = cipherTextArea.getText();
        boolean sourceIsRight = true;

        if (input == null || input.trim().isEmpty()) {
            input = plainTextArea.getText();
            sourceIsRight = false;
        }

        if (input == null || input.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter ciphertext to decrypt.", "Empty Input",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String selectedCipher = (String) cipherComboBox.getSelectedItem();
        String result = "";

        try {
            switch (selectedCipher) {
                case CIPHER_CAESAR:
                    int caesarShift = (Integer) caesarSpinner.getValue();
                    result = CaesarCipher.decrypt(input, caesarShift);
                    statusLabel.setText("Decrypted using Caesar Cipher (Shift: " + caesarShift + ").");
                    break;

                case CIPHER_VIGENERE:
                    String vKey = vigenereKeyField.getText().trim();
                    if (vKey.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Please provide a valid keyword for Vigenère Cipher.",
                                "Missing Key", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    result = VigenereCipher.decrypt(input, vKey);
                    statusLabel.setText("Decrypted using Vigenère Cipher (Key: " + vKey + ").");
                    break;

                case CIPHER_ATBASH:
                    result = AtbashCipher.decrypt(input);
                    statusLabel.setText("Decrypted using Atbash Cipher.");
                    break;

                case CIPHER_RAIL_FENCE:
                    int rails = (Integer) railFenceSpinner.getValue();
                    result = RailFenceCipher.decrypt(input, rails);
                    statusLabel.setText("Decrypted using Rail Fence Cipher (" + rails + " rails).");
                    break;

                case CIPHER_PLAYFAIR:
                    String pKey = playfairKeyField.getText().trim();
                    if (pKey.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Please provide a keyword for Playfair Cipher.",
                                "Missing Key", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    PlayfairCipher playfair = new PlayfairCipher(pKey);
                    result = playfair.decrypt(input.replace(" ", ""));
                    statusLabel.setText("Decrypted using Playfair Cipher (Key: " + pKey + ").");
                    break;

                default:
                    JOptionPane.showMessageDialog(this, "Unknown cipher selected.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
            }

            if (sourceIsRight) {
                plainTextArea.setText(result);
            } else {
                cipherTextArea.setText(result);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Decryption Error: " + ex.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openCaesarCracker() {
        String textToCrack = cipherTextArea.getText();
        if (textToCrack == null || textToCrack.trim().isEmpty()) {
            textToCrack = plainTextArea.getText();
        }
        if (textToCrack == null || textToCrack.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter or encrypt some text to crack with Caesar Brute-Force.",
                    "No Text Available", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        CaesarCrackDialog dialog = new CaesarCrackDialog(this, textToCrack);
        dialog.setVisible(true);
    }

    // Accessors
    public JComboBox<String> getCipherComboBox() {
        return cipherComboBox;
    }

    public JLabel getCipherDescriptionLabel() {
        return cipherDescriptionLabel;
    }

    public JSlider getCaesarSlider() {
        return caesarSlider;
    }

    public JSpinner getCaesarSpinner() {
        return caesarSpinner;
    }

    public JTextField getVigenereKeyField() {
        return vigenereKeyField;
    }

    public JTextField getPlayfairKeyField() {
        return playfairKeyField;
    }

    public JSpinner getRailFenceSpinner() {
        return railFenceSpinner;
    }

    public JTextArea getPlainTextArea() {
        return plainTextArea;
    }

    public JTextArea getCipherTextArea() {
        return cipherTextArea;
    }

    public JLabel getStatusLabel() {
        return statusLabel;
    }
}
