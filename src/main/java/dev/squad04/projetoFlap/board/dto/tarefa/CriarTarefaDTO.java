package dev.squad04.projetoFlap.board.dto.tarefa;

import java.time.LocalDateTime;
import java.util.List;

public record CriarTarefaDTO(
        String titulo,
        String descricao,
        LocalDateTime dtTermino,
        String prioridade,
        Integer idCriador,
        Integer idQuadro,
        Integer idSetor,
        List<Integer> idsResponsaveis) {}
