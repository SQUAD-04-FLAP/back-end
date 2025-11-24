package dev.squad04.projetoFlap.auth.service;

import dev.squad04.projetoFlap.auth.dto.login.LoggedDTO;
import dev.squad04.projetoFlap.auth.dto.login.LoginDTO;
import dev.squad04.projetoFlap.auth.dto.register.ForgotPasswordDTO;
import dev.squad04.projetoFlap.auth.dto.register.RegisterDTO;
import dev.squad04.projetoFlap.auth.dto.user.SetUserRoleDTO;
import dev.squad04.projetoFlap.auth.entity.User;
import dev.squad04.projetoFlap.auth.enums.AuthProvider;
import dev.squad04.projetoFlap.auth.enums.UserRole;
import dev.squad04.projetoFlap.auth.repository.UserRepository;
import dev.squad04.projetoFlap.email.service.EmailService;
import dev.squad04.projetoFlap.exceptions.AppException;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.text.DecimalFormat;
import java.time.LocalDateTime;

@Service
public class AuthService implements UserDetailsService {

    private final UserRepository repository;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository repository, @Lazy AuthenticationManager authenticationManager, TokenService tokenService, EmailService emailService, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = repository.findUserDetailsByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("Usuário não encontrado com o login: " + username);
        }
        return user;
    }

    public LoggedDTO login(LoginDTO data) {
        var userPassword = new UsernamePasswordAuthenticationToken(data.email(), data.senha());
        var auth = this.authenticationManager.authenticate(userPassword);
        var token = tokenService.generateToken((User) auth.getPrincipal());
        return new LoggedDTO(token);
    }

    public User register(RegisterDTO data) {
        if (repository.findByEmail(data.email()).isPresent()) {
            throw new AppException("O email informado já existe.", HttpStatus.CONFLICT);
        }

        String encodedPassword = new BCryptPasswordEncoder().encode(data.senha());
        User newUser = new User(data.nome(), data.email(), encodedPassword, UserRole.USER, AuthProvider.CREDENTIALS, data.dtNascimento());

        return this.repository.save(newUser);
    }

    public void requestPasswordReset(ForgotPasswordDTO data) {
        repository.findByEmail(data.email()).ifPresent(user -> {
            if (user.getProvedor() == AuthProvider.CREDENTIALS) {
                String code = new DecimalFormat("000000").format(new SecureRandom().nextInt(999999));
                user.setResetCode(code);
                user.setResetCodeExpiry(LocalDateTime.now().plusMinutes(10));
                this.repository.save(user);
                this.emailService.sendPasswordResetCode(user.getEmail(), code);
            }
        });
    }

    public void resetPassword(String code, String newPassword) {
        User user = this.repository.findByResetCode(code)
                .orElseThrow(() -> new AppException("Código inválido ou não encontrado", HttpStatus.NOT_FOUND));

        if (user.getResetCodeExpiry().isBefore(LocalDateTime.now())) {
            throw new AppException("Código de redefinição de senha expirado", HttpStatus.BAD_REQUEST);
        }

        user.setSenha(this.passwordEncoder.encode(newPassword));
        user.setResetCode(null);
        user.setResetCodeExpiry(null);

        this.repository.save(user);
    }

    public User setUserRole(Integer id, SetUserRoleDTO data) {
        User user = this.repository.findById(id)
                .orElseThrow(() -> new AppException("Usuário não encontrado.", HttpStatus.NOT_FOUND));

        user.setPermissao(data.permissao());
        return repository.save(user);
    }
}
