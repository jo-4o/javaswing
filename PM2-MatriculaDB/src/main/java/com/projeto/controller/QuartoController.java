package com.projeto.controller;

import com.projeto.model.Quarto;
import com.projeto.util.JPAUtil;
import jakarta.persistence.EntityManager;
import java.util.List;

public class QuartoController {

    // cadastra um novo quarto no banco de dados
    public static void cadastrarQuarto(String tipo, Double preco) {
        EntityManager em = JPAUtil.getEntityManager();

        em.getTransaction().begin();
        em.persist(new Quarto(tipo, preco));
        em.getTransaction().commit();

        em.close();
    }

    // lista todos os quartos do banco de dados
    public static List<Quarto> listarQuartos() {
        EntityManager em = JPAUtil.getEntityManager();
        List<Quarto> quartos = em.createQuery("FROM Quarto", Quarto.class).getResultList();
        em.close();
        return quartos;
    }

    // busca um quarto pelo id
    public static Quarto buscarQuartoPorId(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(Quarto.class, id);
        } finally {
            em.close();
        }
    }

    // atualiza um quarto existente
    public static void atualizarQuarto(Long id, String tipo, Double preco) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            
            Quarto quarto = em.find(Quarto.class, id);
            if (quarto == null) {
                throw new RuntimeException("Quarto nao encontrado");
            }

            quarto.setTipo(tipo);
            quarto.setPreco(preco);

            em.merge(quarto);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Erro ao atualizar quarto: " + e.getMessage());
        } finally {
            em.close();
        }
    }

    // exclui um quarto pelo id
    public static void excluirQuarto(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            
            Quarto quarto = em.find(Quarto.class, id);
            if (quarto != null) {
                em.remove(quarto);
                em.getTransaction().commit();
            } else {
                throw new RuntimeException("Quarto nao encontrado");
            }
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Erro ao excluir quarto: " + e.getMessage());
        } finally {
            em.close();
        }
    }

    // busca quartos por tipo
    public static List<Quarto> buscarQuartosPorTipo(String tipo) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            String jpql = "SELECT q FROM Quarto q WHERE LOWER(q.tipo) LIKE LOWER(:tipo)";
            return em.createQuery(jpql, Quarto.class)
                    .setParameter("tipo", "%" + tipo + "%")
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // busca quartos por preco entre min e max
    public static List<Quarto> buscarQuartosPorPreco(Double precoMin, Double precoMax) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            String jpql = "SELECT q FROM Quarto q WHERE q.preco BETWEEN :min AND :max";
            return em.createQuery(jpql, Quarto.class)
                    .setParameter("min", precoMin)
                    .setParameter("max", precoMax)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // busca quartos disponiveis
    public static List<Quarto> buscarQuartosDisponiveis() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            String jpql = "SELECT q FROM Quarto q WHERE q.id NOT IN " +
                         "(SELECT r.quarto.id FROM Reserva r WHERE r.dataCheckOut > CURRENT_TIMESTAMP)";
            return em.createQuery(jpql, Quarto.class).getResultList();
        } finally {
            em.close();
        }
    }

    // conta reservas por quarto
    public static Long contarReservasPorQuarto(Long quartoId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            String jpql = "SELECT COUNT(r) FROM Reserva r WHERE r.quarto.id = :quartoId";
            return em.createQuery(jpql, Long.class)
                    .setParameter("quartoId", quartoId)
                    .getSingleResult();
        } finally {
            em.close();
        }
    }

    // calcula ocupacao media do quarto em dias
    public static Double calcularOcupacaoMediaQuarto(Long quartoId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            String jpql = "SELECT AVG(DATEDIFF(r.dataCheckOut, r.dataCheckIn)) FROM Reserva r WHERE r.quarto.id = :quartoId";
            return em.createQuery(jpql, Double.class)
                    .setParameter("quartoId", quartoId)
                    .getSingleResult();
        } finally {
            em.close();
        }
    }
}