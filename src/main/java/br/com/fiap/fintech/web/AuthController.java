package br.com.fiap.fintech.web;

import br.com.fiap.fintech.service.ClienteService;
import br.com.fiap.fintech.web.dto.ClienteResponse;
import br.com.fiap.fintech.web.dto.LoginRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final ClienteService clienteService;

    public AuthController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    /** 200 com os dados do cliente quando e-mail e senha conferem; 401 (ProblemDetail) caso contrário. */
    @PostMapping("/login")
    public ClienteResponse login(@Valid @RequestBody LoginRequest request) {
        return ClienteResponse.de(clienteService.autenticar(request.email(), request.senha()));
    }
}
