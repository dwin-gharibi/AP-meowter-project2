package ir.ac.kntu.Meowter.app;

import ir.ac.kntu.Meowter.exceptions.InvalidCommandException;
import ir.ac.kntu.Meowter.model.Comment;
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
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

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
            System.out.println(CliFormatter.bold("User Menu:") + "\n" + CliFormatter.boldYellow("0. Home"));
            System.out.println(CliFormatter.boldYellow("1. Settings") + "\n" + CliFormatter.boldRed("2. Support Section"));
            System.out.println(CliFormatter.green("3. Users Section") + "\n" + CliFormatter.magenta("4. Posts Section"));
            System.out.println(CliFormatter.cyan("5. User Profile") + "\n" + CliFormatter.red("6. Log out"));
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
            } catch (Exception e) {
                System.out.println(CliFormatter.boldRed(e.getMessage()));
            }
        }
    }

    public void displayHome(User loggedInUser) {
        Scanner scanner = new Scanner(System.in);
        LocalDateTime start_date = null;
        LocalDateTime end_date = null;

        while (true) {
            showHomeMenu();

            System.out.print(CliFormatter.green("Choose an option: "));
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    subscribeToPublisher();
                    break;

                case 2:
                    viewFollowingsPosts(loggedInUser, scanner, start_date, end_date);
                    break;

                case 3:
                    LocalDateTime[] dates = setDateFilter(scanner);
                    start_date = dates[0];
                    end_date = dates[1];
                    break;

                case 4:
                    System.out.print("Enter new labels: ");
                    System.out.print(CliFormatter.boldBlue("Available Labels: #SPORT, #ART, #TECHNOLOGY, #TRAVEL, #FOOD, #ENTERTAINMENT, #EDUCATION\n"));
                    String newlabels = scanner.nextLine();
                    userService.setLabels(loggedInUser, newlabels);
                    CliFormatter.printTypingEffect(CliFormatter.boldGreen("Favorite labels updated successfully."));
                    return;

                case 5:
                    return;

                default:
                    System.out.println(CliFormatter.boldRed("Invalid option. Please try again."));
            }
        }
    }

    private void showHomeMenu() {
        System.out.println(CliFormatter.boldYellow("Home Menu:"));
        System.out.println(CliFormatter.boldGreen("1. Subscribe to publisher"));
        System.out.println(CliFormatter.boldBlue("2. View followings posts"));
        System.out.println(CliFormatter.boldRed("3. Change date filter"));
        System.out.println(CliFormatter.boldRed("4. Set favorite labels"));
        System.out.println(CliFormatter.boldPurple("5. Go Back"));
    }

    private void subscribeToPublisher() {
        CliFormatter.progressBar(CliFormatter.boldYellow("Subscribing to posts ..."), 10);
        CliFormatter.printTypingEffect(CliFormatter.boldGreen("Listening for posts..."));
        postService.subscribeToPosts();
    }

    private void viewFollowingsPosts(User loggedInUser, Scanner scanner, LocalDateTime start_date, LocalDateTime end_date) {
        List<Post> posts = postService.getPostsFromFollowings(loggedInUser);
        CliFormatter.progressBar(CliFormatter.boldGreen("Loading the posts ..."), 10);

        if (posts.isEmpty()) {
            System.out.println(CliFormatter.boldRed("No posts found!"));
            return;
        }

        Date start_date_new = start_date != null ? Date.from(start_date.atZone(ZoneId.systemDefault()).toInstant()) : null;
        Date end_date_new = end_date != null ? Date.from(end_date.atZone(ZoneId.systemDefault()).toInstant()) : null;

        posts.stream().filter(post -> {
                    if (loggedInUser.getUser_labels().isEmpty()) {
                        return true;
                    }
                    return post.getLabels().stream().anyMatch(loggedInUser.getUser_labels()::contains);
                })
                .filter(post -> (start_date_new == null || !post.getCreatedAt().before(start_date_new)) &&
                        (end_date_new == null || !post.getCreatedAt().after(end_date_new)))
                .forEach(this::displayPost);

        handlePostRequests(loggedInUser, scanner);
    }

    private void displayPost(Post selectedPost) {
        try {
            String postDetail = "Post ID: #" + CliFormatter.blue(String.valueOf(selectedPost.getId())) + "\n" +
                    "Content: " + CliFormatter.boldGreen(selectedPost.getContent()) + "\n" +
                    "Created At: " + CliFormatter.boldBlue(selectedPost.getCreatedAt().toString()) + "\n" +
                    "Likes: " + CliFormatter.yellow(String.valueOf(selectedPost.getLikes().size())) + "\n" +
                    "Hashtags: " + (selectedPost.getHashtags().isEmpty() ? CliFormatter.red("No hashtags") : CliFormatter.cyan(selectedPost.getHashtags().toString())) + "\n" +
                    "Comments:\n";

            if (!selectedPost.getComments().isEmpty()) {
                StringBuilder commentsDetails = new StringBuilder();
                processComments(selectedPost.getComments(), commentsDetails, 1);
                postDetail += commentsDetails.toString();
            } else {
                postDetail += CliFormatter.red("    No comments yet.\n");
            }

            System.out.println(postDetail);
        } catch (Exception e) {
            System.out.println(CliFormatter.boldRed("No post founded."));
        }
    }

    private void processComments(Set<Comment> comments, StringBuilder commentsDetails, int level) {
        String indentation = "    ".repeat(level);
        comments.forEach(comment -> {
            commentsDetails.append(indentation)
                    .append("- Comment by ")
                    .append(CliFormatter.blue(comment.getUser().getUsername()))
                    .append(": ")
                    .append(CliFormatter.cyan(comment.getContent()))
                    .append(" #")
                    .append(CliFormatter.yellow(comment.getId().toString()))
                    .append("\n");

            if (comment.getReplies() != null && !comment.getReplies().isEmpty()) {
                processComments(comment.getReplies(), commentsDetails, level + 1);
            }
        });
    }

    private void handlePostRequests(User loggedInUser, Scanner scanner) {
        while (true) {
            System.out.println(CliFormatter.boldYellow("1. Handle Requests (L[id], C[id], R[id] #[hashtag])"));
            System.out.println(CliFormatter.boldPurple("2. Back to Main Menu"));
            System.out.print(CliFormatter.magenta("Choose an option: "));
            int choice_request = scanner.nextInt();
            scanner.nextLine();

            switch (choice_request) {
                case 1:
                    postController.handleRequests(loggedInUser, scanner);
                    break;
                case 2:
                    return;
                default:
                    System.out.println(CliFormatter.boldRed("Invalid option. Try again."));
            }
        }
    }

    private LocalDateTime[] setDateFilter(Scanner scanner) {
        System.out.print("Enter date filters: (YYYY-mm-dd|YYYY-mm-dd) \nNote: They can also be empty for open ranges.\n");
        String dateStr = scanner.nextLine();

        if (!dateStr.contains("|")) {
            System.out.println(CliFormatter.boldRed("Invalid date format."));
            return new LocalDateTime[]{null, null};
        }

        String start = dateStr.split("\\|")[0].trim();
        String end = dateStr.split("\\|")[1].trim();

        LocalDateTime start_date = start.isEmpty() ? null : DateConverter.convertStringToDate(start);
        LocalDateTime end_date = end.isEmpty() ? null : DateConverter.convertStringToDate(end);

        CliFormatter.progressBar(CliFormatter.boldYellow("Setting date filters ..."), 5);

        return new LocalDateTime[]{start_date, end_date};
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
