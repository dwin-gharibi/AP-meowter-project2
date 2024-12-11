package ir.ac.kntu.Meowter.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import ir.ac.kntu.Meowter.model.User;
import ir.ac.kntu.Meowter.model.Post;
import ir.ac.kntu.Meowter.model.Like;
import ir.ac.kntu.Meowter.model.Comment;

public class HibernateUtil {

    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            return new Configuration()
                    .configure("hibernate.cfg.xml")
                    .addAnnotatedClass(User.class)
                    .addAnnotatedClass(Post.class)
                    .addAnnotatedClass(Like.class)
                    .addAnnotatedClass(Comment.class)
                    .buildSessionFactory();
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void shutdown() {
        getSessionFactory().close();
    }
}
