package dev.squad04.projetoFlap.auth.controller;

import dev.squad04.projetoFlap.auth.dto.login.LoggedDTO;
import dev.squad04.projetoFlap.auth.dto.login.LoginDTO;
import dev.squad04.projetoFlap.auth.dto.register.ForgotPasswordDTO;
import dev.squad04.projetoFlap.auth.dto.register.RegisterDTO;
import dev.squad04.projetoFlap.auth.dto.register.ResetPasswordDTO;
import dev.squad04.projetoFlap.auth.dto.user.UserResponseDTO;
import dev.squad04.projetoFlap.auth.entity.User;
import dev.squad04.projetoFlap.auth.mapper.UserMapper;
import dev.squad04.projetoFlap.auth.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final UserMapper userMapper;

    public AuthController(AuthService authService, UserMapper userMapper) {
        this.authService = authService;
        this.userMapper = userMapper;
    }

    @PostMapping("/login")
    public ResponseEntity<LoggedDTO> login(@RequestBody @Valid LoginDTO data) {
        LoggedDTO login = authService.login(data);
        return ResponseEntity.ok().body(login);
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@RequestBody @Valid RegisterDTO data) {
        User newUser = authService.register(data);
        return ResponseEntity.status(HttpStatus.CREATED).body(userMapper.toDTO(newUser));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Void> forgotPassword(@RequestBody ForgotPasswordDTO email) {
        this.authService.requestPasswordReset(email);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(@RequestBody @Valid ResetPasswordDTO data) {
        this.authService.resetPassword(data.code(), data.newPassword());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/user/{idUser}")
    public ResponseEntity<UserResponseDTO> findUserById(@PathVariable Integer idUser) {
        User user = this.authService.findUserById(idUser);
        return ResponseEntity.ok(userMapper.toDTO(user));
    }
}
