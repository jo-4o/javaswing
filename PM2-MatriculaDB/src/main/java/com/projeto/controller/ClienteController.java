package com.projeto.controller;

import com.projeto.model.Cliente;
import com.projeto.util.JPAUtil;
import jakarta.persistence.EntityManager;

import java.util.List;

public class ClienteController {

    // cadastra um novo cliente no banco de dados
    public static void cadastrarCliente(String nome, String cpf, String email, String telefone) {
        EntityManager em = JPAUtil.getEntityManager();

        em.getTransaction().begin();
        em.persist(new Cliente(nome, cpf, email, telefone));
        em.getTransaction().commit();

        em.close();
    }

    // lista todos os clientes do banco de dados
    public static List<Cliente> listarClientes() {
        EntityManager em = JPAUtil.getEntityManager();

        List<Cliente> clientes = em.createQuery("FROM Cliente", Cliente.class).getResultList();
        em.close();
        return clientes;
    }

    // busca um cliente pelo id
    public static Cliente buscarClientePorId(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(Cliente.class, id);
        } finally {
            em.close();
        }
    }

    // atualiza um cliente existente
    public static void atualizarCliente(Long id, String nome, String cpf, String telefone, String email) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            
            Cliente cliente = em.find(Cliente.class, id);
            if (cliente == null) {
                throw new RuntimeException("Cliente nao encontrado");
            }

            cliente.setNome(nome);
            cliente.setCpf(cpf);
            cliente.setTelefone(telefone);
            cliente.setEmail(email);

            em.merge(cliente);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Erro ao atualizar cliente: " + e.getMessage());
        } finally {
            em.close();
        }
    }

    // exclui um cliente pelo id
    public static void excluirCliente(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            
            Cliente cliente = em.find(Cliente.class, id);
            if (cliente != null) {
                em.remove(cliente);
                em.getTransaction().commit();
            } else {
                throw new RuntimeException("Cliente nao encontrado");
            }
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Erro ao excluir cliente: " + e.getMessage());
        } finally {
            em.close();
        }
    }

    // busca clientes por nome
    public static List<Cliente> buscarClientesPorNome(String nome) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            String jpql = "SELECT c FROM Cliente c WHERE LOWER(c.nome) LIKE LOWER(:nome)";
            return em.createQuery(jpql, Cliente.class)
                    .setParameter("nome", "%" + nome + "%")
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // busca clientes por email
    public static List<Cliente> buscarClientesPorEmail(String email) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            String jpql = "SELECT c FROM Cliente c WHERE LOWER(c.email) LIKE LOWER(:email)";
            return em.createQuery(jpql, Cliente.class)
                    .setParameter("email", "%" + email + "%")
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // busca clientes por telefone
    public static List<Cliente> buscarClientesPorTelefone(String telefone) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            String jpql = "SELECT c FROM Cliente c WHERE c.telefone LIKE :telefone";
            return em.createQuery(jpql, Cliente.class)
                    .setParameter("telefone", "%" + telefone + "%")
                    .getResultList();
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

    // calcula valor medio gasto por cliente
    public static Double calcularValorMedioGastoPorCliente(Long clienteId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            String jpql = "SELECT AVG(q.preco) FROM Reserva r JOIN r.quarto q WHERE r.cliente.id = :clienteId";
            return em.createQuery(jpql, Double.class)
                    .setParameter("clienteId", clienteId)
                    .getSingleResult();
        } finally {
            em.close();
        }
    }
}