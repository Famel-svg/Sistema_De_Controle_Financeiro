package br.com.fiap.fintech.teste;

import br.com.fiap.fintech.dao.ClienteDAO;
import br.com.fiap.fintech.model.Cliente;

public class MainTeste {
    public static void main(String[] args) {
        Cliente cliente = new Cliente();
        cliente.setNome("TesteDaSilva");
        cliente.setEmail("teste@email.com");
        cliente.setSenha("12345678");

        ClienteDAO dao = new ClienteDAO();
        dao.cadastrar(cliente);
    }
}
