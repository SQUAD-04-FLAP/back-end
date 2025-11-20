package dev.squad04.projetoFlap.board.dto.tarefa;

import java.time.LocalDateTime;

public record CriarTarefaDTO(
        String titulo,
        String descricao,
        LocalDateTime dtTermino,
        String prioridade,
        Integer idQuadro,
        Integer idCriador) {}
