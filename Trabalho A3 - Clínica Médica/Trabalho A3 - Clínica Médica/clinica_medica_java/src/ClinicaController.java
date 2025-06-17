package src;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ClinicaController {
    List<Medico> medicos = new ArrayList<>();
    List<Paciente> pacientes = new ArrayList<>();
    List<Consulta> consultas = new ArrayList<>();

    // Cadastrar médico
    public void cadastrarMedico(String nome, String crm, String especialidade) {
        medicos.add(new Medico(nome, crm, especialidade));
    }

    // Cadastrar paciente
    public void cadastrarPaciente(String nome, String cpf, LocalDate dataNascimento) {
        pacientes.add(new Paciente(nome, cpf, dataNascimento));
    }

    // Agendar consulta
    public String agendarConsulta(Medico medico, Paciente paciente, LocalDateTime dataHora) {
        if (dataHora.isBefore(LocalDateTime.now())) {
            return "Erro: A data da consulta não pode estar no passado.";
        }

        long consultasNoDia = consultas.stream()
                .filter(c -> c.getMedico().equals(medico) && c.getDataHora().toLocalDate().equals(dataHora.toLocalDate()))
                .count();

        if (consultasNoDia >= 10) {
            return "Erro: Este médico já possui 10 consultas neste dia.";
        }

        boolean pacienteJaAgendado = consultas.stream()
                .anyMatch(c -> c.getMedico().equals(medico) &&
                        c.getPaciente().equals(paciente) &&
                        c.getDataHora().toLocalDate().equals(dataHora.toLocalDate()));

        if (pacienteJaAgendado) {
            return "Erro: Este paciente já tem uma consulta com este médico neste dia.";
        }

        boolean conflito = consultas.stream()
                .anyMatch(c -> c.getMedico().equals(medico) &&
                        c.getDataHora().equals(dataHora));

        if (conflito) {
            return "Erro: Já existe uma consulta marcada neste horário para este médico.";
        }

        consultas.add(new Consulta(medico, paciente, dataHora));
        return "Consulta agendada com sucesso.";
    }

    // Cancelar consulta
    public void cancelarConsulta(Consulta consulta) {
        consultas.remove(consulta);
    }

    // Listar consultas por médico
    public List<Consulta> listarConsultasPorMedico(Medico medico) {
        return consultas.stream()
                .filter(c -> c.getMedico().equals(medico))
                .collect(Collectors.toList());
    }

    // Histórico de consultas por paciente
    public List<Consulta> historicoPorPaciente(Paciente paciente) {
        return consultas.stream()
                .filter(c -> c.getPaciente().equals(paciente))
                .collect(Collectors.toList());
    }
}
