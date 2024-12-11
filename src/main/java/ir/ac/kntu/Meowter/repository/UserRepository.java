package ir.ac.kntu.Meowter.repository;

import ir.ac.kntu.Meowter.model.User;
import ir.ac.kntu.Meowter.util.HibernateUtil;

import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class UserRepository {

    public void save(User user) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
            session.saveOrUpdate(user);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public void update(User user) {
        save(user);
    }


    public List<User> findAll() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<User> users = session.createQuery("FROM User", User.class).getResultList();
        session.close();
        return users;
    }

    public long count() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        long count = (long) session.createQuery("SELECT COUNT(u) FROM User u").getSingleResult();
        session.close();
        return count;
    }

    public boolean existsByEmail(String email) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        boolean exists = false;

        try {
            String hql = "SELECT COUNT(u) FROM User u WHERE u.email = :email";
            Long count = (Long) session.createQuery(hql)
                    .setParameter("email", email)
                    .uniqueResult();
            exists = count > 0;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }

        return exists;
    }

    public User findByEmail(String email) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        User user = null;

        try {
            user = session.createQuery("FROM User WHERE email = :email", User.class)
                    .setParameter("email", email)
                    .uniqueResult();
        } finally {
            session.close();
        }

        return user;
    }

}

