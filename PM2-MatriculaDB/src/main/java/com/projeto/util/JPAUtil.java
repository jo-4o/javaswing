package com.projeto.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JPAUtil {
    // cria a fabrica de entity manager uma vez
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("alunoPU");

    // pega o entity manager pra operacoes com banco
    public static EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    // fecha a fabrica de entity manager
    public static void close() {
        if (emf.isOpen()) {
            emf.close();
        }
    }
}
