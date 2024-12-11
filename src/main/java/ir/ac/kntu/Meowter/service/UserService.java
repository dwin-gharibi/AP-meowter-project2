package ir.ac.kntu.Meowter.service;

import ir.ac.kntu.Meowter.model.Role;
import ir.ac.kntu.Meowter.model.User;
import ir.ac.kntu.Meowter.repository.UserRepository;
import ir.ac.kntu.Meowter.util.ValidationUtil;

public class UserService {

    private UserRepository userRepository;

    public UserService() {
        this.userRepository = new UserRepository();
    }

    public void createUser(String username, String email, String password) {
        User user = new User(username, email, password, Role.USER);
        userRepository.save(user);
    }

    public User updateUsername(User user, String newUsername) {
        user.setUsername(newUsername);
        userRepository.update(user);
        return user;
    }

    public User updatePassword(User user, String newPassword) {
        user.setPassword(newPassword);
        userRepository.update(user);
        return user;
    }

    public User updatePrivacySetting(User user, boolean isPrivate) {
        user.setPrivate(isPrivate);
        userRepository.update(user);
        return user;
    }

    public User loginWithUsername(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    public User loginWithEmail(String email, String password) {
        User user = userRepository.findByEmail(email);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    public User register(String username, String email, String password) {

        ValidationUtil.validateUsername(username);
        ValidationUtil.validateEmail(email);
        ValidationUtil.validatePassword(password);


        if (userRepository.existsByEmail(email)) {
            return null;
        }

        User newUser = new User(username, email, password, Role.USER);
        userRepository.save(newUser);
        return newUser;
    }

    public void viewAllUsers() {
        for (User user : userRepository.findAll()) {
            System.out.println("User: " + user.getUsername());
        }
    }
}

