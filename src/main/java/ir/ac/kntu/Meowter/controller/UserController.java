package ir.ac.kntu.Meowter.controller;

import ir.ac.kntu.Meowter.service.PostService;
import ir.ac.kntu.Meowter.service.UserService;
import ir.ac.kntu.Meowter.model.User;
import ir.ac.kntu.Meowter.model.FollowRequestStatus;

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
            System.out.println("5. Send Follow Request");
            System.out.println("6. View Follow Requests (Received & Sent)");
            System.out.println("7. Remove Follower");
            System.out.println("8. Unfollow User");
            System.out.println("9. Back to Main Menu");
            System.out.print("Choose an option: ");

            String input = scanner.nextLine();

            if (input.startsWith("#")) {
                String targetUsername = input.substring(1);
                User recipientUser = userService.searchUserByUsername(targetUsername);
                if (recipientUser != null) {
                    userService.sendFollowRequest(loggedInUser, recipientUser);
                    System.out.println("Follow request sent to @" + recipientUser.getUsername());
                } else {
                    System.out.println("User not found.");
                }
                continue;
            }

            int choice = -1;
            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid option. Please try again.");
                continue;
            }

            switch (choice) {
                case 1:
                    userService.getAllUsers().forEach(user -> {
                        System.out.println("Username: @" + user.getUsername() + " | Email: " + user.getEmail());
                        System.out.println("Type #username to follow @" + user.getUsername());
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
                    System.out.print("Enter #username to follow: ");
                    String targetUser = scanner.nextLine().substring(1);
                    User recipient = userService.searchUserByUsername(targetUser);
                    if (recipient != null) {
                        userService.sendFollowRequest(loggedInUser, recipient);
                        System.out.println("Follow request sent to @" + recipient.getUsername());
                    } else {
                        System.out.println("User not found.");
                    }
                    break;

                case 6:
                    System.out.println("Your Follow Requests (Sent & Received):");
                    userService.getFollowRequests(loggedInUser).forEach(request -> {
                        String requesterUsername = request.getRequester().getUsername();
                        String recipientUsername = request.getRecipient().getUsername();

                        if (request.getRequester().equals(loggedInUser)) {
                            System.out.println("Follow request sent to: @" + recipientUsername + " | Status: " + request.getStatus());
                        } else if (request.getRecipient().equals(loggedInUser)) {
                            System.out.println("Follow request received from: @" + requesterUsername + " | Status: " + request.getStatus());
                        }

                        if (request.getStatus() == FollowRequestStatus.PENDING && request.getRecipient().equals(loggedInUser)) {
                            System.out.print("Accept (y/n)? ");
                            String response = scanner.nextLine();
                            if ("y".equalsIgnoreCase(response)) {
                                userService.acceptFollowRequest(loggedInUser, request.getRequester());
                                System.out.println("Follow request accepted.");
                            } else {
                                userService.rejectFollowRequest(loggedInUser, request.getRequester());
                                System.out.println("Follow request rejected.");
                            }
                        }
                    });
                    break;

                case 7:
                    System.out.print("Enter the username of the follower to remove: ");
                    String followerUsername = scanner.nextLine();
                    User follower = userService.searchUserByUsername(followerUsername);
                    if (follower != null) {
                        userService.removeFollower(loggedInUser, follower);
                    } else {
                        System.out.println("Follower not found.");
                    }
                    break;

                case 8:
                    System.out.print("Enter the username of the user to unfollow: ");
                    String unfollowUsername = scanner.nextLine();
                    User unfollowUser = userService.searchUserByUsername(unfollowUsername);
                    if (unfollowUser != null) {
                        userService.unfollowUser(loggedInUser, unfollowUser);
                    } else {
                        System.out.println("User not found.");
                    }
                    break;

                case 9:
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
