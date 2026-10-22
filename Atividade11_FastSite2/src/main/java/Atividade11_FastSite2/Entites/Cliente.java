package Atividade11_FastSite2.Entites;

public class Cliente {
    private int id;
    private String nome;
    private String endereco;
    private String cep;
    private String numero;
    private String telefone;
    private String observacao;

    public Cliente() {}

    public Cliente(int id, String nome, String endereco, String cep, String numero, String telefone, String observacao) {
        this.id = id;
        this.nome = nome;
        this.endereco = endereco;
        this.cep = cep;
        this.numero = numero;
        this.telefone = telefone;
        this.observacao = observacao;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }
    public String getCep() { return cep; }
    public void setCep(String cep) { this.cep = cep; }
    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    public String getObservacao() { return observacao; }
    public void setObservacao(String observacao) { this.observacao = observacao; }
}
