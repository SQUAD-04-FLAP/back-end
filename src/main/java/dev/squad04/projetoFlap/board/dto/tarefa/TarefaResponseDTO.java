package dev.squad04.projetoFlap.board.dto.tarefa;

import dev.squad04.projetoFlap.board.dto.anexo.AnexoDTO;
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
        String nomeSetor,
        Set<ResponsavelDTO> responsaveis,
        Set<ComentarioDTO> comentarios,
        Set<AnexoDTO> anexos,
        LocalDateTime dtTermino,
        String prioridade,
        Boolean ativo,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
