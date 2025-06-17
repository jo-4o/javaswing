package com.projeto.view;

import com.projeto.controller.ReservaController;
import com.projeto.controller.ClienteController;
import com.projeto.controller.QuartoController;
import com.projeto.model.Cliente;
import com.projeto.model.Quarto;
import com.projeto.model.Reserva;
import com.toedter.calendar.JDateChooser;
import com.toedter.calendar.JTextFieldDateEditor;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

// painel para gerenciamento de reservas
public class ReservasPanel extends JPanel {
    private final DefaultTableModel tableModel;
    // cores para o layout do painel
    private static final Color BACKGROUND_COLOR = new Color(248, 250, 252);
    private static final Color PRIMARY_COLOR = new Color(0, 102, 204);
    private static final Color SECONDARY_COLOR = new Color(51, 65, 85);
    // fontes para o layout do painel
    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 24);
    private static final Font LABEL_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 14);
    private JLabel totalLabel;
    private JLabel mediaLabel;

    // construtor da classe
    public ReservasPanel() {
        setLayout(new BorderLayout(20, 20));
        setBackground(BACKGROUND_COLOR);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // painel do titulo
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setBackground(BACKGROUND_COLOR);
        JLabel titleLabel = new JLabel("Gerenciamento de Reservas");
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(SECONDARY_COLOR);
        titlePanel.add(titleLabel);

        // botao nova reserva
        JButton novaReservaBtn = createStyledButton("Nova Reserva");
        novaReservaBtn.addActionListener(e -> abrirDialogoNovaReserva());
        titlePanel.add(Box.createHorizontalStrut(20));
        titlePanel.add(novaReservaBtn);

        // painel principal com jsplitpane
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
        JTextField clienteField = createStyledTextField();
        JTextField quartoField = createStyledTextField();
        JDateChooser dataInicioChooser = createStyledDateChooser();
        JDateChooser dataFimChooser = createStyledDateChooser();

        // botoes de consulta
        JButton buscarClienteBtn = createStyledButton("Buscar por Cliente");
        JButton buscarQuartoBtn = createStyledButton("Buscar por Quarto");
        JButton buscarPeriodoBtn = createStyledButton("Buscar por Periodo");
        JButton limparBtn = createStyledButton("Limpar Filtros");
        limparBtn.setBackground(new Color(220, 38, 38));

        // painel de estatisticas
        JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statsPanel.setBackground(BACKGROUND_COLOR);
        mediaLabel = createStyledLabel("Valor Medio: R$ 0,00");
        totalLabel = createStyledLabel("Total de Reservas: 0");
        statsPanel.add(mediaLabel);
        statsPanel.add(Box.createHorizontalStrut(20));
        statsPanel.add(totalLabel);

        // adicionando componentes ao painel de consulta
        gbcConsulta.gridx = 0; gbcConsulta.gridy = 0;
        consultaPanel.add(createStyledLabel("Cliente:"), gbcConsulta);
        gbcConsulta.gridx = 1;
        consultaPanel.add(clienteField, gbcConsulta);
        gbcConsulta.gridx = 2;
        consultaPanel.add(buscarClienteBtn, gbcConsulta);

        gbcConsulta.gridx = 0; gbcConsulta.gridy = 1;
        consultaPanel.add(createStyledLabel("Quarto:"), gbcConsulta);
        gbcConsulta.gridx = 1;
        consultaPanel.add(quartoField, gbcConsulta);
        gbcConsulta.gridx = 2;
        consultaPanel.add(buscarQuartoBtn, gbcConsulta);

        gbcConsulta.gridx = 0; gbcConsulta.gridy = 2;
        consultaPanel.add(createStyledLabel("Periodo:"), gbcConsulta);
        gbcConsulta.gridx = 1;
        JPanel periodoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        periodoPanel.setBackground(BACKGROUND_COLOR);
        periodoPanel.add(dataInicioChooser);
        periodoPanel.add(createStyledLabel("ate"));
        periodoPanel.add(dataFimChooser);
        consultaPanel.add(periodoPanel, gbcConsulta);
        gbcConsulta.gridx = 2;
        consultaPanel.add(buscarPeriodoBtn, gbcConsulta);

        gbcConsulta.gridx = 0; gbcConsulta.gridy = 3;
        gbcConsulta.gridwidth = 3;
        consultaPanel.add(statsPanel, gbcConsulta);

        gbcConsulta.gridx = 2; gbcConsulta.gridy = 4;
        gbcConsulta.gridwidth = 1;
        consultaPanel.add(limparBtn, gbcConsulta);

        // configurando acoes dos botoes de consulta
        buscarClienteBtn.addActionListener(e -> {
            String nomeCliente = clienteField.getText().trim();
            if (!nomeCliente.isEmpty()) {
                List<Reserva> reservas = ReservaController.buscarReservasPorCliente(nomeCliente);
                atualizarTabelaComReservas(reservas);
                atualizarEstatisticas(reservas);
            }
        });

        buscarQuartoBtn.addActionListener(e -> {
            String tipoQuarto = quartoField.getText().trim();
            if (!tipoQuarto.isEmpty()) {
                List<Reserva> reservas = ReservaController.buscarReservasPorQuarto(tipoQuarto);
                atualizarTabelaComReservas(reservas);
                atualizarEstatisticas(reservas);
            }
        });

        buscarPeriodoBtn.addActionListener(e -> {
            if (dataInicioChooser.getDate() != null && dataFimChooser.getDate() != null) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                String dataInicio = dateFormat.format(dataInicioChooser.getDate());
                String dataFim = dateFormat.format(dataFimChooser.getDate());
                List<Reserva> reservas = ReservaController.buscarReservasPorPeriodo(dataInicio, dataFim);
                atualizarTabelaComReservas(reservas);
                atualizarEstatisticas(reservas);
            }
        });

        limparBtn.addActionListener(e -> {
            clienteField.setText("");
            quartoField.setText("");
            dataInicioChooser.setDate(null);
            dataFimChooser.setDate(null);
            atualizarTabela();
            atualizarEstatisticas(ReservaController.listarReservas());
        });

        // tabela de reservas
        String[] colunas = {"ID", "Cliente", "Quarto", "Check-In", "Check-Out"};
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
        
        // estilizando o cabecalho da tabela
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(SECONDARY_COLOR);
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setPreferredSize(new Dimension(0, 40));

        // configurando as colunas da tabela
        table.getColumnModel().getColumn(0).setPreferredWidth(50);  // ID
        table.getColumnModel().getColumn(1).setPreferredWidth(200); // Cliente
        table.getColumnModel().getColumn(2).setPreferredWidth(150); // Quarto
        table.getColumnModel().getColumn(3).setPreferredWidth(200); // Check-In
        table.getColumnModel().getColumn(4).setPreferredWidth(200); // Check-Out

        // adicionando menu de contexto (editar/excluir)
        JPopupMenu popupMenu = new JPopupMenu();
        popupMenu.setFont(LABEL_FONT);
        
        JMenuItem editarItem = new JMenuItem("Editar");
        editarItem.setFont(LABEL_FONT);
        editarItem.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                Long reservaId = (Long) table.getValueAt(row, 0);
                editarReserva(reservaId);
            }
        });
        
        JMenuItem excluirItem = new JMenuItem("Excluir");
        excluirItem.setFont(LABEL_FONT);
        excluirItem.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                Long reservaId = (Long) table.getValueAt(row, 0);
                excluirReserva(reservaId);
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

        // atualiza a tabela ao iniciar
        atualizarTabela();

        // adicionando os paineis ao jsplitpane
        splitPane.setTopComponent(consultaPanel);
        splitPane.setBottomComponent(scrollPane);

        add(titlePanel, BorderLayout.NORTH);
        add(splitPane, BorderLayout.CENTER);
    }

    // cria um jlabel estilizado
    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(LABEL_FONT);
        label.setForeground(SECONDARY_COLOR);
        return label;
    }

    // cria um jcombobox estilizado
    private JComboBox<String> createStyledComboBox() {
        JComboBox<String> comboBox = new JComboBox<>();
        comboBox.setFont(LABEL_FONT);
        comboBox.setPreferredSize(new Dimension(250, 35));
        comboBox.setBackground(Color.WHITE);
        comboBox.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(226, 232, 240), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        return comboBox;
    }

    // cria um jdatechooser estilizado para selecao de datas
    private JDateChooser createStyledDateChooser() {
        JDateChooser dateChooser = new JDateChooser();
        dateChooser.setLocale(new Locale("pt", "BR"));
        dateChooser.setDateFormatString("dd/MM/yyyy");
        dateChooser.setPreferredSize(new Dimension(150, 35));
        
        // estilizando o campo de texto do seletor de data
        JTextFieldDateEditor editor = (JTextFieldDateEditor) dateChooser.getDateEditor();
        editor.setFont(LABEL_FONT);
        editor.setBackground(Color.WHITE);
        editor.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(226, 232, 240), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        
        return dateChooser;
    }

    // cria um jtextfield estilizado
    private JTextField createStyledTextField() {
        JTextField textField = new JTextField();
        textField.setFont(LABEL_FONT);
        textField.setPreferredSize(new Dimension(250, 35));
        textField.setBackground(Color.WHITE);
        textField.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(226, 232, 240), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        return textField;
    }

    // cria um jbutton estilizado
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

    // atualiza os dados da tabela de reservas
    private void atualizarTabela() {
        tableModel.setRowCount(0);
        List<Reserva> reservas = ReservaController.listarReservas();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        
        for (Reserva r : reservas) {
            tableModel.addRow(new Object[]{
                r.getId(),
                r.getCliente().getNome(),
                r.getQuarto().getTipo(),
                r.getDataCheckIn().format(formatter),
                r.getDataCheckOut().format(formatter)
            });
        }
    }

    // atualiza a tabela com uma lista especifica de reservas
    private void atualizarTabelaComReservas(List<Reserva> reservas) {
        tableModel.setRowCount(0);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        
        for (Reserva r : reservas) {
            tableModel.addRow(new Object[]{
                r.getId(),
                r.getCliente().getNome(),
                r.getQuarto().getTipo(),
                r.getDataCheckIn().format(formatter),
                r.getDataCheckOut().format(formatter)
            });
        }
    }

    // atualiza as estatisticas de reservas (total e valor medio)
    private void atualizarEstatisticas(List<Reserva> reservas) {
        // atualiza o total de reservas
        totalLabel.setText("Total de Reservas: " + reservas.size());

        // calcula o valor medio
        if (!reservas.isEmpty()) {
            double valorMedio = reservas.stream()
                .mapToDouble(r -> r.getQuarto().getPreco())
                .average()
                .orElse(0.0);
            mediaLabel.setText(String.format("Valor Medio: R$ %.2f", valorMedio));
        } else {
            mediaLabel.setText("Valor Medio: R$ 0,00");
        }
    }

    // abre um dialogo para editar uma reserva existente
    private void editarReserva(Long reservaId) {
        try {
            Reserva reserva = ReservaController.buscarReservaPorId(reservaId);
            if (reserva == null) {
                throw new RuntimeException("Reserva nao encontrada");
            }

            // criando o dialogo de edicao
            JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Editar Reserva", true);
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

            // carregando dados para os combos de cliente e quarto
            List<Cliente> clientes = ClienteController.listarClientes();
            List<Quarto> quartos = QuartoController.listarQuartos();

            JComboBox<String> clienteDropdown = createStyledComboBox();
            for (Cliente c : clientes) {
                clienteDropdown.addItem(c.getId() + " --- " + c.getNome());
                if (c.getId().equals(reserva.getCliente().getId())) {
                    clienteDropdown.setSelectedItem(c.getId() + " --- " + c.getNome());
                }
            }

            JComboBox<String> quartoDropdown = createStyledComboBox();
            for (Quarto q : quartos) {
                quartoDropdown.addItem(q.getId() + " --- " + q.getTipo());
                if (q.getId().equals(reserva.getQuarto().getId())) {
                    quartoDropdown.setSelectedItem(q.getId() + " --- " + q.getTipo());
                }
            }

            // configurando os seletores de data
            JDateChooser checkInDateChooser = createStyledDateChooser();
            JDateChooser checkOutDateChooser = createStyledDateChooser();

            // convertendo as datas do localdatetime para date
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
            LocalDateTime checkIn = LocalDateTime.parse(reserva.getDataCheckIn().toString(), formatter);
            LocalDateTime checkOut = LocalDateTime.parse(reserva.getDataCheckOut().toString(), formatter);

            Calendar checkInCal = Calendar.getInstance();
            checkInCal.set(checkIn.getYear(), checkIn.getMonthValue() - 1, checkIn.getDayOfMonth());
            checkInDateChooser.setDate(checkInCal.getTime());

            Calendar checkOutCal = Calendar.getInstance();
            checkOutCal.set(checkOut.getYear(), checkOut.getMonthValue() - 1, checkOut.getDayOfMonth());
            checkOutDateChooser.setDate(checkOutCal.getTime());

            // configurando os seletores de hora
            JComboBox<String> checkInHourCombo = createHourComboBox();
            JComboBox<String> checkInMinuteCombo = createMinuteComboBox();
            JComboBox<String> checkOutHourCombo = createHourComboBox();
            JComboBox<String> checkOutMinuteCombo = createMinuteComboBox();

            checkInHourCombo.setSelectedItem(String.format("%02d", checkIn.getHour()));
            checkInMinuteCombo.setSelectedItem(String.format("%02d", checkIn.getMinute()));
            checkOutHourCombo.setSelectedItem(String.format("%02d", checkOut.getHour()));
            checkOutMinuteCombo.setSelectedItem(String.format("%02d", checkOut.getMinute()));

            // paineis para agrupar data e hora
            JPanel checkInPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
            checkInPanel.setBackground(BACKGROUND_COLOR);
            checkInPanel.add(checkInDateChooser);
            checkInPanel.add(createStyledLabel("Hora:"));
            checkInPanel.add(checkInHourCombo);
            checkInPanel.add(createStyledLabel(":"));
            checkInPanel.add(checkInMinuteCombo);

            JPanel checkOutPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
            checkOutPanel.setBackground(BACKGROUND_COLOR);
            checkOutPanel.add(checkOutDateChooser);
            checkOutPanel.add(createStyledLabel("Hora:"));
            checkOutPanel.add(checkOutHourCombo);
            checkOutPanel.add(createStyledLabel(":"));
            checkOutPanel.add(checkOutMinuteCombo);

            // adicionando componentes ao formulario
            gbc.gridx = 0; gbc.gridy = 0;
            formPanel.add(createStyledLabel("Cliente:"), gbc);
            gbc.gridx = 1;
            formPanel.add(clienteDropdown, gbc);

            gbc.gridx = 0; gbc.gridy = 1;
            formPanel.add(createStyledLabel("Quarto:"), gbc);
            gbc.gridx = 1;
            formPanel.add(quartoDropdown, gbc);

            gbc.gridx = 0; gbc.gridy = 2;
            formPanel.add(createStyledLabel("Check-In:"), gbc);
            gbc.gridx = 1;
            formPanel.add(checkInPanel, gbc);

            gbc.gridx = 0; gbc.gridy = 3;
            formPanel.add(createStyledLabel("Check-Out:"), gbc);
            gbc.gridx = 1;
            formPanel.add(checkOutPanel, gbc);

            // painel de botoes
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
            buttonPanel.setBackground(BACKGROUND_COLOR);

            JButton salvarBtn = createStyledButton("Salvar");
            JButton cancelarBtn = createStyledButton("Cancelar");
            cancelarBtn.setBackground(new Color(220, 38, 38)); // vermelho

            buttonPanel.add(salvarBtn);
            buttonPanel.add(cancelarBtn);

            // adicionando os paineis ao dialogo
            dialog.add(formPanel, BorderLayout.CENTER);
            dialog.add(buttonPanel, BorderLayout.SOUTH);

            // configurando acoes dos botoes de salvar/cancelar
            salvarBtn.addActionListener(e -> {
                try {
                    String clienteSelecionado = (String) clienteDropdown.getSelectedItem();
                    String quartoSelecionado = (String) quartoDropdown.getSelectedItem();
                    Long clienteId = Long.parseLong(clienteSelecionado.split(" --- ")[0]);
                    Long quartoId = Long.parseLong(quartoSelecionado.split(" --- ")[0]);

                    if (checkInDateChooser.getDate() == null || checkOutDateChooser.getDate() == null) {
                        throw new IllegalArgumentException("data e obrigatoria");
                    }

                    // formatando a data e hora
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    Calendar checkInCalendar = Calendar.getInstance();
                    checkInCalendar.setTime(checkInDateChooser.getDate());
                    checkInCalendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt((String)checkInHourCombo.getSelectedItem()));
                    checkInCalendar.set(Calendar.MINUTE, Integer.parseInt((String)checkInMinuteCombo.getSelectedItem()));

                    Calendar checkOutCalendar = Calendar.getInstance();
                    checkOutCalendar.setTime(checkOutDateChooser.getDate());
                    checkOutCalendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt((String)checkOutHourCombo.getSelectedItem()));
                    checkOutCalendar.set(Calendar.MINUTE, Integer.parseInt((String)checkOutMinuteCombo.getSelectedItem()));

                    ReservaController.atualizarReserva(
                        reservaId,
                        clienteId,
                        quartoId,
                        dateFormat.format(checkInCalendar.getTime()),
                        dateFormat.format(checkOutCalendar.getTime())
                    );
                    
                    atualizarTabela();
                    dialog.dispose();
                    JOptionPane.showMessageDialog(this, 
                        "reserva atualizada com sucesso", 
                        "sucesso", 
                        JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(dialog, 
                        "erro ao atualizar reserva: " + ex.getMessage(), 
                        "erro", 
                        JOptionPane.ERROR_MESSAGE);
                }
            });

            cancelarBtn.addActionListener(e -> dialog.dispose());

            // configurando o dialogo
            dialog.pack();
            dialog.setLocationRelativeTo(this);
            dialog.setResizable(false);
            dialog.setVisible(true);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "erro ao carregar reserva: " + e.getMessage(), 
                "erro", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    // exclui uma reserva apos confirmacao do usuario
    private void excluirReserva(Long reservaId) {
        int confirmacao = JOptionPane.showConfirmDialog(
            this,
            "tem certeza que deseja excluir esta reserva",
            "confirmar exclusao",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );
        
        if (confirmacao == JOptionPane.YES_OPTION) {
            try {
                ReservaController.excluirReserva(reservaId);
                atualizarTabela();
                JOptionPane.showMessageDialog(this, 
                    "reserva excluida com sucesso", 
                    "sucesso", 
                    JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, 
                    "erro ao excluir reserva: " + e.getMessage(), 
                    "erro", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // cria um jcombobox estilizado para selecao de horas
    private JComboBox<String> createHourComboBox() {
        JComboBox<String> comboBox = new JComboBox<>();
        comboBox.setFont(LABEL_FONT);
        comboBox.setPreferredSize(new Dimension(60, 35));
        comboBox.setBackground(Color.WHITE);
        comboBox.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(226, 232, 240), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        
        for (int i = 0; i < 24; i++) {
            comboBox.addItem(String.format("%02d", i));
        }
        
        return comboBox;
    }

    // cria um jcombobox estilizado para selecao de minutos
    private JComboBox<String> createMinuteComboBox() {
        JComboBox<String> comboBox = new JComboBox<>();
        comboBox.setFont(LABEL_FONT);
        comboBox.setPreferredSize(new Dimension(60, 35));
        comboBox.setBackground(Color.WHITE);
        comboBox.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(226, 232, 240), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        
        for (int i = 0; i < 60; i++) {
            comboBox.addItem(String.format("%02d", i));
        }
        
        return comboBox;
    }

    // abre um dialogo para cadastrar uma nova reserva
    private void abrirDialogoNovaReserva() {
        // criando o dialogo de cadastro
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Nova Reserva", true);
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

        // carregando dados para os combos de cliente e quarto
        List<Cliente> clientes = ClienteController.listarClientes();
        List<Quarto> quartos = QuartoController.listarQuartos();

        JComboBox<String> clienteDropdown = createStyledComboBox();
        for (Cliente c : clientes) {
            clienteDropdown.addItem(c.getId() + " --- " + c.getNome());
        }

        JComboBox<String> quartoDropdown = createStyledComboBox();
        for (Quarto q : quartos) {
            quartoDropdown.addItem(q.getId() + " --- " + q.getTipo());
        }

        // configurando os seletores de data
        JDateChooser checkInDateChooser = createStyledDateChooser();
        JDateChooser checkOutDateChooser = createStyledDateChooser();

        // configurando os seletores de hora
        JComboBox<String> checkInHourCombo = createHourComboBox();
        JComboBox<String> checkInMinuteCombo = createMinuteComboBox();
        JComboBox<String> checkOutHourCombo = createHourComboBox();
        JComboBox<String> checkOutMinuteCombo = createMinuteComboBox();

        // paineis para agrupar data e hora
        JPanel checkInPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        checkInPanel.setBackground(BACKGROUND_COLOR);
        checkInPanel.add(checkInDateChooser);
        checkInPanel.add(createStyledLabel("Hora:"));
        checkInPanel.add(checkInHourCombo);
        checkInPanel.add(createStyledLabel(":"));
        checkInPanel.add(checkInMinuteCombo);

        JPanel checkOutPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        checkOutPanel.setBackground(BACKGROUND_COLOR);
        checkOutPanel.add(checkOutDateChooser);
        checkOutPanel.add(createStyledLabel("Hora:"));
        checkOutPanel.add(checkOutHourCombo);
        checkOutPanel.add(createStyledLabel(":"));
        checkOutPanel.add(checkOutMinuteCombo);

        // adicionando componentes ao formulario
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(createStyledLabel("Cliente:"), gbc);
        gbc.gridx = 1;
        formPanel.add(clienteDropdown, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(createStyledLabel("Quarto:"), gbc);
        gbc.gridx = 1;
        formPanel.add(quartoDropdown, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(createStyledLabel("Check-In:"), gbc);
        gbc.gridx = 1;
        formPanel.add(checkInPanel, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(createStyledLabel("Check-Out:"), gbc);
        gbc.gridx = 1;
        formPanel.add(checkOutPanel, gbc);

        // painel de botoes
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(BACKGROUND_COLOR);

        JButton salvarBtn = createStyledButton("Salvar");
        JButton cancelarBtn = createStyledButton("Cancelar");
        cancelarBtn.setBackground(new Color(220, 38, 38)); // vermelho

        buttonPanel.add(salvarBtn);
        buttonPanel.add(cancelarBtn);

        // adicionando os paineis ao dialogo
        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        // configurando acoes dos botoes de salvar/cancelar
        salvarBtn.addActionListener(e -> {
            try {
                String clienteSelecionado = (String) clienteDropdown.getSelectedItem();
                String quartoSelecionado = (String) quartoDropdown.getSelectedItem();
                Long clienteId = Long.parseLong(clienteSelecionado.split(" --- ")[0]);
                Long quartoId = Long.parseLong(quartoSelecionado.split(" --- ")[0]);

                if (checkInDateChooser.getDate() == null || checkOutDateChooser.getDate() == null) {
                    throw new IllegalArgumentException("data e obrigatoria");
                }

                // formatando a data e hora
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                Calendar checkInCalendar = Calendar.getInstance();
                checkInCalendar.setTime(checkInDateChooser.getDate());
                checkInCalendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt((String)checkInHourCombo.getSelectedItem()));
                checkInCalendar.set(Calendar.MINUTE, Integer.parseInt((String)checkInMinuteCombo.getSelectedItem()));

                Calendar checkOutCalendar = Calendar.getInstance();
                checkOutCalendar.setTime(checkOutDateChooser.getDate());
                checkOutCalendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt((String)checkOutHourCombo.getSelectedItem()));
                checkOutCalendar.set(Calendar.MINUTE, Integer.parseInt((String)checkOutMinuteCombo.getSelectedItem()));

                ReservaController.cadastrarReserva(
                    clienteId,
                    quartoId,
                    dateFormat.format(checkInCalendar.getTime()),
                    dateFormat.format(checkOutCalendar.getTime())
                );
                
                atualizarTabela();
                dialog.dispose();
                JOptionPane.showMessageDialog(this, 
                    "reserva cadastrada com sucesso", 
                    "sucesso", 
                    JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, 
                    "erro ao cadastrar reserva: " + ex.getMessage(), 
                    "erro", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelarBtn.addActionListener(e -> dialog.dispose());

        // configurando o dialogo
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setResizable(false);
        dialog.setVisible(true);
    }
}