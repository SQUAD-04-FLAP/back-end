package dev.squad04.projetoFlap.board.mapper;

import dev.squad04.projetoFlap.board.dto.comentario.ComentarioDTO;
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

        return new TarefaResponseDTO(
                tarefa.getIdTarefa(),
                tarefa.getTitulo(),
                tarefa.getDescricao(),
                tarefa.getQuadro().getIdQuadro(),
                tarefa.getQuadro().getNome(),
                tarefa.getStatus().getIdStatus(),
                tarefa.getStatus().getNome(),
                tarefa.getCriadoPor().getIdUsuario(),
                tarefa.getCriadoPor().getNome(),
                tarefa.getResponsavel() != null ? tarefa.getResponsavel().getIdUsuario() : null,
                tarefa.getResponsavel() != null ? tarefa.getResponsavel().getNome() : null,
                comentariosDto,
                tarefa.getPrazo(),
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
