package src;

public class Medico extends Pessoa {
    private String crm;
    private String especialidade;

    public Medico(String nome, String crm, String especialidade) {
        super(nome);
        this.crm = crm;
        this.especialidade = especialidade;
    }

    public String getCrm() {
        return crm;
    }

    public String getEspecialidade() {
        return especialidade;
    }
}
