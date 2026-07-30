package Atividade2_Swagger.Exercicio3_CredenciamentoHackathon;

public class Participante {
    protected String nome;
    protected int idade;
    protected boolean valido = true;

    public void setIdade(int idade) {
        if (idade >= 14 && idade <= 21) {
            this.idade = idade;
        } else {
            this.valido = false;
        }
    }
    public boolean isValido() { return valido; }
    public String getNome() { return nome; }

    public void setNome(String dado) {
    }
}
