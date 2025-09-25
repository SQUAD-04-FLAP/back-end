package dev.squad04.projetoFlap.board.controller;

import dev.squad04.projetoFlap.board.dto.quadro.QuadroDTO;
import dev.squad04.projetoFlap.board.dto.quadro.QuadroResponseDTO;
import dev.squad04.projetoFlap.board.entity.Quadro;
import dev.squad04.projetoFlap.board.service.QuadroService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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
    public ResponseEntity<List<QuadroResponseDTO>> listarPorSetor(@PathVariable Integer idSetor) {
        List<Quadro> quadros = quadroService.listarPorSetor(idSetor);

        List<QuadroResponseDTO> dtos = quadros.stream()
                .map(quadro -> new QuadroResponseDTO(
                        quadro.getIdQuadro(),
                        quadro.getNome(),
                        quadro.getAtivo(),
                        quadro.getSetor() != null ? quadro.getSetor().getIdSetor() : null,
                        quadro.getSetor() != null ? quadro.getSetor().getNome() : null
                ))
                .toList();

        return ResponseEntity.ok(dtos);
    }
}
