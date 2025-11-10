package dev.squad04.projetoFlap.board.controller;

import dev.squad04.projetoFlap.board.dto.tarefa.*;
import dev.squad04.projetoFlap.board.entity.Tarefa;
import dev.squad04.projetoFlap.board.mapper.TarefaMapper;
import dev.squad04.projetoFlap.board.service.TarefaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/flapboard/tarefa")
@Tag(name = "Tarefas", description = "Endpoints para manipulação de tarefas dentro dos quadros.")
public class TarefaController {

    private final TarefaService tarefaService;
    private final TarefaMapper tarefaMapper;

    public TarefaController(TarefaService tarefaService, TarefaMapper tarefaMapper) {
        this.tarefaService = tarefaService;
        this.tarefaMapper = tarefaMapper;
    }

    @Operation(summary = "Cria uma nova tarefa em um quadro")
    @ApiResponse(responseCode = "201", description = "Tarefa criada com sucesso")
    @PostMapping
    public ResponseEntity<TarefaResponseDTO> criarTarefa(@RequestBody CriarTarefaDTO data) {
        Tarefa novaTarefa = tarefaService.criarTarefa(data);
        return ResponseEntity.status(HttpStatus.CREATED).body(tarefaMapper.toDTO(novaTarefa));
    }

    @Operation(summary = "Atualiza os dados de uma tarefa")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tarefa atualizada"),
            @ApiResponse(responseCode = "404", description = "Tarefa não encontrada")
    })
    @PutMapping("/{idTarefa}")
    public ResponseEntity<TarefaResponseDTO> atualizarTarefa(@PathVariable Integer idTarefa, @RequestBody AtualizarTarefaDTO data) {
        Tarefa tarefaAtualizada = tarefaService.atualizarTarefa(idTarefa, data);
        return ResponseEntity.ok(tarefaMapper.toDTO(tarefaAtualizada));
    }

    @Operation(summary = "Busca todas as tarefas de um quadro")
    @GetMapping("/quadro/{idQuadro}")
    public ResponseEntity<List<TarefaResponseDTO>> buscarTarefaPorQuadro(@PathVariable Integer idQuadro) {
        List<Tarefa> tarefas = tarefaService.buscarTarefasPorQuadro(idQuadro);
        return ResponseEntity.ok(tarefaMapper.toDTOList(tarefas));
    }

    @Operation(summary = "Busca uma tarefa pelo seu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tarefa encontrada"),
            @ApiResponse(responseCode = "404", description = "Tarefa não encontrada")
    })
    @GetMapping("/{idTarefa}")
    public ResponseEntity<TarefaResponseDTO> buscarTarefaPorId(@PathVariable Integer idTarefa) {
        Tarefa tarefa = tarefaService.buscarPorId(idTarefa);
        return ResponseEntity.ok(tarefaMapper.toDTO(tarefa));
    }

    @Operation(summary = "Move uma tarefa para um novo status", description = "Altera o status de uma tarefa e cria um registro de histórico.")
    @PutMapping("/mover/{idTarefa}")
    public ResponseEntity<TarefaResponseDTO> moverTarefaParaStatus(@PathVariable Integer idTarefa, @RequestBody MoverTarefaDTO data) {
        Tarefa tarefaAtualizada = tarefaService.moverTarefaParaStatus(idTarefa, data);
        return ResponseEntity.ok(tarefaMapper.toDTO(tarefaAtualizada));
    }

    @Operation(summary = "Exclui uma tarefa")
    @ApiResponse(responseCode = "204", description = "Tarefa excluída com sucesso")
    @DeleteMapping("/{idTarefa}")
    public ResponseEntity<Void> deletarTarefa(@PathVariable Integer idTarefa) {
        tarefaService.deletarTarefa(idTarefa);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Atribui um usuário como responsável por uma tarefa")
    @PatchMapping("/responsavel/{idTarefa}")
    public ResponseEntity<TarefaResponseDTO> atribuirResponsavel(@PathVariable Integer idTarefa, @RequestBody AtribuirResponsavelDTO data) {
        Tarefa tarefa = tarefaService.atribuirResponsavel(idTarefa, data);
        return ResponseEntity.ok(tarefaMapper.toDTO(tarefa));
    }

    @Operation(summary = "Adiciona um novo comentário a uma tarefa")
    @PostMapping("/comentario/{idTarefa}")
    public ResponseEntity<TarefaResponseDTO> adicionarComentario(@PathVariable Integer idTarefa, @RequestBody AdicionarComentarioDTO data) {
        Tarefa tarefa = tarefaService.adicionarComentario(idTarefa, data);
        return ResponseEntity.ok(tarefaMapper.toDTO(tarefa));
    }
}
