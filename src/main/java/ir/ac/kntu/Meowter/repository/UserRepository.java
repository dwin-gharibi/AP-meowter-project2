package ir.ac.kntu.Meowter.repository;

import ir.ac.kntu.Meowter.model.User;
import ir.ac.kntu.Meowter.model.FollowRequest;
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

    public User findByUsername(String username) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        User user = null;
        try {
            user = session.createQuery("FROM User WHERE username = :username", User.class)
                    .setParameter("username", username)
                    .uniqueResult();
        } finally {
            session.close();
        }
        return user;
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

    public void saveFollowRequest(FollowRequest followRequest) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
            session.save(followRequest);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
        } finally {
            session.close();
        }
    }

    public FollowRequest findFollowRequest(User requester, User recipient) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        FollowRequest followRequest = null;

        try {
            String hql = "FROM FollowRequest fr WHERE fr.requester = :requester AND fr.recipient = :recipient";
            followRequest = session.createQuery(hql, FollowRequest.class)
                    .setParameter("requester", requester)
                    .setParameter("recipient", recipient)
                    .uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }

        return followRequest;
    }

    public List<FollowRequest> getFollowRequests(User user) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<FollowRequest> requests = null;

        try {
            String hql = "FROM FollowRequest fr WHERE fr.recipient = :user OR fr.requester = :user";
            requests = session.createQuery(hql, FollowRequest.class)
                    .setParameter("user", user)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }

        return requests;
    }

    public void updateFollowRequest(User loggedinuser, FollowRequest updatedRequest) {
        List<FollowRequest> followRequests = getFollowRequests(loggedinuser);
        for (int i = 0; i < followRequests.size(); i++) {
            FollowRequest currentRequest = followRequests.get(i);
            if (currentRequest.getId().equals(updatedRequest.getId())) {
                followRequests.set(i, updatedRequest);
                return;
            }
        }
        throw new IllegalArgumentException("Follow request not found");
    }

    public void sendFollowRequest(User loggedInUser, User recipientUser) {
        FollowRequest existingRequest = this.findFollowRequest(loggedInUser, recipientUser);
        if (existingRequest != null) {
            System.out.println("Follow request already exists.");
            return;
        }

        FollowRequest followRequest = new FollowRequest(loggedInUser, recipientUser);
        this.saveFollowRequest(followRequest);
    }

}

