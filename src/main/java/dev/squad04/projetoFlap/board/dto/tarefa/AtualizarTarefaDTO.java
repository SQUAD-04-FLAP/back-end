package dev.squad04.projetoFlap.board.dto.tarefa;

import java.time.LocalDateTime;
import java.util.List;

public record AtualizarTarefaDTO(
        String titulo,
        String descricao,
        LocalDateTime dtTermino,
        Boolean ativo,
        String prioridade,
        Integer idSetor,
        List<Integer> idsResponsaveis) {}
