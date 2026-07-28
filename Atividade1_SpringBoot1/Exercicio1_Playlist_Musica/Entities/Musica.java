package Atividade1_SpringBoot1.Exercicio1_Playlist_Musica.Entities;

public class Musica {

    private String titulo;
    private String artista;
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