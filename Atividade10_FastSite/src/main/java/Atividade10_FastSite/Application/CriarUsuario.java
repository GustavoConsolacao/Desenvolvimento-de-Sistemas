package Atividade9_FastSite.Application;
import Atividade9_FastSite.db.Conexao;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CriarUsuario {

    public static void main(String[] args) {

        String login = "adm";
        String senha = "12345678";

        String senhaCriptografada = BCrypt.hashpw(
                senha,
                BCrypt.gensalt()
        );

        String sql = "INSERT INTO usuarios (login, senha) VALUES (?, ?)";

        try (Connection conexao = Conexao.conectar();
             PreparedStatement statement = conexao.prepareStatement(sql)) {

            statement.setString(1, login);
            statement.setString(2, senhaCriptografada);

            statement.executeUpdate();

            System.out.println("Usuário criado com sucesso!");

        } catch (SQLException e) {
            System.out.println("Erro ao criar usuário: " + e.getMessage());
        }
    }
}