package dev.squad04.projetoFlap.auth.controller;

import dev.squad04.projetoFlap.auth.dto.login.LoggedDTO;
import dev.squad04.projetoFlap.auth.dto.login.LoginDTO;
import dev.squad04.projetoFlap.auth.dto.register.ForgotPasswordDTO;
import dev.squad04.projetoFlap.auth.dto.register.RegisterDTO;
import dev.squad04.projetoFlap.auth.dto.register.ResetPasswordDTO;
import dev.squad04.projetoFlap.auth.dto.user.SetUserRoleDTO;
import dev.squad04.projetoFlap.auth.dto.user.UserResponseDTO;
import dev.squad04.projetoFlap.auth.entity.User;
import dev.squad04.projetoFlap.auth.mapper.UserMapper;
import dev.squad04.projetoFlap.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticação", description = "Endpoints para login, registro e recuperação de senha.")
public class AuthController {

    private final AuthService authService;
    private final UserMapper userMapper;

    public AuthController(AuthService authService, UserMapper userMapper) {
        this.authService = authService;
        this.userMapper = userMapper;
    }

    @Operation(summary = "Realiza o login do usuário", description = "Autentica um usuário com email e senha e retorna um token de acesso.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login bem-sucedido"),
            @ApiResponse(responseCode = "400", description = "Credenciais inválidas")
    })
    @PostMapping("/login")
    public ResponseEntity<LoggedDTO> login(@RequestBody @Valid LoginDTO data) {
        LoggedDTO login = authService.login(data);
        return ResponseEntity.ok().body(login);
    }

    @Operation(summary = "Registra um novo usuário", description = "Cria uma nova conta de usuário no sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuário registrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou email já existente")
    })
    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@RequestBody @Valid RegisterDTO data) {
        User newUser = authService.register(data);
        return ResponseEntity.status(HttpStatus.CREATED).body(userMapper.toDTO(newUser));
    }

    @Operation(summary = "Solicita a redefinição de senha", description = "Envia um código de redefinição para o email do usuário.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Solicitação processada"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    @PostMapping("/forgot-password")
    public ResponseEntity<Void> forgotPassword(@RequestBody ForgotPasswordDTO email) {
        this.authService.requestPasswordReset(email);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Redefine a senha do usuário", description = "Altera a senha do usuário usando o código de redefinição.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Senha alterada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Código inválido ou expirado")
    })
    @PostMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(@RequestBody @Valid ResetPasswordDTO data) {
        this.authService.resetPassword(data.code(), data.newPassword());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Busca um usuário pelo ID", description = "Retorna os dados públicos de um usuário específico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário encontrado"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    @GetMapping("/user/{idUser}")
    public ResponseEntity<UserResponseDTO> findUserById(@PathVariable Integer idUser) {
        User user = this.authService.findUserById(idUser);
        return ResponseEntity.ok(userMapper.toDTO(user));
    }

    @Operation(summary = "Atualiza a permissão de um usuário pelo ID", description = "Retorna um usuário com dados atualizados, requer permissão ADMIN")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Permissão atualizada"),
            @ApiResponse(responseCode = "401", description = "Usuário com permissão insuficiente"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    @PatchMapping("/update-role/{idUser}")
    public ResponseEntity<UserResponseDTO> setUserRole(@PathVariable Integer idUser, @RequestBody SetUserRoleDTO data) {
        User user = this.authService.setUserRole(idUser, data);
        return ResponseEntity.ok(userMapper.toDTO(user));
    }
}
