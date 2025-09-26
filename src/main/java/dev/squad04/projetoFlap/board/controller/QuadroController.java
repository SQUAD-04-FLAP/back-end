package dev.squad04.projetoFlap.board.controller;

import dev.squad04.projetoFlap.board.dto.quadro.QuadroDTO;
import dev.squad04.projetoFlap.board.dto.quadro.QuadroResponseDTO;
import dev.squad04.projetoFlap.board.entity.Quadro;
import dev.squad04.projetoFlap.board.mapper.QuadroMapper;
import dev.squad04.projetoFlap.board.service.QuadroService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/flapbaord/quadro")
public class QuadroController {

    private final QuadroService quadroService;
    private final QuadroMapper quadroMapper;

    public QuadroController(QuadroService quadroService, QuadroMapper quadroMapper) {
        this.quadroService = quadroService;
        this.quadroMapper = quadroMapper;
    }

    @PostMapping
    public ResponseEntity<QuadroResponseDTO> criarQuadro(@RequestBody QuadroDTO data) {
        Quadro novoQuadro = quadroService.criarQuadroComStatus(data);
        return ResponseEntity.status(HttpStatus.CREATED).body(quadroMapper.toDTO(novoQuadro));
    }

    @GetMapping("/setor/{idSetor}")
    public ResponseEntity<List<QuadroResponseDTO>> listarPorSetor(@PathVariable Integer idSetor) {
        List<Quadro> quadros = quadroService.listarPorSetor(idSetor);
        return ResponseEntity.ok(quadroMapper.toDTOList(quadros));
    }
}
