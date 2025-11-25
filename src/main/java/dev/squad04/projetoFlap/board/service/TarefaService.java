package dev.squad04.projetoFlap.board.service;

import dev.squad04.projetoFlap.auth.entity.User;
import dev.squad04.projetoFlap.auth.repository.UserRepository;
import dev.squad04.projetoFlap.board.dto.tarefa.*;
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
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        if (data.idQuadro() == null) {
            throw new AppException("O ID do quadro é obrigatório.", HttpStatus.BAD_REQUEST);
        }

        if (data.idCriador() == null) {
            throw new AppException("O ID do criador é obrigatório.", HttpStatus.BAD_REQUEST);
        }

        User user = userRepository.findById(data.idCriador())
                .orElseThrow(() -> new AppException("Usuário criador não encontrado", HttpStatus.NOT_FOUND));

        Quadro quadro = quadroRepository.findById(data.idQuadro())
                .orElseThrow(() -> new AppException("Quadro não encontrado", HttpStatus.NOT_FOUND));

        if (data.idSetor() != null) {
            if (quadro.getSetor() == null || !quadro.getSetor().getIdSetor().equals(data.idSetor())) {
                throw new AppException("O quadro informado não pertence à empresa/setor selecionado", HttpStatus.BAD_REQUEST);
            }
        }

        Set<User> responsaveis = new HashSet<>();
        if (data.idsResponsaveis() != null && !data.idsResponsaveis().isEmpty()) {
            List<User> usersFounded = userRepository.findAllById(data.idsResponsaveis());

            if (usersFounded.size() != data.idsResponsaveis().size()) {
                throw new AppException("Um ou mais usuários responsáveis não foram encontrados", HttpStatus.NOT_FOUND);
            }
            responsaveis.addAll(usersFounded);
        }

        WorkflowStatus statusInicial = quadro.getWorkflowStatus().stream()
                .min(Comparator.comparingInt(WorkflowStatus::getOrdem))
                .orElseThrow(() -> new AppException("O quadro não possui status definidos", HttpStatus.BAD_REQUEST));

        Tarefa novaTarefa = new Tarefa();
        novaTarefa.setTitulo(data.titulo());
        novaTarefa.setDescricao(data.descricao());
        novaTarefa.setCriadoPor(user);
        novaTarefa.setResponsaveis(responsaveis);
        novaTarefa.setQuadro(quadro);
        novaTarefa.setStatus(statusInicial);
        novaTarefa.setDtTermino(data.dtTermino());
        novaTarefa.setPrioridade(data.prioridade());
        novaTarefa.setAtivo(true);
        novaTarefa.setCreatedAt(LocalDateTime.now());
        novaTarefa.setUpdatedAt(LocalDateTime.now());

        return tarefaRepository.save(novaTarefa);
    }

    @Transactional
    public Tarefa atualizarTarefa(Integer idTarefa, AtualizarTarefaDTO data) {
        Tarefa tarefa = buscarPorId(idTarefa);

        tarefa.setTitulo(data.titulo());
        tarefa.setDescricao(data.descricao());
        tarefa.setDtTermino(data.dtTermino());
        tarefa.setAtivo(data.ativo());
        tarefa.setPrioridade(data.prioridade());
        tarefa.setUpdatedAt(LocalDateTime.now());

        return tarefaRepository.save(tarefa);
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
    public Tarefa atualizarResponsaveis(Integer idTarefa, AtribuirResponsavelDTO idResponsavel) {
        Tarefa tarefa = buscarPorId(idTarefa);

        User usuarioAlvo = userRepository.findById(idResponsavel.idResponsavel())
                .orElseThrow(() -> new AppException("Usuário não encontrado!", HttpStatus.NOT_FOUND));

        if (tarefa.getResponsaveis().contains(usuarioAlvo)) {
            tarefa.getResponsaveis().remove(usuarioAlvo);
        } else {
            tarefa.getResponsaveis().add(usuarioAlvo);
        }

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

    public List<Tarefa> buscarTarefasPorResponsavel(Integer idUsuario) {
        return tarefaRepository.findByResponsaveisIdUsuario(idUsuario);
    }
}
