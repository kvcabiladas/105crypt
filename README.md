# 105crypt - Cryptographic Suite (Java Swing)

A desktop graphical user interface application built with Java Swing that implements foundational historical ciphers and interactive cryptographic tools.

## Supported Ciphers
- **Caesar Cipher**: Shift substitution cipher with synchronized slider & spinner (1-25).
- **Vigenère Cipher**: Repeating-keyword polyalphabetic substitution cipher.
- **Atbash Cipher**: Direct alphabet reflection ($A \leftrightarrow Z, B \leftrightarrow Y$), with dynamic key panel hiding.
- **Rail Fence Cipher**: Zig-zag transposition cipher with customizable rail count.
- **Playfair Cipher**: 5×5 key matrix digraph substitution cipher ($I/J$ merged).

## Core Features
- **Dynamic Control Panel**: Automatically adapts its inputs according to the active cipher, hiding completely for keyless ciphers like Atbash.
- **Live Brute-Force Cracker**: Displays all 25 Caesar shift candidates simultaneously with one-click restoration to the workspace.
- **Auto-Sanitizer**: Toggle option to clean inputs into uppercase $A-Z$ only prior to encryption.
- **Interactive Visualizers**:
  - Playfair 5×5 Matrix viewer.
  - Rail Fence Zig-Zag transposition viewer.
- **Dual-Text Interaction Area**: Split workspace with Encrypt, Decrypt, and Swap controls.

## How to Compile & Run
```bash
# Compile
javac -d bin src/cipher/*.java src/gui/*.java src/App.java

# Run
java -cp bin App
```
