package br.com.fiap.fintech.repository;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.fiap.fintech.domain.Cliente;
import br.com.fiap.fintech.domain.Lancamento;
import br.com.fiap.fintech.domain.TipoLancamento;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

/** Usa o H2 em modo PostgreSQL do perfil test, com o esquema criado pela migration Flyway. */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class LancamentoRepositoryTest {

    @Autowired
    private LancamentoRepository lancamentoRepository;
    @Autowired
    private ClienteRepository clienteRepository;

    private Cliente ana;
    private Cliente bruno;

    @BeforeEach
    void setUp() {
        ana = clienteRepository.save(new Cliente("Ana", "ana-repo@example.com", "hash"));
        bruno = clienteRepository.save(new Cliente("Bruno", "bruno-repo@example.com", "hash"));

        salvar(ana, TipoLancamento.GASTO, "10.00", LocalDate.of(2025, 12, 31));
        salvar(ana, TipoLancamento.GASTO, "20.00", LocalDate.of(2026, 1, 1));
        salvar(ana, TipoLancamento.GASTO, "30.00", LocalDate.of(2026, 1, 15));
        salvar(ana, TipoLancamento.GASTO, "40.00", LocalDate.of(2026, 1, 31));
        salvar(ana, TipoLancamento.GASTO, "50.00", LocalDate.of(2026, 2, 1));
        salvar(ana, TipoLancamento.RECEITA, "1000.00", LocalDate.of(2026, 1, 10));
        salvar(bruno, TipoLancamento.GASTO, "999.00", LocalDate.of(2026, 1, 15));
    }

    @Test
    void filtroDePeriodoIncluiOsLimitesESoTrazOClienteEOTipo() {
        Page<Lancamento> pagina = lancamentoRepository.findByClienteIdAndTipoAndDataBetween(
                ana.getId(), TipoLancamento.GASTO, LocalDate.of(2026, 1, 1), LocalDate.of(2026, 1, 31),
                PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "data")));

        assertThat(pagina.getTotalElements()).isEqualTo(3);
        assertThat(pagina.getContent()).extracting(Lancamento::getValor)
                .containsExactly(new BigDecimal("40.00"), new BigDecimal("30.00"), new BigDecimal("20.00"));
    }

    @Test
    void paginacaoRespeitaTamanhoETotal() {
        Page<Lancamento> pagina = lancamentoRepository.findByClienteIdAndTipoAndDataBetween(
                ana.getId(), TipoLancamento.GASTO, LocalDate.of(2000, 1, 1), LocalDate.of(2100, 1, 1),
                PageRequest.of(1, 2, Sort.by("data")));

        assertThat(pagina.getTotalElements()).isEqualTo(5);
        assertThat(pagina.getTotalPages()).isEqualTo(3);
        assertThat(pagina.getContent()).extracting(Lancamento::getData)
                .containsExactly(LocalDate.of(2026, 1, 15), LocalDate.of(2026, 1, 31));
    }

    @Test
    void somaPorTipoNoPeriodo() {
        LocalDate inicio = LocalDate.of(2026, 1, 1);
        LocalDate fim = LocalDate.of(2026, 1, 31);

        assertThat(lancamentoRepository.somarValores(ana.getId(), TipoLancamento.GASTO, inicio, fim))
                .isEqualByComparingTo("90.00");
        assertThat(lancamentoRepository.somarValores(ana.getId(), TipoLancamento.RECEITA, inicio, fim))
                .isEqualByComparingTo("1000.00");
        assertThat(lancamentoRepository.somarValores(ana.getId(), TipoLancamento.RECEITA,
                LocalDate.of(2030, 1, 1), LocalDate.of(2030, 12, 31)))
                .isEqualByComparingTo("0");
    }

    @Test
    void buscaPorIdRespeitaDonoETipo() {
        Lancamento doBruno = lancamentoRepository.findByClienteIdAndTipoAndDataBetween(bruno.getId(),
                TipoLancamento.GASTO, LocalDate.of(2000, 1, 1), LocalDate.of(2100, 1, 1), PageRequest.of(0, 1))
                .getContent().getFirst();

        assertThat(lancamentoRepository.findByIdAndClienteIdAndTipo(doBruno.getId(), bruno.getId(), TipoLancamento.GASTO))
                .isPresent();
        assertThat(lancamentoRepository.findByIdAndClienteIdAndTipo(doBruno.getId(), ana.getId(), TipoLancamento.GASTO))
                .isEmpty();
        assertThat(lancamentoRepository.findByIdAndClienteIdAndTipo(doBruno.getId(), bruno.getId(), TipoLancamento.RECEITA))
                .isEmpty();
    }

    private void salvar(Cliente cliente, TipoLancamento tipo, String valor, LocalDate data) {
        lancamentoRepository.save(new Lancamento(cliente, tipo, "item", "descrição", new BigDecimal(valor), data));
    }
}
