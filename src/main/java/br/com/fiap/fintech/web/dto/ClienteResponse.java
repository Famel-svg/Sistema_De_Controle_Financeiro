package br.com.fiap.fintech.web.dto;

import br.com.fiap.fintech.domain.Cliente;

/** Dados públicos do cliente. O hash da senha nunca sai da API. */
public record ClienteResponse(Long id, String nome, String email) {

    public static ClienteResponse de(Cliente cliente) {
        return new ClienteResponse(cliente.getId(), cliente.getNome(), cliente.getEmail());
    }
}
