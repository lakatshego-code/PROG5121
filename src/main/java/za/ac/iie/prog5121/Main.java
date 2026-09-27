package za.ac.iie.prog5121;

import java.util.Scanner;

/**
 * Console application for Programming 1A Part 1.
 */
public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Login login = new Login();

        System.out.println("=== QuickChat Registration and Login ===");

        System.out.print("Enter your first name: ");
        String firstName = scanner.nextLine();

        System.out.print("Enter your last name: ");
        String lastName = scanner.nextLine();
        login.setUserDetails(firstName, lastName);

        System.out.print("Enter a username (must contain _ and be no more than 5 characters): ");
        String username = scanner.nextLine();
        System.out.println(login.checkUserName(username)
                ? "Username successfully captured."
                : "Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.");

        System.out.print("Enter a password (8+ chars, capital, number, special character): ");
        String password = scanner.nextLine();
        System.out.println(login.checkPasswordComplexity(password)
                ? "Password successfully captured."
                : "Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.");

        System.out.print("Enter South African cell number in international format (+27XXXXXXXXX): ");
        String cellPhone = scanner.nextLine();
        System.out.println(login.checkCellPhoneNumber(cellPhone)
                ? "Cell phone number successfully added."
                : "Cell phone number incorrectly formatted or does not contain international code.");

        String registrationMessage = login.registerUser(username, password, cellPhone);
        System.out.println(registrationMessage);

        if (registrationMessage.equals("Registration successful.")) {
            System.out.println("\n=== Login ===");
            System.out.print("Enter username: ");
            String enteredUsername = scanner.nextLine();

            System.out.print("Enter password: ");
            String enteredPassword = scanner.nextLine();

            boolean authenticated = login.loginUser(enteredUsername, enteredPassword);
            System.out.println(login.returnLoginStatus(authenticated));
        }

        scanner.close();
    }
}
