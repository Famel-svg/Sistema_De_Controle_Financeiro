package br.com.fiap.fintech.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import br.com.fiap.fintech.domain.Cliente;
import br.com.fiap.fintech.domain.Lancamento;
import br.com.fiap.fintech.domain.TipoLancamento;
import br.com.fiap.fintech.exception.RecursoNaoEncontradoException;
import br.com.fiap.fintech.exception.RegraNegocioException;
import br.com.fiap.fintech.repository.ClienteRepository;
import br.com.fiap.fintech.repository.LancamentoRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class LancamentoServiceTest {

    private static final long CLIENTE = 1L;
    private static final LocalDate INICIO = LocalDate.of(2026, 1, 1);
    private static final LocalDate FIM = LocalDate.of(2026, 1, 31);

    @Mock
    private LancamentoRepository lancamentoRepository;
    @Mock
    private ClienteRepository clienteRepository;
    @InjectMocks
    private LancamentoService service;

    @Test
    void saldoEhTotalDeReceitasMenosTotalDeGastos() {
        when(lancamentoRepository.somarValores(CLIENTE, TipoLancamento.RECEITA, INICIO, FIM))
                .thenReturn(new BigDecimal("5000.00"));
        when(lancamentoRepository.somarValores(CLIENTE, TipoLancamento.GASTO, INICIO, FIM))
                .thenReturn(new BigDecimal("1234.56"));

        Resumo resumo = service.resumir(CLIENTE, INICIO, FIM);

        assertThat(resumo.totalReceitas()).isEqualByComparingTo("5000.00");
        assertThat(resumo.totalGastos()).isEqualByComparingTo("1234.56");
        assertThat(resumo.saldo()).isEqualTo(new BigDecimal("3765.44"));
        assertThat(resumo.dataInicio()).isEqualTo(INICIO);
        assertThat(resumo.dataFim()).isEqualTo(FIM);
    }

    @Test
    void saldoPodeSerNegativo() {
        when(lancamentoRepository.somarValores(CLIENTE, TipoLancamento.RECEITA, INICIO, FIM))
                .thenReturn(new BigDecimal("100.00"));
        when(lancamentoRepository.somarValores(CLIENTE, TipoLancamento.GASTO, INICIO, FIM))
                .thenReturn(new BigDecimal("250.10"));

        assertThat(service.resumir(CLIENTE, INICIO, FIM).saldo()).isEqualTo(new BigDecimal("-150.10"));
    }

    @Test
    void periodoSemLancamentosTemSaldoZero() {
        when(lancamentoRepository.somarValores(eq(CLIENTE), any(), any(), any())).thenReturn(null);

        Resumo resumo = service.resumir(CLIENTE, INICIO, FIM);

        assertThat(resumo.totalReceitas()).isEqualTo(new BigDecimal("0.00"));
        assertThat(resumo.totalGastos()).isEqualTo(new BigDecimal("0.00"));
        assertThat(resumo.saldo()).isEqualTo(new BigDecimal("0.00"));
    }

    @Test
    void periodoAbertoUsaLimitesExtremos() {
        when(lancamentoRepository.somarValores(eq(CLIENTE), any(), eq(Periodo.DATA_MINIMA), eq(Periodo.DATA_MAXIMA)))
                .thenReturn(BigDecimal.TEN);

        Resumo resumo = service.resumir(CLIENTE, null, null);

        assertThat(resumo.saldo()).isEqualTo(new BigDecimal("0.00"));
        assertThat(resumo.dataInicio()).isNull();
        assertThat(resumo.dataFim()).isNull();
    }

    @Test
    void dataInicioDepoisDeDataFimEhRejeitada() {
        assertThatThrownBy(() -> service.resumir(CLIENTE, FIM, INICIO))
                .isInstanceOf(RegraNegocioException.class)
                .hasMessageContaining("dataInicio");
        assertThatThrownBy(() -> service.listar(CLIENTE, TipoLancamento.GASTO, FIM, INICIO, Pageable.unpaged()))
                .isInstanceOf(RegraNegocioException.class);
        verifyNoInteractions(lancamentoRepository);
    }

    @Test
    void criarRejeitaValorZeroOuNegativo() {
        for (String valor : new String[] {"0", "0.00", "-10.00"}) {
            DadosLancamento dados = dados(new BigDecimal(valor), "Mercado");
            assertThatThrownBy(() -> service.criar(CLIENTE, TipoLancamento.GASTO, dados))
                    .isInstanceOf(RegraNegocioException.class)
                    .hasMessageContaining("maior que zero");
        }
        verify(lancamentoRepository, never()).save(any());
    }

    @Test
    void criarRejeitaMaisDeDuasCasasDecimais() {
        assertThatThrownBy(() -> service.criar(CLIENTE, TipoLancamento.RECEITA, dados(new BigDecimal("10.001"), "x")))
                .isInstanceOf(RegraNegocioException.class);
    }

    @Test
    void criarRejeitaDescricaoEmBranco() {
        assertThatThrownBy(() -> service.criar(CLIENTE, TipoLancamento.RECEITA, dados(BigDecimal.ONE, "   ")))
                .isInstanceOf(RegraNegocioException.class)
                .hasMessageContaining("descricao");
        verify(lancamentoRepository, never()).save(any());
    }

    @Test
    void criarAssociaAoClienteAutenticadoENormalizaValor() {
        Cliente cliente = new Cliente("Ana", "ana@example.com", "hash");
        when(clienteRepository.getReferenceById(CLIENTE)).thenReturn(cliente);
        when(lancamentoRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Lancamento criado = service.criar(CLIENTE, TipoLancamento.RECEITA,
                new DadosLancamento("  Salário ", " Mensal ", new BigDecimal("5000"), INICIO));

        assertThat(criado.getCliente()).isSameAs(cliente);
        assertThat(criado.getTipo()).isEqualTo(TipoLancamento.RECEITA);
        assertThat(criado.getNome()).isEqualTo("Salário");
        assertThat(criado.getDescricao()).isEqualTo("Mensal");
        assertThat(criado.getValor()).isEqualTo(new BigDecimal("5000.00"));
    }

    @Test
    void lancamentoDeOutroClienteNaoEhEncontrado() {
        when(lancamentoRepository.findByIdAndClienteIdAndTipo(99L, CLIENTE, TipoLancamento.GASTO))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.excluir(CLIENTE, TipoLancamento.GASTO, 99L))
                .isInstanceOf(RecursoNaoEncontradoException.class);
        verify(lancamentoRepository, never()).delete(any());
    }

    private static DadosLancamento dados(BigDecimal valor, String descricao) {
        return new DadosLancamento("Mercado", descricao, valor, INICIO);
    }
}
