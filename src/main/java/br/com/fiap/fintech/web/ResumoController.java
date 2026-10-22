package br.com.fiap.fintech.web;

import br.com.fiap.fintech.security.ClienteAutenticado;
import br.com.fiap.fintech.service.LancamentoService;
import br.com.fiap.fintech.service.Resumo;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/resumo")
public class ResumoController {

    private final LancamentoService service;

    public ResumoController(LancamentoService service) {
        this.service = service;
    }

    /** Total de receitas, total de gastos e saldo (receitas - gastos) do período. */
    @GetMapping
    public Resumo resumo(
            @AuthenticationPrincipal ClienteAutenticado cliente,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim) {
        return service.resumir(cliente.id(), dataInicio, dataFim);
    }
}
