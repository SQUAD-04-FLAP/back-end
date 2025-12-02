package dev.squad04.projetoFlap.board.dto.dashboard;

public record DashboardStatusDTO(
        long totalTarefas,
        long tarefasConcluidas,
        long tarefasPendentes,
        long tarefasAtrasadas
) {
}
