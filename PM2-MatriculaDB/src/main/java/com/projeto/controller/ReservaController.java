package com.projeto.controller;

import com.projeto.model.Cliente;
import com.projeto.model.Quarto;
import com.projeto.model.Reserva;
import com.projeto.util.JPAUtil;
import jakarta.persistence.EntityManager;
import java.util.List;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ReservaController {

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

    public static List<Reserva> listarReservas() {
        EntityManager em = JPAUtil.getEntityManager();
        List<Reserva> reservas = em.createQuery("FROM Reserva", Reserva.class).getResultList();
        em.close();
        return reservas;
    }
}