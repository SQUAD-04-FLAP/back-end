package dev.squad04.projetoFlap.board.mapper;

import dev.squad04.projetoFlap.board.dto.anexo.AnexoDTO;
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

    private static final String BASE_URL = "/flapboard/arquivos/";

    public TarefaResponseDTO toDTO(Tarefa tarefa) {
        Set<ComentarioDTO> comentariosDto = tarefa.getComentarios().stream()
                .map(this::toComentarioDTO)
                .collect(Collectors.toSet());

        Set<ResponsavelDTO> responsaveisDto = tarefa.getResponsaveis().stream()
                .map(user -> {
                    String urlFoto = null;
                    if (user.getFotoUrl() != null && !user.getFotoUrl().isBlank()) {
                        urlFoto = BASE_URL + user.getFotoUrl();
                    }

                    return new ResponsavelDTO(
                            user.getIdUsuario(),
                            user.getNome(),
                            user.getEmail(),
                            urlFoto
                    );
                })
                .collect(Collectors.toSet());

        Set<AnexoDTO> anexosDto = tarefa.getAnexos().stream()
                .map(anexo -> new AnexoDTO(
                        anexo.getIdAnexo(),
                        anexo.getNomeOriginal(),
                        BASE_URL + anexo.getNomeArquivo()
                ))
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

        String fotoUrlCriador = null;
        if (tarefa.getCriadoPor().getFotoUrl() != null) {
            fotoUrlCriador = BASE_URL + tarefa.getCriadoPor().getFotoUrl();
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
                fotoUrlCriador,
                nomeSetor,
                responsaveisDto,
                comentariosDto,
                anexosDto,
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
                comentario.getUsuario().getNome(),
                comentario.getUsuario().getFotoUrl() != null ? BASE_URL + comentario.getUsuario().getFotoUrl() : null
        );
    }
}
