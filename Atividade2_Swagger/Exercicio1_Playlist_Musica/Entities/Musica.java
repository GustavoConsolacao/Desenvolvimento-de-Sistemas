package Atividade2_Swagger.Exercicio1_Playlist_Musica.Entities;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Representa uma música da playlist")
public class Musica {

    @Schema(description = "Título da música", example = "Numb")
    private String titulo;

    @Schema(description = "Nome do artista", example = "Linkin Park")
    private String artista;

    @Schema(description = "Ano de lançamento", example = "2003")
    private int anoLancamento;

    public Musica() {
    }

    public Musica(String titulo, String artista, int anoLancamento) {
        this.titulo = titulo;
        this.artista = artista;
        this.anoLancamento = anoLancamento;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getArtista() {
        return artista;
    }

    public void setArtista(String artista) {
        this.artista = artista;
    }

    public int getAnoLancamento() {
        return anoLancamento;
    }

    public void setAnoLancamento(int anoLancamento) {
        this.anoLancamento = anoLancamento;
    }

    @Override
    public String toString() {
        return "Título: " + titulo + " | Artista: " + artista + " | Ano: " + anoLancamento;
    }
}