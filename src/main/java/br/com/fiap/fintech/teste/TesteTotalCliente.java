package br.com.fiap.fintech.teste;

import br.com.fiap.fintech.dao.ClienteDAO;
import br.com.fiap.fintech.dao.GastoDAO;
import br.com.fiap.fintech.dao.ReceitaDAO;
import br.com.fiap.fintech.model.Cliente;

import java.util.Scanner;

public class TesteTotalCliente {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== TESTE DE TOTAL DO CLIENTE ===");
        System.out.print("Digite o email: ");
        String email = scanner.nextLine();

        System.out.print("Digite a senha: ");
        String senha = scanner.nextLine();

        ClienteDAO clienteDAO = new ClienteDAO();
        Cliente cliente = clienteDAO.buscarPorEmail(email);

        if (cliente == null) {
            System.out.println("❌ Cliente não encontrado com o email informado.");
        } else if (cliente.getSenha() == null || !cliente.getSenha().equals(senha)) {
            System.out.println("❌ Senha incorreta.");
        } else {
            System.out.println("✅ Login bem-sucedido!");
            System.out.println("🔐 ID do cliente: " + cliente.getId());

            int idCliente = cliente.getId();

            GastoDAO gastoDAO = new GastoDAO();
            ReceitaDAO receitaDAO = new ReceitaDAO();

            double totalGastos = gastoDAO.calcularTotalGastos(idCliente);
            double totalReceitas = receitaDAO.calcularTotalReceitas(idCliente);
            double total = totalReceitas - totalGastos;

            System.out.println("📊 Total de Receitas: R$ " + String.format("%.2f", totalReceitas));
            System.out.println("📉 Total de Gastos:    R$ " + String.format("%.2f", totalGastos));
            System.out.println("💰 Total Final:        R$ " + String.format("%.2f", total));
        }

        scanner.close();
    }
}
