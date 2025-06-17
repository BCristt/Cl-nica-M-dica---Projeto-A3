package src;
import java.time.LocalDate;

public class Paciente extends Pessoa {
    private String cpf;
    private LocalDate dataNascimento;

    public Paciente(String nome, String cpf, LocalDate dataNascimento) {
        super(nome);
        this.cpf = cpf;
        this.dataNascimento = dataNascimento;
    }

    public String getCpf() {
        return cpf;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    @Override
    public String toString() {
        return getNome() + " (CPF: " + cpf + ")";
    }
}
