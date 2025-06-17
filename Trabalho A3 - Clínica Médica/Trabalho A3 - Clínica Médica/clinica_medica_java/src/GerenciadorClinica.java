package src;

import javax.swing.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

// ------- GERENCIADOR -------
public class GerenciadorClinica {
    private List<Medico> medicos = new ArrayList<>();
    private List<Paciente> pacientes = new ArrayList<>();
    private List<Consulta> consultas = new ArrayList<>();

    public static void main(String[] args) {
        GerenciadorClinica app = new GerenciadorClinica();
        SwingUtilities.invokeLater(() -> app.menu());
    }

    public void menu() {
        JFrame frame = new JFrame("Sistema Clínica Médica");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Tela cheia
        frame.setLocationRelativeTo(null);

        while (true) {
            String opcao = JOptionPane.showInputDialog(frame,
                    "=== Menu Clínica Médica ===\n" +
                    "1 - Cadastrar Médico\n" +
                    "2 - Cadastrar Paciente\n" +
                    "3 - Agendar Consulta\n" +
                    "4 - Cancelar Consulta\n" +
                    "5 - Listar Consultas por Médico\n" +
                    "6 - Histórico de Consultas por Paciente\n" +
                    "0 - Sair\n\n" +
                    "Informe a opção:");

            if (opcao == null) break; // Cancelou

            switch (opcao.trim()) {
                case "1" -> cadastrarMedico();
                case "2" -> cadastrarPaciente();
                case "3" -> agendarConsulta();
                case "4" -> cancelarConsulta();
                case "5" -> listarConsultasPorMedico();
                case "6" -> historicoPorPaciente();
                case "0" -> {
                    JOptionPane.showMessageDialog(null, "Saindo do sistema. Até logo!");
                    System.exit(0);
                }
                default -> JOptionPane.showMessageDialog(null, "Opção inválida!");
            }
        }
    }
// ------- MÉDICO -------
    public void cadastrarMedico() {
        try {
            String nome = JOptionPane.showInputDialog("Nome do Médico:");
            if (nome == null || nome.isBlank()) {
                JOptionPane.showMessageDialog(null, "Nome inválido.");
                return;
            }
            String crm = JOptionPane.showInputDialog("CRM:");
            if (crm == null || crm.isBlank()) {
                JOptionPane.showMessageDialog(null, "CRM inválido.");
                return;
            }

            String[] especialidades = {"Clínico Geral", "Pediatra", "Cardiologista", "Dermatologista", "Ortopedista", "Ginecologista", "Psiquiatra"};
            String especialidade = (String) JOptionPane.showInputDialog(
                    null,
                    "Selecione a Especialidade:",
                    "Cadastro de Médico",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    especialidades,
                    especialidades[0]
            );

            if (especialidade == null) {
                JOptionPane.showMessageDialog(null, "Cadastro cancelado.");
                return;
            }

            Medico medico = new Medico(nome.trim(), crm.trim(), especialidade);
            medicos.add(medico);
            JOptionPane.showMessageDialog(null, "Médico cadastrado com sucesso!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao cadastrar médico.");
        }
    }
// ------- PACIENTE -------
    public void cadastrarPaciente() {
        try {
            String nome = JOptionPane.showInputDialog("Nome do Paciente:");
            if (nome == null || nome.isBlank()) {
                JOptionPane.showMessageDialog(null, "Nome inválido.");
                return;
            }
            String cpf = JOptionPane.showInputDialog("CPF:");
            if (cpf == null || cpf.isBlank()) {
                JOptionPane.showMessageDialog(null, "CPF inválido.");
                return;
            }

            int dia = Integer.parseInt(JOptionPane.showInputDialog("Dia de Nascimento (ex: 15):"));
            int mes = Integer.parseInt(JOptionPane.showInputDialog("Mês de Nascimento (ex: 6):"));
            int ano = Integer.parseInt(JOptionPane.showInputDialog("Ano de Nascimento (ex: 1990):"));
            LocalDate dataNascimento = LocalDate.of(ano, mes, dia);

            Paciente paciente = new Paciente(nome.trim(), cpf.trim(), dataNascimento);
            pacientes.add(paciente);
            JOptionPane.showMessageDialog(null, "Paciente cadastrado com sucesso!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao cadastrar paciente. Verifique os dados informados.");
        }
    }
// ------- CONSULTA -------
    public void agendarConsulta() {
        try {
            String crm = JOptionPane.showInputDialog("Informe o CRM do Médico:");
            Medico medico = buscarMedico(crm);
            if (medico == null) {
                JOptionPane.showMessageDialog(null, "Médico não encontrado.");
                return;
            }

            String cpf = JOptionPane.showInputDialog("Informe o CPF do Paciente:");
            Paciente paciente = buscarPaciente(cpf);
            if (paciente == null) {
                JOptionPane.showMessageDialog(null, "Paciente não encontrado.");
                return;
            }

            int dia = Integer.parseInt(JOptionPane.showInputDialog("Dia da Consulta (ex: 16):"));
            int mes = Integer.parseInt(JOptionPane.showInputDialog("Mês da Consulta (ex: 6):"));
            int ano = Integer.parseInt(JOptionPane.showInputDialog("Ano da Consulta (ex: 2025):"));
            int hora = Integer.parseInt(JOptionPane.showInputDialog("Hora da Consulta (0-23):"));
            int minuto = Integer.parseInt(JOptionPane.showInputDialog("Minuto da Consulta (0-59):"));

            LocalDateTime horario = LocalDateTime.of(ano, mes, dia, hora, minuto);

            if (horario.isBefore(LocalDateTime.now())) {
                JOptionPane.showMessageDialog(null, "A data da consulta não pode estar no passado.");
                return;
            }

            long consultasNoDia = consultas.stream()
                    .filter(c -> c.getMedico().equals(medico) &&
                            c.getDataHora().toLocalDate().equals(horario.toLocalDate()))
                    .count();

            if (consultasNoDia >= 10) {
                JOptionPane.showMessageDialog(null, "Médico já possui 10 consultas nesse dia.");
                return;
            }

            boolean pacienteJaMarcado = consultas.stream()
                    .anyMatch(c -> c.getMedico().equals(medico) &&
                            c.getPaciente().equals(paciente) &&
                            c.getDataHora().toLocalDate().equals(horario.toLocalDate()));

            if (pacienteJaMarcado) {
                JOptionPane.showMessageDialog(null, "Paciente já possui consulta com esse médico nesse dia.");
                return;
            }

            Consulta consulta = new Consulta(medico, paciente, horario);
            consultas.add(consulta);
            JOptionPane.showMessageDialog(null, "Consulta agendada com sucesso!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao agendar consulta. Verifique os dados informados.");
        }
    }

