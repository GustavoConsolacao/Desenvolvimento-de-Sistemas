package Atividade2_Swagger.Exercicio3_CredenciamentoHackathon;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Relatório gerado após o processamento em lote das inscrições do Hackathon.")
public class RelatorioProcessamento {

    @Schema(description = "Quantidade de inscrições aprovadas e salvas no arquivo aprovados_hackathon.txt", example = "42")
    private int quantidadeAprovados;

    @Schema(description = "Quantidade de inscrições com dados inválidos ou idade fora da regra (14 a 21 anos) salvas em pendencias_inscricao.txt", example = "5")
    private int quantidadeRejeitados;

    @Schema(description = "Mensagem informando o resultado da operação", example = "Processamento concluído com sucesso!")
    private String status;

    public RelatorioProcessamento(int quantidadeAprovados, int quantidadeRejeitados, String status) {
        this.quantidadeAprovados = quantidadeAprovados;
        this.quantidadeRejeitados = quantidadeRejeitados;
        this.status = status;
    }

    public int getQuantidadeAprovados() { return quantidadeAprovados; }
    public int getQuantidadeRejeitados() { return quantidadeRejeitados; }
    public String getStatus() { return status; }
}
