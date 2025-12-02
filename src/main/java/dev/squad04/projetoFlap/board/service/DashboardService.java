package dev.squad04.projetoFlap.board.service;

import dev.squad04.projetoFlap.board.dto.dashboard.DashboardStatusDTO;
import dev.squad04.projetoFlap.board.entity.Tarefa;
import dev.squad04.projetoFlap.board.repository.SetorRepository;
import dev.squad04.projetoFlap.board.repository.TarefaRepository;
import dev.squad04.projetoFlap.exceptions.AppException;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DashboardService {

    private final SetorRepository setorRepository;
    private final TarefaRepository tarefaRepository;

    public DashboardService(SetorRepository setorRepository, TarefaRepository tarefaRepository) {
        this.setorRepository = setorRepository;
        this.tarefaRepository = tarefaRepository;
    }

    public List<Tarefa> buscarTarefasProximasAoVencimento(Integer idSetor) {
        if (idSetor != null) {
            if (!setorRepository.existsById(idSetor)) {
                throw new AppException("Setor/empresa não encontrado!", HttpStatus.NOT_FOUND);
            }
            return tarefaRepository.findTop10BySetorIdSetorAndAtivoTrueOrderByDtTerminoAsc(idSetor);
        }
        return tarefaRepository.findTop10ByAtivoTrueOrderByDtTerminoAsc();
    }

    @Transactional
    public DashboardStatusDTO gerarEstatisticasDashboard(Integer idSetor) {
        long total;
        long concluidas;
        long atrasadas;

        String statusConcluido = "Concluído";
        LocalDateTime horaAtual = LocalDateTime.now();

        if (idSetor != null) {
            if (!setorRepository.existsById(idSetor)) {
                throw new AppException("Setor não encontrado", HttpStatus.NOT_FOUND);
            }
            total = tarefaRepository.countBySetorIdSetor(idSetor);
            concluidas = tarefaRepository.countBySetorIdSetorAndStatusNome(idSetor, statusConcluido);
            atrasadas = tarefaRepository.countBySetorIdSetorAndDtTerminoBeforeAndStatusNomeNot(idSetor, horaAtual, statusConcluido);
        } else {
            total = tarefaRepository.count();
            concluidas = tarefaRepository.countByStatusNome(statusConcluido);
            atrasadas = tarefaRepository.countByDtTerminoBeforeAndStatusNomeNot(horaAtual, statusConcluido);
        }
        long pendentes = total - concluidas;

        return new DashboardStatusDTO(total, concluidas, pendentes, atrasadas);

    }
}
