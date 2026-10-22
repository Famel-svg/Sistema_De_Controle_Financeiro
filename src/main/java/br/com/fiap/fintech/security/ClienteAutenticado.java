package br.com.fiap.fintech.security;

import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/** Principal autenticado: carrega o id do cliente para as consultas ficarem restritas ao dono. */
public record ClienteAutenticado(Long id, String email, String senhaHash) implements UserDetails {

    private static final List<GrantedAuthority> AUTORIDADES = List.of(new SimpleGrantedAuthority("ROLE_CLIENTE"));

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return AUTORIDADES;
    }

    @Override
    public String getPassword() {
        return senhaHash;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String toString() {
        return "ClienteAutenticado[id=" + id + ", email=" + email + "]";
    }
}
