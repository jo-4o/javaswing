package com.projeto.controller;

import com.projeto.model.Reserva;
import com.projeto.model.Cliente;
import com.projeto.model.Quarto;
import com.projeto.util.JPAUtil;
import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

import java.time.format.DateTimeFormatter;

public class ReservaController {

    // cadastra uma nova reserva no banco de dados
    public static void cadastrarReserva(Long clienteId, Long quartoId, String checkIn, String checkOut) {
        EntityManager em = JPAUtil.getEntityManager();

        Cliente cliente = em.find(Cliente.class, clienteId);
        Quarto quarto = em.find(Quarto.class, quartoId);

        if (cliente == null || quarto == null) {
            throw new IllegalArgumentException("Cliente ou Quarto não encontrado.");
        }

        LocalDateTime dataCheckIn = LocalDateTime.parse(checkIn, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        LocalDateTime dataCheckOut = LocalDateTime.parse(checkOut, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

        Reserva reserva = new Reserva(cliente, quarto, dataCheckIn, dataCheckOut);

        em.getTransaction().begin();
        em.persist(reserva);
        em.getTransaction().commit();

        em.close();
    }

    // lista todas as reservas do banco de dados
    public static List<Reserva> listarReservas() {
        EntityManager em = JPAUtil.getEntityManager();
        List<Reserva> reservas = em.createQuery("FROM Reserva", Reserva.class).getResultList();
        em.close();
        return reservas;
    }

    // exclui uma reserva pelo id
    public static void excluirReserva(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            
            Reserva reserva = em.find(Reserva.class, id);
            if (reserva != null) {
                em.remove(reserva);
                em.getTransaction().commit();
            } else {
                throw new RuntimeException("Reserva não encontrada");
            }
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Erro ao excluir reserva: " + e.getMessage());
        } finally {
            em.close();
        }
    }

    // busca uma reserva pelo id
    public static Reserva buscarReservaPorId(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(Reserva.class, id);
        } finally {
            em.close();
        }
    }

    // atualiza uma reserva existente
    public static void atualizarReserva(Long id, Long clienteId, Long quartoId, String dataCheckIn, String dataCheckOut) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            
            Reserva reserva = em.find(Reserva.class, id);
            if (reserva == null) {
                throw new RuntimeException("Reserva não encontrada");
            }

            Cliente cliente = em.find(Cliente.class, clienteId);
            if (cliente == null) {
                throw new RuntimeException("Cliente não encontrado");
            }

            Quarto quarto = em.find(Quarto.class, quartoId);
            if (quarto == null) {
                throw new RuntimeException("Quarto não encontrado");
            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            LocalDateTime checkIn = LocalDateTime.parse(dataCheckIn, formatter);
            LocalDateTime checkOut = LocalDateTime.parse(dataCheckOut, formatter);

            reserva.setCliente(cliente);
            reserva.setQuarto(quarto);
            reserva.setDataCheckIn(checkIn);
            reserva.setDataCheckOut(checkOut);

            em.merge(reserva);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Erro ao atualizar reserva: " + e.getMessage());
        } finally {
            em.close();
        }
    }

    // busca reservas por nome do cliente
    public static List<Reserva> buscarReservasPorCliente(String nomeCliente) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            String jpql = "SELECT r FROM Reserva r JOIN r.cliente c WHERE LOWER(c.nome) LIKE LOWER(:nome)";
            return em.createQuery(jpql, Reserva.class)
                    .setParameter("nome", "%" + nomeCliente + "%")
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // busca reservas por periodo
    public static List<Reserva> buscarReservasPorPeriodo(String dataInicio, String dataFim) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            String jpql = "SELECT r FROM Reserva r WHERE r.dataCheckIn BETWEEN :inicio AND :fim";
            return em.createQuery(jpql, Reserva.class)
                    .setParameter("inicio", dataInicio)
                    .setParameter("fim", dataFim)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // busca reservas por tipo de quarto
    public static List<Reserva> buscarReservasPorQuarto(String tipoQuarto) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            String jpql = "SELECT r FROM Reserva r JOIN r.quarto q WHERE LOWER(q.tipo) LIKE LOWER(:tipo)";
            return em.createQuery(jpql, Reserva.class)
                    .setParameter("tipo", "%" + tipoQuarto + "%")
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // calcula valor medio das reservas
    public static Double calcularValorMedioReservas() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            String jpql = "SELECT AVG(q.preco) FROM Reserva r JOIN r.quarto q";
            return em.createQuery(jpql, Double.class).getSingleResult();
        } finally {
            em.close();
        }
    }

    // conta reservas por cliente
    public static Long contarReservasPorCliente(Long clienteId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            String jpql = "SELECT COUNT(r) FROM Reserva r WHERE r.cliente.id = :clienteId";
            return em.createQuery(jpql, Long.class)
                    .setParameter("clienteId", clienteId)
                    .getSingleResult();
        } finally {
            em.close();
        }
    }
}