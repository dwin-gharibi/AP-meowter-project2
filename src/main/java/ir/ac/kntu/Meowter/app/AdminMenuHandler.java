package ir.ac.kntu.Meowter.app;

import ir.ac.kntu.Meowter.model.User;
import ir.ac.kntu.Meowter.service.AdminService;
import ir.ac.kntu.Meowter.util.CliFormatter;

import java.util.Scanner;

public class AdminMenuHandler {

    private final AdminService adminService = new AdminService();
    private final Scanner scanner = new Scanner(System.in);

    public void displayAdminMenu(User adminUser) {
        System.out.println(CliFormatter.boldGreen("Welcome to the Admin Panel, " + adminUser.getUsername() + "!"));
        while (true) {
            System.out.println(CliFormatter.boldYellow("Admin Menu:"));
            System.out.println("1. View All Users");
            System.out.println("2. Ban a User");
            System.out.println("3. Manage Posts");
            System.out.println("4. View Reports");
            System.out.println("5. Logout");

            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    viewAllUsers();
                    break;
                case 2:
                    banUser();
                    break;
                case 3:
                    managePosts();
                    break;
                case 4:
                    viewReports();
                    break;
                case 5:
                    System.out.println("Exiting Admin Menu...");
                    return;
                default:
                    System.out.println(CliFormatter.red("Invalid option. Please try again."));
            }
        }
    }

    private void viewAllUsers() {
        System.out.println(CliFormatter.boldYellow("List of all users:"));
        adminService.getAllUsers().forEach(user ->
                System.out.println("Username: " + user.getUsername() + ", Role: " + user.getRole())
        );
    }

    private void banUser() {
        System.out.print("Enter the username of the user to ban: ");
        String username = scanner.nextLine();
        boolean success = adminService.banUser(username);
        if (success) {
            System.out.println(CliFormatter.green("User " + username + " has been banned successfully."));
        } else {
            System.out.println(CliFormatter.red("Failed to ban user. Ensure the username is correct."));
        }
    }

    private void managePosts() {
        System.out.println("Feature under development: Post management.");

    }

    private void viewReports() {
        System.out.println("Feature under development: Viewing reports.");

    }
}
