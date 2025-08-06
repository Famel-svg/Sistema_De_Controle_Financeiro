package br.com.fiap.fintech.dao;

import br.com.fiap.fintech.model.Gasto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GastoDAO {
    public void inserir(Gasto gasto) {
        String sql = "INSERT INTO T_GASTOS (ID_CLIENTE, NOMEDOGASTO, VALOR, DESCRICAO, DATA) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, new String[]{"ID_GASTO"})) {

            stmt.setInt(1, gasto.getIdCliente());
            stmt.setString(2, gasto.getNomeDoGasto());
            stmt.setDouble(3, gasto.getValor());
            stmt.setString(4, gasto.getDescricao());
            stmt.setDate(5, new Date(gasto.getData().getTime()));
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) gasto.setIdGasto(rs.getInt(1));
            }
            System.out.println("👍 Gasto cadastrado com sucesso!");
        } catch (SQLException e) {
            System.err.println("❌ Erro ao cadastrar gasto:");
            e.printStackTrace();
        }
    }

    public Gasto buscarPorId(int idGasto) {
        String sql = "SELECT * FROM T_GASTOS WHERE ID_GASTO = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idGasto);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return new Gasto(
                        rs.getInt("ID_GASTO"),
                        rs.getInt("ID_CLIENTE"),
                        rs.getString("NOMEDOGASTO"),
                        rs.getDouble("VALOR"),
                        rs.getString("DESCRICAO"),
                        rs.getDate("DATA")
                );
            }
        } catch (SQLException e) {
            System.err.println("❌ Erro ao buscar gasto por ID:");
            e.printStackTrace();
        }
        return null;
    }

    public List<Gasto> listarPorCliente(int idCliente) {
        List<Gasto> lista = new ArrayList<>();
        String sql = "SELECT * FROM T_GASTOS WHERE ID_CLIENTE = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idCliente);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) lista.add(new Gasto(
                        rs.getInt("ID_GASTO"),
                        rs.getInt("ID_CLIENTE"),
                        rs.getString("NOMEDOGASTO"),
                        rs.getDouble("VALOR"),
                        rs.getString("DESCRICAO"),
                        rs.getDate("DATA")
                ));
            }
        } catch (SQLException e) {
            System.err.println("❌ Erro ao listar gastos por cliente:");
            e.printStackTrace();
        }
        return lista;
    }

    public void atualizar(Gasto gasto) {
        String sql = "UPDATE T_GASTOS SET NOMEDOGASTO = ?, VALOR = ?, DESCRICAO = ?, DATA = ? WHERE ID_GASTO = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, gasto.getNomeDoGasto());
            stmt.setDouble(2, gasto.getValor());
            stmt.setString(3, gasto.getDescricao());
            stmt.setDate(4, new Date(gasto.getData().getTime()));
            stmt.setInt(5, gasto.getIdGasto());
            stmt.executeUpdate();
            System.out.println("✅ Gasto atualizado com sucesso!");
        } catch (SQLException e) {
            System.err.println("❌ Erro ao atualizar gasto:");
            e.printStackTrace();
        }
    }

    public void deletar(int idGasto) {
        String sql = "DELETE FROM T_GASTOS WHERE ID_GASTO = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idGasto);
            stmt.executeUpdate();
            System.out.println("🗑️ Gasto deletado com sucesso!");
        } catch (SQLException e) {
            System.err.println("❌ Erro ao deletar gasto:");
            e.printStackTrace();
        }
    }

    public double calcularTotalGastos(int idCliente) {
        double total = 0.0;
        String sql = "SELECT SUM(VALOR) FROM T_GASTOS WHERE ID_CLIENTE = ?";

        try (Connection conexao = ConnectionFactory.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, idCliente);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) total = rs.getDouble(1);
            }
        } catch (SQLException e) {
            System.err.println("❌ Erro ao calcular total de gastos:");
            e.printStackTrace();
        }
        return total;
    }
}