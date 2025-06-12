package com.projeto.view;

import javax.swing.*;
import java.awt.*;
import com.projeto.controller.LoginController;

public class RegisterView extends JFrame {
    public RegisterView() {
        setTitle("Registrar - Sistema de Hotelaria");
        setSize(400, 400);
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

        JButton registerButton = new JButton("Registrar");
        registerButton.setFont(new Font("Segoe UI", Font.BOLD, 18));
        registerButton.setBackground(new Color(0, 153, 76));
        registerButton.setForeground(Color.WHITE);
        registerButton.setFocusPainted(false);
        registerButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton backButton = new JButton("Voltar para Login");
        backButton.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        backButton.setBackground(new Color(30, 41, 59));
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        registerButton.addActionListener(e -> {
            String username = userField.getText();
            String password = new String(passwordField.getPassword());

            if (LoginController.registerUser(username, password)) {
                JOptionPane.showMessageDialog(this, "Usuário registrado com sucesso!");
                new LoginView();
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao registrar usuário!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        backButton.addActionListener(e -> {
            new LoginView();
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
        panel.add(registerButton);
        panel.add(Box.createVerticalStrut(10));
        panel.add(backButton);
        panel.add(Box.createVerticalGlue());

        add(panel, BorderLayout.CENTER);

        setVisible(true);
    }
}