package dev.squad04.projetoFlap.board.mapper;

import dev.squad04.projetoFlap.board.dto.setor.AssociacaoResponseDTO;
import dev.squad04.projetoFlap.board.entity.associations.UsuarioSetor;
import org.springframework.stereotype.Component;

@Component
public class SetorMapper {

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
}
