package br.com.fiap.fintech.web.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "é obrigatório") String email,
        @NotBlank(message = "é obrigatória") String senha) {

    @Override
    public String toString() {
        return "LoginRequest[email=" + email + ", senha=***]";
    }
}
