package com.projeto.controller;

import com.projeto.model.Cliente;
import com.projeto.util.JPAUtil;
import jakarta.persistence.EntityManager;

import java.util.List;

public class ClienteController {

    public static void cadastrarCliente(String nome, String cpf, String email, String telefone) {
        EntityManager em = JPAUtil.getEntityManager();

        em.getTransaction().begin();
        em.persist(new Cliente(nome, cpf, email, telefone));
        em.getTransaction().commit();

        em.close();
    }

    public static List<Cliente> listarClientes() {
        EntityManager em = JPAUtil.getEntityManager();

        List<Cliente> clientes = em.createQuery("FROM Cliente", Cliente.class).getResultList();
        em.close();
        return clientes;
    }
}