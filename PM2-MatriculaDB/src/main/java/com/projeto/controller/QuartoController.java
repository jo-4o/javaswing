package com.projeto.controller;

import com.projeto.model.Quarto;
import com.projeto.util.JPAUtil;
import jakarta.persistence.EntityManager;
import java.util.List;

public class QuartoController {

    public static void cadastrarQuarto(String tipo, Double preco) {
        EntityManager em = JPAUtil.getEntityManager();

        em.getTransaction().begin();
        em.persist(new Quarto(tipo, preco));
        em.getTransaction().commit();

        em.close();
    }

    public static List<Quarto> listarQuartos() {
        EntityManager em = JPAUtil.getEntityManager();
        List<Quarto> quartos = em.createQuery("FROM Quarto", Quarto.class).getResultList();
        em.close();
        return quartos;
    }
}