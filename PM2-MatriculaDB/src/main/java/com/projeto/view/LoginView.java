package com.projeto.view;

import com.projeto.controller.LoginController;

import javax.swing.*;
import java.awt.*;

public class LoginView extends JFrame {
    public LoginView() {
        setTitle("Login - Sistema de Hotelaria");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setBackground(new Color(248, 250, 252));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        JLabel logo = new JLabel("Hotelaria");
        logo.setFont(new Font("Segoe UI", Font.BOLD, 32));
        logo.setForeground(new Color(0, 102, 204));
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel userLabel = new JLabel("Usuário:");
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        userLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField userField = new JTextField();
        userField.setMaximumSize(new Dimension(250, 35));
        userField.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel passwordLabel = new JLabel("Senha:");
        passwordLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        passwordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPasswordField passwordField = new JPasswordField();
        passwordField.setMaximumSize(new Dimension(250, 35));
        passwordField.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton loginButton = new JButton("Entrar");
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 18));
        loginButton.setBackground(new Color(0, 153, 76));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton registerButton = new JButton("Registrar-se");
        registerButton.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        registerButton.setBackground(new Color(30, 41, 59));
        registerButton.setForeground(Color.WHITE);
        registerButton.setFocusPainted(false);
        registerButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        loginButton.addActionListener(e -> {
            String username = userField.getText();
            String password = new String(passwordField.getPassword());
            if (LoginController.validateLogin(username, password)) {
                new MainMenuView();
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Usuário ou senha inválidos!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        registerButton.addActionListener(e -> {
            new RegisterView();
            dispose();
        });

        panel.add(Box.createVerticalGlue());
        panel.add(logo);
        panel.add(Box.createVerticalStrut(30));
        panel.add(userLabel);
        panel.add(userField);
        panel.add(Box.createVerticalStrut(15));
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(Box.createVerticalStrut(30));
        panel.add(loginButton);
        panel.add(Box.createVerticalStrut(10));
        panel.add(registerButton);
        panel.add(Box.createVerticalGlue());

        add(panel, BorderLayout.CENTER);

        setVisible(true);
    }
}