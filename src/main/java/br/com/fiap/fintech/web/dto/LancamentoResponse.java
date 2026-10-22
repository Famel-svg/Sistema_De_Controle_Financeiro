package br.com.fiap.fintech.web.dto;

import br.com.fiap.fintech.domain.Lancamento;
import br.com.fiap.fintech.domain.TipoLancamento;
import java.math.BigDecimal;
import java.time.LocalDate;

public record LancamentoResponse(Long id, TipoLancamento tipo, String nome, String descricao,
                                 BigDecimal valor, LocalDate data) {

    public static LancamentoResponse de(Lancamento l) {
        return new LancamentoResponse(l.getId(), l.getTipo(), l.getNome(), l.getDescricao(), l.getValor(), l.getData());
    }
}
