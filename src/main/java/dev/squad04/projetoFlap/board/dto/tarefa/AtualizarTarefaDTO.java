package dev.squad04.projetoFlap.board.dto.tarefa;

import java.time.LocalDateTime;

public record AtualizarTarefaDTO(
        String titulo,
        String descricao,
        LocalDateTime dtTermino,
        Boolean ativo,
        String prioridade) {}
