package br.com.fiap.fintech.service;

import br.com.fiap.fintech.domain.Cliente;
import br.com.fiap.fintech.domain.Lancamento;
import br.com.fiap.fintech.domain.TipoLancamento;
import br.com.fiap.fintech.exception.RecursoNaoEncontradoException;
import br.com.fiap.fintech.exception.RegraNegocioException;
import br.com.fiap.fintech.repository.ClienteRepository;
import br.com.fiap.fintech.repository.LancamentoRepository;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Regras de receitas e gastos. Toda operação recebe o id do cliente autenticado e só enxerga
 * lançamentos dele: o lançamento de outro cliente responde como inexistente (404).
 */
@Service
public class LancamentoService {

    private final LancamentoRepository lancamentoRepository;
    private final ClienteRepository clienteRepository;

    public LancamentoService(LancamentoRepository lancamentoRepository, ClienteRepository clienteRepository) {
        this.lancamentoRepository = lancamentoRepository;
        this.clienteRepository = clienteRepository;
    }

    @Transactional(readOnly = true)
    public Page<Lancamento> listar(Long clienteId, TipoLancamento tipo, LocalDate dataInicio,
                                   LocalDate dataFim, Pageable pageable) {
        Periodo periodo = Periodo.de(dataInicio, dataFim);
        return lancamentoRepository.findByClienteIdAndTipoAndDataBetween(
                clienteId, tipo, periodo.inicio(), periodo.fim(), pageable);
    }

    @Transactional(readOnly = true)
    public Lancamento buscar(Long clienteId, TipoLancamento tipo, Long id) {
        return lancamentoRepository.findByIdAndClienteIdAndTipo(id, clienteId, tipo)
                .orElseThrow(() -> new RecursoNaoEncontradoException(nomeDoTipo(tipo) + " não encontrada(o)."));
    }

    @Transactional
    public Lancamento criar(Long clienteId, TipoLancamento tipo, DadosLancamento dados) {
        validar(dados);
        Cliente cliente = clienteRepository.getReferenceById(clienteId);
        Lancamento lancamento = new Lancamento(cliente, tipo, dados.nome().trim(), dados.descricao().trim(),
                normalizarValor(dados.valor()), dados.data());
        return lancamentoRepository.save(lancamento);
    }

    @Transactional
    public Lancamento atualizar(Long clienteId, TipoLancamento tipo, Long id, DadosLancamento dados) {
        validar(dados);
        Lancamento lancamento = buscar(clienteId, tipo, id);
        lancamento.atualizar(dados.nome().trim(), dados.descricao().trim(),
                normalizarValor(dados.valor()), dados.data());
        return lancamento;
    }

    @Transactional
    public void excluir(Long clienteId, TipoLancamento tipo, Long id) {
        lancamentoRepository.delete(buscar(clienteId, tipo, id));
    }

    /** Saldo do período = total de receitas - total de gastos (pode ser negativo). */
    @Transactional(readOnly = true)
    public Resumo resumir(Long clienteId, LocalDate dataInicio, LocalDate dataFim) {
        Periodo periodo = Periodo.de(dataInicio, dataFim);
        BigDecimal receitas = somar(clienteId, TipoLancamento.RECEITA, periodo);
        BigDecimal gastos = somar(clienteId, TipoLancamento.GASTO, periodo);
        return new Resumo(dataInicio, dataFim, receitas, gastos, receitas.subtract(gastos));
    }

    private BigDecimal somar(Long clienteId, TipoLancamento tipo, Periodo periodo) {
        BigDecimal total = lancamentoRepository.somarValores(clienteId, tipo, periodo.inicio(), periodo.fim());
        return (total == null ? BigDecimal.ZERO : total).setScale(2, RoundingMode.HALF_EVEN);
    }

    /**
     * Mesmas regras do Bean Validation na entrada HTTP, repetidas aqui porque o service
     * também pode ser chamado por outros pontos da aplicação.
     */
    void validar(DadosLancamento dados) {
        if (dados == null) {
            throw new RegraNegocioException("Dados do lançamento são obrigatórios.");
        }
        if (dados.valor() == null || dados.valor().signum() <= 0) {
            throw new RegraNegocioException("valor deve ser maior que zero.");
        }
        if (dados.valor().stripTrailingZeros().scale() > 2) {
            throw new RegraNegocioException("valor deve ter no máximo 2 casas decimais.");
        }
        if (dados.nome() == null || dados.nome().isBlank()) {
            throw new RegraNegocioException("nome é obrigatório.");
        }
        if (dados.descricao() == null || dados.descricao().isBlank()) {
            throw new RegraNegocioException("descricao é obrigatória.");
        }
        if (dados.data() == null) {
            throw new RegraNegocioException("data é obrigatória.");
        }
    }

    private static BigDecimal normalizarValor(BigDecimal valor) {
        return valor.setScale(2, RoundingMode.UNNECESSARY);
    }

    private static String nomeDoTipo(TipoLancamento tipo) {
        return tipo == TipoLancamento.RECEITA ? "Receita" : "Gasto";
    }
}
