import java.io.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Advanced Caesar Cipher with Network Simulation and File Operations
 * Demonstrates secure communication with logging and persistence
 * 
 * @author Cybersecurity Project
 * @version 2.0
 */
public class CaesarCipherAdvanced {
    
    private static final String LOG_FILE = "cipher_log.txt";
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    /**
     * Represents a message in the communication system
     */
    static class Message {
        String sender;
        String receiver;
        String content;
        int shift;
        LocalDateTime timestamp;
        boolean encrypted;
        
        public Message(String sender, String receiver, String content, int shift, boolean encrypted) {
            this.sender = sender;
            this.receiver = receiver;
            this.content = content;
            this.shift = shift;
            this.timestamp = LocalDateTime.now();
            this.encrypted = encrypted;
        }
        
        @Override
        public String toString() {
            return String.format("[%s] %s -> %s | Encrypted: %s | Shift: %d\nContent: %s",
                timestamp.format(DATE_FORMAT), sender, receiver, encrypted, shift, content);
        }
    }
    
    /**
     * Simulates a communication channel
     */
    static class CommunicationChannel {
        private List<Message> messageLog;
        
        public CommunicationChannel() {
            this.messageLog = new ArrayList<>();
        }
        
        public void sendMessage(Message message) {
            messageLog.add(message);
            logToFile(message);
            System.out.println("\n[CHANNEL] Message transmitted successfully!");
            System.out.println("Timestamp: " + message.timestamp.format(DATE_FORMAT));
        }
        
        public Message receiveLatestMessage() {
            if (messageLog.isEmpty()) {
                return null;
            }
            return messageLog.get(messageLog.size() - 1);
        }
        
        public void displayMessageLog() {
            System.out.println("\n╔════════════════════════════════════════════════════════════╗");
            System.out.println("║ COMMUNICATION CHANNEL - MESSAGE LOG                        ║");
            System.out.println("╚════════════════════════════════════════════════════════════╝\n");
            
            if (messageLog.isEmpty()) {
                System.out.println("No messages transmitted yet.");
                return;
            }
            
            for (int i = 0; i < messageLog.size(); i++) {
                System.out.printf("Message #%d:\n%s\n\n", i + 1, messageLog.get(i));
            }
        }
        
        private void logToFile(Message message) {
            try (FileWriter fw = new FileWriter(LOG_FILE, true);
                 PrintWriter pw = new PrintWriter(fw)) {
                pw.println("=".repeat(80));
                pw.println(message);
                pw.println("=".repeat(80));
                pw.println();
            } catch (IOException e) {
                System.err.println("Error writing to log file: " + e.getMessage());
            }
        }
    }
    
    /**
     * Represents a user in the communication system
     */
    static class User {
        String name;
        int secretKey;
        
        public User(String name, int secretKey) {
            this.name = name;
            this.secretKey = secretKey;
        }
        
        public Message encryptAndSend(String plaintext, String receiverName) {
            String encrypted = encrypt(plaintext, secretKey);
            return new Message(this.name, receiverName, encrypted, secretKey, true);
        }
        
        public String receiveAndDecrypt(Message message) {
            if (!message.receiver.equals(this.name)) {
                System.out.println("[ERROR] This message is not for you!");
                return null;
            }
            
            if (!message.encrypted) {
                return message.content;
            }
            
            return decrypt(message.content, message.shift);
        }
    }
    
    /**
     * Encrypts text using Caesar cipher
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
                ciphertext.append(character);
            }
        }
        
        return ciphertext.toString();
    }
    
    /**
     * Decrypts text using Caesar cipher
     */
    public static String decrypt(String ciphertext, int shift) {
        return encrypt(ciphertext, -shift);
    }
    
    private static int normalizeShift(int shift) {
        shift = shift % 26;
        if (shift < 0) {
            shift += 26;
        }
        return shift;
    }
    
    /**
     * Performs frequency analysis on ciphertext
     */
    public static void frequencyAnalysis(String text) {
        Map<Character, Integer> frequency = new HashMap<>();
        int totalLetters = 0;
        
        for (char c : text.toUpperCase().toCharArray()) {
            if (Character.isLetter(c)) {
                frequency.put(c, frequency.getOrDefault(c, 0) + 1);
                totalLetters++;
            }
        }
        
        System.out.println("\n╔════════════════════════════════════════════════════════════╗");
        System.out.println("║ FREQUENCY ANALYSIS                                         ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝\n");
        
        frequency.entrySet().stream()
            .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
            .forEach(entry -> {
                double percentage = (entry.getValue() * 100.0) / totalLetters;
                String bar = "█".repeat((int) (percentage * 2));
                System.out.printf("%c: %3d (%.2f%%) %s\n", 
                    entry.getKey(), entry.getValue(), percentage, bar);
            });
    }
    
