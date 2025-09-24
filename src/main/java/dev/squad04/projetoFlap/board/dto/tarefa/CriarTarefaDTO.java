package dev.squad04.projetoFlap.board.dto.tarefa;

public record CriarTarefaDTO(
        String titulo,
        String descricao,
        Integer idQuadro,
        Integer idCriador) {}
