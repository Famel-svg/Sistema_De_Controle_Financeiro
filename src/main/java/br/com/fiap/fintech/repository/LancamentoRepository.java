package br.com.fiap.fintech.repository;

import br.com.fiap.fintech.domain.Lancamento;
import br.com.fiap.fintech.domain.TipoLancamento;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long> {

    /** Lista os lançamentos de um tipo do cliente com data entre inicio e fim (inclusive). */
    Page<Lancamento> findByClienteIdAndTipoAndDataBetween(Long clienteId, TipoLancamento tipo,
                                                          LocalDate inicio, LocalDate fim,
                                                          Pageable pageable);

    /** Busca restrita ao dono: lançamento de outro cliente se comporta como inexistente. */
    Optional<Lancamento> findByIdAndClienteIdAndTipo(Long id, Long clienteId, TipoLancamento tipo);

    @Query("""
            select coalesce(sum(l.valor), 0)
              from Lancamento l
             where l.cliente.id = :clienteId
               and l.tipo = :tipo
               and l.data between :inicio and :fim
            """)
    BigDecimal somarValores(@Param("clienteId") Long clienteId,
                            @Param("tipo") TipoLancamento tipo,
                            @Param("inicio") LocalDate inicio,
                            @Param("fim") LocalDate fim);
}
