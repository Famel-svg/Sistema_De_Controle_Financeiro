package br.com.fiap.fintech.security;

import br.com.fiap.fintech.repository.ClienteRepository;
import br.com.fiap.fintech.service.Emails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClienteUserDetailsService implements UserDetailsService {

    private final ClienteRepository clienteRepository;

    public ClienteUserDetailsService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) {
        return clienteRepository.findByEmail(Emails.normalizar(username))
                .map(c -> new ClienteAutenticado(c.getId(), c.getEmail(), c.getSenhaHash()))
                .orElseThrow(() -> new UsernameNotFoundException("Cliente não encontrado"));
    }
}
