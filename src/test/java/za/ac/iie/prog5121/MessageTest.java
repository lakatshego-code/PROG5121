package za.ac.iie.prog5121;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.nio.file.Files;
import java.nio.file.Path;
import static org.junit.jupiter.api.Assertions.*;

class MessageTest {
    @BeforeEach
    void resetCounter() { Message.resetTotalMessagesSent(); }

    @Test
    void messageOf250CharactersIsValid() {
        assertTrue(new Message("1234567890", 0, "08575975889", "x".repeat(250)).isMessageLengthValid());
    }

    @Test
    void messageOver250CharactersIsInvalid() {
        assertFalse(new Message("1234567890", 0, "08575975889", "x".repeat(251)).isMessageLengthValid());
    }

    @Test
    void validLocalAndInternationalRecipientsAreAccepted() {
        assertTrue(new Message("1234567890", 0, "08575975889", "Hello").isRecipientValid());
        assertTrue(new Message("1234567890", 0, "+27718693002", "Hello").isRecipientValid());
    }

    @Test
    void malformedRecipientIsRejected() {
        assertFalse(new Message("1234567890", 0, "abc123", "Hello").isRecipientValid());
    }

    @Test
    void messageIdIsTenDigitsOrFewer() {
        Message message = new Message(0, "08575975889", "Hi tonight");
        assertTrue(message.checkMessageID());
        assertEquals(10, message.getMessageID().length());
        assertTrue(message.getMessageID().matches("[0-9]{10}"));
    }

    @Test
    void hashUsesFirstTwoIdDigitsCounterAndFirstAndLastWords() {
        Message message = new Message("0012345678", 0, "08575975889", "Hi Mike, can you join us for dinner tonight?");
        assertEquals("00:0:HITONIGHT", message.createMessageHash());
    }

      
@Test
void sendDisregardAndStoreChoicesReturnRequiredMessages() throws Exception {
    Message sent = new Message("0012345678", 0, "08575975889", "Hi tonight");
    assertEquals("Message successfully sent", sent.SentMessage("1"));

    Message discarded = new Message(1, "08575975889", "Discard me");
    assertEquals("Press 0 to delete the message", discarded.SentMessage("2"));

    Path storedPath = Files.createTempFile("quickchat-test-", ".json");
    Files.deleteIfExists(storedPath);
    String previousStorageFile = System.getProperty("quickchat.storage.file");

    try {
        System.setProperty("quickchat.storage.file", storedPath.toString());

        Message stored = new Message(2, "08575975889", "Store me");
        assertEquals("Message successfully stored", stored.SentMessage("3"));
        assertTrue(Files.exists(storedPath));
        assertTrue(Files.readString(storedPath).contains("Store me"));
    } finally {
        if (previousStorageFile == null) {
            System.clearProperty("quickchat.storage.file");
        } else {
            System.setProperty("quickchat.storage.file", previousStorageFile);
        }
        Files.deleteIfExists(storedPath);
    }
}

    @Test
    void totalCountsOnlySentMessages() {
        Message message = new Message(0, "08575975889", "Hello");
        message.SentMessage("send");
        message.SentMessage("discard");
        assertEquals(1, message.returnTotalMessagess());
    }
}
