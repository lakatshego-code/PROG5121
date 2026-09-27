package za.ac.iie.prog5121;

import java.util.regex.Pattern;

/**
 * Handles registration validation and user authentication for Part 1.
 */
public class Login {

    private String username;
    private String password;
    private String cellPhoneNumber;
    private String firstName;
    private String lastName;

    /**
     * Username must contain an underscore and be no more than five characters.
     */
    public boolean checkUserName(String username) {
        return username != null && username.contains("_") && username.length() <= 5;
    }

    /**
     * Password must contain at least eight characters, one capital letter,
     * one number and one special character.
     */
    public boolean checkPasswordComplexity(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }

        boolean hasCapital = password.matches(".*[A-Z].*");
        boolean hasNumber = password.matches(".*[0-9].*");
        boolean hasSpecial = password.matches(".*[^a-zA-Z0-9].*");

        return hasCapital && hasNumber && hasSpecial;
    }

    /**
     * South African international format: +27 followed by exactly nine digits.
     * The complete value is therefore 12 characters including +27.
     *
     * Regex adapted from the standard regular-expression approach of matching
     * the literal country code followed by a fixed number of digits.
     */
    public boolean checkCellPhoneNumber(String cellPhoneNumber) {
        if (cellPhoneNumber == null) {
            return false;
        }
        String regex = "^\\+27[0-9]{9}$";
        return Pattern.matches(regex, cellPhoneNumber);
    }

    /**
     * Registers the user when all required fields are valid.
     */
    public String registerUser(String username, String password, String cellPhoneNumber) {
        if (!checkUserName(username)) {
            return "Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.";
        }

        if (!checkPasswordComplexity(password)) {
            return "Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.";
        }

        if (!checkCellPhoneNumber(cellPhoneNumber)) {
            return "Cell phone number is incorrectly formatted or does not contain international code.";
        }

        this.username = username;
        this.password = password;
        this.cellPhoneNumber = cellPhoneNumber;

        return "Registration successful.";
    }

    /**
     * Stores the names used by the successful-login message.
     */
    public void setUserDetails(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    /**
     * Checks whether entered login details match the registered details.
     */
    public boolean loginUser(String username, String password) {
        return this.username != null
                && this.password != null
                && this.username.equals(username)
                && this.password.equals(password);
    }

    /**
     * Returns the required login status message.
     */
    public String returnLoginStatus(boolean loginSuccessful) {
        if (loginSuccessful) {
            String first = firstName == null ? "User" : firstName;
            String last = lastName == null ? "" : " " + lastName;
            return "Welcome " + first + last + " it is great to see you again.";
        }
        return "Username or password incorrect, please try again.";
    }

    public String getUsername() {
        return username;
    }

    public String getCellPhoneNumber() {
        return cellPhoneNumber;
    }
}
