package dev.squad04.projetoFlap.board.dto.quadro;

import java.time.LocalDateTime;
import java.util.Set;

public record QuadroResponseDTO(
    Integer idQuadro,
    String nome,
    Integer idSetor,
    String nomeSetor,
    Set<WorkflowStatusDTO> status,
    Boolean ativo,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}
