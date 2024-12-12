package ir.ac.kntu.Meowter.repository;

import ir.ac.kntu.Meowter.model.Post;
import ir.ac.kntu.Meowter.model.User;
import ir.ac.kntu.Meowter.util.HibernateUtil;

import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class PostRepository {

    public void save(Post post) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            post.extractHashtags();
            session.save(post);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
        } finally {
            session.close();
        }
    }

    public void update(Post post) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
            session.update(post);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
        } finally {
            session.close();
        }
    }

    public void delete(Long postId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
            Post post = session.get(Post.class, postId);
            if (post != null) {
                session.delete(post);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public List<Post> findByHashtag(String hashtag) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Post> posts = null;

        try {
            String hql = "SELECT p FROM Post p JOIN p.hashtags h WHERE h = :hashtag";
            posts = session.createQuery(hql, Post.class)
                    .setParameter("hashtag", hashtag)
                    .getResultList();
        } finally {
            session.close();
        }
        return posts;
    }

    public Post findById(Long postId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Post post = null;

        try {
            post = session.get(Post.class, postId);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }

        return post;
    }

    public List<Post> findAll() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Post> posts = null;

        try {
            posts = session.createQuery("FROM Post", Post.class).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }

        return posts;
    }

    public List<Post> findByUser(User user) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Post> posts = null;

        try {
            String hql = "FROM Post p WHERE p.user = :user";
            posts = session.createQuery(hql, Post.class)
                    .setParameter("user", user)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }

        return posts;
    }

    public long count() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        long count = (long) session.createQuery("SELECT COUNT(p) FROM Post p").getSingleResult();
        session.close();
        return count;
    }

    public List<Post> findAllPaginated(int page, int size) {
        return null;
    }

    public void addLike(User user, Post post) {
    }

    public void addComment(User user, Post post, String content) {
    }

    public void removeComment(User user, Post post, long commentId) {
    }

    public void removeLike(User user, Post post) {

    }
}

