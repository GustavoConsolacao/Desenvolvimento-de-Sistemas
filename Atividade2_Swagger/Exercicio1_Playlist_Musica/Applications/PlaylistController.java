package Atividade2_Swagger.Exercicio1_Playlist_Musica.Applications;

import Atividade2_Swagger.Exercicio1_Playlist_Musica.Entities.Musica;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/playlist")
@Tag(name = "Playlist", description = "Gerenciamento da playlist de músicas")
public class PlaylistController {

    @Operation(
            summary = "Listar músicas",
            description = "Lê o arquivo TXT e retorna todas as músicas cadastradas."
    )
    @GetMapping("/listar")
    public List<Musica> listar() {

        List<Musica> playlist = new ArrayList<>();

        File arquivo = new File("C:\\Playlist_de_Musicas\\minha_playlist.txt");

        try (BufferedReader br = new BufferedReader(new FileReader(arquivo))) {

            String linha;

            while ((linha = br.readLine()) != null) {

                linha = linha.replace("Título: ", "");
                linha = linha.replace("Artista: ", "");
                linha = linha.replace("Ano: ", "");

                String[] partes = linha.split("\\|");

                String titulo = partes[0].trim();
                String artista = partes[1].trim();
                int ano = Integer.parseInt(partes[2].trim());

                Musica musica = new Musica(titulo, artista, ano);

                playlist.add(musica);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return playlist;
    }

    @Operation(
            summary = "Adicionar música",
            description = "Recebe uma música em JSON e grava no arquivo TXT."
    )
    @PostMapping("/adicionar")
    public String adicionar(@RequestBody Musica novaMusica) {

        try {

            File pasta = new File("C:\\Playlist_de_Musicas");

            if (!pasta.exists()) {
                pasta.mkdir();
            }

            File arquivo = new File("C:\\Playlist_de_Musicas\\minha_playlist.txt");

            if (!arquivo.exists()) {
                arquivo.createNewFile();
            }

            BufferedWriter bw = new BufferedWriter(new FileWriter(arquivo, true));

            bw.write(novaMusica.toString());
            bw.newLine();

            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
            return "Erro ao salvar a música.";
        }

        return "Música salva com sucesso!";
    }
}