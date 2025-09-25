package dev.squad04.projetoFlap.board.dto.setor;

import java.time.LocalDateTime;

public record AssociacaoResponseDTO(
        Integer idUsuario,
        String nomeUsuario,
        Integer idSetor,
        String nomeSetor,
        LocalDateTime associadoEm
) {
}
