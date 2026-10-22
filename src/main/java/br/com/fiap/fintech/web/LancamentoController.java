package br.com.fiap.fintech.web;

import br.com.fiap.fintech.domain.Lancamento;
import br.com.fiap.fintech.domain.TipoLancamento;
import br.com.fiap.fintech.security.ClienteAutenticado;
import br.com.fiap.fintech.service.LancamentoService;
import br.com.fiap.fintech.web.dto.LancamentoRequest;
import br.com.fiap.fintech.web.dto.LancamentoResponse;
import jakarta.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedModel;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Endpoints comuns de receitas e gastos. As subclasses só definem a rota e o tipo;
 * toda regra fica no LancamentoService.
 */
public abstract class LancamentoController {

    private final LancamentoService service;
    private final TipoLancamento tipo;
    private final String rotaBase;

    protected LancamentoController(LancamentoService service, TipoLancamento tipo, String rotaBase) {
        this.service = service;
        this.tipo = tipo;
        this.rotaBase = rotaBase;
    }

    @GetMapping
    public PagedModel<LancamentoResponse> listar(
            @AuthenticationPrincipal ClienteAutenticado cliente,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim,
            @PageableDefault(size = 20, sort = "data", direction = Sort.Direction.DESC) Pageable pageable) {
        return new PagedModel<>(service.listar(cliente.id(), tipo, dataInicio, dataFim, pageable)
                .map(LancamentoResponse::de));
    }

    @GetMapping("/{id}")
    public LancamentoResponse buscar(@AuthenticationPrincipal ClienteAutenticado cliente, @PathVariable Long id) {
        return LancamentoResponse.de(service.buscar(cliente.id(), tipo, id));
    }

    @PostMapping
    public ResponseEntity<LancamentoResponse> criar(@AuthenticationPrincipal ClienteAutenticado cliente,
                                                    @Valid @RequestBody LancamentoRequest request) {
        Lancamento criado = service.criar(cliente.id(), tipo, request.paraDados());
        return ResponseEntity.created(URI.create(rotaBase + "/" + criado.getId()))
                .body(LancamentoResponse.de(criado));
    }

    @PutMapping("/{id}")
    public LancamentoResponse atualizar(@AuthenticationPrincipal ClienteAutenticado cliente, @PathVariable Long id,
                                        @Valid @RequestBody LancamentoRequest request) {
        return LancamentoResponse.de(service.atualizar(cliente.id(), tipo, id, request.paraDados()));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@AuthenticationPrincipal ClienteAutenticado cliente, @PathVariable Long id) {
        service.excluir(cliente.id(), tipo, id);
    }
}
