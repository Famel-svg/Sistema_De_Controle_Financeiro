package br.com.fiap.fintech.web;

import br.com.fiap.fintech.domain.Cliente;
import br.com.fiap.fintech.security.ClienteAutenticado;
import br.com.fiap.fintech.service.ClienteService;
import br.com.fiap.fintech.web.dto.CadastroClienteRequest;
import br.com.fiap.fintech.web.dto.ClienteResponse;
import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @PostMapping
    public ResponseEntity<ClienteResponse> cadastrar(@Valid @RequestBody CadastroClienteRequest request) {
        Cliente cliente = clienteService.cadastrar(request.nome(), request.email(), request.senha());
        return ResponseEntity.created(URI.create("/api/clientes/me")).body(ClienteResponse.de(cliente));
    }

    @GetMapping("/me")
    public ClienteResponse me(@AuthenticationPrincipal ClienteAutenticado cliente) {
        return ClienteResponse.de(clienteService.buscar(cliente.id()));
    }
}
