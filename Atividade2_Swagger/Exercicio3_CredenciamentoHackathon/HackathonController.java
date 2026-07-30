package Atividade2_Swagger.Exercicio3_CredenciamentoHackathon;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;

@RestController
@RequestMapping("/hackathon")
@Tag(name = "Portal do Hackathon", description = "Painel de controle para a organização do evento processar as inscrições.")
public class HackathonController {

    @PostMapping({"/processar", "/processar"})
    @Operation(
            summary = "Processar Lote de Inscrições",
            description = "Abre o arquivo local 'inscricoes_brutas.txt', valida a regra de idade (14 a 21 anos) e gera os arquivos 'aprovados_hackathon.txt' e 'pendencias_inscricao.txt'. Retorna um relatório JSON em tempo real com o resumo."
    )
    public RelatorioProcessamento processarInscricoes() {
        int aprovados = 0;
        int rejeitados = 0;

        try (BufferedReader br = new BufferedReader(new FileReader("inscricoes_brutas.txt"));
             BufferedWriter bwAprovados = new BufferedWriter(new FileWriter("aprovados_hackathon.txt"));
             BufferedWriter bwPendencias = new BufferedWriter(new FileWriter("pendencias_inscricao.txt"))) {

            String linha;
            while ((linha = br.readLine()) != null) {
                String[] dados = linha.split(";");
                if (dados.length >= 2) {
                    Participante p = new Participante();
                    p.setNome(dados[0]);
                    p.setIdade(Integer.parseInt(dados[1])); // Aplica a regra de 14 a 21 anos

                    if (p.isValido()) {
                        bwAprovados.write(linha);
                        bwAprovados.newLine();
                        aprovados++;
                    } else {
                        bwPendencias.write(linha + " - ERRO: Idade inválida");
                        bwPendencias.newLine();
                        rejeitados++;
                    }
                }
            }
            return new RelatorioProcessamento(aprovados, rejeitados, "Processamento concluído com sucesso!");

        } catch (IOException e) {
            return new RelatorioProcessamento(0, 0, "Erro ao ler ficheiro: " + e.getMessage());
        }
    }
    }