    /**
     * Encrypts a file using Caesar cipher
     */
    public static void encryptFile(String inputFile, String outputFile, int shift) {
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
            
            String line;
            while ((line = reader.readLine()) != null) {
                writer.write(encrypt(line, shift));
                writer.newLine();
            }
            
            System.out.println("\n[SUCCESS] File encrypted successfully!");
            System.out.println("Input: " + inputFile);
            System.out.println("Output: " + outputFile);
            System.out.println("Shift: " + shift);
            
        } catch (IOException e) {
            System.err.println("Error processing file: " + e.getMessage());
        }
    }
    
    /**
     * Decrypts a file using Caesar cipher
     */
    public static void decryptFile(String inputFile, String outputFile, int shift) {
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
            
            String line;
            while ((line = reader.readLine()) != null) {
                writer.write(decrypt(line, shift));
                writer.newLine();
            }
            
            System.out.println("\n[SUCCESS] File decrypted successfully!");
            System.out.println("Input: " + inputFile);
            System.out.println("Output: " + outputFile);
            System.out.println("Shift: " + shift);
            
        } catch (IOException e) {
            System.err.println("Error processing file: " + e.getMessage());
        }
    }
    
    /**
     * Main method demonstrating advanced features
     */
    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════════════════════════╗");
        System.out.println("║   ADVANCED CAESAR CIPHER - COMMUNICATION SYSTEM            ║");
        System.out.println("║   With Network Simulation & File Operations                ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝\n");
        
        // Create communication channel
        CommunicationChannel channel = new CommunicationChannel();
        
        // Create users
        int sharedKey = 13; // Secret key agreed upon by both parties
        User alice = new User("Alice", sharedKey);
        User bob = new User("Bob", sharedKey);
        
        // Scenario 1: Alice sends encrypted message to Bob
        System.out.println("═══════════════════════════════════════════════════════════");
        System.out.println("SCENARIO 1: Secure Communication");
        System.out.println("═══════════════════════════════════════════════════════════\n");
        
        String secretMessage = "The package will be delivered at dawn. Use the north entrance.";
        System.out.println("[Alice] Original Message: " + secretMessage);
        
        Message encryptedMsg = alice.encryptAndSend(secretMessage, bob.name);
        System.out.println("[Alice] Encrypted Message: " + encryptedMsg.content);
        
        channel.sendMessage(encryptedMsg);
        
        Message receivedMsg = channel.receiveLatestMessage();
        String decryptedMsg = bob.receiveAndDecrypt(receivedMsg);
        System.out.println("\n[Bob] Received and Decrypted: " + decryptedMsg);
        
        // Verify integrity
        System.out.println("\n[VERIFICATION] Message Match: " + secretMessage.equals(decryptedMsg));
        
        // Scenario 2: Frequency Analysis
        System.out.println("\n═══════════════════════════════════════════════════════════");
        System.out.println("SCENARIO 2: Cryptanalysis Demonstration");
        System.out.println("═══════════════════════════════════════════════════════════");
        
        String longText = "This is a longer message to demonstrate frequency analysis. " +
                         "The Caesar cipher is vulnerable to statistical attacks. " +
                         "Notice how letter patterns can reveal information.";
        String longEncrypted = encrypt(longText, 7);
        
        System.out.println("\nEncrypted Text: " + longEncrypted);
        frequencyAnalysis(longEncrypted);
        
        // Scenario 3: Multiple Messages
        System.out.println("\n═══════════════════════════════════════════════════════════");
        System.out.println("SCENARIO 3: Multiple Message Exchange");
        System.out.println("═══════════════════════════════════════════════════════════\n");
        
        String[] messages = {
            "Rendezvous confirmed for midnight",
            "Agent has been compromised",
            "Abort mission immediately"
        };
        
        for (String msg : messages) {
            Message encrypted = alice.encryptAndSend(msg, bob.name);
            channel.sendMessage(encrypted);
            System.out.println("Sent: " + msg + " -> " + encrypted.content);
        }
        
        // Display message log
        channel.displayMessageLog();
        
        // Scenario 4: File Encryption Demo
        System.out.println("\n═══════════════════════════════════════════════════════════");
        System.out.println("SCENARIO 4: File Encryption/Decryption");
        System.out.println("═══════════════════════════════════════════════════════════");
        
        // Create a sample file
        try (PrintWriter pw = new PrintWriter("sample_message.txt")) {
            pw.println("This is a confidential document.");
            pw.println("It contains sensitive information.");
            pw.println("Encryption is necessary for security.");
            System.out.println("\n[INFO] Created sample_message.txt");
        } catch (IOException e) {
            System.err.println("Error creating sample file: " + e.getMessage());
        }
        
        // Encrypt the file
        encryptFile("sample_message.txt", "encrypted_message.txt", sharedKey);
        
        // Decrypt the file
        decryptFile("encrypted_message.txt", "decrypted_message.txt", sharedKey);
        
        // Security Summary
        System.out.println("\n╔════════════════════════════════════════════════════════════╗");
        System.out.println("║ SECURITY SUMMARY                                           ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝");
        System.out.println("\n✓ Messages encrypted successfully");
        System.out.println("✓ Communication logged to: " + LOG_FILE);
        System.out.println("✓ File encryption/decryption completed");
        System.out.println("\n⚠ REMEMBER: Caesar cipher is NOT secure for production use!");
        System.out.println("⚠ Use modern encryption (AES, RSA) for real applications");
        System.out.println("\n[INFO] Check " + LOG_FILE + " for complete message history");
    }
}
