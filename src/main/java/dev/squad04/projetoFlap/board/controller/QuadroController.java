package dev.squad04.projetoFlap.board.controller;

import dev.squad04.projetoFlap.board.dto.quadro.AtualizarQuadroDTO;
import dev.squad04.projetoFlap.board.dto.quadro.QuadroDTO;
import dev.squad04.projetoFlap.board.dto.quadro.QuadroResponseDTO;
import dev.squad04.projetoFlap.board.dto.quadro.WorkflowStatusDTO;
import dev.squad04.projetoFlap.board.dto.status.AtualizarStatusDTO;
import dev.squad04.projetoFlap.board.dto.status.CriarStatusDTO;
import dev.squad04.projetoFlap.board.entity.Quadro;
import dev.squad04.projetoFlap.board.entity.WorkflowStatus;
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

    @Operation(summary = "Busca todos os quadros")
    @ApiResponse(responseCode = "200", description = "Lista de todos os quadros")
    @GetMapping
    public ResponseEntity<List<QuadroResponseDTO>> buscarTodosQuadros() {
        List<Quadro> quadros = quadroService.buscarTodosQuadros();
        return ResponseEntity.ok(quadroMapper.toDTOList(quadros));
    }

    @Operation(summary = "Atualiza os dados de um quadro")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Quadro atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Quadro ou Setor de destino não encontrado")
    })
    @PutMapping("/{idQuadro}")
    public ResponseEntity<QuadroResponseDTO> atualizarQuadro(
            @PathVariable Integer idQuadro, @RequestBody AtualizarQuadroDTO dto) {

        Quadro quadroAtualizado = quadroService.atualizarQuadro(idQuadro, dto);
        return ResponseEntity.ok(quadroMapper.toDTO(quadroAtualizado));
    }

    @Operation(summary = "Deleta um quadro pelo seu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Quadro deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Quadro não encontrado")
    })
    @DeleteMapping("/{idQuadro}")
    public ResponseEntity<Void> deletarQuadro(@PathVariable Integer idQuadro) {
        quadroService.deletarQuadro(idQuadro);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Adiciona um novo status (coluna) a um quadro")
    @ApiResponse(responseCode = "200", description = "Status adicionado, retorna o quadro atualizado")
    @PostMapping("/{idQuadro}/status")
    public ResponseEntity<QuadroResponseDTO> adicionarStatus(
            @PathVariable Integer idQuadro,
            @RequestBody CriarStatusDTO dto) {

        Quadro quadroAtualizado = quadroService.adicionarStatusAoQuadro(idQuadro, dto);
        return ResponseEntity.ok(quadroMapper.toDTO(quadroAtualizado));
    }

    @Operation(summary = "Atualiza um status (coluna) existente")
    @ApiResponse(responseCode = "200", description = "Status atualizado")
    @PutMapping("/status/{idStatus}")
    public ResponseEntity<WorkflowStatusDTO> atualizarStatus(
            @PathVariable Integer idStatus,
            @RequestBody AtualizarStatusDTO dto) {

        WorkflowStatus statusAtualizado = quadroService.atualizarStatus(idStatus, dto);
        return ResponseEntity.ok(quadroMapper.toStatusDTO(statusAtualizado));
    }

    @Operation(summary = "Exclui um status (coluna) de um quadro")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Status excluído com sucesso"),
            @ApiResponse(responseCode = "400", description = "Não é possível excluir, status ainda possui tarefas")
    })
    @DeleteMapping("/status/{idStatus}")
    public ResponseEntity<Void> deletarStatus(@PathVariable Integer idStatus) {
        quadroService.deletarStatus(idStatus);
        return ResponseEntity.noContent().build();
    }
}
