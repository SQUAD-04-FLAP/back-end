package dev.squad04.projetoFlap.auth.service;

import dev.squad04.projetoFlap.auth.dto.LoggedDTO;
import dev.squad04.projetoFlap.auth.dto.LoginDTO;
import dev.squad04.projetoFlap.auth.dto.RegisterDTO;
import dev.squad04.projetoFlap.auth.entity.User;
import dev.squad04.projetoFlap.auth.enums.AuthProvider;
import dev.squad04.projetoFlap.auth.repository.UserRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements UserDetailsService {

    private final UserRepository repository;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public AuthService(UserRepository repository, @Lazy AuthenticationManager authenticationManager, TokenService tokenService) {
        this.repository = repository;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
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

}
