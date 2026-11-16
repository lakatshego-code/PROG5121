package za.ac.iie.prog5121;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Manages sent, disregarded and stored messages for Part 3.
 */
public class Task {

    private Message[] sentMessages = new Message[0];
    private Message[] disregardedMessages = new Message[0];
    private Message[] storedMessages = new Message[0];
    private String[] messageHashes = new String[0];
    private String[] messageIDs = new String[0];

    private final Path storageFile = Path.of(
            System.getProperty("quickchat.storage.file", "messages.json"));

    private static class MessageRecord {
        String messageID;
        int messageNumber;
        String recipient;
        String messageText;
        String messageHash;
        String status;
    }

    private <T> T[] addToArray(T[] array, T item) {
        T[] updated = Arrays.copyOf(array, array.length + 1);
        updated[array.length] = item;
        return updated;
    }

    /**
     * Adds a message to the appropriate array and records its ID and hash.
     */
    public void addMessage(Message message) {
        if (message == null) {
            return;
        }

        switch (message.getStatus()) {
            case "Sent":
                sentMessages = addToArray(sentMessages, message);
                break;
            case "Disregarded":
                disregardedMessages = addToArray(disregardedMessages, message);
                break;
            case "Stored":
                storedMessages = addToArray(storedMessages, message);
                break;
            default:
                break;
        }

        messageIDs = addToArray(messageIDs, message.getMessageID());
        messageHashes = addToArray(messageHashes, message.getMessageHash());
    }

    /**
     * Reads stored messages from the JSON file into the stored messages array.
     */
    public void loadStoredMessages() throws IOException {
        if (!Files.exists(storageFile) || Files.size(storageFile) == 0) {
            storedMessages = new Message[0];
            return;
        }

        Gson gson = new Gson();
        MessageRecord[] records;

        try {
            records = gson.fromJson(
                    Files.readString(storageFile), MessageRecord[].class);
        } catch (RuntimeException ex) {
            throw new IOException("Could not read messages.json", ex);
        }

        storedMessages = new Message[0];

        if (records == null) {
            return;
        }

        for (MessageRecord record : records) {
            if (record != null && "Stored".equalsIgnoreCase(record.status)) {
                Message message = new Message(
                        record.messageID,
                        record.messageNumber,
                        record.recipient,
                        record.messageText
                );

                storedMessages = addToArray(storedMessages, message);
                messageIDs = addUnique(messageIDs, record.messageID);
                messageHashes = addUnique(messageHashes, record.messageHash);
            }
        }
    }

    private String[] addUnique(String[] array, String value) {
        if (value == null || Arrays.asList(array).contains(value)) {
            return array;
        }
        return addToArray(array, value);
    }

    public String displayStoredSendersAndRecipients() {
        if (storedMessages.length == 0) {
            return "No stored messages found.";
        }

        StringBuilder result = new StringBuilder();

        for (Message message : storedMessages) {
            result.append("Sender: Developer")
                    .append(System.lineSeparator())
                    .append("Recipient: ")
                    .append(message.getRecipient())
                    .append(System.lineSeparator());
        }

        return result.toString();
    }

    public String getLongestStoredMessage() {
        if (storedMessages.length == 0) {
            return "";
        }

        Message longest = storedMessages[0];

        for (Message message : storedMessages) {
            if (message.getMessageText().length()
                    > longest.getMessageText().length()) {
                longest = message;
            }
        }

        return longest.getMessageText();
    }

    public Message searchMessageByID(String id) {
        if (id == null) {
            return null;
        }

        for (Message message : storedMessages) {
            if (id.equals(message.getMessageID())) {
                return message;
            }
        }

        for (Message message : sentMessages) {
            if (id.equals(message.getMessageID())) {
                return message;
            }
        }

        return null;
    }

    public String searchMessagesByRecipient(String recipient) {
        if (recipient == null) {
            return "";
        }

        StringBuilder result = new StringBuilder();

        for (Message message : storedMessages) {
            if (recipient.equals(message.getRecipient())) {
                result.append(message.getMessageText())
                        .append(System.lineSeparator());
            }
        }

        for (Message message : sentMessages) {
            if (recipient.equals(message.getRecipient())) {
                result.append(message.getMessageText())
                        .append(System.lineSeparator());
            }
        }

        return result.toString().trim();
    }

    /**
     * Deletes a stored message by its message hash and updates the JSON file.
     */
    public boolean deleteMessageByHash(String hash) throws IOException {
        if (hash == null) {
            return false;
        }

        List<Message> remaining = new ArrayList<>();
        boolean deleted = false;

        for (Message message : storedMessages) {
            if (!deleted && hash.equals(message.getMessageHash())) {
                deleted = true;
            } else {
                remaining.add(message);
            }
        }

        if (!deleted) {
            return false;
        }

        storedMessages = remaining.toArray(new Message[0]);
        writeStoredMessages();
        return true;
    }

    private void writeStoredMessages() throws IOException {
        List<MessageRecord> records = new ArrayList<>();

        for (Message message : storedMessages) {
            MessageRecord record = new MessageRecord();
            record.messageID = message.getMessageID();
            record.messageNumber = message.getMessageNumber();
            record.recipient = message.getRecipient();
            record.messageText = message.getMessageText();
            record.messageHash = message.getMessageHash();
            record.status = "Stored";
            records.add(record);
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Files.writeString(storageFile, gson.toJson(records));
    }

    public String displayReport() {
        if (storedMessages.length == 0) {
            return "No stored messages available.";
        }

        StringBuilder report = new StringBuilder();

        for (Message message : storedMessages) {
            report.append("Message ID: ")
                    .append(message.getMessageID())
                    .append(System.lineSeparator())
                    .append("Message Hash: ")
                    .append(message.getMessageHash())
                    .append(System.lineSeparator())
                    .append("Recipient: ")
                    .append(message.getRecipient())
                    .append(System.lineSeparator())
                    .append("Message: ")
                    .append(message.getMessageText())
                    .append(System.lineSeparator())
                    .append("------------------------------")
                    .append(System.lineSeparator());
        }

        return report.toString();
    }

    public Message[] getSentMessages() {
        return Arrays.copyOf(sentMessages, sentMessages.length);
    }

    public Message[] getDisregardedMessages() {
        return Arrays.copyOf(disregardedMessages, disregardedMessages.length);
    }

    public Message[] getStoredMessages() {
        return Arrays.copyOf(storedMessages, storedMessages.length);
    }

    public String[] getMessageHashes() {
        return Arrays.copyOf(messageHashes, messageHashes.length);
    }

    public String[] getMessageIDs() {
        return Arrays.copyOf(messageIDs, messageIDs.length);
    }
}
