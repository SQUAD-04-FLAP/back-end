package dev.squad04.projetoFlap.board.service;

import dev.squad04.projetoFlap.board.dto.quadro.AtualizarQuadroDTO;
import dev.squad04.projetoFlap.board.dto.quadro.QuadroDTO;
import dev.squad04.projetoFlap.board.dto.status.CriarStatusDTO;
import dev.squad04.projetoFlap.board.entity.Quadro;
import dev.squad04.projetoFlap.board.entity.Setor;
import dev.squad04.projetoFlap.board.entity.WorkflowStatus;
import dev.squad04.projetoFlap.board.repository.QuadroRepository;
import dev.squad04.projetoFlap.board.repository.SetorRepository;
import dev.squad04.projetoFlap.board.repository.TarefaRepository;
import dev.squad04.projetoFlap.board.repository.WorkflowStatusRepository;
import dev.squad04.projetoFlap.exceptions.AppException;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
public class QuadroService {

    private final QuadroRepository quadroRepository;
    private final SetorRepository setorRepository;
    private final WorkflowStatusRepository workflowStatusRepository;
    private final TarefaRepository tarefaRepository;

    public QuadroService(QuadroRepository quadroRepository, SetorRepository setorRepository, WorkflowStatusRepository workflowStatusRepository, TarefaRepository tarefaRepository) {
        this.quadroRepository = quadroRepository;
        this.setorRepository = setorRepository;
        this.workflowStatusRepository = workflowStatusRepository;
        this.tarefaRepository = tarefaRepository;
    }

    @Transactional
    public Quadro criarQuadroComStatus(QuadroDTO data) {
        Setor setor = setorRepository.findById(data.idSetor())
                .orElseThrow(() -> new AppException("Setor não encontrado", HttpStatus.NOT_FOUND));

        Quadro novoQuadro = new Quadro();
        novoQuadro.setNome(data.nome());
        novoQuadro.setSetor(setor);
        novoQuadro.setAtivo(true);
        novoQuadro.setWorkflowStatus(new HashSet<>());
        novoQuadro.setCreatedAt(LocalDateTime.now());
        novoQuadro.setUpdatedAt(LocalDateTime.now());

        Quadro quadroSalvo = quadroRepository.save(novoQuadro);

        List<String> nomesStatus = data.statusPadroes() != null && !data.statusPadroes().isEmpty()
                ? data.statusPadroes()
                : List.of("A Fazer", "Em Progresso", "Concluído");

        for (int i = 0; i < nomesStatus.size(); i++) {
            WorkflowStatus status = new WorkflowStatus();
            status.setQuadro(quadroSalvo);
            status.setNome(nomesStatus.get(i));
            status.setOrdem(i);
            workflowStatusRepository.save(status);
        }

        return quadroRepository.findById(quadroSalvo.getIdQuadro()).get();
    }

    public List<Quadro> listarPorSetor(Integer idSetor) {
        return quadroRepository.findBySetorIdSetor(idSetor);
    }

    public Quadro buscarPorId(Integer idQuadro) {
        return quadroRepository.findById(idQuadro)
                .orElseThrow(() -> new AppException("Quadro com ID " + idQuadro + " não encontrado.", HttpStatus.NOT_FOUND));
    }

    public List<Quadro> buscarTodosQuadros() {
        return quadroRepository.findAll();
    }

    @Transactional
    public Quadro atualizarQuadro(Integer idQuadro, AtualizarQuadroDTO data) {
        Quadro quadroExistente = buscarPorId(idQuadro);

        Setor novoSetor = null;
        if (data.idSetor() != null) {
            novoSetor = setorRepository.findById(data.idSetor())
                    .orElseThrow(() -> new AppException(
                            "Setor de destino com ID " + data.idSetor() + " não encontrado.", HttpStatus.NOT_FOUND
                    ));
        } else {
            novoSetor = quadroExistente.getSetor();
        }

        quadroExistente.setNome(data.nome());
        quadroExistente.setSetor(novoSetor);
        quadroExistente.setAtivo(data.ativo());
        quadroExistente.setUpdatedAt(LocalDateTime.now());

        return quadroRepository.save(quadroExistente);
    }

    @Transactional
    public void deletarQuadro(Integer idQuadro) {
        if (!quadroRepository.existsById(idQuadro)) {
            throw new AppException(
                    "Quadro com ID " + idQuadro + " não encontrado.",
                    HttpStatus.NOT_FOUND
            );
        }

        // --- VERIFICAÇÃO DE SEGURANÇA ---
        if (tarefaRepository.existsByQuadroIdQuadro(idQuadro)) {
            throw new AppException(
                    "Não é possível excluir o quadro. Ele ainda contém tarefas.",
                    HttpStatus.BAD_REQUEST
            );
        }
        quadroRepository.deleteById(idQuadro);
    }

    @Transactional
    public Quadro adicionarStatusAoQuadro(Integer idQuadro, CriarStatusDTO data) {
        Quadro quadro = buscarPorId(idQuadro);

        Optional<Integer> maxOrdemOpt = workflowStatusRepository.findMaxOrdemByQuadroId(idQuadro);
        int novaOrdem = maxOrdemOpt.map(max -> max + 1).orElse(0);

        WorkflowStatus novoStatus = new WorkflowStatus();
        novoStatus.setNome(data.nome());
        novoStatus.setQuadro(quadro);
        novoStatus.setOrdem(novaOrdem);

        workflowStatusRepository.save(novoStatus);

        return quadroRepository.findById(idQuadro).get();
    }

    @Transactional
    public WorkflowStatus atualizarStatus(Integer idStatus, CriarStatusDTO data) {
        WorkflowStatus statusExistente = workflowStatusRepository.findById(idStatus)
                .orElseThrow(() -> new AppException("Status com ID " + idStatus + " não encontrado.", HttpStatus.NOT_FOUND));

        statusExistente.setNome(data.nome());
        return workflowStatusRepository.save(statusExistente);
    }

    @Transactional
    public void deletarStatus(Integer idStatus) {
        if (tarefaRepository.existsByStatusIdStatus(idStatus)) {
            throw new AppException(
                    "Não é possível excluir este status, pois ele ainda contém tarefas.",
                    HttpStatus.BAD_REQUEST
            );
        }

        workflowStatusRepository.deleteById(idStatus);
    }
}
