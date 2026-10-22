package br.com.fiap.fintech.service;

import java.math.BigDecimal;
import java.time.LocalDate;

/** Totais do período. dataInicio/dataFim nulos significam período aberto naquele lado. */
public record Resumo(LocalDate dataInicio, LocalDate dataFim, BigDecimal totalReceitas,
                     BigDecimal totalGastos, BigDecimal saldo) {
}
