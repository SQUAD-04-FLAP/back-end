package dev.squad04.projetoFlap.auth.entity;

import dev.squad04.projetoFlap.auth.enums.AuthProvider;
import dev.squad04.projetoFlap.auth.enums.UserRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Table(name = "usuarios")
@Entity
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUsuario;
    private String nome;
    private String email;
    private String senha;
    @Enumerated(EnumType.STRING)
    private UserRole permissao;

    @Enumerated(EnumType.STRING)
    private AuthProvider provedor;

    private String resetCode;
    private LocalDateTime resetCodeExpiry;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public User(String nome, String email, String senha, UserRole permissao, AuthProvider provedor) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.permissao = permissao;
        this.provedor = provedor;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.permissao == UserRole.ADMIN) return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"),
                new SimpleGrantedAuthority("ROLE_USER"));
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return senha;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
