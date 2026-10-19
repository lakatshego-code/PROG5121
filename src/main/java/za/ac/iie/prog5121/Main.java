package za.ac.iie.prog5121;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/** Console application for Programming 1A Parts 1 and 2. */
public class Main {
    private static int totalMessagesSent = 0;
    private static int nextMessageNumber = 0;
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            Login login = new Login();
            System.out.println("=== QuickChat Registration and Login ===");
            System.out.print("Enter your first name: ");
            String firstName = scanner.nextLine();
            System.out.print("Enter your last name: ");
            String lastName = scanner.nextLine();
            login.setUserDetails(firstName, lastName);

            System.out.print("Enter a username (must contain _ and be no more than 5 characters): ");
            String username = scanner.nextLine();
            System.out.println(login.checkUserName(username) ? "Username successfully captured."
                    : "Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.");
            System.out.print("Enter a password (8+ chars, capital, number, special character): ");
            String password = scanner.nextLine();
            System.out.println(login.checkPasswordComplexity(password) ? "Password successfully captured."
                    : "Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.");
            System.out.print("Enter South African cell number in international format (+27XXXXXXXXX): ");
            String cellPhone = scanner.nextLine();
            System.out.println(login.checkCellPhoneNumber(cellPhone) ? "Cell phone number successfully added."
                    : "Cell phone number incorrectly formatted or does not contain international code.");

            String registrationMessage = login.registerUser(username, password, cellPhone);
            System.out.println(registrationMessage);
            if (!registrationMessage.equals("Registration successful.")) return;

            System.out.println("\n=== Login ===");
            System.out.print("Enter username: ");
            String enteredUsername = scanner.nextLine();
            System.out.print("Enter password: ");
            String enteredPassword = scanner.nextLine();
            boolean authenticated = login.loginUser(enteredUsername, enteredPassword);
            System.out.println(login.returnLoginStatus(authenticated));
            if (authenticated) quickChatMenu(scanner);
        }
    }

    private static void quickChatMenu(Scanner scanner) {
        System.out.println("\nWelcome to QuickChat.");
        boolean running = true;
        while (running) {
            System.out.println("\n1) Send Messages");
            System.out.println("2) Show recently sent messages");
            System.out.println("3) Quit");
            System.out.print("Choose an option: ");
            String option = scanner.nextLine().trim();
            switch (option) {
                case "1": sendMessages(scanner); break;
                case "2": System.out.println("Coming Soon."); break;
                case "3": running = false; System.out.println("Goodbye."); break;
                default: System.out.println("Invalid option. Please choose 1, 2, or 3.");
            }
        }
    }

    private static void sendMessages(Scanner scanner) {
        int numberOfMessages;
        while (true) {
            System.out.print("How many messages would you like to enter? ");
            try {
                numberOfMessages = Integer.parseInt(scanner.nextLine().trim());
                if (numberOfMessages >= 0) break;
            } catch (NumberFormatException ignored) { }
            System.out.println("Please enter a valid non-negative whole number.");
        }

        List<Message> sentMessages = new ArrayList<>();
        for (int i = 0; i < numberOfMessages; i++) {
            System.out.println("\nMessage " + (i + 1) + " of " + numberOfMessages);
            Message message;
            while (true) {
                System.out.print("Enter recipient number (for example +27718693002 or 08575975889): ");
                String recipient = scanner.nextLine().trim();
                System.out.print("Enter message (maximum 250 characters): ");
                String text = scanner.nextLine();
                message = new Message(nextMessageNumber, recipient, text);
                if (!message.isRecipientValid()) {
                    System.out.println("Invalid recipient number. Please enter a valid phone number.");
                    continue;
                }
                if (!message.isMessageLengthValid()) {
                    System.out.println("Please enter a message of less than 250 characters.");
                    continue;
                }
                break;
            }
            nextMessageNumber++;
            System.out.println("Message ID generated: " + message.getMessageID());
            System.out.println("Message Hash: " + message.getMessageHash());
           
System.out.println("Choose: 1) Send  2) Disregard  3) Store Message for later");
System.out.print("Your choice: ");
String choice = scanner.nextLine().trim();

if (choice.equals("2")) {
    System.out.println("Press 0 to delete the message");
    System.out.print("Enter 0 to confirm deletion, or any other key to keep it: ");
    String deleteChoice = scanner.nextLine().trim();

    if (deleteChoice.equals("0")) {
        message.SentMessage("2");
        System.out.println("Message deleted.");
    } else {
        System.out.println("Message kept.");
    }
} else {
    String result = message.SentMessage(choice);
    System.out.println(result);

    if ("Sent".equals(message.getStatus())) {
        totalMessagesSent++;
        System.out.println(message.printMessages());
        sentMessages.add(message);
    }
}

        }
        int total = totalMessagesSent;
        System.out.println("Total messages sent: " + total);
    }
}
