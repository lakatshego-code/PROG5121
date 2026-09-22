package za.ac.iie.prog5121;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests required for Programming 1A Part 1.
 */
public class LoginTest {

    private Login login;

    @BeforeEach
    void setUp() {
        login = new Login();
        login.setUserDetails("John", "Smith");
        login.registerUser("kyl_1", "Ch&&sec@ke99!", "+27838968976");
    }

    @Test
    void testUsernameCorrectlyFormatted() {
        assertTrue(login.checkUserName("kyl_1"));
    }

    @Test
    void testUsernameIncorrectlyFormatted() {
        assertFalse(login.checkUserName("kyle!!!!!!!"));
        assertEquals(
                "Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.",
                new Login().registerUser("kyle!!!!!!!", "Ch&&sec@ke99!", "+27838968976"));
    }

    @Test
    void testPasswordMeetsComplexityRequirements() {
        assertTrue(login.checkPasswordComplexity("Ch&&sec@ke99!"));
        assertEquals("Password successfully captured.",
                messageForPassword("Ch&&sec@ke99!"));
    }

    @Test
    void testPasswordDoesNotMeetComplexityRequirements() {
        assertFalse(login.checkPasswordComplexity("password"));
        assertEquals(
                "Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.",
                messageForPassword("password"));
    }

    @Test
    void testCellPhoneCorrectlyFormatted() {
        assertTrue(login.checkCellPhoneNumber("+27838968976"));
        assertEquals("Cell phone number successfully added.",
                messageForCell("+27838968976"));
    }

    @Test
    void testCellPhoneIncorrectlyFormatted() {
        assertFalse(login.checkCellPhoneNumber("08966553"));
        assertEquals("Cell phone number incorrectly formatted or does not contain international code.",
                messageForCell("08966553"));
    }

    @Test
    void testLoginSuccessful() {
        assertTrue(login.loginUser("kyl_1", "Ch&&sec@ke99!"));
        assertEquals("Welcome John Smith it is great to see you again.",
                login.returnLoginStatus(true));
    }

    @Test
    void testLoginFailed() {
        assertFalse(login.loginUser("kyl_1", "wrongPassword"));
        assertEquals("Username or password incorrect, please try again.",
                login.returnLoginStatus(false));
    }

    private String messageForPassword(String password) {
        if (!login.checkUserName("kyl_1")) {
            return "Username error";
        }
        if (!login.checkPasswordComplexity(password)) {
            return "Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.";
        }
        return "Password successfully captured.";
    }

    private String messageForCell(String cell) {
        if (login.checkCellPhoneNumber(cell)) {
            return "Cell phone number successfully added.";
        }
        return "Cell phone number incorrectly formatted or does not contain international code.";
    }
}
