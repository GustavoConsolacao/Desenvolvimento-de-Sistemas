package Atividade11_FastSite2.dao;
import Atividade11_FastSite2.Entites.Cliente;
import Atividade11_FastSite2.db.Conexao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {
    public boolean salvar(Cliente cliente) throws SQLException {
        String sql = "INSERT INTO clientes (nome, endereco, cep, numero, telefone, observacao) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conexao = Conexao.conectar();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getEndereco());
            stmt.setString(3, cliente.getCep());
            stmt.setString(4, cliente.getNumero());
            stmt.setString(5, cliente.getTelefone());
            stmt.setString(6, cliente.getObservacao());

            return stmt.executeUpdate() > 0;
        }
    }

    public Cliente buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM clientes WHERE id = ?";

        try (Connection conexao = Conexao.conectar();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Cliente(
                            rs.getInt("id"),
                            rs.getString("nome"),
                            rs.getString("endereco"),
                            rs.getString("cep"),
                            rs.getString("numero"),
                            rs.getString("telefone"),
                            rs.getString("observacao")
                    );
                }
            }
        }
        return null;
    }
    public List<Cliente> listarTodos() throws SQLException {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT * FROM clientes";

        try (Connection conexao = Conexao.conectar();
             PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(new Cliente(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("endereco"),
                        rs.getString("cep"),
                        rs.getString("numero"),
                        rs.getString("telefone"),
                        rs.getString("observacao")
                ));
            }
        }
        return lista;
    }
}