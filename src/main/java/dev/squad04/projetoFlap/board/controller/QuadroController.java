package dev.squad04.projetoFlap.board.controller;

import dev.squad04.projetoFlap.board.dto.quadro.QuadroDTO;
import dev.squad04.projetoFlap.board.entity.Quadro;
import dev.squad04.projetoFlap.board.service.QuadroService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/flapbaord/quadro")
public class QuadroController {

    private final QuadroService quadroService;

    public QuadroController(QuadroService quadroService) {
        this.quadroService = quadroService;
    }

    @PostMapping
    public ResponseEntity<Quadro> criarQuadro(@RequestBody QuadroDTO data) {
        Quadro novoQuadro = quadroService.criarQuadroComStatus(data);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoQuadro);
    }

    @GetMapping("/setor/{idSetor}")
    public ResponseEntity<List<Quadro>> listarPorSetor(@PathVariable Integer idSetor) {
        List<Quadro> quadros = quadroService.listarPorSetor(idSetor);
        return ResponseEntity.ok(quadros);
    }
}
