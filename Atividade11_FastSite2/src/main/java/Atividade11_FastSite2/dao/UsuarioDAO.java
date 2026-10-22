package Atividade11_FastSite2.dao;

import Atividade11_FastSite2.Entites.Usuario;
import Atividade11_FastSite2.db.Conexao;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioDAO {

    public Usuario buscarPorLogin(String login) throws SQLException {
        String sql = "SELECT id, nome, login, senha FROM usuarios WHERE login = ?";
        try (Connection conexao = Conexao.conectar();
             PreparedStatement statement = conexao.prepareStatement(sql)) {
            statement.setString(1, login);
            try (ResultSet resultado = statement.executeQuery()) {
                if (resultado.next()) {
                    return new Usuario(resultado.getInt("id"), resultado.getString("nome"),
                            resultado.getString("login"), resultado.getString("senha"));
                }
            }
        }
        return null;
    }

    public boolean autenticar(String login, String senhaDigitada) throws SQLException {
        Usuario usuario = buscarPorLogin(login);
        if (usuario == null || usuario.getSenha() == null) return false;
        try {
            return BCrypt.checkpw(senhaDigitada, usuario.getSenha());
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public boolean cadastrar(Usuario usuario) throws SQLException {
        String sql = "INSERT INTO usuarios (nome, login, senha) VALUES (?, ?, ?)";
        String hash = BCrypt.hashpw(usuario.getSenha(), BCrypt.gensalt());
        try (Connection conexao = Conexao.conectar();
             PreparedStatement statement = conexao.prepareStatement(sql)) {
            statement.setString(1, usuario.getNome());
            statement.setString(2, usuario.getLogin());
            statement.setString(3, hash);
            return statement.executeUpdate() == 1;
        }
    }
}
