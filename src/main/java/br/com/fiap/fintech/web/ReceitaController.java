package br.com.fiap.fintech.web;

import br.com.fiap.fintech.domain.TipoLancamento;
import br.com.fiap.fintech.service.LancamentoService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ReceitaController.ROTA)
public class ReceitaController extends LancamentoController {

    static final String ROTA = "/api/receitas";

    public ReceitaController(LancamentoService service) {
        super(service, TipoLancamento.RECEITA, ROTA);
    }
}
