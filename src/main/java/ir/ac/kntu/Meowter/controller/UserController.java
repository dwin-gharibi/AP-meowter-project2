package ir.ac.kntu.Meowter.controller;

import ir.ac.kntu.Meowter.service.PostService;
import ir.ac.kntu.Meowter.service.UserService;
import ir.ac.kntu.Meowter.model.User;

import java.util.Scanner;

public class UserController {

    private PostService postService;
    private UserService userService;

    public UserController() {
        this.postService = new PostService();
        this.userService = new UserService();
    }

    public void displayUsersSection(User loggedInUser) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Users Section");
            System.out.println("1. View All Users");
            System.out.println("2. View Followers");
            System.out.println("3. View Followings");
            System.out.println("4. Search Users");
            System.out.println("5. View Follow Requests");
            System.out.println("6. Back to Main Menu");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    userService.getAllUsers().forEach(user -> {
                        System.out.println("Username: @" + user.getUsername() + " | Email: " + user.getEmail());
                    });
                    break;
                case 2:
                    loggedInUser.getFollowers().forEach(follower -> {
                        System.out.println("Follower: @" + follower.getUsername());
                    });
                    break;
                case 3:
                    loggedInUser.getFollowing().forEach(following -> {
                        System.out.println("Following: @" + following.getUsername());
                    });
                    break;
                case 4:
                    System.out.print("Enter username to search: ");
                    String searchTerm = scanner.nextLine();
                    User foundUser = userService.searchUserByUsername(searchTerm);
                    if (foundUser != null) {
                        System.out.println("Username: @" + foundUser.getUsername() + " | Email: " + foundUser.getEmail());
                        if (foundUser.isPrivate() && !loggedInUser.getFollowing().contains(foundUser)) {
                            System.out.println("This profile is private. Follow to see posts.");
                        }
                    } else {
                        System.out.println("User not found.");
                    }
                    break;
                case 5:
                    userService.getFollowRequests(loggedInUser).forEach(request -> {
                        System.out.println("Follow request from: @" + request.getUsername());
                        System.out.print("Accept (y/n)? ");
                        String response = scanner.nextLine();
                        if ("y".equalsIgnoreCase(response)) {
                            userService.acceptFollowRequest(loggedInUser, request);
                            System.out.println("Follow request accepted.");
                        } else {
                            userService.rejectFollowRequest(loggedInUser, request);
                            System.out.println("Follow request rejected.");
                        }
                    });
                    break;
                case 6:
                    return;
                default:
                    System.out.println("Invalid option. Try again.");
                    break;
            }
        }
    }

    private void createPost(User loggedInUser) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter post content: ");
        String content = scanner.nextLine();

        postService.createPost(content, loggedInUser);
        System.out.println("Post created successfully!");
    }

    private void viewPosts(User loggedInUser) {
        postService.viewPostsByUser(loggedInUser);
    }
}
