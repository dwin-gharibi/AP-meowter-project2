package ir.ac.kntu.Meowter.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

class PostTest {

    private Post post;
    private User user;
    private Comment comment;
    private Like like;

    @BeforeEach
    void setUp() {
        user = new User("john_doe", "john@example.com", "password123", Role.USER);
        post = new Post("This is a #test post with #hashtags", user);
        comment = new Comment("Nice post!", post, user);
        like = new Like(user, post);
    }

    @Test
    void testCreatePost() {
        assertNotNull(post);
        assertEquals("This is a #test post with #hashtags", post.getContent());
        assertEquals(user, post.getUser());
        assertNotNull(post.getCreatedAt());
    }

    @Test
    void testSetAndGetContent() {
        post.setContent("Updated content");
        assertEquals("Updated content", post.getContent());
    }

    @Test
    void testSetAndGetUser() {
        User newUser = new User("jane_doe", "jane@example.com", "password123", Role.USER);
        post.setUser(newUser);
        assertEquals(newUser, post.getUser());
    }

    @Test
    void testAddComment() {
        post.addComment(comment);
        assertTrue(post.getComments().contains(comment));
        assertEquals(post, comment.getPost());
    }

    @Test
    void testRemoveComment() {
        post.addComment(comment);
        post.removeComment(comment);
        assertFalse(post.getComments().contains(comment));
        assertNull(comment.getPost());
    }

    @Test
    void testAddLike() {
        post.addLike(like);
        assertTrue(post.getLikes().contains(like));
        assertEquals(post, like.getPost());
    }

    @Test
    void testRemoveLike() {
        post.addLike(like);
        post.removeLike(like);
        assertFalse(post.getLikes().contains(like));
        assertNull(like.getPost());
    }

    @Test
    @Disabled
    void testExtractHashtags() {
        post.extractHashtags();
        Set<String> expectedHashtags = Set.of("#test", "#hashtags");
        assertEquals(expectedHashtags, post.getHashtags());
    }

    @Test
    void testSetAndGetHashtags() {
        Set<String> hashtags = Set.of("#example", "#junit");
        post.setHashtags(hashtags);
        assertEquals(hashtags, post.getHashtags());
    }

    @Test
    void testSetAndGetComments() {
        Set<Comment> comments = new HashSet<>();
        comments.add(comment);
        post.setComments(comments);
        assertEquals(comments, post.getComments());
    }

    @Test
    void testSetAndGetLikes() {
        Set<Like> likes = new HashSet<>();
        likes.add(like);
        post.setLikes(likes);
        assertEquals(likes, post.getLikes());
    }

    @Test
    void testSetAndGetCreatedAt() {
        Date newDate = new Date();
        post.setCreatedAt(newDate);
        assertEquals(newDate, post.getCreatedAt());
    }

    @Test
    void testEqualsSameObject() {
        assertTrue(post.equals(post));
    }

    @Test
    void testEqualsDifferentId() {
        Post otherPost = new Post("Another post", user);
        otherPost.setId(99L);
        assertFalse(post.equals(otherPost));
    }

    @Test
    void testHashCode() {
        Post otherPost = new Post("Another post", user);
        otherPost.setId(post.getId());
        assertEquals(post.hashCode(), otherPost.hashCode());
    }

    @Test
    void testHasNoCommentsInitially() {
        assertTrue(post.getComments().isEmpty());
    }

    @Test
    void testHasNoLikesInitially() {
        assertTrue(post.getLikes().isEmpty());
    }

    @Test
    void testCommentsAfterAdding() {
        post.addComment(comment);
        Set<Comment> comments = post.getComments();
        assertTrue(comments.contains(comment));
    }
}
