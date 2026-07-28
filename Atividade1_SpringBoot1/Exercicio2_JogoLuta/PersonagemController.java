package Atividade1_SpringBoot1.Exercicio2_JogoLuta;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/personagens")
public class PersonagemController {

    private final String ARQUIVO_CSV = "C:\\mortalkombat\\personagens_db.csv.txt";


    @GetMapping("/todos")
    public List<Personagem> listarTodos() {
        List<Personagem> personagens = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(ARQUIVO_CSV))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] dados = linha.split(";");

                if (dados.length >= 3) {
                    String tipo = dados[0];
                    String nome = dados[1];
                    int poder = Integer.parseInt(dados[2]);


                    if (tipo.toUpperCase().startsWith("L")) {
                        personagens.add(new LutadorCorpoACorpo(nome, poder));
                    } else if (tipo.toUpperCase().startsWith("A")) {
                        personagens.add(new Atirador(nome, poder));
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao ler o arquivo: " + e.getMessage());
        }

        return personagens;
    }


    @GetMapping("/categoria/{tipo}")
    public Object listarPorCategoria(@PathVariable String tipo) {
        List<Personagem> personagensFiltrados = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(ARQUIVO_CSV))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] dados = linha.split(";");

                if (dados.length >= 3) {
                    String tipoCsv = dados[0];
                    String nome = dados[1];
                    int poder = Integer.parseInt(dados[2]);

                    Personagem p = null;


                    if (tipoCsv.toUpperCase().startsWith("L")) {
                        p = new LutadorCorpoACorpo(nome, poder);
                    } else if (tipoCsv.toUpperCase().startsWith("A")) {
                        p = new Atirador(nome, poder);
                    }


                    if (p != null) {
                        if (tipo.equalsIgnoreCase("lutador") && p instanceof LutadorCorpoACorpo) {
                            personagensFiltrados.add(p);
                        } else if (tipo.equalsIgnoreCase("atirador") && p instanceof Atirador) {
                            personagensFiltrados.add(p);
                        }
                    }
                }
            }
            return personagensFiltrados;

        } catch (IOException e) {

            return "Erro: O arquivo " + ARQUIVO_CSV + " não foi encontrado no servidor.";
        }
    }
}



