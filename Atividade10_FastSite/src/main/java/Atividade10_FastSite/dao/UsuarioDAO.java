package Atividade9_FastSite.dao;
import Atividade9_FastSite.Entites.Usuario;
import Atividade9_FastSite.db.Conexao;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioDAO {

    public Usuario buscarPorLogin(String login) throws SQLException {

        String sql = "SELECT * FROM usuarios WHERE login = ?";

        try (Connection conexao = Conexao.conectar();
             PreparedStatement statement = conexao.prepareStatement(sql)) {

            statement.setString(1, login);

            try (ResultSet resultado = statement.executeQuery()) {

                if (resultado.next()) {
                    return new Usuario(
                            resultado.getInt("id"),
                            resultado.getString("login"),
                            resultado.getString("senha")
                    );
                }
            }
        }

        return null;
    }

    public boolean autenticar(String login, String senhaDigitada)
            throws SQLException {

        Usuario usuario = buscarPorLogin(login);

        if (usuario == null) {
            return false;
        }

        return BCrypt.checkpw(senhaDigitada, usuario.getSenha());
    }
}