    public void cancelarConsulta() {
        try {
            String crm = JOptionPane.showInputDialog("Informe o CRM do Médico:");
            Medico medico = buscarMedico(crm);
            if (medico == null) {
                JOptionPane.showMessageDialog(null, "Médico não encontrado.");
                return;
            }

            String cpf = JOptionPane.showInputDialog("Informe o CPF do Paciente:");
            Paciente paciente = buscarPaciente(cpf);
            if (paciente == null) {
                JOptionPane.showMessageDialog(null, "Paciente não encontrado.");
                return;
            }

            int dia = Integer.parseInt(JOptionPane.showInputDialog("Dia da Consulta a cancelar (ex: 16):"));
            int mes = Integer.parseInt(JOptionPane.showInputDialog("Mês da Consulta a cancelar (ex: 6):"));
            int ano = Integer.parseInt(JOptionPane.showInputDialog("Ano da Consulta a cancelar (ex: 2025):"));
            int hora = Integer.parseInt(JOptionPane.showInputDialog("Hora da Consulta a cancelar (0-23):"));
            int minuto = Integer.parseInt(JOptionPane.showInputDialog("Minuto da Consulta a cancelar (0-59):"));

            LocalDateTime horario = LocalDateTime.of(ano, mes, dia, hora, minuto);

            Optional<Consulta> consultaOpt = consultas.stream()
                    .filter(c -> c.getMedico().equals(medico) &&
                            c.getPaciente().equals(paciente) &&
                            c.getDataHora().equals(horario))
                    .findFirst();

            if (consultaOpt.isPresent()) {
                consultas.remove(consultaOpt.get());
                JOptionPane.showMessageDialog(null, "Consulta cancelada com sucesso!");
            } else {
                JOptionPane.showMessageDialog(null, "Consulta não encontrada.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao cancelar consulta. Verifique os dados informados.");
        }
    }
// ------- LISTAGEM DE CONSULTAS -------
    public void listarConsultasPorMedico() {
        String crm = JOptionPane.showInputDialog("Informe o CRM do Médico:");
        Medico medico = buscarMedico(crm);
        if (medico == null) {
            JOptionPane.showMessageDialog(null, "Médico não encontrado.");
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Consultas do médico ").append(medico.getNome())
          .append(" (").append(medico.getEspecialidade()).append("):\n");

        consultas.stream()
                .filter(c -> c.getMedico().equals(medico))
                .sorted(Comparator.comparing(Consulta::getDataHora))
                .forEach(c -> sb.append("- ")
                        .append(c.getDataHora().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")))
                        .append(" com paciente: ").append(c.getPaciente().getNome())
                        .append("\n")
                );

        if (sb.toString().endsWith("):\n")) {
            sb.append("Nenhuma consulta encontrada.");
        }

        JOptionPane.showMessageDialog(null, sb.toString());
    }
// ------- HISTÓRICO -------
    public void historicoPorPaciente() {
        String cpf = JOptionPane.showInputDialog("Informe o CPF do Paciente:");
        Paciente paciente = buscarPaciente(cpf);
        if (paciente == null) {
            JOptionPane.showMessageDialog(null, "Paciente não encontrado.");
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Histórico de consultas do paciente ").append(paciente.getNome()).append(":\n");

        consultas.stream()
                .filter(c -> c.getPaciente().equals(paciente))
                .sorted(Comparator.comparing(Consulta::getDataHora))
                .forEach(c -> sb.append("- ")
                        .append(c.getDataHora().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")))
                        .append(" com médico: ").append(c.getMedico().getNome())
                        .append(" (").append(c.getMedico().getEspecialidade()).append(")")
                        .append("\n")
                );

        if (sb.toString().endsWith(":\n")) {
            sb.append("Nenhuma consulta encontrada.");
        }

        JOptionPane.showMessageDialog(null, sb.toString());
    }

    private Medico buscarMedico(String crm) {
        if (crm == null) return null;
        return medicos.stream()
                .filter(m -> m.getCrm().equalsIgnoreCase(crm.trim()))
                .findFirst()
                .orElse(null);
    }

    private Paciente buscarPaciente(String cpf) {
        if (cpf == null) return null;
        return pacientes.stream()
                .filter(p -> p.getCpf().equalsIgnoreCase(cpf.trim()))
                .findFirst()
                .orElse(null);
    }
}

// Classes modelo (Medico, Paciente, Consulta)

class Pessoa {
    private String nome;

    public Pessoa(String nome) {
        this.nome = nome;
    }
    public String getNome() { return nome; }
}

class Medico extends Pessoa {
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Medico)) return false;
        Medico medico = (Medico) o;
        return crm.equalsIgnoreCase(medico.crm);
    }

    @Override
    public int hashCode() {
        return crm.toLowerCase().hashCode();
    }
}

class Paciente extends Pessoa {
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Paciente)) return false;
        Paciente paciente = (Paciente) o;
        return cpf.equalsIgnoreCase(paciente.cpf);
    }

    @Override
    public int hashCode() {
        return cpf.toLowerCase().hashCode();
    }
}

class Consulta {
    private Medico medico;
    private Paciente paciente;
    private LocalDateTime dataHora;

    public Consulta(Medico medico, Paciente paciente, LocalDateTime dataHora) {
        this.medico = medico;
        this.paciente = paciente;
        this.dataHora = dataHora;
    }

    public Medico getMedico() { return medico; }
    public Paciente getPaciente() { return paciente; }
    public LocalDateTime getDataHora() { return dataHora; }
}
