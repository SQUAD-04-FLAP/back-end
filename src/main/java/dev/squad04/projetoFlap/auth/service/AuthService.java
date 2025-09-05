package dev.squad04.projetoFlap.auth.service;

import dev.squad04.projetoFlap.auth.dto.LoggedDTO;
import dev.squad04.projetoFlap.auth.dto.LoginDTO;
import dev.squad04.projetoFlap.auth.dto.RegisterDTO;
import dev.squad04.projetoFlap.auth.entity.User;
import dev.squad04.projetoFlap.auth.enums.AuthProvider;
import dev.squad04.projetoFlap.auth.repository.UserRepository;
import dev.squad04.projetoFlap.email.service.EmailService;
import org.springframework.context.annotation.Lazy;
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
        return repository.findByLogin(username);
    }

    public LoggedDTO login(LoginDTO data) {
        var userPassword = new UsernamePasswordAuthenticationToken(data.login(), data.password());
        var auth = this.authenticationManager.authenticate(userPassword);
        var token = tokenService.generateToken((User) auth.getPrincipal());

        return new LoggedDTO(token);
    }

    public User register(RegisterDTO user) {
        String encodedPassword = new BCryptPasswordEncoder().encode(user.password());
        User newUser = new User(user.login(), encodedPassword, user.role(), AuthProvider.CREDENTIALS);
        this.repository.save(newUser);

        return newUser;
    }

    public User findByLogin(String login) {
        return (User) repository.findByLogin(login);
    }

    public void requestPasswordReset(String login) {
        User user = (User) this.repository.findByLogin(login);

        if (user != null && user.getProvider() == AuthProvider.CREDENTIALS) {
            String code = new DecimalFormat("000000").format(new SecureRandom().nextInt(999999));

            user.setPasswordResetCode(code);
            user.setPasswordResetCodeExpiry(LocalDateTime.now().plusMinutes(10));
            this.repository.save(user);

            this.emailService.sendPasswordResetCode(user.getLogin(), code);
        }
    }

    public void resetPassword(String code, String newPassword) {
        User user = this.repository.findByPasswordResetCode(code)
                .orElseThrow(() -> new RuntimeException("Código inválido ou expirado"));

        if (user.getPasswordResetCodeExpiry().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Código de redefinição de senha expirado");
        }

        user.setPassword(this.passwordEncoder.encode(newPassword));

        user.setPasswordResetCode(null);
        user.setPasswordResetCodeExpiry(null);

        this.repository.save(user);
    }
}
