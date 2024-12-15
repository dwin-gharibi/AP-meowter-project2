package ir.ac.kntu.Meowter.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class UserTest {

    private User user1;
    private User user2;
    private User user3;
    private Post post;
    private Like like;

    @BeforeEach
    void setUp() {
        user1 = new User("john_doe", "john@example.com", "password123", Role.USER);
        user2 = new User("jane_doe", "jane@example.com", "password123", Role.USER);
        user3 = new User("admin_user", "admin@example.com", "admin123", Role.ADMIN);
        post = new Post("Hello World!", user1);
        like = new Like(user1, post);
    }

    @Test
    void testCreateUser() {
        assertNotNull(user1);
        assertEquals("john_doe", user1.getUsername());
        assertEquals("john@example.com", user1.getEmail());
        assertTrue(user1.isActive());
    }

    @Test
    void testSetAndGetBio() {
        user1.setBio("This is my bio");
        assertEquals("This is my bio", user1.getBio());
    }

    @Test
    void testFollowUser() {
        user1.followUser(user2);
        assertTrue(user1.getFollowing().contains(user2));
        assertTrue(user2.getFollowers().contains(user1));
    }

    @Test
    void testUnfollowUser() {
        user1.followUser(user2);
        user1.unfollowUser(user2);
        assertFalse(user1.getFollowing().contains(user2));
        assertFalse(user2.getFollowers().contains(user1));
    }

    @Test
    void testAddLike() {
        user1.addLike(like);
        assertTrue(user1.getLikes().contains(like));
    }

    @Test
    void testRemoveLike() {
        user1.addLike(like);
        user1.removeLike(like);
        assertFalse(user1.getLikes().contains(like));
    }

    @Test
    void testUserRole() {
        assertEquals(Role.USER, user1.getRole());
        assertEquals(Role.ADMIN, user3.getRole());
    }

    @Test
    void testIsPrivate() {
        assertFalse(user1.isPrivate());
        user1.setIsPrivate(true);
        assertTrue(user1.isPrivate());
    }

    @Test
    void testEqualsSameObject() {
        assertTrue(user1.equals(user1));
    }

    @Test
    void testEqualsDifferentId() {
        user1.setId(1L);
        user2.setId(2L);
        assertFalse(user1.equals(user2));
    }

    @Test
    void testSetDateOfBirth() {
        LocalDateTime birthDate = LocalDateTime.of(1990, 5, 15, 0, 0);
        user1.setDateofbirth(birthDate);
        assertEquals(birthDate, user1.getDateofbirth());
    }

    @Test
    void testGetActiveStatus() {
        assertTrue(user1.isActive());
        user1.setActive(false);
        assertFalse(user1.isActive());
    }

    @Test
    void testChangeUsername() {
        user1.setUsername("new_username");
        assertEquals("new_username", user1.getUsername());
    }

    @Test
    void testAddFollower() {
        user1.followUser(user2);
        assertTrue(user2.getFollowers().contains(user1));
    }

    @Test
    void testRemoveFollower() {
        user1.followUser(user2);
        user1.unfollowUser(user2);
        assertFalse(user2.getFollowers().contains(user1));
    }

    @Test
    void testRoleAssignment() {
        user1.setRole(Role.ADMIN);
        assertEquals(Role.ADMIN, user1.getRole());
    }

    @Test
    void testHasNoFollowersInitially() {
        assertTrue(user1.getFollowers().isEmpty());
    }

    @Test
    void testGetFollowers() {
        user1.followUser(user2);
        Set<User> followers = user2.getFollowers();
        assertTrue(followers.contains(user1));
    }

    @Test
    void testIsEqualBasedOnId() {
        user1.setId(10L);
        User anotherUser = new User("different_user", "different@example.com", "password", Role.USER);
        anotherUser.setId(10L);
        assertTrue(user1.equals(anotherUser));
    }
}
