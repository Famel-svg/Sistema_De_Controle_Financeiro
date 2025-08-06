package br.com.fiap.fintech.teste;

import java.sql.Connection;
import java.sql.SQLException;
import br.com.fiap.fintech.dao.ConnectionFactory;

public class TesteConexao {
    public static void main(String[] args) {
        try (Connection conexao = ConnectionFactory.getConnection()) {
            if (conexao != null) {
                System.out.println("✅ Conexão com o banco estabelecida com sucesso!");
            } else {
                System.out.println("❌ Falha ao conectar com o banco de dados.");
            }
        } catch (SQLException e) {
            System.out.println("❌ Erro ao conectar com o banco de dados:");
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("❌ Erro inesperado:");
            e.printStackTrace();
        }
    }
}
