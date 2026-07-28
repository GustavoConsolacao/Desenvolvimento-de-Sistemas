package Atividade1_SpringBoot1.Exercicio1_Playlist_Musica.Applications;

import Atividade1_SpringBoot1.Exercicio1_Playlist_Musica.Entities.Musica;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/playlist")
public class PlaylistController {

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