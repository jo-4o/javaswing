package com.projeto.view;

import javax.swing.*;
import java.awt.*;

public class MainMenuView extends JFrame {
    private final CardLayout cardLayout;
    private final JPanel mainPanel;

    public MainMenuView() {
        setTitle("Dashboard - Sistema de Hotelaria");
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // sidebar
        JPanel sidebar = new JPanel();
        sidebar.setBackground(new Color(30, 41, 59));
        sidebar.setPreferredSize(new Dimension(220, getHeight()));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));

        JLabel logo = new JLabel("Hotelaria", JLabel.CENTER);
        logo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        logo.setForeground(Color.WHITE);
        logo.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));
        sidebar.add(logo);

        JButton clientesBtn = createSidebarButton("Clientes");
        JButton quartosBtn = createSidebarButton("Quartos");
        JButton reservasBtn = createSidebarButton("Reservas");
        JButton sairBtn = createSidebarButton("Sair");

        sidebar.add(clientesBtn);
        sidebar.add(Box.createVerticalStrut(10));
        sidebar.add(quartosBtn);
        sidebar.add(Box.createVerticalStrut(10));
        sidebar.add(reservasBtn);
        sidebar.add(Box.createVerticalGlue());
        sidebar.add(sairBtn);

        // painel central
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainPanel.add(new ClientesPanel(), "clientes");
        mainPanel.add(new QuartosPanel(), "quartos");
        mainPanel.add(new ReservasPanel(), "reservas");

        clientesBtn.addActionListener(e -> cardLayout.show(mainPanel, "clientes"));
        quartosBtn.addActionListener(e -> cardLayout.show(mainPanel, "quartos"));
        reservasBtn.addActionListener(e -> cardLayout.show(mainPanel, "reservas"));
        sairBtn.addActionListener(e -> {
            dispose();
            new LoginView();
        });

        // rodape
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(new Color(248, 250, 252));
        JLabel footerText = new JLabel("merilu purinho", JLabel.CENTER);
        footerText.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        footerText.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        footerText.setForeground(new Color(120, 120, 120));
        footerPanel.add(footerText);

        add(sidebar, BorderLayout.WEST);
        add(mainPanel, BorderLayout.CENTER);
        add(footerPanel, BorderLayout.SOUTH);

        // tela de clientes por padrao
        cardLayout.show(mainPanel, "clientes");

        setVisible(true);
    }

    private JButton createSidebarButton(String text) {
        JButton btn = new JButton(text);
        btn.setMaximumSize(new Dimension(180, 40));
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        btn.setBackground(new Color(51, 65, 85));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        return btn;
    }
}