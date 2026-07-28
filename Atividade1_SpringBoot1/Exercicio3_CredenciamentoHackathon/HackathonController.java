package Atividade1_SpringBoot1.Exercicio3_CredenciamentoHackathon;

import org.springframework.web.bind.annotation.*;
import java.io.*;

@RestController
@RequestMapping("/hackathon") // Mapeamento base
public class HackathonController {

    @PostMapping("/processar")
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
                    p.nome = dados[0];
                    p.setIdade(Integer.parseInt(dados[1]));

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
            return new RelatorioProcessamento(aprovados, rejeitados, "Erro ao processar ficheiros: " + e.getMessage());
        }
    }
}