package br.com.fiap.fintech.teste;

import br.com.fiap.fintech.dao.ClienteDAO;
import br.com.fiap.fintech.model.Cliente;

import java.util.Scanner;

public class TesteLoginCliente {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== TESTE DE LOGIN DO CLIENTE ===");
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
        }

        scanner.close();
    }
}
