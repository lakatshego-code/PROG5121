package za.ac.iie.prog5121;

import java.io.IOException;
import java.util.Scanner;

public class Main {

    private static int totalMessagesSent = 0;
    private static int nextMessageNumber = 0;
    private static final Task task = new Task();

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
            System.out.println(login.checkUserName(username)
                    ? "Username successfully captured."
                    : "Username is not correctly formatted.");

            System.out.print("Enter a password (8+ chars, capital, number, special character): ");
            String password = scanner.nextLine();
            System.out.println(login.checkPasswordComplexity(password)
                    ? "Password successfully captured."
                    : "Password is not correctly formatted.");

            System.out.print("Enter South African cell number in international format (+27XXXXXXXXX): ");
            String cellPhone = scanner.nextLine();
            System.out.println(login.checkCellPhoneNumber(cellPhone)
                    ? "Cell phone number successfully added."
                    : "Cell phone number incorrectly formatted.");

            String registrationMessage = login.registerUser(username, password, cellPhone);
            System.out.println(registrationMessage);

            if (!registrationMessage.equals("Registration successful.")) {
                return;
            }

            System.out.println("\n=== Login ===");
            System.out.print("Enter username: ");
            String enteredUsername = scanner.nextLine();
            System.out.print("Enter password: ");
            String enteredPassword = scanner.nextLine();

            boolean authenticated = login.loginUser(enteredUsername, enteredPassword);
            System.out.println(login.returnLoginStatus(authenticated));

            if (authenticated) {
                quickChatMenu(scanner);
            }
        }
    }

    private static void quickChatMenu(Scanner scanner) {
        System.out.println("\nWelcome to QuickChat.");

        boolean running = true;

        while (running) {
            System.out.println("\n1) Send Messages");
            System.out.println("2) Show recently sent messages");
            System.out.println("3) Quit");
            System.out.println("4) Stored Messages");
            System.out.print("Choose an option: ");

            String option = scanner.nextLine().trim();

            switch (option) {
                case "1":
                    sendMessages(scanner);
                    break;
                case "2":
                    System.out.println("Coming Soon.");
                    break;
                case "3":
                    running = false;
                    System.out.println("Goodbye.");
                    break;
                case "4":
                    storedMessagesMenu(scanner);
                    break;
                default:
                    System.out.println("Invalid option. Please choose 1, 2, 3, or 4.");
            }
        }
    }

    private static void sendMessages(Scanner scanner) {
        int numberOfMessages;

        while (true) {
            System.out.print("How many messages would you like to enter? ");

            try {
                numberOfMessages = Integer.parseInt(scanner.nextLine().trim());
                if (numberOfMessages >= 0) {
                    break;
                }
            } catch (NumberFormatException ignored) {
            }

            System.out.println("Please enter a valid non-negative whole number.");
        }

        for (int i = 0; i < numberOfMessages; i++) {
            System.out.println("\nMessage " + (i + 1) + " of " + numberOfMessages);

            Message message;

            while (true) {
                System.out.print("Enter recipient number: ");
                String recipient = scanner.nextLine().trim();

                System.out.print("Enter message (maximum 250 characters): ");
                String text = scanner.nextLine();

                message = new Message(nextMessageNumber, recipient, text);

                if (!message.isRecipientValid()) {
                    System.out.println("Invalid recipient number. Please try again.");
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
                message.SentMessage("2");
                task.addMessage(message);

                System.out.println("Press 0 to delete the message");
                System.out.print("Enter 0 to confirm deletion, or any other key to keep it: ");
                String deleteChoice = scanner.nextLine().trim();

                if (deleteChoice.equals("0")) {
                    System.out.println("Message deleted.");
                } else {
                    System.out.println("Message kept.");
                }
            } else {
                String result = message.SentMessage(choice);
                System.out.println(result);

                if ("Sent".equals(message.getStatus())) {
                    totalMessagesSent++;
                    task.addMessage(message);
                    System.out.println(message.printMessages());
                } else if ("Stored".equals(message.getStatus())) {
                    task.addMessage(message);
                }
            }
        }

        System.out.println("Total messages sent: " + totalMessagesSent);
    }

    private static void storedMessagesMenu(Scanner scanner) {
        try {
            task.loadStoredMessages();
        } catch (IOException ex) {
            System.out.println("Error loading stored messages: " + ex.getMessage());
            return;
        }

        boolean running = true;

        while (running) {
            System.out.println("\n=== Stored Messages Menu ===");
            System.out.println("1) Display senders and recipients");
            System.out.println("2) Display longest stored message");
            System.out.println("3) Search for a message by ID");
            System.out.println("4) Search messages by recipient");
            System.out.println("5) Delete a message using its hash");
            System.out.println("6) Display full report of stored messages");
            System.out.println("7) Return to main menu");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    System.out.println(task.displayStoredSendersAndRecipients());
                    break;

                case "2":
                    String longest = task.getLongestStoredMessage();
                    System.out.println(longest.isEmpty()
                            ? "No stored messages found."
                            : "Longest stored message: " + longest);
                    break;

                case "3":
                    System.out.print("Enter message ID: ");
                    String id = scanner.nextLine().trim();
                    Message found = task.searchMessageByID(id);

                    if (found == null) {
                        System.out.println("Message not found.");
                    } else {
                        System.out.println("Recipient: " + found.getRecipient());
                        System.out.println("Message: " + found.getMessageText());
                    }
                    break;

                case "4":
                    System.out.print("Enter recipient number: ");
                    String recipient = scanner.nextLine().trim();
                    String messages = task.searchMessagesByRecipient(recipient);

                    System.out.println(messages.isEmpty()
                            ? "No messages found for this recipient."
                            : messages);
                    break;

                case "5":
                    System.out.print("Enter message hash: ");
                    String hash = scanner.nextLine().trim();

                    try {
                        if (task.deleteMessageByHash(hash)) {
                            System.out.println("Message successfully deleted.");
                        } else {
                            System.out.println("Message hash not found.");
                        }
                    } catch (IOException ex) {
                        System.out.println("Could not update messages.json: " + ex.getMessage());
                    }
                    break;

                case "6":
                    System.out.println(task.displayReport());
                    break;

                case "7":
                    running = false;
                    break;

                default:
                    System.out.println("Invalid option. Please choose 1 to 7.");
            }
        }
    }
}
