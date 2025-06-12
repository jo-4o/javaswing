package com.projeto.view;

import com.projeto.util.JPAUtil;

public class Main {
    public static void main(String[] args) {
        // inicia a tela de login
        new LoginView();

        // fecha o entity manager ao encerrar o app
        Runtime.getRuntime().addShutdownHook(new Thread(JPAUtil::close));
    }
}