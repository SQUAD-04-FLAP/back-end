package dev.squad04.projetoFlap.auth.controller;

import dev.squad04.projetoFlap.auth.dto.LoggedDTO;
import dev.squad04.projetoFlap.auth.dto.LoginDTO;
import dev.squad04.projetoFlap.auth.dto.RegisterDTO;
import dev.squad04.projetoFlap.auth.entity.User;
import dev.squad04.projetoFlap.auth.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoggedDTO> login(@RequestBody @Valid LoginDTO data) {
        LoggedDTO login = authService.login(data);
        return ResponseEntity.ok().body(login);
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody @Valid RegisterDTO user) {
        if (this.authService.findByLogin(user.login()) != null) {
            throw new RuntimeException("O usuário já existe.");
        }

        User newUser = authService.register(user);

        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }
}
