/**
 * Caesar Cipher Encryption/Decryption Implementation
 * A classic substitution cipher for educational cybersecurity demonstration
 * 
 * @author Cybersecurity Project
 * @version 1.0
 */
public class CaesarCipher {
    
    /**
     * Encrypts a message using Caesar cipher with given shift
     * @param plaintext The original message to encrypt
     * @param shift The number of positions to shift (1-25)
     * @return The encrypted message
     */
    public static String encrypt(String plaintext, int shift) {
        StringBuilder ciphertext = new StringBuilder();
        shift = normalizeShift(shift);
        
        for (char character : plaintext.toCharArray()) {
            if (Character.isUpperCase(character)) {
                char encryptedChar = (char) ((character - 'A' + shift) % 26 + 'A');
                ciphertext.append(encryptedChar);
            } else if (Character.isLowerCase(character)) {
                char encryptedChar = (char) ((character - 'a' + shift) % 26 + 'a');
                ciphertext.append(encryptedChar);
            } else {
                // Keep non-alphabetic characters unchanged
                ciphertext.append(character);
            }
        }
        
        return ciphertext.toString();
    }
    
    /**
     * Decrypts a message using Caesar cipher with given shift
     * @param ciphertext The encrypted message to decrypt
     * @param shift The number of positions shifted during encryption
     * @return The decrypted message
     */
    public static String decrypt(String ciphertext, int shift) {
        // Decryption is encryption with negative shift
        return encrypt(ciphertext, -shift);
    }
    
    /**
     * Normalizes the shift value to be within 0-25 range
     * @param shift The shift value to normalize
     * @return Normalized shift value
     */
    private static int normalizeShift(int shift) {
        shift = shift % 26;
        if (shift < 0) {
            shift += 26;
        }
        return shift;
    }
    
    /**
     * Performs brute force attack on Caesar cipher by trying all possible shifts
     * @param ciphertext The encrypted message to crack
     */
    public static void bruteForceAttack(String ciphertext) {
        System.out.println("\n=== BRUTE FORCE ATTACK ===");
        System.out.println("Trying all possible shifts (1-25):\n");
        
        for (int shift = 1; shift <= 25; shift++) {
            String decrypted = decrypt(ciphertext, shift);
            System.out.printf("Shift %2d: %s\n", shift, decrypted);
        }
    }
    
    /**
     * Main method demonstrating the complete sender-receiver flow
     */
    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════════════════════════╗");
        System.out.println("║     CAESAR CIPHER - SECURE COMMUNICATION SYSTEM            ║");
        System.out.println("║     Demonstrating Classical Cryptography                   ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝\n");
        
        // Sender Information
        String sender = "Alice";
        String receiver = "Bob";
        String originalMessage = "Meet me at the secret location at midnight!";
        int secretKey = 7; // The agreed upon shift value
        
        System.out.println("┌─────────────────────────────────────────────────────────┐");
        System.out.println("│ SENDER: " + sender + "                                              │");
        System.out.println("└─────────────────────────────────────────────────────────┘");
        System.out.println("Original Message: " + originalMessage);
        System.out.println("Secret Key (Shift): " + secretKey);
        
        // Encryption Process
        String encryptedMessage = encrypt(originalMessage, secretKey);
        System.out.println("\n[ENCRYPTING MESSAGE...]");
        System.out.println("Encrypted Message: " + encryptedMessage);
        
        // Transmission
        System.out.println("\n[TRANSMITTING ENCRYPTED MESSAGE OVER INSECURE CHANNEL...]");
        System.out.println("Message sent to: " + receiver);
        
        // Receiver Side
        System.out.println("\n┌─────────────────────────────────────────────────────────┐");
        System.out.println("│ RECEIVER: " + receiver + "                                             │");
        System.out.println("└─────────────────────────────────────────────────────────┘");
        System.out.println("Received Encrypted Message: " + encryptedMessage);
        System.out.println("Using Secret Key: " + secretKey);
        
        // Decryption Process
        String decryptedMessage = decrypt(encryptedMessage, secretKey);
        System.out.println("\n[DECRYPTING MESSAGE...]");
        System.out.println("Decrypted Message: " + decryptedMessage);
        
        // Verification
        System.out.println("\n┌─────────────────────────────────────────────────────────┐");
        System.out.println("│ VERIFICATION                                            │");
        System.out.println("└─────────────────────────────────────────────────────────┘");
        System.out.println("Original Message:  " + originalMessage);
        System.out.println("Decrypted Message: " + decryptedMessage);
        System.out.println("Messages Match: " + originalMessage.equals(decryptedMessage));
        
        // Security Analysis
        System.out.println("\n╔════════════════════════════════════════════════════════════╗");
        System.out.println("║ SECURITY ANALYSIS                                          ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝");
        System.out.println("Strength: Simple symmetric encryption");
        System.out.println("Weakness: Vulnerable to brute force (only 25 possible keys)");
        System.out.println("Weakness: Vulnerable to frequency analysis");
        System.out.println("Use Case: Educational purposes only - NOT for real security");
        
        // Demonstrate vulnerability with brute force
        bruteForceAttack(encryptedMessage);
        
        // Additional Examples
        System.out.println("\n╔════════════════════════════════════════════════════════════╗");
        System.out.println("║ ADDITIONAL EXAMPLES                                        ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝\n");
        
        demonstrateExample("HELLO WORLD", 3);
        demonstrateExample("Cybersecurity is important!", 13);
        demonstrateExample("The quick brown fox jumps over the lazy dog", 5);
    }
    
    /**
     * Demonstrates encryption/decryption with a specific example
     */
    private static void demonstrateExample(String message, int shift) {
        String encrypted = encrypt(message, shift);
        String decrypted = decrypt(encrypted, shift);
        
        System.out.println("Original:  " + message);
        System.out.println("Shift:     " + shift);
        System.out.println("Encrypted: " + encrypted);
        System.out.println("Decrypted: " + decrypted);
        System.out.println();
    }
}
