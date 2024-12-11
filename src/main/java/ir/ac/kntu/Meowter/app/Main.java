package ir.ac.kntu.Meowter.app;

import ir.ac.kntu.Meowter.model.Role;
import ir.ac.kntu.Meowter.model.User;
import ir.ac.kntu.Meowter.service.UserService;
import ir.ac.kntu.Meowter.service.SessionManager;
import ir.ac.kntu.Meowter.exceptions.InvalidCommandException;
import ir.ac.kntu.Meowter.util.CliFormatter;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        CliFormatter.printMeow();
        Scanner scanner = new Scanner(System.in);
        UserService userService = new UserService();

        User loggedInUser = SessionManager.loadSession();
        Role role = null;

        if (loggedInUser != null) {
            System.out.println(CliFormatter.blueUnderlined("❇\uFE0F Welcome back, " + loggedInUser.getUsername() + "! ❇\uFE0F"));
            role = loggedInUser.getRole();
        }

        while (true) {
            if (loggedInUser == null) {
                System.out.println(CliFormatter.boldGreen("Welcome to Meowter!"));
                System.out.println("1. 👤 User");
                System.out.println("2. ⚙️ Admin");
                System.out.println("3. 🛠️ Support User");
                System.out.println("4. Exit");
                System.out.print("Choose your role (1, 2, 3 or 4 to exit): ");

                int roleChoice = scanner.nextInt();
                scanner.nextLine();

                if (roleChoice == 4) {
                    System.out.println("Goodbye!");
                    break;
                }

                if (roleChoice < 1 || roleChoice > 3) {
                    try {
                        throw new InvalidCommandException("Please enter a valid option.");
                    } catch (InvalidCommandException e) {
                        System.out.println(CliFormatter.red(e.getMessage()));
                        continue;
                    }
                }

                role = Role.USER;
                if (roleChoice == 2) {
                    role = Role.ADMIN;
                } else if (roleChoice == 3) {
                    role = Role.SUPPORT;
                }

                loggedInUser = null;

                System.out.println("1. 🔒 Login");
                System.out.println("2. 📝 Register");
                System.out.print("Choose an option: ");
                int choice = scanner.nextInt();
                scanner.nextLine();

                if (choice == 1) {
                    System.out.print("Enter your email: ");
                    String email = scanner.nextLine();
                    System.out.print("Enter your password: ");
                    String password = scanner.nextLine();

                    loggedInUser = userService.login(email, password);
                    if (loggedInUser != null) {
                        System.out.println("Login successful!");
                        SessionManager.saveSession(loggedInUser);
                    } else {
                        System.out.println("Invalid email or password.");
                        continue;
                    }

                } else if (choice == 2) {
                    System.out.print("Enter a username: ");
                    String username = scanner.nextLine();
                    System.out.print("Enter your email: ");
                    String email = scanner.nextLine();
                    System.out.print("Enter your password: ");
                    String password = scanner.nextLine();

                    loggedInUser = userService.register(username, email, password);
                    if (loggedInUser != null) {
                        System.out.println("Registration successful! Please log in.");
                        continue;
                    } else {
                        System.out.println("Registration failed. Email may already be in use.");
                        continue;
                    }
                } else {
                    System.out.println("Invalid option. Please try again.");
                    continue;
                }

            } else {
                MenuHandler menuHandler = new MenuHandler();
                menuHandler.displayMainMenu(loggedInUser, role);

                System.out.println("Do you want to log out? (y/n): ");
                String logoutChoice = scanner.nextLine();
                if ("y".equalsIgnoreCase(logoutChoice)) {
                    System.out.println("Logging out...");
                    SessionManager.clearSession();
                    loggedInUser = null;
                }
            }
        }
    }
}
