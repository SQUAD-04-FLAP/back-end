package dev.squad04.projetoFlap.board.controller;

import dev.squad04.projetoFlap.board.dto.perfil.PerfilDTO;
import dev.squad04.projetoFlap.board.entity.Perfil;
import dev.squad04.projetoFlap.board.service.PerfilService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/flapboard/perfil")
public class PerfilController {

    private final PerfilService perfilService;

    public PerfilController(PerfilService perfilService) {
        this.perfilService = perfilService;
    }

    @PostMapping
    public ResponseEntity<Perfil> criarPerfil(@RequestBody PerfilDTO data) {
        Perfil novoPerfil = perfilService.criarPerfil(data);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoPerfil);
    }

    @GetMapping("/id/{idPerfil}")
    public ResponseEntity<Optional<Perfil>> buscarPorId(@PathVariable Integer idPerfil) {
        Optional<Perfil> perfilOptional = perfilService.buscarPorId(idPerfil);
        return ResponseEntity.ok(perfilOptional);
    }

    @GetMapping
    public ResponseEntity<List<Perfil>> listarTodos() {
        List<Perfil> perfis = perfilService.listarTodos();
        return ResponseEntity.ok(perfis);
    }

    @PutMapping("/id/{idPerfil}")
    public ResponseEntity<Perfil> atualizarPerfil(@PathVariable Integer idPerfil, @RequestBody PerfilDTO data) {
        Perfil perfilAtt = perfilService.atualizarPerfil(idPerfil, data);
        return ResponseEntity.ok(perfilAtt);
    }

    @DeleteMapping("/id/{idPerfil}")
    public ResponseEntity<Void> deletarPerfil(@PathVariable Integer idPerfil) {
        perfilService.deletarPerfil(idPerfil);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
