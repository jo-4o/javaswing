package com.projeto.view;

import com.projeto.controller.ClienteController;
import com.projeto.model.Cliente;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ClientesPanel extends JPanel {
    private final DefaultTableModel tableModel;
    // cores padrao do painel
    private static final Color BACKGROUND_COLOR = new Color(248, 250, 252);
    private static final Color PRIMARY_COLOR = new Color(0, 102, 204);
    private static final Color SECONDARY_COLOR = new Color(51, 65, 85);
    // fontes padrao do painel
    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 24);
    private static final Font LABEL_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 14);
    private JLabel reservasLabel;
    private JLabel valorMedioLabel;

    // construtor do painel de clientes
    public ClientesPanel() {
        // configura layout e cores do painel principal
        setLayout(new BorderLayout(20, 20));
        setBackground(BACKGROUND_COLOR);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // painel do titulo
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setBackground(BACKGROUND_COLOR);
        JLabel titleLabel = new JLabel("Gerenciamento de Clientes");
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(SECONDARY_COLOR);
        titlePanel.add(titleLabel);

        // botao novo cliente
        JButton novoClienteBtn = createStyledButton("Novo Cliente");
        novoClienteBtn.addActionListener(e -> abrirDialogoNovoCliente());
        titlePanel.add(Box.createHorizontalStrut(20));
        titlePanel.add(novoClienteBtn);

        // painel principal com JSplitPane
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setDividerLocation(350);
        splitPane.setDividerSize(5);
        splitPane.setBackground(BACKGROUND_COLOR);
        splitPane.setBorder(null);

        // painel de consultas
        JPanel consultaPanel = new JPanel(new GridBagLayout());
        consultaPanel.setBackground(BACKGROUND_COLOR);
        consultaPanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(226, 232, 240), 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        GridBagConstraints gbcConsulta = new GridBagConstraints();
        gbcConsulta.fill = GridBagConstraints.HORIZONTAL;
        gbcConsulta.insets = new Insets(5, 5, 5, 5);
        gbcConsulta.weightx = 1.0;

        // campos de consulta
        JTextField nomeField = createStyledTextField();
        JTextField emailField = createStyledTextField();
        JTextField telefoneField = createStyledTextField();

        // botoes de consulta
        JButton buscarNomeBtn = createStyledButton("Buscar por Nome");
        JButton buscarEmailBtn = createStyledButton("Buscar por Email");
        JButton buscarTelefoneBtn = createStyledButton("Buscar por Telefone");
        JButton limparBtn = createStyledButton("Limpar Filtros");
        limparBtn.setBackground(new Color(220, 38, 38));

        // painel de estatisticas
        JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statsPanel.setBackground(BACKGROUND_COLOR);
        reservasLabel = createStyledLabel("Total de Reservas: 0");
        valorMedioLabel = createStyledLabel("Valor Medio Gasto: R$ 0,00");
        statsPanel.add(reservasLabel);
        statsPanel.add(Box.createHorizontalStrut(20));
        statsPanel.add(valorMedioLabel);

        // adiciona componentes ao painel de consulta
        gbcConsulta.gridx = 0; gbcConsulta.gridy = 0;
        consultaPanel.add(createStyledLabel("Nome:"), gbcConsulta);
        gbcConsulta.gridx = 1;
        consultaPanel.add(nomeField, gbcConsulta);
        gbcConsulta.gridx = 2;
        consultaPanel.add(buscarNomeBtn, gbcConsulta);

        gbcConsulta.gridx = 0; gbcConsulta.gridy = 1;
        consultaPanel.add(createStyledLabel("Email:"), gbcConsulta);
        gbcConsulta.gridx = 1;
        consultaPanel.add(emailField, gbcConsulta);
        gbcConsulta.gridx = 2;
        consultaPanel.add(buscarEmailBtn, gbcConsulta);

        gbcConsulta.gridx = 0; gbcConsulta.gridy = 2;
        consultaPanel.add(createStyledLabel("Telefone:"), gbcConsulta);
        gbcConsulta.gridx = 1;
        consultaPanel.add(telefoneField, gbcConsulta);
        gbcConsulta.gridx = 2;
        consultaPanel.add(buscarTelefoneBtn, gbcConsulta);

        gbcConsulta.gridx = 0; gbcConsulta.gridy = 3;
        gbcConsulta.gridwidth = 3;
        consultaPanel.add(statsPanel, gbcConsulta);

        gbcConsulta.gridx = 2; gbcConsulta.gridy = 4;
        gbcConsulta.gridwidth = 1;
        consultaPanel.add(limparBtn, gbcConsulta);

        // configura acoes dos botoes de consulta
        buscarNomeBtn.addActionListener(e -> {
            String nome = nomeField.getText().trim();
            if (!nome.isEmpty()) {
                List<Cliente> clientes = ClienteController.buscarClientesPorNome(nome);
                atualizarTabelaComClientes(clientes);
                atualizarEstatisticas(clientes);
            }
        });

        buscarEmailBtn.addActionListener(e -> {
            String email = emailField.getText().trim();
            if (!email.isEmpty()) {
                List<Cliente> clientes = ClienteController.buscarClientesPorEmail(email);
                atualizarTabelaComClientes(clientes);
                atualizarEstatisticas(clientes);
            }
        });

        buscarTelefoneBtn.addActionListener(e -> {
            String telefone = telefoneField.getText().trim();
            if (!telefone.isEmpty()) {
                List<Cliente> clientes = ClienteController.buscarClientesPorTelefone(telefone);
                atualizarTabelaComClientes(clientes);
                atualizarEstatisticas(clientes);
            }
        });

        limparBtn.addActionListener(e -> {
            nomeField.setText("");
            emailField.setText("");
            telefoneField.setText("");
            atualizarTabela();
            atualizarEstatisticas(ClienteController.listarClientes());
        });

        // tabela de clientes
        String[] colunas = {"ID", "Nome", "CPF", "Email", "Telefone"};
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable table = new JTable(tableModel);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(30);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        
        // estiliza o cabecalho da tabela
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(SECONDARY_COLOR);
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setPreferredSize(new Dimension(0, 40));

        // configura as colunas da tabela
        table.getColumnModel().getColumn(0).setPreferredWidth(50);  // ID
        table.getColumnModel().getColumn(1).setPreferredWidth(200); // Nome
        table.getColumnModel().getColumn(2).setPreferredWidth(150); // CPF
        table.getColumnModel().getColumn(3).setPreferredWidth(150); // Telefone
        table.getColumnModel().getColumn(4).setPreferredWidth(200); // Email

        // adiciona menu de contexto (editar e excluir)
        JPopupMenu popupMenu = new JPopupMenu();
        popupMenu.setFont(LABEL_FONT);
        
        JMenuItem editarItem = new JMenuItem("Editar");
        editarItem.setFont(LABEL_FONT);
        editarItem.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                Long clienteId = (Long) table.getValueAt(row, 0);
                editarCliente(clienteId);
            }
        });
        
        JMenuItem excluirItem = new JMenuItem("Excluir");
        excluirItem.setFont(LABEL_FONT);
        excluirItem.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                Long clienteId = (Long) table.getValueAt(row, 0);
                excluirCliente(clienteId);
            }
        });
        
        popupMenu.add(editarItem);
        popupMenu.add(excluirItem);
        
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseReleased(java.awt.event.MouseEvent e) {
                if (e.isPopupTrigger()) {
                    int row = table.rowAtPoint(e.getPoint());
                    if (row >= 0) {
                        table.setRowSelectionInterval(row, row);
                        popupMenu.show(e.getComponent(), e.getX(), e.getY());
                    }
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(226, 232, 240), 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        // atualiza a tabela com todos os clientes
        atualizarTabela();

        // adiciona os paineis ao JSplitPane
        splitPane.setTopComponent(consultaPanel);
        splitPane.setBottomComponent(scrollPane);

        // adiciona os paineis principais ao frame
        add(titlePanel, BorderLayout.NORTH);
        add(splitPane, BorderLayout.CENTER);
    }

    // cria label estilizada
    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(LABEL_FONT);
        label.setForeground(SECONDARY_COLOR);
        return label;
    }

    // cria textfield estilizado
    private JTextField createStyledTextField() {
        JTextField field = new JTextField(20);
        field.setFont(LABEL_FONT);
        field.setPreferredSize(new Dimension(250, 35));
        field.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(226, 232, 240), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        return field;
    }

    // cria botao estilizado
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(BUTTON_FONT);
        button.setBackground(PRIMARY_COLOR);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(200, 40));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    // atualiza a tabela com todos os clientes
    private void atualizarTabela() {
        tableModel.setRowCount(0);
        List<Cliente> clientes = ClienteController.listarClientes();
        for (Cliente c : clientes) {
            tableModel.addRow(new Object[]{
                c.getId(), c.getNome(), c.getCpf(), c.getEmail(), c.getTelefone()
            });
        }
    }

    // atualiza a tabela com uma lista especifica de clientes
    private void atualizarTabelaComClientes(List<Cliente> clientes) {
        tableModel.setRowCount(0);
        for (Cliente c : clientes) {
            tableModel.addRow(new Object[]{
                c.getId(),
                c.getNome(),
                c.getEmail(),
                c.getTelefone()
            });
        }
    }

    // atualiza estatisticas de reservas e gasto medio por cliente
    private void atualizarEstatisticas(List<Cliente> clientes) {
        if (!clientes.isEmpty()) {
            Cliente cliente = clientes.get(0);
            Long totalReservas = ClienteController.contarReservasPorCliente(cliente.getId());
            Double valorMedio = ClienteController.calcularValorMedioGastoPorCliente(cliente.getId());
            
            reservasLabel.setText("Total de Reservas: " + totalReservas);
            valorMedioLabel.setText(String.format("Valor Medio Gasto: R$ %.2f", valorMedio != null ? valorMedio : 0.0));
        } else {
            reservasLabel.setText("Total de Reservas: 0");
            valorMedioLabel.setText("Valor Medio Gasto: R$ 0,00");
        }
    }

    // abre dialogo para editar um cliente
    private void editarCliente(Long clienteId) {
        try {
            Cliente cliente = ClienteController.buscarClientePorId(clienteId);
            if (cliente == null) {
                throw new RuntimeException("Cliente nao encontrado");
            }

            // cria o dialogo de edicao
            JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Editar Cliente", true);
            dialog.setLayout(new BorderLayout(20, 20));
            dialog.getContentPane().setBackground(BACKGROUND_COLOR);

            // painel do formulario
            JPanel formPanel = new JPanel(new GridBagLayout());
            formPanel.setBackground(BACKGROUND_COLOR);
            formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.insets = new Insets(5, 5, 5, 5);
            gbc.weightx = 1.0;

            // campos do formulario
            JTextField nomeField = createStyledTextField();
            nomeField.setText(cliente.getNome());

            JTextField cpfField = createStyledTextField();
            cpfField.setText(cliente.getCpf());

            JTextField telefoneField = createStyledTextField();
            telefoneField.setText(cliente.getTelefone());

            JTextField emailField = createStyledTextField();
            emailField.setText(cliente.getEmail());

            // adiciona componentes ao formulario
            gbc.gridx = 0; gbc.gridy = 0;
            formPanel.add(createStyledLabel("Nome:"), gbc);
            gbc.gridx = 1;
            formPanel.add(nomeField, gbc);

            gbc.gridx = 0; gbc.gridy = 1;
            formPanel.add(createStyledLabel("CPF:"), gbc);
            gbc.gridx = 1;
            formPanel.add(cpfField, gbc);

            gbc.gridx = 0; gbc.gridy = 2;
            formPanel.add(createStyledLabel("Telefone:"), gbc);
            gbc.gridx = 1;
            formPanel.add(telefoneField, gbc);

            gbc.gridx = 0; gbc.gridy = 3;
            formPanel.add(createStyledLabel("Email:"), gbc);
            gbc.gridx = 1;
            formPanel.add(emailField, gbc);

            // painel de botoes
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
            buttonPanel.setBackground(BACKGROUND_COLOR);

            JButton salvarBtn = createStyledButton("Salvar");
            JButton cancelarBtn = createStyledButton("Cancelar");
            cancelarBtn.setBackground(new Color(220, 38, 38)); // Vermelho

            buttonPanel.add(salvarBtn);
            buttonPanel.add(cancelarBtn);

            // adiciona os paineis ao dialogo
            dialog.add(formPanel, BorderLayout.CENTER);
            dialog.add(buttonPanel, BorderLayout.SOUTH);

            // configura acoes dos botoes
            salvarBtn.addActionListener(e -> {
                try {
                    String nome = nomeField.getText().trim();
                    String cpf = cpfField.getText().trim();
                    String telefone = telefoneField.getText().trim();
                    String email = emailField.getText().trim();

                    if (nome.isEmpty() || cpf.isEmpty() || telefone.isEmpty() || email.isEmpty()) {
                        throw new IllegalArgumentException("Todos os campos sao obrigatorios");
                    }

                    ClienteController.atualizarCliente(clienteId, nome, cpf, telefone, email);
                    atualizarTabela();
                    dialog.dispose();
                    JOptionPane.showMessageDialog(this, 
                        "Cliente atualizado com sucesso!", 
                        "Sucesso", 
                        JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(dialog, 
                        "Erro ao atualizar cliente: " + ex.getMessage(), 
                        "Erro", 
                        JOptionPane.ERROR_MESSAGE);
                }
            });

            cancelarBtn.addActionListener(e -> dialog.dispose());

            // configura o dialogo
            dialog.pack();
            dialog.setLocationRelativeTo(this);
            dialog.setResizable(false);
            dialog.setVisible(true);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Erro ao carregar cliente: " + e.getMessage(), 
                "Erro", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    // exclui um cliente com confirmacao
    private void excluirCliente(Long clienteId) {
        int confirmacao = JOptionPane.showConfirmDialog(
            this,
            "Tem certeza que deseja excluir este cliente?",
            "Confirmar exclusao",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );
        
        if (confirmacao == JOptionPane.YES_OPTION) {
            try {
                ClienteController.excluirCliente(clienteId);
                atualizarTabela();
                JOptionPane.showMessageDialog(this, 
                    "Cliente excluido com sucesso!", 
                    "Sucesso", 
                    JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, 
                    "Erro ao excluir cliente: " + e.getMessage(), 
                    "Erro", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // abre dialogo para cadastrar novo cliente
    private void abrirDialogoNovoCliente() {
        // cria o dialogo de cadastro
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Novo Cliente", true);
        dialog.setLayout(new BorderLayout(20, 20));
        dialog.getContentPane().setBackground(BACKGROUND_COLOR);

        // painel do formulario
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(BACKGROUND_COLOR);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.weightx = 1.0;

        // campos do formulario
        JTextField nomeField = createStyledTextField();
        JTextField cpfField = createStyledTextField();
        JTextField emailField = createStyledTextField();
        JTextField telefoneField = createStyledTextField();

        // adiciona componentes ao formulario
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(createStyledLabel("Nome:"), gbc);
        gbc.gridx = 1;
        formPanel.add(nomeField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(createStyledLabel("CPF:"), gbc);
        gbc.gridx = 1;
        formPanel.add(cpfField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(createStyledLabel("Email:"), gbc);
        gbc.gridx = 1;
        formPanel.add(emailField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(createStyledLabel("Telefone:"), gbc);
        gbc.gridx = 1;
        formPanel.add(telefoneField, gbc);

        // painel de botoes
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(BACKGROUND_COLOR);

        JButton salvarBtn = createStyledButton("Salvar");
        JButton cancelarBtn = createStyledButton("Cancelar");
        cancelarBtn.setBackground(new Color(220, 38, 38)); // Vermelho

        buttonPanel.add(salvarBtn);
        buttonPanel.add(cancelarBtn);

        // adiciona os paineis ao dialogo
        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        // configura acoes dos botoes
        salvarBtn.addActionListener(e -> {
            try {
                ClienteController.cadastrarCliente(
                    nomeField.getText(),
                    cpfField.getText(),
                    emailField.getText(),
                    telefoneField.getText()
                );
                
                atualizarTabela();
                dialog.dispose();
                JOptionPane.showMessageDialog(this, 
                    "Cliente cadastrado com sucesso!", 
                    "Sucesso", 
                    JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, 
                    "Erro ao cadastrar cliente: " + ex.getMessage(), 
                    "Erro", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelarBtn.addActionListener(e -> dialog.dispose());

        // configura o dialogo
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setResizable(false);
        dialog.setVisible(true);
    }
}