# PROG5121 Programming 1A — QuickChat

## Project Overview

QuickChat is a Java-based messaging application developed for the PROG5121 Programming 1A project. It builds on the original registration and login system by adding message creation, validation, message management and JSON file storage.

The project uses Java 17, Maven, JUnit 5 and Gson.

## Features

### 1. Registration and Login

The application includes a registration and login system with validation for:

- Username requirements.
- Password complexity requirements.
- South African cellphone number formats.
- Login credentials and successful login status.

The cellphone validation supports local numbers beginning with `0` and international numbers beginning with `+27`, subject to the application's validation rules.

### 2. QuickChat Messaging

After successful login, the user can access the QuickChat menu.

The messaging functionality includes:

- Sending a specified number of messages.
- Generating unique-format, 10-digit message IDs.
- Maintaining a message counter.
- Validating recipient cellphone numbers.
- Restricting messages to a maximum of 250 characters.
- Generating a message hash from the message ID, message number, and the first and last words of the message.
- Sending, disregarding or storing messages.
- Saving stored messages in JSON format.
- Displaying the total number of messages sent.

The menu also includes a placeholder for displaying recently sent messages, which currently returns `Coming Soon.`

### 3. Message Storage

Stored messages are written to `messages.json` using the Gson library.

The application uses `messages.json` as its default storage file. The storage path can also be configured through the `quickchat.storage.file` Java system property. This allows automated tests to use temporary files rather than modifying the default message-storage file.

### 4. Automated Testing

JUnit 5 is used to test the login and messaging functionality.

The test classes include:

- `LoginTest.java` — tests registration, login and validation rules.
- `MessageTest.java` — tests message IDs, recipient validation, message hashes, message actions and message-related functionality.

The latest local test run completed successfully with **16 tests passing and no failures or errors**.

### 5. Continuous Integration

A GitHub Actions workflow is configured in `.github/workflows/TestJava.yml`.

The workflow uses Java 17 and Maven to run the project's automated tests when the workflow is triggered by repository changes.


## Technologies Used

- **Java 17** — application development.
- **Apache Maven** — project building and dependency management.
- **JUnit 5** — automated unit testing.
- **Gson 2.11.0** — JSON message storage.
- **Git and GitHub** — version control and source-code hosting.
- **GitHub Actions** — automated test execution.

## How to Build and Test

Make sure Java 17 and Apache Maven are installed and configured.

Open a terminal in the project directory containing `pom.xml` and run:

```bash
mvn clean test
```

This command cleans the previous build, compiles the application and executes the automated tests.

To run the application, open the project in NetBeans, locate `za.ac.iie.prog5121.Main`, and run the main class.

## Development Improvements

The project has been updated to integrate the messaging functionality with the existing login application. Additional improvements include automated test coverage, a GitHub Actions test workflow, and safer message-storage testing using temporary JSON files.

The changes are maintained on the `main` branch of the GitHub repository.

## Repository

[https://github.com/lakatshego-code/PROG5121](https://github.com/lakatshego-code/PROG5121)
