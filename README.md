# PROG5121 Programming 1A — Part 1

## Project Information

This project was developed for the PROG5121 module.
It demonstrates a Java-based registration and login system.

## Cellphone Number Validation

The application uses a regular expression (regex) to validate the cellphone number entered by the user.

Regex used:

^(\\+27|0)[6-8][0-9]{8}$

This regex allows South African cellphone numbers that:
- Start with `0` or the international prefix `+27`
- Use a valid mobile prefix from `6` to `8`
- Contain the required number of digits
- Reject numbers that do not follow the expected format

Example of a valid number:
0821234567

Example using the international format:
+27821234567
## Project Classes

### Main.java
The Main class controls the main flow of the QuickChat registration and login application. It collects information from the user and interacts with the Login class.

### Login.java
The Login class contains the registration and login functionality. It validates information such as the username, password and cellphone number before allowing the user to continue.

### LoginTest.java
The LoginTest class is used to test the functionality of the Login class and determine whether the registration and login requirements are working correctly.

The validation functionality makes use of Java's built-in classes and regular-expression capabilities (Oracle, 2026).