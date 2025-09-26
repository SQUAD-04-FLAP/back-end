package dev.squad04.projetoFlap.board.dto.quadro;

import java.util.List;

public record QuadroDTO(
        String nome,
        Integer idSetor,
        List<String> statusPadroes) {}
