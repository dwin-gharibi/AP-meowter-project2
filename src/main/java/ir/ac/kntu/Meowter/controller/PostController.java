package ir.ac.kntu.Meowter.controller;

import ir.ac.kntu.Meowter.model.Post;
import ir.ac.kntu.Meowter.model.User;
import ir.ac.kntu.Meowter.service.PostService;

import java.util.Scanner;

public class PostController {

    private PostService postService;

    public PostController() {
        this.postService = new PostService();
    }

    public void displayPostsSection(User loggedInUser) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Posts Section");
            System.out.println("1. View My Posts");
            System.out.println("2. Add a New Post");
            System.out.println("3. Edit a Post");
            System.out.println("4. Delete a Post");
            System.out.println("5. Manage Comments");
            System.out.println("6. Back to Main Menu");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    postService.getUserPosts(loggedInUser).forEach(post -> {
                        System.out.println("Post ID: " + post.getId() + " | Content: " + post.getContent());
                    });
                    break;
                case 2:
                    System.out.print("Enter post content: ");
                    String content = scanner.nextLine();
                    postService.addPost(loggedInUser, content);
                    System.out.println("Post added successfully.");
                    break;
                case 3:
                    System.out.print("Enter Post ID to edit: ");
                    long postIdToEdit = scanner.nextLong();
                    scanner.nextLine();
                    System.out.print("Enter new content: ");
                    String newContent = scanner.nextLine();
                    postService.editPost(loggedInUser, postIdToEdit, newContent);
                    System.out.println("Post updated successfully.");
                    break;
                case 4:
                    System.out.print("Enter Post ID to delete: ");
                    long postIdToDelete = scanner.nextLong();
                    postService.deletePost(loggedInUser, postIdToDelete);
                    System.out.println("Post deleted successfully.");
                    break;
                case 5:
                    System.out.print("Enter Post ID to manage comments: ");
                    long postIdToManage = scanner.nextLong();
                    scanner.nextLine();
                    postService.manageComments(loggedInUser, postIdToManage);
                    break;
                case 6:
                    return;
                default:
                    System.out.println("Invalid option. Try again.");
                    break;
            }
        }
    }
}
