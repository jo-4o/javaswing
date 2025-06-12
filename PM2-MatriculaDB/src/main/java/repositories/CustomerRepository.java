package repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.List;

import com.projeto.model.Cliente;

// Classe responsável pela comunicação com o banco de dados relacionada aos clientes
public class CustomerRepository {

    // Instancia a fábrica de EntityManager, responsável por criar as conexões com o banco de dados
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("alunoPU");

    // Método para buscar clientes pelo nome, com base em um termo fornecido
    public List<Cliente> buscarPorNome(String termo) {
        // Cria uma instância do EntityManager para fazer as consultas no banco de dados
        EntityManager em = emf.createEntityManager();

        // Executa a consulta JPQL para buscar clientes cujo nome contenha o termo fornecido
        // O operador LIKE é usado para buscar padrões no nome
        List<Cliente> resultado = em.createQuery("SELECT c FROM Customer c WHERE c.name LIKE :name", Cliente.class)
                .setParameter("name", "%" + termo + "%") // O parâmetro %...% permite buscar qualquer nome que contenha o termo
                .getResultList(); // Retorna a lista de clientes encontrados

        // Fecha o EntityManager após a execução da consulta
        em.close();
        return resultado; // Retorna a lista de clientes encontrados
    }
}