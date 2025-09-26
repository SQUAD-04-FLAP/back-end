package dev.squad04.projetoFlap.board.controller;

import dev.squad04.projetoFlap.board.dto.tarefa.*;
import dev.squad04.projetoFlap.board.entity.Tarefa;
import dev.squad04.projetoFlap.board.mapper.TarefaMapper;
import dev.squad04.projetoFlap.board.service.TarefaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/flapboard/tarefa")
public class TarefaController {

    private final TarefaService tarefaService;
    private final TarefaMapper tarefaMapper;

    public TarefaController(TarefaService tarefaService, TarefaMapper tarefaMapper) {
        this.tarefaService = tarefaService;
        this.tarefaMapper = tarefaMapper;
    }

    @PostMapping
    public ResponseEntity<TarefaResponseDTO> criarTarefa(@RequestBody CriarTarefaDTO data) {
        Tarefa novaTarefa = tarefaService.criarTarefa(data);
        return ResponseEntity.status(HttpStatus.CREATED).body(tarefaMapper.toDTO(novaTarefa));
    }

    @GetMapping("/quadro/{idQuadro}")
    public ResponseEntity<List<TarefaResponseDTO>> buscarTarefaPorQuadro(@PathVariable Integer idQuadro) {
        List<Tarefa> tarefas = tarefaService.buscarTarefasPorQuadro(idQuadro);
        return ResponseEntity.ok(tarefaMapper.toDTOList(tarefas));
    }

    @PutMapping("/mover/{idTarefa}")
    public ResponseEntity<TarefaResponseDTO> moverTarefaParaStatus(@PathVariable Integer idTarefa, @RequestBody MoverTarefaDTO data) {
        Tarefa tarefaAtualizada = tarefaService.moverTarefaParaStatus(idTarefa, data);
        return ResponseEntity.ok(tarefaMapper.toDTO(tarefaAtualizada));
    }

    @DeleteMapping("/{idTarefa}")
    public ResponseEntity<Void> deletarTarefa(@PathVariable Integer idTarefa) {
        tarefaService.deletarTarefa(idTarefa);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/responsavel/{idTarefa}")
    public ResponseEntity<TarefaResponseDTO> atribuirResponsavel(@PathVariable Integer idTarefa, @RequestBody AtribuirResponsavelDTO data) {
        Tarefa tarefa = tarefaService.atribuirResponsavel(idTarefa, data);
        return ResponseEntity.ok(tarefaMapper.toDTO(tarefa));
    }

    @PostMapping("/comentario/{idTarefa}")
    public ResponseEntity<TarefaResponseDTO> adicionarComentario(@PathVariable Integer idTarefa, @RequestBody AdicionarComentarioDTO data) {
        Tarefa tarefa = tarefaService.adicionarComentario(idTarefa, data);
        return ResponseEntity.ok(tarefaMapper.toDTO(tarefa));
    }
}
