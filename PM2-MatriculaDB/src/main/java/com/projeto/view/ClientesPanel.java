package com.projeto.view;

import com.projeto.controller.ClienteController;
import com.projeto.model.Cliente;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ClientesPanel extends JPanel {
    private final DefaultTableModel tableModel;

    public ClientesPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(248, 250, 252));

        // Formulário de cadastro
        JPanel formPanel = new JPanel(new GridLayout(2, 5, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("Cadastrar Cliente"));
        JTextField nomeField = new JTextField();
        JTextField cpfField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField telefoneField = new JTextField();
        JButton cadastrarBtn = new JButton("Cadastrar");

        formPanel.add(new JLabel("Nome:"));
        formPanel.add(nomeField);
        formPanel.add(new JLabel("CPF:"));
        formPanel.add(cpfField);
        formPanel.add(new JLabel("Email:"));
        formPanel.add(emailField);
        formPanel.add(new JLabel("Telefone:"));
        formPanel.add(telefoneField);
        formPanel.add(new JLabel(""));
        formPanel.add(cadastrarBtn);

        // Tabela de clientes
        String[] colunas = {"ID", "Nome", "CPF", "Email", "Telefone"};
        tableModel = new DefaultTableModel(colunas, 0);
        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        atualizarTabela();

        cadastrarBtn.addActionListener(e -> {
            ClienteController.cadastrarCliente(
                nomeField.getText(),
                cpfField.getText(),
                emailField.getText(),
                telefoneField.getText()
            );
            atualizarTabela();
            nomeField.setText("");
            cpfField.setText("");
            emailField.setText("");
            telefoneField.setText("");
        });

        add(formPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void atualizarTabela() {
        tableModel.setRowCount(0);
        List<Cliente> clientes = ClienteController.listarClientes();
        for (Cliente c : clientes) {
            tableModel.addRow(new Object[]{
                c.getId(), c.getNome(), c.getCpf(), c.getEmail(), c.getTelefone()
            });
        }
    }
}