package dev.squad04.projetoFlap.board.controller;

import dev.squad04.projetoFlap.board.dto.quadro.QuadroDTO;
import dev.squad04.projetoFlap.board.dto.quadro.QuadroResponseDTO;
import dev.squad04.projetoFlap.board.entity.Quadro;
import dev.squad04.projetoFlap.board.mapper.QuadroMapper;
import dev.squad04.projetoFlap.board.service.QuadroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/flapboard/quadro")
@Tag(name = "Quadros", description = "Gerenciamento de quadros (boards).")
public class QuadroController {

    private final QuadroService quadroService;
    private final QuadroMapper quadroMapper;

    public QuadroController(QuadroService quadroService, QuadroMapper quadroMapper) {
        this.quadroService = quadroService;
        this.quadroMapper = quadroMapper;
    }

    @Operation(summary = "Cria um novo quadro", description = "Cria um quadro e seus status iniciais (colunas).")
    @ApiResponse(responseCode = "201", description = "Quadro criado com sucesso")
    @PostMapping
    public ResponseEntity<QuadroResponseDTO> criarQuadro(@RequestBody QuadroDTO data) {
        Quadro novoQuadro = quadroService.criarQuadroComStatus(data);
        return ResponseEntity.status(HttpStatus.CREATED).body(quadroMapper.toDTO(novoQuadro));
    }

    @Operation(summary = "Lista todos os quadros de um setor específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Quadros listados com sucesso"),
            @ApiResponse(responseCode = "200", description = "[] Setor não encontrado")
    })
    @GetMapping("/setor/{idSetor}")
    public ResponseEntity<List<QuadroResponseDTO>> listarPorSetor(@PathVariable Integer idSetor) {
        List<Quadro> quadros = quadroService.listarPorSetor(idSetor);
        return ResponseEntity.ok(quadroMapper.toDTOList(quadros));
    }

    @Operation(summary = "Busca um quadro pelo seu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Quadro encontrado"),
            @ApiResponse(responseCode = "404", description = "Quadro não encontrado")
    })
    @GetMapping("/{idQuadro}")
    public ResponseEntity<QuadroResponseDTO> buscarQuadroPorId(@PathVariable Integer idQuadro) {
        Quadro quadro = quadroService.buscarPorId(idQuadro);
        return ResponseEntity.ok(quadroMapper.toDTO(quadro));
    }
}
