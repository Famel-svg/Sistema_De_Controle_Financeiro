package br.com.fiap.fintech.service;

import br.com.fiap.fintech.exception.RegraNegocioException;
import java.time.LocalDate;

/**
 * Intervalo de datas fechado [inicio, fim]. Limites ausentes viram datas extremas aceitas
 * tanto pelo PostgreSQL quanto pelo H2, para a consulta não precisar de parâmetros nulos.
 */
public record Periodo(LocalDate inicio, LocalDate fim) {

    static final LocalDate DATA_MINIMA = LocalDate.of(1, 1, 1);
    static final LocalDate DATA_MAXIMA = LocalDate.of(9999, 12, 31);

    public static Periodo de(LocalDate dataInicio, LocalDate dataFim) {
        if (dataInicio != null && dataFim != null && dataInicio.isAfter(dataFim)) {
            throw new RegraNegocioException("dataInicio deve ser anterior ou igual a dataFim.");
        }
        return new Periodo(dataInicio == null ? DATA_MINIMA : dataInicio,
                dataFim == null ? DATA_MAXIMA : dataFim);
    }
}
