package ir.ac.kntu.Meowter.controller;

import ir.ac.kntu.Meowter.service.PostService;
import ir.ac.kntu.Meowter.model.User;

import java.util.Scanner;

public class UserController {

    private PostService postService;

    public UserController() {
        this.postService = new PostService();
    }

    public void displayUserMenu(User loggedInUser) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("User Menu");
        System.out.println("1. Create Post");
        System.out.println("2. View Posts");
        System.out.println("3. Log out");

        System.out.print("Choose an option: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1:
                createPost(loggedInUser);
                break;
            case 2:
                viewPosts(loggedInUser);
                break;
            case 3:
                System.out.println("Logging out...");
                break;
            default:
                System.out.println("Invalid option! Try again.");
                displayUserMenu(loggedInUser);
                break;
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
