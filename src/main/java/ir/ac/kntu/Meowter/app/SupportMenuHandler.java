package ir.ac.kntu.Meowter.app;

import ir.ac.kntu.Meowter.model.User;
import ir.ac.kntu.Meowter.service.SupportService;
import ir.ac.kntu.Meowter.util.CliFormatter;

import java.util.Scanner;

public class SupportMenuHandler {

    private final SupportService supportService = new SupportService();
    private final Scanner scanner = new Scanner(System.in);

    public void displaySupportMenu(User supportUser) {
        System.out.println(CliFormatter.boldGreen("Welcome to the Support Panel, " + supportUser.getUsername() + "!"));
        while (true) {
            System.out.println(CliFormatter.boldYellow("Support Menu:"));
            System.out.println("1. View All Reports");
            System.out.println("2. Respond to a Report");
            System.out.println("3. Assist a User");
            System.out.println("4. Logout");

            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    viewAllReports();
                    break;
                case 2:
                    respondToReport();
                    break;
                case 3:
                    assistUser();
                    break;
                case 4:
                    System.out.println("Exiting Support Menu...");
                    return;
                default:
                    System.out.println(CliFormatter.red("Invalid option. Please try again."));
            }
        }
    }

    private void viewAllReports() {
        System.out.println(CliFormatter.boldYellow("List of all reports:"));
    }

    private void respondToReport() {
        System.out.print("Enter the Report ID to respond to: ");
    }

    private void assistUser() {
        System.out.print("Enter the username of the user to assist: ");
    }
}
