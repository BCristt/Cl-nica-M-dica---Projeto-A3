package src;

import java.awt.*;
import java.time.*;
import java.time.format.*;
import java.util.*;
import java.util.List;
import java.util.stream.*;
import javax.swing.*;

public class SistemaClinica {

    private List<Medico> medicos = new ArrayList<>();
    private List<Paciente> pacientes = new ArrayList<>();
    private List<Consulta> consultas = new ArrayList<>();

    private JFrame mainFrame; // Adicionado para ser o frame principal

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SistemaClinica().menu());
    }

    private void menu() {
        // Configurações visuais do UIManager para JOptionPane e outros componentes
        UIManager.put("OptionPane.minimumSize", new java.awt.Dimension(800, 600)); // Define o tamanho mínimo para OptionPanes
        UIManager.put("OptionPane.messageFont", new java.awt.Font("Arial", java.awt.Font.BOLD, 22));
        UIManager.put("OptionPane.buttonFont", new java.awt.Font("Arial", java.awt.Font.PLAIN, 18));
        UIManager.put("TextField.font", new java.awt.Font("Arial", java.awt.Font.PLAIN, 18));
        UIManager.put("OptionPane.background", new java.awt.Color(200, 230, 255));
        UIManager.put("Panel.background", new java.awt.Color(200, 230, 255));
        UIManager.put("Button.background", new java.awt.Color(100, 149, 237)); // Azul cobalto
        UIManager.put("Button.foreground", java.awt.Color.WHITE);              // Texto branco
        UIManager.put("Button.font", new java.awt.Font("Arial", java.awt.Font.BOLD, 16));
        UIManager.put("Button.select", new java.awt.Color(70, 130, 180));      // Cor ao pressionar

        // Configuração do JFrame principal
        mainFrame = new JFrame("🔷 MENU PRINCIPAL - Clínica Médica");
        mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Maximiza a janela, mas respeita a barra de tarefas
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Garante que a aplicação feche ao fechar o frame

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout()); // Usando GridBagLayout para centralizar
        mainPanel.setBackground(new java.awt.Color(200, 230, 255)); // Cor de fundo do painel principal

        // --- Adicionar Logo ---
        ImageIcon logoIcon = null;
        try {
            // Tenta carregar a imagem da logo
            // Certifique-se de que 'src/imagens/logo.png' seja o caminho correto
            logoIcon = new ImageIcon(getClass().getResource("/src/imagens/logo.png"));
            // Redimensiona a logo se necessário (exemplo para 200x200 pixels)
            Image logoImage = logoIcon.getImage();
            Image scaledLogo = logoImage.getScaledInstance(200, 200, Image.SCALE_SMOOTH);
            logoIcon = new ImageIcon(scaledLogo);
        } catch (Exception e) {
            System.err.println("Erro ao carregar a logo: " + e.getMessage());
            // Fallback: se a logo não carregar, exibe um texto em vez da imagem
        }

        JLabel logoLabel = new JLabel();
        if (logoIcon != null) {
            logoLabel.setIcon(logoIcon);
        } else { // Fallback se a imagem não for carregada
            logoLabel.setText("Clínica Médica");
            logoLabel.setFont(new Font("Arial", Font.BOLD, 40));
            logoLabel.setForeground(new Color(50, 90, 150));
        }

        // Painel para os botões do menu
        JPanel menuButtonsPanel = new JPanel();
        menuButtonsPanel.setLayout(new GridLayout(7, 1, 15, 15)); // 7 linhas para a nova opção
        menuButtonsPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50)); // Margem interna
        menuButtonsPanel.setBackground(new java.awt.Color(200, 230, 255));

        // As strings das opções podem continuar com emojis se desejar, mas não terão ícones de imagem
        String[] opcoes = {
            " Cadastrar Médico",
            "Cadastrar Paciente",
            " Agendar Consulta",
            " Cancelar Consulta",
            " Agenda",
            " Registrar Atendimento",
            " Histórico do Paciente", // <-- adicione aqui
            " Sair do Sistema"
        };

        for (String opcao : opcoes) {
            JButton button = new JButton(opcao);
            button.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 20));
            button.setBackground(new java.awt.Color(100, 149, 237));
            button.setForeground(java.awt.Color.WHITE);
            button.setFocusPainted(false);

            button.addActionListener(e -> {
                switch (opcao) {
                    case " Cadastrar Médico": cadastrarMedico(); break;
                    case "Cadastrar Paciente": cadastrarPaciente(); break;
                    case " Agendar Consulta": agendarConsulta(); break;
                    case " Cancelar Consulta": cancelarConsulta(); break;
                    case " Agenda": agenda(); break;
                    case " Registrar Atendimento": registrarAtendimento(); break;
                    case " Histórico do Paciente": historicoPorPaciente(); break; // <-- ADICIONE ESTA LINHA
                    case " Sair do Sistema": System.exit(0); break;
                }
            });
            menuButtonsPanel.add(button);
        }

        // --- Layout principal com logo e botões ---
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Margem externa para os componentes

        // Adiciona a logo
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 0.2; // Ocupa um pouco mais de espaço na parte superior
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(logoLabel, gbc);

        // Adiciona o painel de botões
        gbc.gridy = 1;
        gbc.weighty = 0.8; // Ocupa o restante do espaço vertical
        gbc.fill = GridBagConstraints.NONE; // Não preenche o espaço
        mainPanel.add(menuButtonsPanel, gbc);


        mainFrame.add(mainPanel);
        mainFrame.setVisible(true);
    }

    // ================ [ CADASTRAR MÉDICO ] ================
    private void cadastrarMedico() {
        String nome = JOptionPane.showInputDialog(mainFrame, "Nome do médico:");
        if (nome == null || nome.trim().isEmpty()) return;

        String crm = JOptionPane.showInputDialog(mainFrame, "CRM do médico:");
        if (crm == null || crm.trim().isEmpty()) return;

        String[] especialidades = {"Clínica Geral", "Cardiologia", "Dermatologia", "Ortopedia", "Pediatria"};
        String especialidade = (String) JOptionPane.showInputDialog(mainFrame,
            "Escolha a especialidade:", "Especialidade",
            JOptionPane.QUESTION_MESSAGE, null,
            especialidades, especialidades[0]);

        if (especialidade == null) return;

        Medico medico = new Medico(nome, crm, especialidade);
        medicos.add(medico);
        JOptionPane.showMessageDialog(mainFrame, "Médico cadastrado com sucesso!");
    }

    // ================ [ CADASTRAR PACIENTE ] ================
    private void cadastrarPaciente() {
        String nome = JOptionPane.showInputDialog(mainFrame, "Nome do paciente:");
        if (nome == null || nome.trim().isEmpty()) return;

        String cpf = JOptionPane.showInputDialog(mainFrame, "CPF do paciente:");
        if (cpf == null || cpf.trim().isEmpty()) return;

        JComboBox<String> cbDia = new JComboBox<>();
        for (int i = 1; i <= 31; i++) cbDia.addItem(String.format("%02d", i));

        JComboBox<String> cbMes = new JComboBox<>();
        for (int i = 1; i <= 12; i++) cbMes.addItem(String.format("%02d", i));

        JComboBox<String> cbAno = new JComboBox<>();
        int anoAtual = LocalDate.now().getYear();
        for (int i = anoAtual; i >= 1900; i--) cbAno.addItem(String.valueOf(i));

        JPanel painel = new JPanel();
        painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));
        painel.add(new JLabel("Selecione a data de nascimento:"));

        JPanel linhaData = new JPanel();
        linhaData.add(new JLabel("Dia:"));
        linhaData.add(cbDia);
        linhaData.add(new JLabel("Mês:"));
        linhaData.add(cbMes);
        linhaData.add(new JLabel("Ano:"));
        linhaData.add(cbAno);
        painel.add(linhaData);

        int result = JOptionPane.showConfirmDialog(mainFrame, painel, "Data de Nascimento", JOptionPane.OK_CANCEL_OPTION);
        if (result != JOptionPane.OK_OPTION) return;

        String dia = (String) cbDia.getSelectedItem();
        String mes = (String) cbMes.getSelectedItem();
        String ano = (String) cbAno.getSelectedItem();

        try {
            LocalDate dataNascimento = LocalDate.parse(ano + "-" + mes + "-" + dia);
            Paciente paciente = new Paciente(nome, cpf, dataNascimento);
            pacientes.add(paciente);
            JOptionPane.showMessageDialog(mainFrame, "Paciente cadastrado com sucesso!");
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(mainFrame, "Data inválida.");
        }
    }

    // ================ [ AGENDAR CONSULTA ] ================
    private void agendarConsulta() {
        if (medicos.isEmpty()) {
            JOptionPane.showMessageDialog(mainFrame, "Nenhum médico cadastrado.");
            return;
        }
        if (pacientes.isEmpty()) {
            JOptionPane.showMessageDialog(mainFrame, "Nenhum paciente cadastrado.");
            return;
        }

        Set<String> especialidades = medicos.stream()
                .map(Medico::getEspecialidade)
                .collect(Collectors.toSet());

        String especialidadeSelecionada = (String) JOptionPane.showInputDialog(mainFrame,
                "Selecione a especialidade médica:", "Especialidade",
                JOptionPane.QUESTION_MESSAGE, null,
                especialidades.toArray(), especialidades.iterator().next());

        if (especialidadeSelecionada == null) return;

        List<Medico> medicosFiltrados = medicos.stream()
                .filter(m -> m.getEspecialidade().equals(especialidadeSelecionada))
                .collect(Collectors.toList());

        if (medicosFiltrados.isEmpty()) {
            JOptionPane.showMessageDialog(mainFrame, "Não há médicos cadastrados para esta especialidade.");
            return;
        }

        Medico medico = (Medico) JOptionPane.showInputDialog(mainFrame, "Selecione o médico:",
                "Agendar Consulta", JOptionPane.QUESTION_MESSAGE, null,
                medicosFiltrados.toArray(), medicosFiltrados.get(0));

        if (medico == null) return;

        Paciente paciente = (Paciente) JOptionPane.showInputDialog(mainFrame, "Selecione o paciente:",
                "Agendar Consulta", JOptionPane.QUESTION_MESSAGE, null,
                pacientes.toArray(), pacientes.get(0));

        if (paciente == null) return;

        // ComboBoxes para data
        JComboBox<String> cbDia = new JComboBox<>();
        for (int i = 1; i <= 31; i++) cbDia.addItem(String.format("%02d", i));

        JComboBox<String> cbMes = new JComboBox<>();
        for (int i = 1; i <= 12; i++) cbMes.addItem(String.format("%02d", i));

        JComboBox<String> cbAno = new JComboBox<>();
        int anoAtual = LocalDate.now().getYear();
        for (int i = anoAtual; i <= anoAtual + 1; i++) cbAno.addItem(String.valueOf(i));

        JComboBox<String> cbHora = new JComboBox<>();
        for (int i = 8; i <= 17; i++) cbHora.addItem(String.format("%02d", i));
        JComboBox<String> cbMinuto = new JComboBox<>(new String[] {"00", "15", "30", "45"});

        // Botão "Hoje"
        JButton btnHoje = new JButton("Hoje");
        btnHoje.addActionListener(e -> {
            LocalDate hoje = LocalDate.now();
            cbDia.setSelectedItem(String.format("%02d", hoje.getDayOfMonth()));
            cbMes.setSelectedItem(String.format("%02d", hoje.getMonthValue()));
            cbAno.setSelectedItem(String.valueOf(hoje.getYear()));
        });

        JPanel painel = new JPanel();
        painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));
        painel.add(new JLabel("Selecione data e hora da consulta:"));

        JPanel linha = new JPanel();
        linha.add(new JLabel("Dia:")); linha.add(cbDia);
        linha.add(new JLabel("Mês:")); linha.add(cbMes);
        linha.add(new JLabel("Ano:")); linha.add(cbAno);
        linha.add(btnHoje); // Adiciona o botão "Hoje"
        linha.add(new JLabel("Hora:")); linha.add(cbHora);
        linha.add(new JLabel("Min:")); linha.add(cbMinuto);
        painel.add(linha);

        int r = JOptionPane.showConfirmDialog(mainFrame, painel, "Data e Hora", JOptionPane.OK_CANCEL_OPTION);
        if (r != JOptionPane.OK_OPTION) return;

        String dia = (String) cbDia.getSelectedItem();
        String mes = (String) cbMes.getSelectedItem();
        String ano = (String) cbAno.getSelectedItem();
        String hora = (String) cbHora.getSelectedItem();
        String minuto = (String) cbMinuto.getSelectedItem();

        LocalDateTime dataHora;
        try {
            dataHora = LocalDateTime.parse(ano + "-" + mes + "-" + dia + "T" + hora + ":" + minuto);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(mainFrame, "Data ou hora inválida.");
            return;
        }

        if (dataHora.isBefore(LocalDateTime.now())) {
            JOptionPane.showMessageDialog(mainFrame, "Data/hora não pode ser no passado.");
            return;
        }
        // ================ [ PACIENTE + DE 1 CONSULTA ] ================
        boolean pacienteConsultaNoMesmoDia = consultas.stream()
                .anyMatch(c -> c.getPaciente().equals(paciente)
                        && c.getMedico().equals(medico)
                        && c.getDataHora().toLocalDate().equals(dataHora.toLocalDate()));
        if (pacienteConsultaNoMesmoDia) {
            JOptionPane.showMessageDialog(mainFrame, "Paciente já possui consulta com este médico neste dia.");
            return;
        }

        long consultasDoMedicoNoDia = consultas.stream()
                .filter(c -> c.getMedico().equals(medico)
                        && c.getDataHora().toLocalDate().equals(dataHora.toLocalDate()))
                .count();
        if (consultasDoMedicoNoDia >= 10) {
            JOptionPane.showMessageDialog(mainFrame, "Médico já possui 10 consultas neste dia.");
            return;
        }

        boolean horarioJaAgendado = consultas.stream()
                .anyMatch(c -> c.getMedico().equals(medico)
                        && c.getDataHora().equals(dataHora));
        if (horarioJaAgendado) {
            JOptionPane.showMessageDialog(mainFrame, "Já existe uma consulta neste horário.");
            return;
        }

        Consulta novaConsulta = new Consulta(medico, paciente, dataHora);
        consultas.add(novaConsulta);
        JOptionPane.showMessageDialog(mainFrame, "Consulta agendada com sucesso!");
    }

    // ================ [ CANCELAR CONSULTA ] ================
    private void cancelarConsulta() {
        if (consultas.isEmpty()) {
            JOptionPane.showMessageDialog(mainFrame, "Nenhuma consulta agendada para cancelar.");
            return;
        }

        // Filtra apenas as consultas futuras ou de hoje que não foram registradas como atendidas
        // Usamos .minusMinutes(1) para incluir consultas que estão acontecendo "agora" ou que acabaram de passar.
        List<Consulta> consultasAtivas = consultas.stream()
            .filter(c -> c.getDataHora().isAfter(LocalDateTime.now().minusMinutes(1)) && c.getProntuario().isEmpty())
            .collect(Collectors.toList());

        if (consultasAtivas.isEmpty()) {
            JOptionPane.showMessageDialog(mainFrame, "Não há consultas ativas para cancelar.");
            return;
        }

        Consulta consultaParaCancelar = (Consulta) JOptionPane.showInputDialog(mainFrame,
                "Selecione a consulta para cancelar:", "Cancelar Consulta",
                JOptionPane.QUESTION_MESSAGE, null,
                consultasAtivas.toArray(), consultasAtivas.get(0));

        if (consultaParaCancelar == null) {
            return; // Usuário cancelou a seleção
        }

        int confirm = JOptionPane.showConfirmDialog(mainFrame,
                "Tem certeza que deseja cancelar esta consulta?\n" + consultaParaCancelar.toString(),
                "Confirmar Cancelamento", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            if (consultas.remove(consultaParaCancelar)) {
                JOptionPane.showMessageDialog(mainFrame, "Consulta cancelada com sucesso!");
            } else {
                JOptionPane.showMessageDialog(mainFrame, "Erro ao cancelar a consulta.");
            }
        }
    }

    // ================ [ CONSULTAS DO DIA ] ================
    private void agenda() {
        JComboBox<String> cbDia = new JComboBox<>();
        for (int i = 1; i <= 31; i++) cbDia.addItem(String.format("%02d", i));

        JComboBox<String> cbMes = new JComboBox<>();
        for (int i = 1; i <= 12; i++) cbMes.addItem(String.format("%02d", i));

        JComboBox<String> cbAno = new JComboBox<>();
        int anoAtual = LocalDate.now().getYear();
        for (int i = anoAtual - 1; i <= anoAtual + 1; i++) cbAno.addItem(String.valueOf(i));

        JButton btnHoje = new JButton("Hoje");
        btnHoje.addActionListener(e -> {
            LocalDate hoje = LocalDate.now();
            cbDia.setSelectedItem(String.format("%02d", hoje.getDayOfMonth()));
            cbMes.setSelectedItem(String.format("%02d", hoje.getMonthValue()));
            cbAno.setSelectedItem(String.valueOf(hoje.getYear()));
        });

        JPanel painel = new JPanel();
        painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));
        painel.add(new JLabel("Selecione a data para listar as consultas:"));

        JPanel linhaData = new JPanel();
        linhaData.add(new JLabel("Dia:"));
        linhaData.add(cbDia);
        linhaData.add(new JLabel("Mês:"));
        linhaData.add(cbMes);
        linhaData.add(new JLabel("Ano:"));
        linhaData.add(cbAno);
        linhaData.add(btnHoje);
        painel.add(linhaData);

        int result = JOptionPane.showConfirmDialog(mainFrame, painel, "Escolher Data", JOptionPane.OK_CANCEL_OPTION);
        if (result != JOptionPane.OK_OPTION) return;

        String dia = (String) cbDia.getSelectedItem();
        String mes = (String) cbMes.getSelectedItem();
        String ano = (String) cbAno.getSelectedItem();

        LocalDate dataEscolhida;
        try {
            dataEscolhida = LocalDate.parse(ano + "-" + mes + "-" + dia);
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(mainFrame, "Data inválida.");
            return;
        }

        List<Consulta> consultasDoDia = consultas.stream()
                .filter(c -> c.getDataHora().toLocalDate().equals(dataEscolhida))
                .collect(Collectors.toList());

        if (consultasDoDia.isEmpty()) {
            JOptionPane.showMessageDialog(mainFrame, "Não há consultas agendadas para esta data.");
            return;
        }

        StringBuilder sb = new StringBuilder("Consultas do dia " + dataEscolhida.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + ":\n\n");
        for (Consulta c : consultasDoDia) {
            sb.append("Médico: ").append(c.getMedico().getNome())
              .append(" | Paciente: ").append(c.getPaciente().getNome())
              .append(" | Horário: ").append(c.getDataHora().format(DateTimeFormatter.ofPattern("HH:mm")))
              .append("\n");
        }

        JTextArea textArea = new JTextArea(sb.toString());
        textArea.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 18));
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(600, 400));

        JOptionPane.showMessageDialog(mainFrame, scrollPane, "Agenda", JOptionPane.PLAIN_MESSAGE);
    }

    // ================ [ REGISTRAR ATENDIMENTO ] ================
    private void registrarAtendimento() {
        if (consultas.isEmpty()) {
            JOptionPane.showMessageDialog(mainFrame, "Nenhuma consulta cadastrada.");
            return;
        }

        // Filtra apenas as consultas que ainda não foram "atendidas" ou com prontuário vazio
        List<Consulta> consultasPendentes = consultas.stream()
            .filter(c -> c.getProntuario().isEmpty() || c.getDiagnostico().isEmpty() || c.getPrescricoes().isEmpty())
            .collect(Collectors.toList());

        if (consultasPendentes.isEmpty()) {
            JOptionPane.showMessageDialog(mainFrame, "Todos os atendimentos já foram registrados.");
            return;
        }

        Consulta consultaSelecionada = (Consulta) JOptionPane.showInputDialog(mainFrame, "Selecione a consulta para registrar atendimento:",
                "Registrar Atendimento", JOptionPane.QUESTION_MESSAGE, null,
                consultasPendentes.toArray(), consultasPendentes.get(0));

        if (consultaSelecionada == null) return;

        // Criação de um painel para coletar as informações do prontuário
        JPanel atendimentoPanel = new JPanel();
        atendimentoPanel.setLayout(new GridLayout(3, 2, 5, 5));
        atendimentoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextField prontuarioField = new JTextField(consultaSelecionada.getProntuario(), 20);
        JTextField diagnosticoField = new JTextField(consultaSelecionada.getDiagnostico(), 20);
        JTextField prescricoesField = new JTextField(consultaSelecionada.getPrescricoes(), 20);

        atendimentoPanel.add(new JLabel("Prontuário:"));
        atendimentoPanel.add(prontuarioField);
        atendimentoPanel.add(new JLabel("Diagnóstico:"));
        atendimentoPanel.add(diagnosticoField);
        atendimentoPanel.add(new JLabel("Prescrições:"));
        atendimentoPanel.add(prescricoesField);

        int result = JOptionPane.showConfirmDialog(mainFrame, atendimentoPanel, "Detalhes do Atendimento", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            consultaSelecionada.setProntuario(prontuarioField.getText());
            consultaSelecionada.setDiagnostico(diagnosticoField.getText());
            consultaSelecionada.setPrescricoes(prescricoesField.getText());
            JOptionPane.showMessageDialog(mainFrame, "Atendimento registrado com sucesso.");
        }
    }

    private void historicoPorPaciente() {
        if (pacientes.isEmpty()) {
            JOptionPane.showMessageDialog(mainFrame, "Nenhum paciente cadastrado.");
            return;
        }

        Paciente paciente = (Paciente) JOptionPane.showInputDialog(
            mainFrame,
            "Selecione o paciente:",
            "Histórico do Paciente",
            JOptionPane.QUESTION_MESSAGE,
            null,
            pacientes.toArray(),
            pacientes.get(0)
        );

        if (paciente == null) return;

        List<Consulta> historico = consultas.stream()
            .filter(c -> c.getPaciente().equals(paciente))
            .sorted(Comparator.comparing(Consulta::getDataHora))
            .collect(Collectors.toList());

        if (historico.isEmpty()) {
            JOptionPane.showMessageDialog(mainFrame, "Nenhuma consulta encontrada para este paciente.");
            return;
        }

        StringBuilder sb = new StringBuilder("Histórico de consultas de " + paciente.getNome() + ":\n\n");
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        for (Consulta c : historico) {
            sb.append("Data/Hora: ").append(c.getDataHora().format(dtf))
              .append(" | Médico: ").append(c.getMedico().getNome())
              .append(" (").append(c.getMedico().getEspecialidade()).append(")")
              .append("\n");
            if (!c.getProntuario().isEmpty() || !c.getDiagnostico().isEmpty() || !c.getPrescricoes().isEmpty()) {
                sb.append("  Prontuário: ").append(c.getProntuario()).append("\n");
                sb.append("  Diagnóstico: ").append(c.getDiagnostico()).append("\n");
                sb.append("  Prescrições: ").append(c.getPrescricoes()).append("\n");
            }
            sb.append("\n");
        }

        JTextArea textArea = new JTextArea(sb.toString());
        textArea.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 16));
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(600, 400));

        JOptionPane.showMessageDialog(mainFrame, scrollPane, "Histórico do Paciente", JOptionPane.PLAIN_MESSAGE);
    }

    public abstract class Pessoa {
        private String nome;
        public Pessoa(String nome) { this.nome = nome; }
        public String getNome() { return nome; }
        @Override
        public String toString() { return nome; }
    }

    public class Medico extends Pessoa {
        private String crm;
        private String especialidade;

        public Medico(String nome, String crm, String especialidade) {
            super(nome);
            this.crm = crm;
            this.especialidade = especialidade;
        }
        public String getCrm() { return crm; }
        public String getEspecialidade() { return especialidade; }
        @Override
        public String toString() {
            return getNome() + " (" + especialidade + ")";
        }
    }

    public class Paciente extends Pessoa {
        private String cpf;
        private LocalDate dataNascimento;

        public Paciente(String nome, String cpf, LocalDate dataNascimento) {
            super(nome);
            this.cpf = cpf;
            this.dataNascimento = dataNascimento;
        }
        public String getCpf() { return cpf; }
        public LocalDate getDataNascimento() { return dataNascimento; }
        @Override
        public String toString() {
            return getNome() + " (CPF: " + cpf + ")";
        }
    }

    public class Consulta {
        private Medico medico;
        private Paciente paciente;
        private LocalDateTime dataHora;
        private String prontuario = "";
        private String diagnostico = "";
        private String prescricoes = "";

        public Consulta(Medico medico, Paciente paciente, LocalDateTime dataHora) {
            this.medico = medico;
            this.paciente = paciente;
            this.dataHora = dataHora;
        }

        public Medico getMedico() { return medico; }
        public Paciente getPaciente() { return paciente; }
        public LocalDateTime getDataHora() { return dataHora; }
        public String getProntuario() { return prontuario; }
        public void setProntuario(String prontuario) { this.prontuario = prontuario; }
        public String getDiagnostico() { return diagnostico; }
        public void setDiagnostico(String diagnostico) { this.diagnostico = diagnostico; }
        public String getPrescricoes() { return prescricoes; }
        public void setPrescricoes(String prescricoes) { this.prescricoes = prescricoes; }

        @Override
        public String toString() {
            return "Consulta - Médico: " + medico.getNome() + ", Paciente: " + paciente.getNome()
                + ", Data/Hora: " + dataHora.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        }
    }
}