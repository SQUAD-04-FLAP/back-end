package dev.squad04.projetoFlap.board.mapper;

import dev.squad04.projetoFlap.board.dto.anexo.AnexoDTO;
import dev.squad04.projetoFlap.board.dto.setor.AssociacaoResponseDTO;
import dev.squad04.projetoFlap.board.dto.setor.SetorResponseDTO;
import dev.squad04.projetoFlap.board.entity.Setor;
import dev.squad04.projetoFlap.board.entity.associations.UsuarioSetor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class SetorMapper {

    private static final String BASE_URL = "/flapboard/arquivos/";

    public AssociacaoResponseDTO toDTO(UsuarioSetor usuarioSetor) {
        return new AssociacaoResponseDTO(
                usuarioSetor.getUsuario().getIdUsuario(),
                usuarioSetor.getUsuario().getNome(),
                usuarioSetor.getSetor().getIdSetor(),
                usuarioSetor.getSetor().getNome(),
                usuarioSetor.getAssociadoEm(),
                usuarioSetor.getAssociadoPor().getIdUsuario(),
                usuarioSetor.getAssociadoPor().getNome()
        );
    }

    public SetorResponseDTO toDTO(Setor setor) {

        Set<AnexoDTO> anexosDto = setor.getAnexos().stream()
                .map(anexo -> new AnexoDTO(
                        anexo.getIdAnexo(),
                        anexo.getNomeOriginal(),
                        BASE_URL + anexo.getNomeArquivo()
                ))
                .collect(Collectors.toSet());

        return new SetorResponseDTO(
                setor.getIdSetor(),
                setor.getNome(),
                setor.getDescricao(),
                setor.getCreatedAt(),
                setor.getUpdatedAt(),
                anexosDto
        );
    }

    public List<SetorResponseDTO> toDTOList(List<Setor> setores) {
        return setores.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}
