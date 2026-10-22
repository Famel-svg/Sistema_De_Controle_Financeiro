package br.com.fiap.fintech.service;

import java.math.BigDecimal;
import java.time.LocalDate;

/** Dados editáveis de um lançamento, independentes do formato de entrada da API. */
public record DadosLancamento(String nome, String descricao, BigDecimal valor, LocalDate data) {
}
