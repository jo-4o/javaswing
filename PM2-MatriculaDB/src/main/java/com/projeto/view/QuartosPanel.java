package com.projeto.view;

import com.projeto.controller.QuartoController;
import com.projeto.model.Quarto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class QuartosPanel extends JPanel {
    private final DefaultTableModel tableModel;

    public QuartosPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(248, 250, 252));

        JPanel formPanel = new JPanel(new GridLayout(2, 3, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("Cadastrar Quarto"));
        JTextField tipoField = new JTextField();
        JTextField precoField = new JTextField();
        JButton cadastrarBtn = new JButton("Cadastrar");

        formPanel.add(new JLabel("Tipo:"));
        formPanel.add(tipoField);
        formPanel.add(new JLabel("Preço:"));
        formPanel.add(precoField);
        formPanel.add(new JLabel(""));
        formPanel.add(cadastrarBtn);

        String[] colunas = {"ID", "Tipo", "Preço"};
        tableModel = new DefaultTableModel(colunas, 0);
        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        atualizarTabela();

        cadastrarBtn.addActionListener(e -> {
            try {
                QuartoController.cadastrarQuarto(
                    tipoField.getText(),
                    Double.parseDouble(precoField.getText())
                );
                atualizarTabela();
                tipoField.setText("");
                precoField.setText("");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Preço inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        add(formPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void atualizarTabela() {
        tableModel.setRowCount(0);
        List<Quarto> quartos = QuartoController.listarQuartos();
        for (Quarto q : quartos) {
            tableModel.addRow(new Object[]{
                q.getId(), q.getTipo(), q.getPreco()
            });
        }
    }
}