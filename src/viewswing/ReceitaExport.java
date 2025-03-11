package viewswing;

public class ReceitaExport {
    private final String paciente;
    private final String cpf;
    private final String medicamentos;
    private final String dataPrescricao;
    private final String status;

    public ReceitaExport(String paciente, String cpf, String medicamentos, String dataPrescricao, String status) {
        this.paciente = paciente;
        this.cpf = cpf;
        this.medicamentos = medicamentos;
        this.dataPrescricao = dataPrescricao;
        this.status = status;
    }

    public String getPaciente() {
        return paciente;
    }

    public String getCpf() {
        return cpf;
    }

    public String getMedicamentos() {
        return medicamentos;
    }

    public String getDataPrescricao() {
        return dataPrescricao;
    }

    public String getStatus() {
        return status;
    }
}
