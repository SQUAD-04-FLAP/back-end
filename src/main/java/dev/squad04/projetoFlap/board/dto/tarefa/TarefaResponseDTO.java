package dev.squad04.projetoFlap.board.dto.tarefa;

import dev.squad04.projetoFlap.board.dto.comentario.ComentarioDTO;

import java.time.LocalDateTime;
import java.util.Set;

public record TarefaResponseDTO (
        Integer idTarefa,
        String titulo,
        String descricao,
        Integer idStatus,
        String nomeStatus,
        LocalDateTime prazo,
        Integer idQuadro,
        String nomeQuadro,
        Integer idResponsavel,
        String nomeResponsavel,
        Set<ComentarioDTO> comentarios
) {}
