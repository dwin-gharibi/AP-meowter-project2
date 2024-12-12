package ir.ac.kntu.Meowter.controller;

import ir.ac.kntu.Meowter.model.Post;
import ir.ac.kntu.Meowter.model.User;
import ir.ac.kntu.Meowter.service.PostService;

import java.util.List;
import java.util.Scanner;

public class PostController {

    private PostService postService;

    public PostController() {
        this.postService = new PostService();
    }

    public void displayPostsSection(User loggedInUser) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nPosts Section");
            System.out.println("1. View My Posts");
            System.out.println("2. Add a New Post");
            System.out.println("3. Edit a Post");
            System.out.println("4. Delete a Post");
            System.out.println("5. Manage Comments");
            System.out.println("6. Handle Requests (L[id], C[id], #[hashtag])");
            System.out.println("7. Back to Main Menu");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    displayUserPosts(loggedInUser);
                    break;
                case 2:
                    addNewPost(loggedInUser, scanner);
                    break;
                case 3:
                    editPost(loggedInUser, scanner);
                    break;
                case 4:
                    deletePost(loggedInUser, scanner);
                    break;
                case 5:
                    manageComments(loggedInUser, scanner);
                    break;
                case 6:
                    handleRequests(loggedInUser, scanner);
                    break;
                case 7:
                    return;
                default:
                    System.out.println("Invalid option. Try again.");
                    break;
            }
        }
    }

    private void displayUserPosts(User loggedInUser) {
        List<Post> posts = postService.getUserPosts(loggedInUser);
        if (posts.isEmpty()) {
            System.out.println("You haven't created any posts yet.");
        } else {
            posts.forEach(post -> System.out.println("Post ID: " + post.getId() + " | Content: " + post.getContent()));
        }
    }

    private void addNewPost(User loggedInUser, Scanner scanner) {
        System.out.print("Enter post content: ");
        String content = scanner.nextLine();
        postService.addPost(loggedInUser, content);
        System.out.println("Post added successfully.");
    }

    private void editPost(User loggedInUser, Scanner scanner) {
        System.out.print("Enter Post ID to edit: ");
        long postIdToEdit = scanner.nextLong();
        scanner.nextLine();
        System.out.print("Enter new content: ");
        String newContent = scanner.nextLine();
        boolean success = postService.editPost(loggedInUser, postIdToEdit, newContent);
        if (success) {
            System.out.println("Post updated successfully.");
        } else {
            System.out.println("Failed to update post. Make sure you are the owner of the post.");
        }
    }

    private void deletePost(User loggedInUser, Scanner scanner) {
        System.out.print("Enter Post ID to delete: ");
        long postIdToDelete = scanner.nextLong();
        boolean success = postService.deletePost(loggedInUser, postIdToDelete);
        if (success) {
            System.out.println("Post deleted successfully.");
        } else {
            System.out.println("Failed to delete post. Make sure you are the owner of the post.");
        }
    }

    private void manageComments(User loggedInUser, Scanner scanner) {
        System.out.print("Enter Post ID to manage comments: ");
        long postIdToManage = scanner.nextLong();
        scanner.nextLine();
        System.out.println("Comment management not yet implemented. Placeholder for future functionality.");

    }

    private void handleRequests(User loggedInUser, Scanner scanner) {
        System.out.println("Handle requests like L[id], C[id], #[hashtag]. Type 'back' to return.");
        while (true) {
            System.out.print("Enter your request: ");
            String input = scanner.nextLine();

            if (input.equalsIgnoreCase("back")) {
                return;
            }

            if (input.startsWith("L")) {
                handleLikeRequest(loggedInUser, input);
            } else if (input.startsWith("C")) {
                handleCommentRequest(loggedInUser, scanner, input);
            } else if (input.startsWith("#")) {
                handleHashtagSearch(input);
            } else {
                System.out.println("Invalid request. Use L[id], C[id], or #[hashtag].");
            }
        }
    }

    private void handleLikeRequest(User loggedInUser, String input) {
        try {
            long postId = Long.parseLong(input.substring(1));
            boolean success = postService.addLike(loggedInUser, postId);
            if (success) {
                System.out.println("You liked the post with ID: " + postId);
            } else {
                System.out.println("Failed to like the post. It might not exist.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid format for like request. Use L[id].");
        }
    }

    private void handleCommentRequest(User loggedInUser, Scanner scanner, String input) {
        try {
            long postId = Long.parseLong(input.substring(1));
            System.out.print("Enter your comment: ");
            String commentContent = scanner.nextLine();
            boolean success = postService.addComment(loggedInUser, postId, commentContent);
            if (success) {
                System.out.println("Your comment was added to post ID: " + postId);
            } else {
                System.out.println("Failed to comment on the post. It might not exist.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid format for comment request. Use C[id].");
        }
    }

    private void handleHashtagSearch(String input) {
        String hashtag = input.substring(1);
        List<Post> posts = postService.searchPostsByHashtag(hashtag);
        if (posts.isEmpty()) {
            System.out.println("No posts found with hashtag #" + hashtag);
        } else {
            System.out.println("Posts with hashtag #" + hashtag + ":");
            posts.forEach(post -> System.out.println("Post ID: " + post.getId() + " | Content: " + post.getContent()));
        }
    }
}
