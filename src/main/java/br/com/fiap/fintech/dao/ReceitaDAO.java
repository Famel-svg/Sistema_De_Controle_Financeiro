package br.com.fiap.fintech.dao;

import br.com.fiap.fintech.model.Receita;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReceitaDAO {
    public void inserir(Receita receita) {
        String sql = "INSERT INTO T_RECEITA (ID_CLIENTE, NOMEDARECEITA, VALOR, DESCRICAO, DATA) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, new String[]{"ID_RECEITA"})) {

            stmt.setInt(1, receita.getIdCliente());
            stmt.setString(2, receita.getNomeDaReceita());
            stmt.setDouble(3, receita.getValor());
            stmt.setString(4, receita.getDescricao());
            stmt.setDate(5, new Date(receita.getData().getTime()));
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) receita.setIdReceita(rs.getInt(1));
            }
            System.out.println("💰 Receita cadastrada com sucesso!");
        } catch (SQLException e) {
            System.err.println("❌ Erro ao cadastrar receita:");
            e.printStackTrace();
        }
    }

    public Receita buscarPorId(int idReceita) {
        String sql = "SELECT * FROM T_RECEITA WHERE ID_RECEITA = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idReceita);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return new Receita(
                        rs.getInt("ID_RECEITA"),
                        rs.getInt("ID_CLIENTE"),
                        rs.getString("NOMEDARECEITA"),
                        rs.getDouble("VALOR"),
                        rs.getString("DESCRICAO"),
                        rs.getDate("DATA")
                );
            }
        } catch (SQLException e) {
            System.err.println("❌ Erro ao buscar receita por ID:");
            e.printStackTrace();
        }
        return null;
    }

    public List<Receita> listarPorCliente(int idCliente) {
        List<Receita> lista = new ArrayList<>();
        String sql = "SELECT * FROM T_RECEITA WHERE ID_CLIENTE = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idCliente);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) lista.add(new Receita(
                        rs.getInt("ID_RECEITA"),
                        rs.getInt("ID_CLIENTE"),
                        rs.getString("NOMEDARECEITA"),
                        rs.getDouble("VALOR"),
                        rs.getString("DESCRICAO"),
                        rs.getDate("DATA")
                ));
            }
        } catch (SQLException e) {
            System.err.println("❌ Erro ao listar receitas por cliente:");
            e.printStackTrace();
        }
        return lista;
    }

    public void atualizar(Receita receita) {
        String sql = "UPDATE T_RECEITA SET NOMEDARECEITA = ?, VALOR = ?, DESCRICAO = ?, DATA = ? WHERE ID_RECEITA = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, receita.getNomeDaReceita());
            stmt.setDouble(2, receita.getValor());
            stmt.setString(3, receita.getDescricao());
            stmt.setDate(4, new Date(receita.getData().getTime()));
            stmt.setInt(5, receita.getIdReceita());
            stmt.executeUpdate();
            System.out.println("✏️ Receita atualizada com sucesso!");
        } catch (SQLException e) {
            System.err.println("❌ Erro ao atualizar receita:");
            e.printStackTrace();
        }
    }

    public void deletar(int idReceita) {
        String sql = "DELETE FROM T_RECEITA WHERE ID_RECEITA = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idReceita);
            stmt.executeUpdate();
            System.out.println("🗑️ Receita deletada com sucesso!");
        } catch (SQLException e) {
            System.err.println("❌ Erro ao deletar receita:");
            e.printStackTrace();
        }
    }

    public double calcularTotalReceitas(int idCliente) {
        double total = 0;
        String sql = "SELECT SUM(VALOR) FROM T_RECEITA WHERE ID_CLIENTE = ?";

        try (Connection conexao = ConnectionFactory.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, idCliente);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) total = rs.getDouble(1);
            }
        } catch (SQLException e) {
            System.err.println("❌ Erro ao calcular total de receitas:");
            e.printStackTrace();
        }
        return total;
    }
}