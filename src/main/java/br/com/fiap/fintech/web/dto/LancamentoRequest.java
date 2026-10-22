package br.com.fiap.fintech.web.dto;

import br.com.fiap.fintech.service.DadosLancamento;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;

/** Entrada de criação/atualização de receita ou gasto. data no formato ISO (yyyy-MM-dd). */
public record LancamentoRequest(
        @NotBlank(message = "é obrigatório") @Size(max = 120, message = "deve ter no máximo 120 caracteres")
        String nome,
        @NotBlank(message = "é obrigatória") @Size(max = 500, message = "deve ter no máximo 500 caracteres")
        String descricao,
        @NotNull(message = "é obrigatório") @Positive(message = "deve ser maior que zero")
        @Digits(integer = 13, fraction = 2, message = "deve ter no máximo 13 dígitos inteiros e 2 decimais")
        BigDecimal valor,
        @NotNull(message = "é obrigatória")
        LocalDate data) {

    public DadosLancamento paraDados() {
        return new DadosLancamento(nome, descricao, valor, data);
    }
}
