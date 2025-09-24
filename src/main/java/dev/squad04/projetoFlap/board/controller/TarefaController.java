package dev.squad04.projetoFlap.board.controller;

import dev.squad04.projetoFlap.board.dto.tarefa.AdicionarComentarioDTO;
import dev.squad04.projetoFlap.board.dto.tarefa.CriarTarefaDTO;
import dev.squad04.projetoFlap.board.dto.tarefa.MoverTarefaDTO;
import dev.squad04.projetoFlap.board.entity.Tarefa;
import dev.squad04.projetoFlap.board.service.TarefaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/flapboard/tarefa")
public class TarefaController {

    private final TarefaService tarefaService;

    public TarefaController(TarefaService tarefaService) {
        this.tarefaService = tarefaService;
    }

    @PostMapping
    public ResponseEntity<Tarefa> criarTarefa(@RequestBody CriarTarefaDTO data) {
        Tarefa novaTarefa = tarefaService.criarTarefa(data);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaTarefa);
    }

    @GetMapping("/quadro/{idQuadro}")
    public ResponseEntity<List<Tarefa>> buscarTarefaPorQuadro(@PathVariable Integer idQuadro) {
        List<Tarefa> tarefas = tarefaService.buscarTarefasPorQuadro(idQuadro);
        return ResponseEntity.ok(tarefas);
    }

    @PutMapping("/mover/{idTarefa}")
    public ResponseEntity<Tarefa> moverTarefaParaStatus(@PathVariable Integer idTarefa, @RequestBody MoverTarefaDTO data) {
        Tarefa tarefaAtualizada = tarefaService.moverTarefaParaStatus(idTarefa, data);
        return ResponseEntity.ok(tarefaAtualizada);
    }

    @DeleteMapping("/{idTarefa}")
    public ResponseEntity<Void> deletarTarefa(@PathVariable Integer idTarefa) {
        tarefaService.deletarTarefa(idTarefa);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/responsavel/{idTarefa}")
    public ResponseEntity<Tarefa> atribuirResponsavel(@PathVariable Integer idTarefa, @PathVariable Integer idResponsavel) {
        Tarefa tarefa = tarefaService.atribuirResponsavel(idTarefa, idResponsavel);
        return ResponseEntity.ok(tarefa);
    }

    @PostMapping("/comentario/{idTarefa}")
    public ResponseEntity<Tarefa> adicionarComentario(@PathVariable Integer idTarefa, @RequestBody AdicionarComentarioDTO data) {
        Tarefa tarefa = tarefaService.adicionarComentario(idTarefa, data);
        return ResponseEntity.ok(tarefa);
    }
}
