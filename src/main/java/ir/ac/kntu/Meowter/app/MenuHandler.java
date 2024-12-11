package ir.ac.kntu.Meowter.app;

import ir.ac.kntu.Meowter.exceptions.InvalidCommandException;
import ir.ac.kntu.Meowter.model.Role;
import ir.ac.kntu.Meowter.model.User;
import ir.ac.kntu.Meowter.service.TicketService;
import ir.ac.kntu.Meowter.service.UserService;
import ir.ac.kntu.Meowter.controller.AdminController;
import ir.ac.kntu.Meowter.controller.UserController;
import ir.ac.kntu.Meowter.controller.SupportController;
import ir.ac.kntu.Meowter.controller.TicketController;
import ir.ac.kntu.Meowter.util.CliFormatter;

import java.util.Scanner;

public class MenuHandler {

    private UserService userService;
    private TicketController ticketController;
    private UserController userController;

    public MenuHandler() {
        this.userService = new UserService();
        this.ticketController = new TicketController();
        this.userController = new UserController();
    }

    public void displayMainMenu(User loggedInUser, Role role) {
        Scanner scanner = new Scanner(System.in);

        if (role == Role.USER) {
            displayUserMenu(loggedInUser);
        } else if (role == Role.ADMIN) {
            displayAdminMenu(loggedInUser);
        } else if (role == Role.SUPPORT) {
            displaySupportUserMenu(loggedInUser);
        }
    }

    private void displaySupportUserMenu(User loggedInUser) {
    }

    private void displayAdminMenu(User loggedInUser) {
    }

    public void displayUserMenu(User loggedInUser) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println(CliFormatter.bold("User Menu:"));
            System.out.println(CliFormatter.boldYellow("1. Settings"));
            System.out.println(CliFormatter.boldRed("2. Support Section"));
            System.out.println(CliFormatter.green("3. Users Section"));
            System.out.println(CliFormatter.magenta("4. Posts Section"));
            System.out.println(CliFormatter.cyan("5. User Profile"));
            System.out.println(CliFormatter.red("6. Log out"));
            System.out.println(CliFormatter.blue("7. Exit"));

            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            try{
                switch (choice) {
                    case 1:
                        displaySettings(loggedInUser);
                        break;
                    case 2:
                        ticketController.displayTicketSection(loggedInUser);
                        break;
                    case 3:
                        userController.displayUsersSection(loggedInUser);
                        break;
                    case 4:
                        displayPostsSection(loggedInUser);
                        break;
                    case 5:
                        displayUserProfile(loggedInUser);
                        break;
                    case 6:
                        loggedInUser = null;
                        System.out.println("You have logged out.");
                        return;
                    case 7:
                        System.out.println("Goodbye!");
                        System.exit(0);
                        break;
                    default:
                        throw new InvalidCommandException("Invalid option! Please try again.");
                }
            }
            catch (Exception e) {
                System.out.println(CliFormatter.boldRed(e.getMessage()));
            }
        }
    }

    public void displaySettings(User loggedInUser) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Settings for: " + loggedInUser.getUsername());
            System.out.println("1. Change Username (" + loggedInUser.getUsername() + ")");
            System.out.println("2. Change Password (" + loggedInUser.getPassword() + ")");
            System.out.println("3. Change Privacy Setting (" + loggedInUser.getIsPrivate() + ")");
            System.out.println("4. Go Back");

            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Enter new username: ");
                    String newUsername = scanner.nextLine();
                    loggedInUser = userService.updateUsername(loggedInUser, newUsername);
                    System.out.println("Username updated successfully.");
                    break;

                case 2:
                    System.out.print("Enter new password: ");
                    String newPassword = scanner.nextLine();
                    loggedInUser = userService.updatePassword(loggedInUser, newPassword);
                    System.out.println("Password updated successfully.");
                    break;

                case 3:
                    System.out.print("Make your profile private? (true/false): ");
                    boolean isPrivate = scanner.nextBoolean();
                    scanner.nextLine();
                    loggedInUser = userService.updatePrivacySetting(loggedInUser, isPrivate);
                    System.out.println("Privacy setting updated successfully.");
                    break;

                case 4:
                    return;

                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }


    private void displaySupportSection(User loggedInUser) {
        System.out.println("Support Section");
    }

    private void displayPostsSection(User loggedInUser) {
        System.out.println("Posts Section");
    }

    private void displayUserProfile(User loggedInUser) {
        System.out.println("User Profile: " + loggedInUser.getUsername());
    }
}
