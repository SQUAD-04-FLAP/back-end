package dev.squad04.projetoFlap.board.controller;

import dev.squad04.projetoFlap.board.dto.dashboard.DashboardStatusDTO;
import dev.squad04.projetoFlap.board.dto.tarefa.TarefaResponseDTO;
import dev.squad04.projetoFlap.board.entity.Tarefa;
import dev.squad04.projetoFlap.board.mapper.TarefaMapper;
import dev.squad04.projetoFlap.board.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/flapboard/dashboard")
@Tag(name = "Dashboard", description = "Endpoints para consulta de dados para o dashboard.")
public class DashboardController {

    private final DashboardService dashboardService;
    private final TarefaMapper tarefaMapper;

    public DashboardController(DashboardService dashboardService, TarefaMapper tarefaMapper) {
        this.dashboardService = dashboardService;
        this.tarefaMapper = tarefaMapper;
    }

    @Operation(summary = "Lista tarefas próximas ao vencimento",
            description = "Retorna as 10 tarefas mais urgentes. Se idSetor for informado, filtra pela empresa.")
    @GetMapping("/proximas/{idSetor}")
    public ResponseEntity<List<TarefaResponseDTO>> buscarTarefasProximasAoVencimento(
            @RequestParam(required = false) Integer idSetor) {
        List<Tarefa> tarefas = dashboardService.buscarTarefasProximasAoVencimento(idSetor);
        return ResponseEntity.ok(tarefaMapper.toDTOList(tarefas));
    }

    @Operation(summary = "Obtém dados do dashboard",
            description = "Se idSetor for informado, retorna dados da empresa. Se não, retorna dados gerais.")
    @GetMapping("/{idSetor}")
    public ResponseEntity<DashboardStatusDTO> gerarEstatisticasDashboard(
            @RequestParam(required = false) Integer idSetor) {
        DashboardStatusDTO estatisticas = dashboardService.gerarEstatisticasDashboard(idSetor);
        return ResponseEntity.ok(estatisticas);
    }
}
