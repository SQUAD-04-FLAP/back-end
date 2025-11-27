package dev.squad04.projetoFlap.board.mapper;

import dev.squad04.projetoFlap.board.dto.comentario.ComentarioDTO;
import dev.squad04.projetoFlap.board.dto.tarefa.ResponsavelDTO;
import dev.squad04.projetoFlap.board.dto.tarefa.TarefaResponseDTO;
import dev.squad04.projetoFlap.board.entity.Comentario;
import dev.squad04.projetoFlap.board.entity.Tarefa;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class TarefaMapper {

    public TarefaResponseDTO toDTO(Tarefa tarefa) {
        Set<ComentarioDTO> comentariosDto = tarefa.getComentarios().stream()
                .map(this::toComentarioDTO)
                .collect(Collectors.toSet());

        Set<ResponsavelDTO> responsaveisDto = tarefa.getResponsaveis().stream()
                .map(user -> new ResponsavelDTO(user.getIdUsuario(), user.getNome(), user.getEmail()))
                .collect(Collectors.toSet());

        String nomeSetor = null;
        if (tarefa.getSetor() != null) {
            nomeSetor = tarefa.getSetor().getNome();
        } else if (tarefa.getQuadro() != null && tarefa.getQuadro().getSetor() != null) {
            nomeSetor = tarefa.getQuadro().getSetor().getNome();
        }

        Integer idQuadro = null;
        String nomeQuadro = null;
        if (tarefa.getQuadro() != null) {
            idQuadro = tarefa.getQuadro().getIdQuadro();
            nomeQuadro = tarefa.getQuadro().getNome();
        }

        Integer idStatus = null;
        String nomeStatus = null;
        if (tarefa.getStatus() != null) {
            idStatus = tarefa.getStatus().getIdStatus();
            nomeStatus = tarefa.getStatus().getNome();
        }

        Integer idCriador = null;
        String nomeCriador = null;
        if (tarefa.getCriadoPor() != null) {
            idCriador = tarefa.getCriadoPor().getIdUsuario();
            nomeCriador = tarefa.getCriadoPor().getNome();
        }

        return new TarefaResponseDTO(
                tarefa.getIdTarefa(),
                tarefa.getTitulo(),
                tarefa.getDescricao(),
                idQuadro,
                nomeQuadro,
                idStatus,
                nomeStatus,
                idCriador,
                nomeCriador,
                nomeSetor,
                responsaveisDto,
                comentariosDto,
                tarefa.getDtTermino(),
                tarefa.getPrioridade(),
                tarefa.getAtivo(),
                tarefa.getCreatedAt(),
                tarefa.getUpdatedAt()
        );
    }

    public List<TarefaResponseDTO> toDTOList(List<Tarefa> tarefas) {
        return tarefas.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private ComentarioDTO toComentarioDTO(Comentario comentario) {
        return new ComentarioDTO(
                comentario.getIdComentario(),
                comentario.getMensagem(),
                comentario.getCreatedAt(),
                comentario.getUsuario().getIdUsuario(),
                comentario.getUsuario().getNome()
        );
    }
}
