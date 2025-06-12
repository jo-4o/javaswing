package com.projeto.view;

import com.projeto.controller.ReservaController;
import com.projeto.controller.ClienteController;
import com.projeto.controller.QuartoController;
import com.projeto.model.Cliente;
import com.projeto.model.Quarto;
import com.projeto.model.Reserva;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ReservasPanel extends JPanel {
    private final DefaultTableModel tableModel;

    public ReservasPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(248, 250, 252));

        JPanel formPanel = new JPanel(new GridLayout(2, 5, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("Criar Reserva"));

        List<Cliente> clientes = ClienteController.listarClientes();
        List<Quarto> quartos = QuartoController.listarQuartos();

        JComboBox<String> clienteDropdown = new JComboBox<>();
        for (Cliente c : clientes) {
            clienteDropdown.addItem(c.getId() + " --- " + c.getNome());
        }

        JComboBox<String> quartoDropdown = new JComboBox<>();
        for (Quarto q : quartos) {
            quartoDropdown.addItem(q.getId() + " --- " + q.getTipo());
        }

        JTextField checkInField = new JTextField();
        JTextField checkOutField = new JTextField();
        JButton cadastrarBtn = new JButton("Reservar");

        formPanel.add(new JLabel("Cliente:"));
        formPanel.add(clienteDropdown);
        formPanel.add(new JLabel("Quarto:"));
        formPanel.add(quartoDropdown);
        formPanel.add(new JLabel("Check-In (yyyy-MM-dd HH:mm):"));
        formPanel.add(checkInField);
        formPanel.add(new JLabel("Check-Out (yyyy-MM-dd HH:mm):"));
        formPanel.add(checkOutField);
        formPanel.add(new JLabel(""));
        formPanel.add(cadastrarBtn);

        String[] colunas = {"ID", "Cliente", "Quarto", "Check-In", "Check-Out"};
        tableModel = new DefaultTableModel(colunas, 0);
        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        atualizarTabela();

        cadastrarBtn.addActionListener(e -> {
            try {
                String clienteSelecionado = (String) clienteDropdown.getSelectedItem();
                String quartoSelecionado = (String) quartoDropdown.getSelectedItem();
                Long clienteId = Long.parseLong(clienteSelecionado.split(" --- ")[0]);
                Long quartoId = Long.parseLong(quartoSelecionado.split(" --- ")[0]);
                String checkIn = checkInField.getText();
                String checkOut = checkOutField.getText();

                ReservaController.cadastrarReserva(clienteId, quartoId, checkIn, checkOut);
                atualizarTabela();
                checkInField.setText("");
                checkOutField.setText("");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Dados inválidos!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        add(formPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void atualizarTabela() {
        tableModel.setRowCount(0);
        List<Reserva> reservas = ReservaController.listarReservas();
        for (Reserva r : reservas) {
            tableModel.addRow(new Object[]{
                r.getId(),
                r.getCliente().getNome(),
                r.getQuarto().getTipo(),
                r.getDataCheckIn(),
                r.getDataCheckOut()
            });
        }
    }
}