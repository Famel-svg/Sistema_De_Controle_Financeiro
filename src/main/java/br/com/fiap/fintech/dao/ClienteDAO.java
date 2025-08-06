package br.com.fiap.fintech.dao;

import br.com.fiap.fintech.model.Cliente;
import java.sql.*;

public class ClienteDAO {
    public void cadastrar(Cliente cliente) {
        String sql = "INSERT INTO T_CLIENTE (EMAIL, NOME, SENHA) VALUES (?, ?, ?)";

        try (Connection conexao = ConnectionFactory.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setString(1, cliente.getEmail());
            stmt.setString(2, cliente.getNome());
            stmt.setString(3, cliente.getSenha());
            stmt.executeUpdate();
            System.out.println("👍 Cliente inserido.");
        } catch (SQLException e) {
            System.err.println("❌ Erro ao cadastrar cliente:");
            e.printStackTrace();
        }
    }

    public Cliente buscarPorEmail(String email) {
        Cliente cliente = null;
        String sql = "SELECT * FROM T_CLIENTE WHERE EMAIL = ?";

        try (Connection conexao = ConnectionFactory.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    cliente = new Cliente();
                    cliente.setId(rs.getInt("ID"));
                    cliente.setNome(rs.getString("NOME"));
                    cliente.setEmail(rs.getString("EMAIL"));
                    cliente.setSenha(rs.getString("SENHA"));
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Erro ao buscar cliente por email:");
            e.printStackTrace();
        }
        return cliente;
    }
}