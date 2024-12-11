package ir.ac.kntu.Meowter.service;

import ir.ac.kntu.Meowter.model.Role;
import ir.ac.kntu.Meowter.model.User;
import ir.ac.kntu.Meowter.repository.UserRepository;
import ir.ac.kntu.Meowter.util.ValidationUtil;

import java.time.LocalDateTime;
import java.util.List;

public class UserService {

    private UserRepository userRepository;

    public UserService() {
        this.userRepository = new UserRepository();
    }

    public void createUser(String username, String email, String password) {
        ValidationUtil.validateUsername(username);
        ValidationUtil.validateEmail(email);
        ValidationUtil.validatePassword(password);

        User user = new User(username, email, password, Role.USER);
        userRepository.save(user);
    }

    public User updateUsername(User user, String newUsername) {
        ValidationUtil.validateUsername(newUsername);

        user.setUsername(newUsername);
        userRepository.update(user);
        SessionManager.saveSession(user);

        return user;
    }

    public User updatePassword(User user, String newPassword) {
        ValidationUtil.validatePassword(newPassword);
        user.setPassword(newPassword);
        userRepository.update(user);
        SessionManager.saveSession(user);

        return user;
    }

    public User updateBio(User user, String newBio) {
        user.setBio(newBio);
        userRepository.update(user);
        SessionManager.saveSession(user);

        return user;
    }

    public User updateDateOfBirth(User user, LocalDateTime dateOfBirth) {
        user.setDateofbirth(dateOfBirth);
        userRepository.update(user);
        SessionManager.saveSession(user);

        return user;
    }

    public User updatePrivacySetting(User user, boolean isPrivate) {
        user.setPrivate(isPrivate);
        userRepository.update(user);
        SessionManager.saveSession(user);
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

    public User searchUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public List<User> getFollowRequests(User user) {
        return userRepository.getFollowRequests(user);
    }

    public void acceptFollowRequest(User loggedInUser, User requestUser) {
        loggedInUser.getFollowing().add(requestUser);
        requestUser.getFollowers().add(loggedInUser);
        userRepository.update(loggedInUser);
        userRepository.update(requestUser);
    }

    public void rejectFollowRequest(User loggedInUser, User requestUser) {
        loggedInUser.getFollowRequests().remove(requestUser);
        userRepository.update(loggedInUser);
    }



    public void viewAllUsers() {
        for (User user : userRepository.findAll()) {
            System.out.println("User: " + user.getUsername());
        }
    }
}

