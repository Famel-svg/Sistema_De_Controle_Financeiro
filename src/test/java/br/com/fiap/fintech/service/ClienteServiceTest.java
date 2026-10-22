package br.com.fiap.fintech.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.fiap.fintech.domain.Cliente;
import br.com.fiap.fintech.exception.CredenciaisInvalidasException;
import br.com.fiap.fintech.exception.EmailJaCadastradoException;
import br.com.fiap.fintech.repository.ClienteRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;
    private final PasswordEncoder encoder = new BCryptPasswordEncoder(4);
    private ClienteService service;

    @BeforeEach
    void setUp() {
        service = new ClienteService(clienteRepository, encoder);
    }

    @Test
    void cadastroGuardaHashBCryptENaoASenha() {
        when(clienteRepository.existsByEmail("ana@example.com")).thenReturn(false);
        when(clienteRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Cliente cliente = service.cadastrar(" Ana ", "  Ana@Example.COM ", "senha-forte-1");

        assertThat(cliente.getEmail()).isEqualTo("ana@example.com");
        assertThat(cliente.getNome()).isEqualTo("Ana");
        assertThat(cliente.getSenhaHash()).startsWith("$2").isNotEqualTo("senha-forte-1");
        assertThat(encoder.matches("senha-forte-1", cliente.getSenhaHash())).isTrue();
    }

    @Test
    void cadastroComEmailExistenteFalha() {
        when(clienteRepository.existsByEmail("ana@example.com")).thenReturn(true);

        assertThatThrownBy(() -> service.cadastrar("Ana", "ana@example.com", "senha-forte-1"))
                .isInstanceOf(EmailJaCadastradoException.class);
        verify(clienteRepository, never()).save(any());
    }

    @Test
    void autenticaComSenhaCorreta() {
        Cliente ana = new Cliente("Ana", "ana@example.com", encoder.encode("senha-forte-1"));
        when(clienteRepository.findByEmail("ana@example.com")).thenReturn(Optional.of(ana));

        assertThat(service.autenticar("ANA@example.com", "senha-forte-1")).isSameAs(ana);
    }

    @Test
    void senhaErradaOuEmailInexistenteGeramCredenciaisInvalidas() {
        Cliente ana = new Cliente("Ana", "ana@example.com", encoder.encode("senha-forte-1"));
        when(clienteRepository.findByEmail("ana@example.com")).thenReturn(Optional.of(ana));
        when(clienteRepository.findByEmail("nao@existe.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.autenticar("ana@example.com", "errada"))
                .isInstanceOf(CredenciaisInvalidasException.class);
        assertThatThrownBy(() -> service.autenticar("nao@existe.com", "senha-forte-1"))
                .isInstanceOf(CredenciaisInvalidasException.class);
    }
}
