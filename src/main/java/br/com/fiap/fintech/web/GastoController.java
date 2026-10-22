package br.com.fiap.fintech.web;

import br.com.fiap.fintech.domain.TipoLancamento;
import br.com.fiap.fintech.service.LancamentoService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(GastoController.ROTA)
public class GastoController extends LancamentoController {

    static final String ROTA = "/api/gastos";

    public GastoController(LancamentoService service) {
        super(service, TipoLancamento.GASTO, ROTA);
    }
}
