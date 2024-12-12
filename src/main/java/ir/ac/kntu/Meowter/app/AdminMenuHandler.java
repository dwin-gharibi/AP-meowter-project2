package ir.ac.kntu.Meowter.app;

import ir.ac.kntu.Meowter.model.Post;
import ir.ac.kntu.Meowter.model.User;
import ir.ac.kntu.Meowter.service.AdminService;
import ir.ac.kntu.Meowter.service.PostService;
import ir.ac.kntu.Meowter.service.UserService;
import ir.ac.kntu.Meowter.util.CliFormatter;
import ir.ac.kntu.Meowter.util.HtmlReportGeneratorUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AdminMenuHandler {

    private final AdminService adminService = new AdminService();
    private final Scanner scanner = new Scanner(System.in);
    private final PostService postService = new PostService();
    private final UserService userService = new UserService();

    public void displayAdminMenu(User adminUser) {
        System.out.println(CliFormatter.boldGreen("Welcome to the Admin Panel, " + adminUser.getUsername() + "!"));
        while (true) {
            System.out.println(CliFormatter.boldYellow("Admin Menu:"));
            System.out.println("1. View All Users");
            System.out.println("2. Ban a User");
            System.out.println("3. Manage Posts");
            System.out.println("4. Generate Reports");
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
                    generateReports();
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
        userService.getAllUsers().forEach(user ->
                System.out.println(user.getId() + ": " + user.getUsername()));
    }

    private void banUser() {
        System.out.print("Enter the username of the user to ban: ");
        String username = scanner.nextLine();
        if (adminService.banUser(username)) {
            System.out.println(CliFormatter.green("User " + username + " has been banned successfully."));
        } else {
            System.out.println(CliFormatter.red("Failed to ban the user. Please check the username."));
        }
    }

    private void managePosts() {
        System.out.println("Feature under development: Post management.");
    }

    private void generateReports() {
        System.out.println(CliFormatter.boldYellow("Generate Reports Menu:"));
        System.out.println("1. Generate Users Report");
        System.out.println("2. Generate Posts Report");
        System.out.println("3. Generate Comments Report");
        System.out.println("4. Back");

        System.out.print("Choose an option: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1:
                generateUsersReport();
                break;
            case 2:
                generatePostsReport();
                break;
            case 3:
                generateCommentsReport();
                break;
            case 4:
                return;
            default:
                System.out.println(CliFormatter.red("Invalid option. Please try again."));
        }
    }

    private void generateUsersReport() {
        List<User> users = userService.getAllUsers();
        List<String> headers = List.of("User ID", "Username", "Email", "Status");
        List<List<String>> data = new ArrayList<>();

        for (User user : users) {
            data.add(List.of(
                    String.valueOf(user.getId()),
                    user.getUsername(),
                    user.getEmail(),
                    user.isActive() ? "Active" : "Deactive"
            ));
        }

        String reportPath = HtmlReportGeneratorUtil.generateReport("Users Report", headers, data);
        System.out.println(CliFormatter.green("Users Report generated: " + reportPath));
    }

    private void generatePostsReport() {
        List<Post> posts = postService.getAllPosts();
        List<String> headers = List.of("Post ID", "Author", "Content", "Created Date");
        List<List<String>> data = new ArrayList<>();

        posts.forEach(post -> {
            data.add(List.of(
                    String.valueOf(post.getId()),
                    post.getUser().getUsername(),
                    post.getContent(),
                    post.getCreatedAt().toString()
            ));
        });

        String reportPath = HtmlReportGeneratorUtil.generateReport("Posts Report", headers, data);
        System.out.println(CliFormatter.green("Posts Report generated: " + reportPath));
    }

    private void generateCommentsReport() {

    }
}
