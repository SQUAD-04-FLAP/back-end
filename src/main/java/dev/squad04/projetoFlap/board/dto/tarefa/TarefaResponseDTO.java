package dev.squad04.projetoFlap.board.dto.tarefa;

import dev.squad04.projetoFlap.board.dto.comentario.ComentarioDTO;

import java.time.LocalDateTime;
import java.util.Set;

public record TarefaResponseDTO (
        Integer idTarefa,
        String titulo,
        String descricao,
        Integer idQuadro,
        String nomeQuadro,
        Integer idStatus,
        String nomeStatus,
        Integer idCriadoPor,
        String nomeCriadoPor,
        Integer idResponsavel,
        String nomeResponsavel,
        Set<ComentarioDTO> comentarios,
        LocalDateTime prazo,
        Boolean ativo,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
