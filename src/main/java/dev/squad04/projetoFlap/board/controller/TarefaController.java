package dev.squad04.projetoFlap.board.controller;

import dev.squad04.projetoFlap.board.dto.comentario.ComentarioDTO;
import dev.squad04.projetoFlap.board.dto.tarefa.*;
import dev.squad04.projetoFlap.board.entity.Tarefa;
import dev.squad04.projetoFlap.board.service.TarefaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/flapboard/tarefa")
public class TarefaController {

    private final TarefaService tarefaService;

    public TarefaController(TarefaService tarefaService) {
        this.tarefaService = tarefaService;
    }

    @PostMapping
    public ResponseEntity<TarefaResponseDTO> criarTarefa(@RequestBody CriarTarefaDTO data) {
        Tarefa novaTarefa = tarefaService.criarTarefa(data);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDto(novaTarefa));
    }

    @GetMapping("/quadro/{idQuadro}")
    public ResponseEntity<List<TarefaResponseDTO>> buscarTarefaPorQuadro(@PathVariable Integer idQuadro) {
        // 1. O service retorna a lista de entidades, como antes
        List<Tarefa> tarefas = tarefaService.buscarTarefasPorQuadro(idQuadro);

        // 2. Mapeia a lista de entidades para uma lista de DTOs
        List<TarefaResponseDTO> dtos = tarefas.stream()
                .map(tarefa -> { // Inicia o mapeamento para CADA tarefa da lista

                    // PASSO A: Para ESTA tarefa, converte sua lista de Comentario (entidade)
                    // para uma lista de ComentarioResponseDTO.
                    Set<ComentarioDTO> comentariosDto = tarefa.getComentarios().stream()
                            .map(comentario -> new ComentarioDTO(
                                    comentario.getIdComentario(),
                                    comentario.getMensagem(),
                                    comentario.getCreatedAt(),
                                    comentario.getUsuario().getIdUsuario(),
                                    comentario.getUsuario().getNome()
                            ))
                            .collect(Collectors.toSet());

                    // PASSO B: Agora, cria o DTO da tarefa principal, passando a
                    // lista de DTOs de comentários que acabamos de criar.
                    return new TarefaResponseDTO(
                            tarefa.getIdTarefa(),
                            tarefa.getTitulo(),
                            tarefa.getDescricao(),
                            tarefa.getStatus().getIdStatus(),
                            tarefa.getStatus().getNome(),
                            tarefa.getPrazo(),
                            tarefa.getQuadro().getIdQuadro(),
                            tarefa.getQuadro().getNome(),
                            tarefa.getResponsavel() != null ? tarefa.getResponsavel().getIdUsuario() : null,
                            tarefa.getResponsavel() != null ? tarefa.getResponsavel().getNome() : null,
                            comentariosDto // <-- Passando a lista de DTOs, não de entidades
                    );
                })
                .collect(Collectors.toList()); // Coleta todos os TarefaResponseDTOs em uma lista final

        return ResponseEntity.ok(dtos);
    }

    @PutMapping("/mover/{idTarefa}")
    public ResponseEntity<TarefaResponseDTO> moverTarefaParaStatus(@PathVariable Integer idTarefa, @RequestBody MoverTarefaDTO data) {
        Tarefa tarefaAtualizada = tarefaService.moverTarefaParaStatus(idTarefa, data);
        return ResponseEntity.ok(toDto(tarefaAtualizada));
    }

    @DeleteMapping("/{idTarefa}")
    public ResponseEntity<Void> deletarTarefa(@PathVariable Integer idTarefa) {
        tarefaService.deletarTarefa(idTarefa);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/responsavel/{idTarefa}")
    public ResponseEntity<TarefaResponseDTO> atribuirResponsavel(@PathVariable Integer idTarefa, @RequestBody AtribuirResponsavelDTO data) {
        Tarefa tarefa = tarefaService.atribuirResponsavel(idTarefa, data);
        return ResponseEntity.ok(toDto(tarefa));
    }

    @PostMapping("/comentario/{idTarefa}")
    public ResponseEntity<TarefaResponseDTO> adicionarComentario(@PathVariable Integer idTarefa, @RequestBody AdicionarComentarioDTO data) {
        Tarefa tarefa = tarefaService.adicionarComentario(idTarefa, data);
        return ResponseEntity.ok(toDto(tarefa));
    }

    private TarefaResponseDTO toDto(Tarefa tarefa) {

        Set<ComentarioDTO> comentariosDto = tarefa.getComentarios().stream()
                .map(comentario -> new ComentarioDTO(
                        comentario.getIdComentario(),
                        comentario.getMensagem(),
                        comentario.getCreatedAt(),
                        comentario.getUsuario().getIdUsuario(),
                        comentario.getUsuario().getNome()
                ))
                .collect(Collectors.toSet());

        return new TarefaResponseDTO(
                tarefa.getIdTarefa(),
                tarefa.getTitulo(),
                tarefa.getDescricao(),
                tarefa.getStatus().getIdStatus(),
                tarefa.getStatus().getNome(),
                tarefa.getPrazo(),
                tarefa.getQuadro().getIdQuadro(),
                tarefa.getQuadro().getNome(),
                tarefa.getResponsavel() != null ? tarefa.getResponsavel().getIdUsuario() : null,
                tarefa.getResponsavel() != null ? tarefa.getResponsavel().getNome() : null,
                comentariosDto
        );
    }
}
