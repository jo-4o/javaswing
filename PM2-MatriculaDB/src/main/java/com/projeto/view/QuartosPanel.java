package com.projeto.view;

import com.projeto.controller.QuartoController;
import com.projeto.model.Quarto;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class QuartosPanel extends JPanel {
    private final DefaultTableModel tableModel;
    // cores padrao do painel
    private static final Color BACKGROUND_COLOR = new Color(248, 250, 252);
    private static final Color PRIMARY_COLOR = new Color(0, 102, 204);
    private static final Color SECONDARY_COLOR = new Color(51, 65, 85);
    // fontes padrao do painel
    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 24);
    private static final Font LABEL_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 14);

    // construtor do painel de quartos
    public QuartosPanel() {
        // configura layout e cores do painel principal
        setLayout(new BorderLayout(20, 20));
        setBackground(BACKGROUND_COLOR);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // painel do titulo
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setBackground(BACKGROUND_COLOR);
        JLabel titleLabel = new JLabel("Gerenciamento de Quartos");
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(SECONDARY_COLOR);
        titlePanel.add(titleLabel);

        // botao novo quarto
        JButton novoQuartoBtn = createStyledButton("Novo Quarto");
        novoQuartoBtn.addActionListener(e -> abrirDialogoNovoQuarto());
        titlePanel.add(Box.createHorizontalStrut(20));
        titlePanel.add(novoQuartoBtn);

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
        JTextField tipoField = createStyledTextField();
        JTextField precoMinField = createStyledTextField();
        JTextField precoMaxField = createStyledTextField();

        // botoes de consulta
        JButton buscarTipoBtn = createStyledButton("Buscar por Tipo");
        JButton buscarPrecoBtn = createStyledButton("Buscar por Preço");
        JButton buscarDisponiveisBtn = createStyledButton("Buscar Disponíveis");
        JButton limparBtn = createStyledButton("Limpar Filtros");
        limparBtn.setBackground(new Color(220, 38, 38));

        // adiciona componentes ao painel de consulta
        gbcConsulta.gridx = 0; gbcConsulta.gridy = 0;
        consultaPanel.add(createStyledLabel("Tipo:"), gbcConsulta);
        gbcConsulta.gridx = 1;
        consultaPanel.add(tipoField, gbcConsulta);
        gbcConsulta.gridx = 2;
        consultaPanel.add(buscarTipoBtn, gbcConsulta);

        gbcConsulta.gridx = 0; gbcConsulta.gridy = 1;
        consultaPanel.add(createStyledLabel("Preço:"), gbcConsulta);
        gbcConsulta.gridx = 1;
        JPanel precoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        precoPanel.setBackground(BACKGROUND_COLOR);
        precoPanel.add(precoMinField);
        precoPanel.add(createStyledLabel("até"));
        precoPanel.add(precoMaxField);
        consultaPanel.add(precoPanel, gbcConsulta);
        gbcConsulta.gridx = 2;
        consultaPanel.add(buscarPrecoBtn, gbcConsulta);

        gbcConsulta.gridx = 0; gbcConsulta.gridy = 2;
        gbcConsulta.gridwidth = 2;
        consultaPanel.add(buscarDisponiveisBtn, gbcConsulta);
        gbcConsulta.gridx = 2;
        gbcConsulta.gridwidth = 1;
        consultaPanel.add(limparBtn, gbcConsulta);

        // configura acoes dos botoes de consulta
        buscarTipoBtn.addActionListener(e -> {
            String tipo = tipoField.getText().trim();
            if (!tipo.isEmpty()) {
                List<Quarto> quartos = QuartoController.buscarQuartosPorTipo(tipo);
                atualizarTabelaComQuartos(quartos);
            }
        });

        buscarPrecoBtn.addActionListener(e -> {
            try {
                Double precoMin = Double.parseDouble(precoMinField.getText().trim());
                Double precoMax = Double.parseDouble(precoMaxField.getText().trim());
                List<Quarto> quartos = QuartoController.buscarQuartosPorPreco(precoMin, precoMax);
                atualizarTabelaComQuartos(quartos);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Precos invalidos!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        buscarDisponiveisBtn.addActionListener(e -> {
            List<Quarto> quartos = QuartoController.buscarQuartosDisponiveis();
            atualizarTabelaComQuartos(quartos);
        });

        limparBtn.addActionListener(e -> {
            tipoField.setText("");
            precoMinField.setText("");
            precoMaxField.setText("");
            atualizarTabela();
        });

        // tabela de quartos
        String[] colunas = {"ID", "Tipo", "Preço"};
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
        table.getColumnModel().getColumn(1).setPreferredWidth(200); // Tipo
        table.getColumnModel().getColumn(2).setPreferredWidth(150); // Preço

        // adiciona menu de contexto (editar e excluir)
        JPopupMenu popupMenu = new JPopupMenu();
        popupMenu.setFont(LABEL_FONT);
        
        JMenuItem editarItem = new JMenuItem("Editar");
        editarItem.setFont(LABEL_FONT);
        editarItem.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                Long quartoId = (Long) table.getValueAt(row, 0);
                editarQuarto(quartoId);
            }
        });
        
        JMenuItem excluirItem = new JMenuItem("Excluir");
        excluirItem.setFont(LABEL_FONT);
        excluirItem.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                Long quartoId = (Long) table.getValueAt(row, 0);
                excluirQuarto(quartoId);
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

        // atualiza a tabela com todos os quartos
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

    // atualiza a tabela com todos os quartos
    private void atualizarTabela() {
        tableModel.setRowCount(0);
        List<Quarto> quartos = QuartoController.listarQuartos();
        for (Quarto q : quartos) {
            tableModel.addRow(new Object[]{
                q.getId(), q.getTipo(), q.getPreco()
            });
        }
    }

    // atualiza a tabela com uma lista especifica de quartos
    private void atualizarTabelaComQuartos(List<Quarto> quartos) {
        tableModel.setRowCount(0);
        for (Quarto q : quartos) {
            tableModel.addRow(new Object[]{
                q.getId(),
                q.getTipo(),
                String.format("R$ %.2f", q.getPreco())
            });
        }
    }

    // abre dialogo para editar um quarto
    private void editarQuarto(Long quartoId) {
        try {
            Quarto quarto = QuartoController.buscarQuartoPorId(quartoId);
            if (quarto == null) {
                throw new RuntimeException("Quarto nao encontrado");
            }

            // cria o dialogo de edicao
            JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Editar Quarto", true);
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
            JTextField tipoField = createStyledTextField();
            tipoField.setText(quarto.getTipo());

            JTextField precoField = createStyledTextField();
            precoField.setText(String.format("%.2f", quarto.getPreco()));

            // adiciona componentes ao formulario
            gbc.gridx = 0; gbc.gridy = 0;
            formPanel.add(createStyledLabel("Tipo:"), gbc);
            gbc.gridx = 1;
            formPanel.add(tipoField, gbc);

            gbc.gridx = 0; gbc.gridy = 1;
            formPanel.add(createStyledLabel("Preço:"), gbc);
            gbc.gridx = 1;
            formPanel.add(precoField, gbc);

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
                    String tipo = tipoField.getText().trim();
                    String precoStr = precoField.getText().trim();

                    if (tipo.isEmpty() || precoStr.isEmpty()) {
                        throw new IllegalArgumentException("Todos os campos sao obrigatorios");
                    }

                    Double preco = Double.parseDouble(precoStr.replace(",", "."));
                    if (preco <= 0) {
                        throw new IllegalArgumentException("O preco deve ser maior que zero");
                    }

                    QuartoController.atualizarQuarto(quartoId, tipo, preco);
                    atualizarTabela();
                    dialog.dispose();
                    JOptionPane.showMessageDialog(this, 
                        "Quarto atualizado com sucesso!", 
                        "Sucesso", 
                        JOptionPane.INFORMATION_MESSAGE);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(dialog, 
                        "Preco invalido. Use numeros com ponto ou virgula.", 
                        "Erro", 
                        JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(dialog, 
                        "Erro ao atualizar quarto: " + ex.getMessage(), 
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
                "Erro ao carregar quarto: " + e.getMessage(), 
                "Erro", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    // exclui um quarto com confirmacao
    private void excluirQuarto(Long quartoId) {
        int confirmacao = JOptionPane.showConfirmDialog(
            this,
            "Tem certeza que deseja excluir este quarto?",
            "Confirmar exclusao",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );
        
        if (confirmacao == JOptionPane.YES_OPTION) {
            try {
                QuartoController.excluirQuarto(quartoId);
                atualizarTabela();
                JOptionPane.showMessageDialog(this, 
                    "Quarto excluido com sucesso!", 
                    "Sucesso", 
                    JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, 
                    "Erro ao excluir quarto: " + e.getMessage(), 
                    "Erro", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // abre dialogo para cadastrar novo quarto
    private void abrirDialogoNovoQuarto() {
        // cria o dialogo de cadastro
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Novo Quarto", true);
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
        JTextField tipoField = createStyledTextField();
        JTextField precoField = createStyledTextField();

        // adiciona componentes ao formulario
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(createStyledLabel("Tipo:"), gbc);
        gbc.gridx = 1;
        formPanel.add(tipoField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(createStyledLabel("Preço:"), gbc);
        gbc.gridx = 1;
        formPanel.add(precoField, gbc);

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
                QuartoController.cadastrarQuarto(
                    tipoField.getText(),
                    Double.parseDouble(precoField.getText())
                );
                
                atualizarTabela();
                dialog.dispose();
                JOptionPane.showMessageDialog(this, 
                    "Quarto cadastrado com sucesso!", 
                    "Sucesso", 
                    JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, 
                    "Erro ao cadastrar quarto: " + ex.getMessage(), 
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