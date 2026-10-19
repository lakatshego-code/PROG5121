package za.ac.iie.prog5121;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.SecureRandom;
import java.util.Locale;

/** Represents a QuickChat message and its validation, hash and storage operations. */
public class Message {
    private static final SecureRandom RANDOM = new SecureRandom();
    private static int totalMessagesSent = 0;
    private static final String DEFAULT_STORAGE_FILE = "messages.json";

    private final String messageID;
    private final int messageNumber;
    private final String recipient;
    private final String messageText;
    private final String messageHash;
    private String status = "Pending";

    public Message(int messageNumber, String recipient, String messageText) {
        this(generateMessageID(), messageNumber, recipient, messageText);
    }

    /** Constructor with a supplied ID, useful for deterministic unit tests. */
    public Message(String messageID, int messageNumber, String recipient, String messageText) {
        this.messageID = messageID == null ? "" : messageID;
        this.messageNumber = messageNumber;
        this.recipient = recipient == null ? "" : recipient.trim();
        this.messageText = messageText == null ? "" : messageText.trim();
        this.messageHash = createMessageHash();
    }

    private static String generateMessageID() {
        StringBuilder id = new StringBuilder(10);
        for (int i = 0; i < 10; i++) id.append(RANDOM.nextInt(10));
        return id.toString();
    }

    public boolean checkMessageID() {
        return messageID != null && messageID.length() <= 10;
    }

    /**
     * Validates a phone number. The POE's sample data includes both local 11-digit
     * numbers and +27 international numbers, so both are supported despite the
     * separate 10-character limit stated in the brief.
     */
    public String checkRecipientCell() {
        boolean valid = recipient.matches("\\+?[0-9]{10,12}");
        return valid ? "Cell phone number successfully captured." :
                "Cell phone number is incorrectly formatted or does not contain a valid number.";
    }

    public boolean isRecipientValid() {
        return recipient.matches("\\+?[0-9]{10,12}");
    }

    public boolean isMessageLengthValid() {
        return messageText.length() <= 250;
    }

    
public String createMessageHash() {
    if (messageID == null || messageID.length() < 2) {
        return "";
    }

    String firstTwoDigits = messageID.substring(0, 2);
    String text = messageText.trim();

    if (text.isEmpty()) {
        return firstTwoDigits + ":" + messageNumber + ":";
    }

    String[] words = text.split("\\s+");

    String firstWord = words[0].replaceAll(
            "^[^\\p{Alnum}]+|[^\\p{Alnum}]+$", "");

    String lastWord = words[words.length - 1].replaceAll(
            "^[^\\p{Alnum}]+|[^\\p{Alnum}]+$", "");

    return (firstTwoDigits + ":" + messageNumber + ":"
            + firstWord + lastWord).toUpperCase(Locale.ROOT);
}


    /** Returns the response associated with the most recent message status. */
    public String SentMessage() {
        switch (status) {
            case "Sent": return "Message successfully sent";
            case "Disregarded": return "Press 0 to delete the message";
            case "Stored": return "Message successfully stored";
            default: return "No message action has been selected yet.";
        }
    }

    /** Applies the user's choice and returns the required console response. */
    public String SentMessage(String choice) {
        if (choice == null) return "Invalid choice.";
        switch (choice.trim().toLowerCase(Locale.ROOT)) {
            case "1":
            case "send":
                status = "Sent";
                totalMessagesSent++;
                return "Message successfully sent";
            case "2":
            case "disregard":
            case "discard":
            case "0":
                status = "Disregarded";
                return "Press 0 to delete the message";
            case "3":
            case "store":
            case "store message for later":
                try {
                    storeMessage();
                    status = "Stored";
                    return "Message successfully stored";
                } catch (IOException ex) {
                    return "Message could not be stored: " + ex.getMessage();
                }
            default:
                return "Invalid choice. Please choose Send, Disregard, or Store Message for later.";
        }
    }

    /** Stores this message as JSON in messages.json in the working directory. */
    public void storeMessage() throws IOException {
        Path path = Path.of(System.getProperty("quickchat.storage.file", DEFAULT_STORAGE_FILE));
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        MessageRecord record = new MessageRecord(messageID, messageNumber, recipient,
                messageText, messageHash, "Stored");
        java.util.List<MessageRecord> records = new java.util.ArrayList<>();
        if (Files.exists(path) && Files.size(path) > 0) {
            try {
                MessageRecord[] existing = gson.fromJson(Files.readString(path), MessageRecord[].class);
                if (existing != null) records.addAll(java.util.Arrays.asList(existing));
            } catch (RuntimeException ex) {
                throw new IOException("messages.json is not valid JSON", ex);
            }
        }
        records.add(record);
        Files.writeString(path, gson.toJson(records));
    }

    public String printMessages() {
        return "Message ID: " + messageID + System.lineSeparator()
                + "Message Hash: " + messageHash + System.lineSeparator()
                + "Recipient: " + recipient + System.lineSeparator()
                + "Message: " + messageText;
    }

    public int returnTotalMessagess() { return totalMessagesSent; }
    public String getMessageID() { return messageID; }
    public int getMessageNumber() { return messageNumber; }
    public String getRecipient() { return recipient; }
    public String getMessageText() { return messageText; }
    public String getMessageHash() { return messageHash; }
    public String getStatus() { return status; }

    public static void resetTotalMessagesSent() { totalMessagesSent = 0; }

    private static class MessageRecord {
        String messageID;
        int messageNumber;
        String recipient;
        String messageText;
        String messageHash;
        String status;
        MessageRecord(String id, int number, String recipient, String text, String hash, String status) {
            this.messageID = id; this.messageNumber = number; this.recipient = recipient;
            this.messageText = text; this.messageHash = hash; this.status = status;
        }
    }
}
