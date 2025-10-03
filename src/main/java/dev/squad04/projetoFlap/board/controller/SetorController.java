package dev.squad04.projetoFlap.board.controller;

import dev.squad04.projetoFlap.board.dto.setor.AssociacaoResponseDTO;
import dev.squad04.projetoFlap.board.dto.setor.AssociarUsuarioSetorDTO;
import dev.squad04.projetoFlap.board.dto.setor.CriarSetorDTO;
import dev.squad04.projetoFlap.board.entity.Setor;
import dev.squad04.projetoFlap.board.entity.associations.UsuarioSetor;
import dev.squad04.projetoFlap.board.mapper.SetorMapper;
import dev.squad04.projetoFlap.board.service.SetorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/flapboard/setor")
@Tag(name = "Setores", description = "Gerenciamento de setores e associação de usuários.")
public class SetorController {

    private final SetorService setorService;
    private final SetorMapper setorMapper;

    public SetorController(SetorService setorService, SetorMapper setorMapper) {
        this.setorService = setorService;
        this.setorMapper = setorMapper;
    }

    @Operation(summary = "Cria um novo setor")
    @ApiResponse(responseCode = "201", description = "Setor criado com sucesso")
    @PostMapping
    public ResponseEntity<Setor> criarSetor(@RequestBody CriarSetorDTO data) {
        Setor novoSetor = setorService.criarSetor(data);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoSetor);
    }

    @Operation(summary = "Associa um usuário a um setor")
    @ApiResponse(responseCode = "200", description = "Associação realizada com sucesso")
    @PostMapping("/associar/{idSetor}")
    public ResponseEntity<AssociacaoResponseDTO> associarUsuario(@PathVariable Integer idSetor, @RequestBody AssociarUsuarioSetorDTO data) {
        UsuarioSetor usuarioSetor = setorService.associarUsuario(idSetor, data);
        return ResponseEntity.ok(setorMapper.toDTO(usuarioSetor));
    }

    @Operation(summary = "Remove a associação de um usuário de um setor")
    @ApiResponse(responseCode = "204", description = "Associação removida com sucesso")
    @DeleteMapping("/desassociar/{idSetor}/{idUsuario}")
    public ResponseEntity<Void> desassociarUsuario(@PathVariable Integer idSetor, @PathVariable Integer idUsuario) {
        setorService.desassociarUsuario(idSetor, idUsuario);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Lista todos os setores")
    @GetMapping
    public ResponseEntity<List<Setor>> listarTodos() {
        List<Setor> setores = setorService.listarTodos();
        return ResponseEntity.ok(setores);
    }

    @Operation(summary = "Busca um setor pelo seu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Setor encontrado"),
            @ApiResponse(responseCode = "404", description = "Setor não encontrado")
    })
    @GetMapping("/{idSetor}")
    public ResponseEntity<Setor> buscarSetorPorId(@PathVariable Integer idSetor) {
        Setor setor = setorService.buscarPorId(idSetor);
        return ResponseEntity.ok(setor);
    }
}
