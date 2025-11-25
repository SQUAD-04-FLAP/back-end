package dev.squad04.projetoFlap.board.controller;

import dev.squad04.projetoFlap.board.dto.perfil.PerfilDTO;
import dev.squad04.projetoFlap.board.entity.Perfil;
import dev.squad04.projetoFlap.board.service.PerfilService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/flapboard/perfil")
@Tag(name = "Perfis", description = "Operações CRUD para gerenciamento de perfis de usuário.")
public class PerfilController {

    private final PerfilService perfilService;

    public PerfilController(PerfilService perfilService) {
        this.perfilService = perfilService;
    }

    @Operation(summary = "Cria um novo perfil")
    @ApiResponse(responseCode = "201", description = "Perfil criado com sucesso")
    @PostMapping
    public ResponseEntity<Perfil> criarPerfil(@RequestBody PerfilDTO data) {
        Perfil novoPerfil = perfilService.criarPerfil(data);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoPerfil);
    }

    @Operation(summary = "Busca um perfil pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Perfil encontrado"),
            @ApiResponse(responseCode = "404", description = "Perfil não encontrado")
    })
    @GetMapping("/id/{idPerfil}")
    public ResponseEntity<Optional<Perfil>> buscarPorId(@PathVariable Integer idPerfil) {
        Optional<Perfil> perfilOptional = perfilService.buscarPorId(idPerfil);
        return ResponseEntity.ok(perfilOptional);
    }

    @Operation(summary = "Lista todos os perfis cadastrados")
    @GetMapping
    public ResponseEntity<List<Perfil>> listarTodos() {
        List<Perfil> perfis = perfilService.listarTodos();
        return ResponseEntity.ok(perfis);
    }

    @Operation(summary = "Atualiza um perfil existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Perfil atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Perfil não encontrado")
    })
    @PutMapping("/id/{idPerfil}")
    public ResponseEntity<Perfil> atualizarPerfil(@PathVariable Integer idPerfil, @RequestBody PerfilDTO data) {
        Perfil perfilAtt = perfilService.atualizarPerfil(idPerfil, data);
        return ResponseEntity.ok(perfilAtt);
    }

    @Operation(summary = "Exclui um perfil")
    @ApiResponse(responseCode = "204", description = "Perfil excluído com sucesso")
    @DeleteMapping("/id/{idPerfil}")
    public ResponseEntity<Void> deletarPerfil(@PathVariable Integer idPerfil) {
        perfilService.deletarPerfil(idPerfil);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
