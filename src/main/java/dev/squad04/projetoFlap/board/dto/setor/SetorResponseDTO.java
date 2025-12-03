package dev.squad04.projetoFlap.board.dto.setor;

import dev.squad04.projetoFlap.board.dto.anexo.AnexoDTO;

import java.time.LocalDateTime;
import java.util.Set;

public record SetorResponseDTO(
        Integer idSetor,
        String nome,
        String descricao,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Set<AnexoDTO> anexos
) {
}
