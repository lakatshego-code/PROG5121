package za.ac.iie.prog5121;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskArrayTest {

    @Test
    void testMessageIDsAndHashesArePopulatedDynamically() {
        Task task = new Task();

        Message first = new Message("1111111111", 0,
                "+27834557896", "Did you get the cake?");
        first.SentMessage("1");
        task.addMessage(first);

        Message second = new Message("2222222222", 1,
                "+27834484567", "Yohoooo, I am at your gate.");
        second.SentMessage("2");
        task.addMessage(second);

        assertEquals(2, task.getMessageIDs().length);
        assertEquals(2, task.getMessageHashes().length);
        assertEquals("1111111111", task.getMessageIDs()[0]);
        assertEquals("2222222222", task.getMessageIDs()[1]);
        assertNotNull(task.getMessageHashes()[0]);
        assertNotNull(task.getMessageHashes()[1]);
    }

    @Test
    void testEmptyArraysAreCreatedWithoutHardCodedMessages() {
        Task task = new Task();

        assertEquals(0, task.getSentMessages().length);
        assertEquals(0, task.getDisregardedMessages().length);
        assertEquals(0, task.getStoredMessages().length);
        assertEquals(0, task.getMessageIDs().length);
        assertEquals(0, task.getMessageHashes().length);
    }
}
