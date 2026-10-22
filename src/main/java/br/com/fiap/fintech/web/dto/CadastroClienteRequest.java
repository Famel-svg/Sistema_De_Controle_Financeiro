package br.com.fiap.fintech.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CadastroClienteRequest(
        @NotBlank(message = "é obrigatório") @Size(max = 120, message = "deve ter no máximo 120 caracteres")
        String nome,
        @NotBlank(message = "é obrigatório") @Email(message = "deve ser um e-mail válido")
        @Size(max = 254, message = "deve ter no máximo 254 caracteres")
        String email,
        // BCrypt só considera os primeiros 72 bytes da senha.
        @NotBlank(message = "é obrigatória") @Size(min = 8, max = 72, message = "deve ter entre 8 e 72 caracteres")
        String senha) {

    @Override
    public String toString() {
        return "CadastroClienteRequest[nome=" + nome + ", email=" + email + ", senha=***]";
    }
}
