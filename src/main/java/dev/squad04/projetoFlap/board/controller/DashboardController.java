package dev.squad04.projetoFlap.board.controller;

import dev.squad04.projetoFlap.board.dto.tarefa.TarefaResponseDTO;
import dev.squad04.projetoFlap.board.entity.Tarefa;
import dev.squad04.projetoFlap.board.mapper.TarefaMapper;
import dev.squad04.projetoFlap.board.service.TarefaService;
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

    private final TarefaService tarefaService;
    private final TarefaMapper tarefaMapper;

    public DashboardController(TarefaService tarefaService, TarefaMapper tarefaMapper) {
        this.tarefaService = tarefaService;
        this.tarefaMapper = tarefaMapper;
    }

    @Operation(summary = "Lista tarefas próximas ao vencimento",
            description = "Retorna as 10 tarefas mais urgentes. Se idSetor for informado, filtra pela empresa.")
    @GetMapping("/proximas/{idSetor}")
    public ResponseEntity<List<TarefaResponseDTO>> buscarTarefasProximasAoVencimento(
            @RequestParam(required = false) Integer idSetor) {
        List<Tarefa> tarefas = tarefaService.buscarTarefasProximasAoVencimento(idSetor);
        return ResponseEntity.ok(tarefaMapper.toDTOList(tarefas));
    }
}
