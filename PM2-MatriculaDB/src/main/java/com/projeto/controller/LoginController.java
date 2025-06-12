package com.projeto.controller;

import com.projeto.model.Usuario;
import com.projeto.util.JPAUtil;
import jakarta.persistence.EntityManager;

import com.projeto.controller.LoginController;

public class LoginController {

    public static boolean validateLogin(String username, String password) {
        EntityManager em = JPAUtil.getEntityManager();
        Usuario usuario = em.createQuery("SELECT u FROM Usuario u WHERE u.username = :username AND u.password = :password", Usuario.class)
                .setParameter("username", username)
                .setParameter("password", password)
                .getResultStream()
                .findFirst()
                .orElse(null);
        em.close();
        return usuario != null;
    }

    public static boolean registerUser(String username, String password) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(new Usuario(username, password));
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            em.getTransaction().rollback();
            return false;
        } finally {
            em.close();
        }
    }
}