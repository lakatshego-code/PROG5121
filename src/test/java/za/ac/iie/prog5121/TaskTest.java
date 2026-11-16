package za.ac.iie.prog5121;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    @TempDir
    Path tempDir;

    private String previousStorageFile;
    private Task task;

    @BeforeEach
    void setUp() {
        previousStorageFile = System.getProperty("quickchat.storage.file");
        System.setProperty("quickchat.storage.file",
                tempDir.resolve("messages.json").toString());
        task = new Task();
    }

    @AfterEach
    void tearDown() {
        if (previousStorageFile == null) {
            System.clearProperty("quickchat.storage.file");
        } else {
            System.setProperty("quickchat.storage.file", previousStorageFile);
        }
    }

    @Test
    void testMessagesAreAddedToCorrectArrays() {
        Message sent = new Message("1111111111", 0,
                "+27834557896", "Did you get the cake?");
        sent.SentMessage("1");
        task.addMessage(sent);

        Message disregarded = new Message("2222222222", 1,
                "+27834484567", "Yohoooo, I am at your gate.");
        disregarded.SentMessage("2");
        task.addMessage(disregarded);

        assertEquals(1, task.getSentMessages().length);
        assertEquals(1, task.getDisregardedMessages().length);
        assertEquals("Did you get the cake?",
                task.getSentMessages()[0].getMessageText());
    }

    @Test
    void testStoredMessagesCanBeLoadedFromJson() throws Exception {
        Message first = new Message("3333333333", 1,
                "+27838884567",
                "Where are you? You are late! I have asked you to be on time.");
        first.SentMessage("3");

        Message second = new Message("4444444444", 2,
                "+27838884567", "Ok, I am leaving without you.");
        second.SentMessage("3");

        task.loadStoredMessages();

        assertEquals(2, task.getStoredMessages().length);
    }

    @Test
    void testLongestStoredMessage() throws Exception {
        Message first = new Message("3333333333", 1,
                "+27838884567",
                "Where are you? You are late! I have asked you to be on time.");
        first.SentMessage("3");

        Message second = new Message("4444444444", 2,
                "+27838884567", "Ok, I am leaving without you.");
        second.SentMessage("3");

        task.loadStoredMessages();

        assertEquals(
                "Where are you? You are late! I have asked you to be on time.",
                task.getLongestStoredMessage());
    }

    @Test
    void testSearchByMessageID() {
        Message message = new Message("0838884567", 3,
                "0838884567", "It is dinner time!");
        message.SentMessage("1");
        task.addMessage(message);

        Message found = task.searchMessageByID("0838884567");

        assertNotNull(found);
        assertEquals("It is dinner time!", found.getMessageText());
    }

    @Test
    void testSearchByRecipient() throws Exception {
        Message first = new Message("3333333333", 1,
                "+27838884567", "Where are you?");
        first.SentMessage("3");

        Message second = new Message("4444444444", 2,
                "+27838884567", "Ok, I am leaving without you.");
        second.SentMessage("3");

        task.loadStoredMessages();

        String results = task.searchMessagesByRecipient("+27838884567");

        assertTrue(results.contains("Where are you?"));
        assertTrue(results.contains("Ok, I am leaving without you."));
    }

    @Test
    void testDeleteStoredMessageUsingHash() throws Exception {
        Message message = new Message("3333333333", 1,
                "+27838884567", "Message to delete");
        message.SentMessage("3");

        task.loadStoredMessages();
        String hash = task.getStoredMessages()[0].getMessageHash();

        assertTrue(task.deleteMessageByHash(hash));
        assertEquals(0, task.getStoredMessages().length);

        Task reloaded = new Task();
        reloaded.loadStoredMessages();
        assertEquals(0, reloaded.getStoredMessages().length);
    }

    @Test
    void testReportContainsStoredMessageDetails() throws Exception {
        Message message = new Message("3333333333", 1,
                "+27838884567", "Report test message");
        message.SentMessage("3");

        task.loadStoredMessages();
        String report = task.displayReport();

        assertTrue(report.contains("Message Hash:"));
        assertTrue(report.contains("Recipient: +27838884567"));
        assertTrue(report.contains("Message: Report test message"));
    }
}
