package dev.squad04.projetoFlap.auth.controller;

import dev.squad04.projetoFlap.auth.dto.*;
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
    public ResponseEntity<User> register(@RequestBody @Valid RegisterDTO data) {
        if (this.authService.findByLogin(data.login()) != null) {
            throw new RuntimeException("O usuário já existe.");
        }

        User newUser = authService.register(data);

        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Void> forgotPassword(@RequestBody @Valid ForgotPasswordDTO data) {
        this.authService.requestPasswordReset(data.email());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(@RequestBody @Valid ResetPasswordDTO data) {
        this.authService.resetPassword(data.code(), data.newPassword());
        return ResponseEntity.ok().build();
    }
}
