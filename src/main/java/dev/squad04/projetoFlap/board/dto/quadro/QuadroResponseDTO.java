package dev.squad04.projetoFlap.board.dto.quadro;

public record QuadroResponseDTO(
    Integer idQuadro,
    String nome,
    Boolean ativo,
    Integer idSetor,
    String nomeSetor) {}
