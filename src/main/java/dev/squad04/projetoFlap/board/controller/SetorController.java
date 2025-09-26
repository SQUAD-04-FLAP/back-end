package dev.squad04.projetoFlap.board.controller;

import dev.squad04.projetoFlap.board.dto.setor.AssociacaoResponseDTO;
import dev.squad04.projetoFlap.board.dto.setor.AssociarUsuarioSetorDTO;
import dev.squad04.projetoFlap.board.dto.setor.CriarSetorDTO;
import dev.squad04.projetoFlap.board.entity.Setor;
import dev.squad04.projetoFlap.board.entity.associations.UsuarioSetor;
import dev.squad04.projetoFlap.board.mapper.SetorMapper;
import dev.squad04.projetoFlap.board.service.SetorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/flapboard/setor")
public class SetorController {

    private final SetorService setorService;
    private final SetorMapper setorMapper;

    public SetorController(SetorService setorService, SetorMapper setorMapper) {
        this.setorService = setorService;
        this.setorMapper = setorMapper;
    }

    @PostMapping
    public ResponseEntity<Setor> criarSetor(@RequestBody CriarSetorDTO data) {
        Setor novoSetor = setorService.criarSetor(data);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoSetor);
    }

    @PostMapping("/associar/{idSetor}")
    public ResponseEntity<AssociacaoResponseDTO> associarUsuario(@PathVariable Integer idSetor, @RequestBody AssociarUsuarioSetorDTO data) {
        UsuarioSetor usuarioSetor = setorService.associarUsuario(idSetor, data);
        return ResponseEntity.ok(setorMapper.toDTO(usuarioSetor));
    }

    @DeleteMapping("/desassociar/{idSetor}/{idUsuario}")
    public ResponseEntity<Void> desassociarUsuario(@PathVariable Integer idSetor, @PathVariable Integer idUsuario) {
        setorService.desassociarUsuario(idSetor, idUsuario);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<Setor>> listarTodos() {
        List<Setor> setores = setorService.listarTodos();
        return ResponseEntity.ok(setores);
    }
}
