package com.example.boardv1.user;

import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class UserRepository {

    // DI
    private final EntityManager em;

    public User save(User user) {
        em.persist(user);
        return user;

    }

    public User findById(int id) {
        return em.find(User.class, id);
    }

    public User findByUsername(String username) {
        Query query = em.createQuery("select u from User u where u.username = :username", User.class);
        query.setParameter("username", username);

        try {
            User findUser = (User) query.getSingleResult();
            return findUser;
        } catch (Exception e) {
            return null;
        }

    }
}
