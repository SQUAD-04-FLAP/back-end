package dev.squad04.projetoFlap.board.service;

import dev.squad04.projetoFlap.auth.entity.User;
import dev.squad04.projetoFlap.auth.repository.UserRepository;
import dev.squad04.projetoFlap.board.dto.tarefa.AdicionarComentarioDTO;
import dev.squad04.projetoFlap.board.dto.tarefa.AtribuirResponsavelDTO;
import dev.squad04.projetoFlap.board.dto.tarefa.CriarTarefaDTO;
import dev.squad04.projetoFlap.board.dto.tarefa.MoverTarefaDTO;
import dev.squad04.projetoFlap.board.entity.Comentario;
import dev.squad04.projetoFlap.board.entity.Quadro;
import dev.squad04.projetoFlap.board.entity.Tarefa;
import dev.squad04.projetoFlap.board.entity.WorkflowStatus;
import dev.squad04.projetoFlap.board.entity.associations.TarefaStatusHistory;
import dev.squad04.projetoFlap.board.repository.QuadroRepository;
import dev.squad04.projetoFlap.board.repository.TarefaRepository;
import dev.squad04.projetoFlap.board.repository.WorkflowStatusRepository;
import dev.squad04.projetoFlap.exceptions.AppException;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TarefaService {

    private final TarefaRepository tarefaRepository;
    private final QuadroRepository quadroRepository;
    private final UserRepository userRepository;
    private final WorkflowStatusRepository workflowStatusRepository;

    public TarefaService(TarefaRepository tarefaRepository, QuadroRepository quadroRepository, UserRepository userRepository, WorkflowStatusRepository workflowStatusRepository) {
        this.tarefaRepository = tarefaRepository;
        this.quadroRepository = quadroRepository;
        this.userRepository = userRepository;
        this.workflowStatusRepository = workflowStatusRepository;
    }

    @Transactional
    public Tarefa criarTarefa(CriarTarefaDTO data) {
        User criador = userRepository.findById(data.idCriador())
                .orElseThrow(() -> new AppException("Usuário criador não encontrado", HttpStatus.NOT_FOUND));

        Quadro quadro = quadroRepository.findById(data.idQuadro())
                .orElseThrow(() -> new AppException("Quadro não encontrado", HttpStatus.NOT_FOUND));

        WorkflowStatus statusInicial = quadro.getWorkflowStatus().stream()
                .min((s1, s2) -> Integer.compare(s1.getOrdem(), s2.getOrdem()))
                .orElseThrow(() -> new AppException("O quadro não possui status definidos", HttpStatus.BAD_REQUEST));

        Tarefa novaTarefa = new Tarefa();
        novaTarefa.setTitulo(data.titulo());
        novaTarefa.setDescricao(data.descricao());
        novaTarefa.setCriadoPor(criador);
        novaTarefa.setQuadro(quadro);
        novaTarefa.setStatus(statusInicial);
        novaTarefa.setAtivo(true);
        novaTarefa.setCreatedAt(LocalDateTime.now());
        novaTarefa.setUpdatedAt(LocalDateTime.now());

        return tarefaRepository.save(novaTarefa);
    }

    public List<Tarefa> buscarTarefasPorQuadro(Integer idQuadro) {
        return tarefaRepository.findByQuadroIdQuadro(idQuadro);
    }

    public Tarefa buscarPorId(Integer idTarefa) {
        return tarefaRepository.findById(idTarefa)
                .orElseThrow(() -> new AppException("Tarefa com ID " + idTarefa + " não encontrada.", HttpStatus.NOT_FOUND));
    }

    @Transactional
    public Tarefa moverTarefaParaStatus(Integer idTarefa, MoverTarefaDTO data) {
        Tarefa tarefa = buscarPorId(idTarefa);

        User usuarioLogado = userRepository.findById(data.idUsuarioLogado())
                .orElseThrow(() -> new AppException("Usuário não encontrado!", HttpStatus.NOT_FOUND));

        WorkflowStatus novoStatus = workflowStatusRepository.findById(data.idNovoStatus())
                .orElseThrow(() -> new AppException("Status não encontrado!", HttpStatus.NOT_FOUND));

        if (!tarefa.getQuadro().getIdQuadro().equals(novoStatus.getQuadro().getIdQuadro())) {
            throw new AppException("O status não pertence ao mesmo quadro da tarefa.", HttpStatus.BAD_REQUEST);
        }

        WorkflowStatus statusAntigo = tarefa.getStatus();

        TarefaStatusHistory historico = new TarefaStatusHistory();
        historico.setTarefa(tarefa);
        historico.setDeStatus(statusAntigo);
        historico.setParaStatus(novoStatus);
        historico.setAlteradoPor(usuarioLogado);
        historico.setAlteradoEm(LocalDateTime.now());

        tarefa.getHistoricosDeStatus().add(historico);
        tarefa.setStatus(novoStatus);

        return tarefaRepository.save(tarefa);
    }

    public void deletarTarefa(Integer idTarefa) {
        if (!tarefaRepository.existsById(idTarefa)) {
            throw new AppException("Tarefa não encontrada!", HttpStatus.NOT_FOUND);
        } else {
            tarefaRepository.deleteById(idTarefa);
        }
    }

    @Transactional
    public Tarefa atribuirResponsavel(Integer idTarefa, AtribuirResponsavelDTO idResponsavel) {
        Tarefa tarefa = buscarPorId(idTarefa);

        User responsavel = userRepository.findById(idResponsavel.idResponsavel())
                .orElseThrow(() -> new AppException("Usuário não encontrado!", HttpStatus.NOT_FOUND));

        tarefa.setResponsavel(responsavel);
        return tarefaRepository.save(tarefa);
    }

    @Transactional
    public Tarefa adicionarComentario(Integer idTarefa, AdicionarComentarioDTO data) {
        Tarefa tarefa = buscarPorId(idTarefa);

        User autor = userRepository.findById(data.idUsuario())
                .orElseThrow(() -> new AppException("Usuário não encontrado!", HttpStatus.NOT_FOUND));

        Comentario novoComentario = new Comentario();
        novoComentario.setMensagem(data.mensagem());
        novoComentario.setUsuario(autor);
        novoComentario.setTarefa(tarefa);
        novoComentario.setCreatedAt(LocalDateTime.now());

        tarefa.getComentarios().add(novoComentario);
        return tarefaRepository.save(tarefa);
    }

}
