package dev.squad04.projetoFlap.auth.controller;

import dev.squad04.projetoFlap.auth.dto.user.UpdateUserDTO;
import dev.squad04.projetoFlap.auth.dto.user.UserResponseDTO;
import dev.squad04.projetoFlap.auth.entity.User;
import dev.squad04.projetoFlap.auth.mapper.UserMapper;
import dev.squad04.projetoFlap.auth.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@Tag(name = "Usuário", description = "Endpoints para controle e gestão de usuários.")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @Operation(summary = "Busca um usuário pelo ID", description = "Retorna os dados públicos de um usuário específico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário encontrado"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    @GetMapping("/find/{idUser}")
    public ResponseEntity<UserResponseDTO> findUserById(@PathVariable Integer idUser) {
        User user = this.userService.findUserById(idUser);
        return ResponseEntity.ok(userMapper.toDTO(user));
    }

    @Operation(summary = "Busca todos os usuários", description = "Retorna os dados públicos de todos usuários.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de usuários"),
    })
    @GetMapping("/findAll")
    public ResponseEntity<List<UserResponseDTO>> findAllUsers() {
        List<User> users = this.userService.findAllUsers();
        List<UserResponseDTO> userDTOs = users.stream()
                .map(userMapper::toDTO)
                .toList();
        return ResponseEntity.ok(userDTOs);
    }

    @Operation(summary = "Atualiza os dados de um usuário", description = "Permite atualizar informações específicas de um usuário.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    @PatchMapping("/update/{idUsuario}")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable Integer idUsuario, @RequestBody UpdateUserDTO data) {
        User updatedUser = userService.updateUser(idUsuario, data);
        return ResponseEntity.ok(userMapper.toDTO(updatedUser));
    }

    @Operation(summary = "Deleta um usuário", description = "Remove um usuário do sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Usuário deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    @DeleteMapping("/delete/{idUsuario}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer idUsuario) {
        userService.deleteUser(idUsuario);
        return ResponseEntity.noContent().build();
    }
}
