package ir.ac.kntu.Meowter.app;

import ir.ac.kntu.Meowter.exceptions.InvalidCommandException;
import ir.ac.kntu.Meowter.model.Post;
import ir.ac.kntu.Meowter.model.Role;
import ir.ac.kntu.Meowter.model.User;
import ir.ac.kntu.Meowter.service.PostService;
import ir.ac.kntu.Meowter.service.TicketService;
import ir.ac.kntu.Meowter.service.UserService;
import ir.ac.kntu.Meowter.controller.AdminController;
import ir.ac.kntu.Meowter.controller.UserController;
import ir.ac.kntu.Meowter.controller.PostController;
import ir.ac.kntu.Meowter.controller.SupportController;
import ir.ac.kntu.Meowter.controller.TicketController;
import ir.ac.kntu.Meowter.util.CliFormatter;
import ir.ac.kntu.Meowter.util.DateConverter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public class MenuHandler {

    private UserService userService;
    private TicketController ticketController;
    private UserController userController;
    private PostController postController;
    private PostService postService;

    public MenuHandler() {
        this.userService = new UserService();
        this.ticketController = new TicketController();
        this.userController = new UserController();
        this.postController = new PostController();
        this.postService = new PostService();
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
            System.out.println(CliFormatter.boldYellow("0. Home"));
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
                    case 0:
                        displayHome(loggedInUser);
                        break;
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
                        postController.displayPostsSection(loggedInUser);
                        break;
                    case 5:
                        userController.displayProfile(loggedInUser);
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

    public void displayHome(User loggedInUser) {
        Scanner scanner = new Scanner(System.in);
        LocalDateTime start_date = null;
        LocalDateTime end_date = null;

        while (true) {
            System.out.println(CliFormatter.boldYellow("Home Menu:"));
            System.out.println(CliFormatter.boldGreen("1. Subscribe to publisher"));
            System.out.println(CliFormatter.boldBlue("2. View followings posts "));
            System.out.println(CliFormatter.boldRed("3. Change date filter"));
            System.out.println(CliFormatter.boldPurple("4. Go Back"));


            System.out.println(start_date);
            System.out.println(end_date);

            System.out.print(CliFormatter.green("Choose an option: "));
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    CliFormatter.progressBar(CliFormatter.boldYellow("Subscribing to posts ..."), 10);
                    CliFormatter.printTypingEffect(CliFormatter.boldGreen("Listening for posts..."));
                    postService.subscribeToPosts();
                    break;

                case 2:
                    List<Post> posts = postService.getPostsFromFollowings(loggedInUser);
                    for (Post post : posts) {
                        System.out.println(post.getContent());
                    }
                    break;

                case 3:
                    System.out.print("Enter date filters: (YYYY-mm-dd|YYYY-mm-dd) \nNote: They can also be empty for open ranges.\n");
                    String dateStr = scanner.nextLine();

                    if (!dateStr.contains("|")){
                        System.out.print(CliFormatter.boldRed("False Date format."));
                    }

                    String start = dateStr.split("\\|")[0];
                    System.out.println(start);
                    String end = dateStr.split("\\|")[1];
                    System.out.println(end);
                    start_date = DateConverter.convertStringToDate(start.trim());
                    end_date = DateConverter.convertStringToDate(end.trim());
                    CliFormatter.progressBar(CliFormatter.boldYellow("Setting date filters ..."), 5);
                    break;

                case 4:
                    return;

                default:
                    System.out.println(CliFormatter.boldRed("Invalid option. Please try again."));
            }
        }
    }

    public void displaySettings(User loggedInUser) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Settings for: " + CliFormatter.boldGreen(loggedInUser.getUsername()));
            System.out.println("1. Change Username (" + CliFormatter.boldBlue(loggedInUser.getUsername()) + ")");
            System.out.println("2. Change Password (" + CliFormatter.boldYellow(loggedInUser.getPassword()) + ")");
            System.out.println("3. Change Privacy Setting (" + (loggedInUser.getIsPrivate() ? CliFormatter.boldGreen("Yes") : CliFormatter.boldRed("No") )+ ")");
            System.out.println(CliFormatter.boldRed("4. Go Back"));

            System.out.print(CliFormatter.green("Choose an option: "));
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Enter new username: ");
                    String newUsername = scanner.nextLine();
                    loggedInUser = userService.updateUsername(loggedInUser, newUsername);
                    CliFormatter.printTypingEffect(CliFormatter.boldGreen("Username updated successfully."));
                    break;

                case 2:
                    System.out.print("Enter new password: ");
                    String newPassword = scanner.nextLine();
                    loggedInUser = userService.updatePassword(loggedInUser, newPassword);
                    CliFormatter.printTypingEffect(CliFormatter.boldGreen("Password updated successfully."));
                    break;

                case 3:
                    System.out.print("Make your profile private? (true/false): ");
                    boolean isPrivate = scanner.nextBoolean();
                    scanner.nextLine();
                    loggedInUser = userService.updatePrivacySetting(loggedInUser, isPrivate);
                    CliFormatter.printTypingEffect(CliFormatter.boldGreen("Privacy setting updated successfully."));
                    break;

                case 4:
                    return;

                default:
                    System.out.println(CliFormatter.boldRed("Invalid option. Please try again."));
            }
        }
    }

}
