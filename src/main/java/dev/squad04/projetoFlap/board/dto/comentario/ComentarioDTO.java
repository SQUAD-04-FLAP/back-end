package dev.squad04.projetoFlap.board.dto.comentario;

import java.time.LocalDateTime;

public record ComentarioDTO(
        Integer idComentario,
        String mensagem,
        LocalDateTime createdAt,
        Integer idUsuario,
        String nomeUsuario
) {
}
