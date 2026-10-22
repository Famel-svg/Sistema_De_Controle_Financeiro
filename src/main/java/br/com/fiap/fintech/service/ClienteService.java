package br.com.fiap.fintech.service;

import br.com.fiap.fintech.domain.Cliente;
import br.com.fiap.fintech.exception.CredenciaisInvalidasException;
import br.com.fiap.fintech.exception.EmailJaCadastradoException;
import br.com.fiap.fintech.exception.RecursoNaoEncontradoException;
import br.com.fiap.fintech.repository.ClienteRepository;
import java.util.Optional;
import java.util.UUID;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final PasswordEncoder passwordEncoder;
    /** Hash usado quando o e-mail não existe, para o tempo de resposta não revelar cadastros. */
    private final String hashFicticio;

    public ClienteService(ClienteRepository clienteRepository, PasswordEncoder passwordEncoder) {
        this.clienteRepository = clienteRepository;
        this.passwordEncoder = passwordEncoder;
        this.hashFicticio = passwordEncoder.encode(UUID.randomUUID().toString());
    }

    @Transactional
    public Cliente cadastrar(String nome, String email, String senha) {
        String emailNormalizado = Emails.normalizar(email);
        if (clienteRepository.existsByEmail(emailNormalizado)) {
            throw new EmailJaCadastradoException();
        }
        Cliente cliente = new Cliente(nome.trim(), emailNormalizado, passwordEncoder.encode(senha));
        return clienteRepository.save(cliente);
    }

    /** Substitui a comparação de String do antigo LoginServlet pela verificação do hash BCrypt. */
    @Transactional(readOnly = true)
    public Cliente autenticar(String email, String senha) {
        Optional<Cliente> cliente = clienteRepository.findByEmail(Emails.normalizar(email));
        // Sempre executa exatamente uma verificação BCrypt, exista o e-mail ou não.
        String hash = cliente.map(Cliente::getSenhaHash).orElse(hashFicticio);
        boolean senhaConfere = passwordEncoder.matches(senha, hash);
        if (cliente.isEmpty() || !senhaConfere) {
            throw new CredenciaisInvalidasException();
        }
        return cliente.get();
    }

    @Transactional(readOnly = true)
    public Cliente buscar(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente não encontrado."));
    }
}
