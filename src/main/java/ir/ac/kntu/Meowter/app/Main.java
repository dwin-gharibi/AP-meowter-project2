package ir.ac.kntu.Meowter.app;

import ir.ac.kntu.Meowter.controller.UserController;
import ir.ac.kntu.Meowter.model.Role;
import ir.ac.kntu.Meowter.model.User;
import ir.ac.kntu.Meowter.service.UserService;
import ir.ac.kntu.Meowter.service.SessionManager;
import ir.ac.kntu.Meowter.exceptions.InvalidCommandException;
import ir.ac.kntu.Meowter.util.ArithmeticCaptchaUtil;
import ir.ac.kntu.Meowter.util.CliFormatter;
import ir.ac.kntu.Meowter.util.DateConverter;
import ir.ac.kntu.Meowter.exceptions.CaptchaVerificationException;

import java.util.Scanner;

//   userService.createUser("admin", "admin@admin.com", "Admin@123", Role.ADMIN);
//   userService.createUser("support", "support@support.com", "Support@123", Role.SUPPORT);

public class Main {
    public static void main(String[] args) {
        CliFormatter.printMeow();
        Scanner scanner = new Scanner(System.in);
        UserService userService = new UserService();
        UserController userController = new UserController();
        User loggedInUser = SessionManager.loadSession();
        Role role = null;
        if (loggedInUser != null) {
            System.out.println(CliFormatter.blueUnderlined("❇\uFE0F Welcome back, @" + loggedInUser.getUsername() + "! ❇\uFE0F"));
            role = loggedInUser.getRole();
        }
        while (true) {
            if (loggedInUser == null) {
                CliFormatter.printTypingEffect(CliFormatter.boldGreen("\uD83D\uDC3E  Welcome to Meowter \uD83D\uDC3E"));
                System.out.println(CliFormatter.blue("1. 👤 User"));
                System.out.println(CliFormatter.green("2. ⚙️ Admin"));
                System.out.println(CliFormatter.yellow("3. 🛠️ Support User"));
                System.out.println(CliFormatter.boldRed("4. Exit"));
                System.out.print(CliFormatter.bold("Choose your role (1, 2, 3 or 4 to exit): "));
                int roleChoice = scanner.nextInt();
                scanner.nextLine();
                if (roleChoice == 4) {
                    System.out.println(CliFormatter.boldRed("Goodbye!"));
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
                try {
                    ArithmeticCaptchaUtil.askCaptchaOrThrow();
                } catch (CaptchaVerificationException e) {
                    System.out.println(CliFormatter.boldRed(e.getMessage()));
                    break;
                }
                if (role == Role.ADMIN || role == Role.SUPPORT) {
                    System.out.println(CliFormatter.boldBlue("1. 🔒 Login"));
                    System.out.print(CliFormatter.boldGreen("Choose an option (Type anything else for turning back to main menu): "));
                    int choice = scanner.nextInt();
                    scanner.nextLine();

                    if (choice == 1) {
                        System.out.print(CliFormatter.boldYellow("Enter your email or username: "));
                        String username = scanner.nextLine();
                        System.out.print(CliFormatter.boldPurple("Enter your password: "));
                        String password = scanner.nextLine();

                        loggedInUser = attemptLogin(userService, username, password, role);
                        if (loggedInUser == null) {
                            continue;
                        }
                    } else {
                        CliFormatter.printTypingEffect("Turning back to main menu...");
                        continue;
                    }
                } else {
                    System.out.println(CliFormatter.blue("1. 🔒 Login"));
                    System.out.println(CliFormatter.cyan("2. 📝 Register"));
                    System.out.print(CliFormatter.boldGreen("Choose an option (Type anything else for turning back to main menu): "));
                    int choice = scanner.nextInt();
                    scanner.nextLine();
                    if (choice == 1) {
                        System.out.print(CliFormatter.boldYellow("Enter your email or username: "));
                        String username = scanner.nextLine();
                        System.out.print(CliFormatter.boldPurple("Enter your password: "));
                        String password = scanner.nextLine();
                        loggedInUser = attemptLogin(userService, username, password, Role.USER);
                        if (loggedInUser == null) {
                            continue;
                        }
                    } else if (choice == 2) {
                        userController.registerSection(loggedInUser);
                    } else {
                        CliFormatter.printTypingEffect("Turning back to main menu...");
                        continue;
                    }
                }
            } else {
                if(userController.menuSection(loggedInUser, role)) {
                    loggedInUser = null;
                }
            }
        }
    }

    private static User attemptLogin(UserService userService, String username, String password, Role expectedRole) {
        User loggedInUser = null;
        if (username.contains("@")) {
            loggedInUser = userService.loginWithEmail(username, password);
        } else {
            loggedInUser = userService.loginWithUsername(username, password);
        }

        if (loggedInUser != null) {
            if (loggedInUser.getRole() != expectedRole) {
                System.out.println(CliFormatter.red("Access denied. You do not have the proper role to log in as " + expectedRole + "."));
                loggedInUser = null;
            } else {
                System.out.println(CliFormatter.boldGreen("Login successful!"));
                SessionManager.saveSession(loggedInUser);
            }
        } else {
            System.out.println(CliFormatter.boldRed("Invalid email/username or password."));
        }
        return loggedInUser;
    }
}
