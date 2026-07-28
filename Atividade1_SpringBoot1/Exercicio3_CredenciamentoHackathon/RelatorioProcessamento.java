package Atividade1_SpringBoot1.Exercicio3_CredenciamentoHackathon;

public class RelatorioProcessamento {
    private int quantidadeAprovados;
    private int quantidadeRejeitados;
    private String status;

    public RelatorioProcessamento(int aprovados, int rejeitados, String status) {
        this.quantidadeAprovados = aprovados;
        this.quantidadeRejeitados = rejeitados;
        this.status = status;
    }
    public int getQuantidadeAprovados() { return quantidadeAprovados; }
    public int getQuantidadeRejeitados() { return quantidadeRejeitados; }
    public String getStatus() { return status; }
}
