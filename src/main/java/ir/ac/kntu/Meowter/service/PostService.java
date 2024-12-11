package ir.ac.kntu.Meowter.service;

import ir.ac.kntu.Meowter.model.Post;
import ir.ac.kntu.Meowter.model.User;
import ir.ac.kntu.Meowter.repository.PostRepository;
import ir.ac.kntu.Meowter.repository.UserRepository;


public class PostService {

    private PostRepository postRepository;
    private UserRepository userRepository;

    public PostService() {
        this.postRepository = new PostRepository();
        this.userRepository = new UserRepository();
    }

    public void createPost(String content, User user) {
        if (user.getId() == null) {
            userRepository.save(user);
        }

        Post post = new Post(content, user);
        postRepository.save(post);
    }

    public void viewAllPosts() {
        for (Post post : postRepository.findAll()) {
            System.out.println("Post: " + post.getContent() + " by " + post.getUser().getUsername());
        }
    }

    public void viewPostsByUser(User user) {
        for (Post post : postRepository.findByUser(user)) {
            System.out.println("Post: " + post.getContent());
        }
    }
}
