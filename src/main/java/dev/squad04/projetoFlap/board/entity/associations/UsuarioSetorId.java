package dev.squad04.projetoFlap.board.entity.associations;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter @Setter
@Embeddable
public class UsuarioSetorId implements Serializable {
    private Integer idUsuario;
    private Integer idSetor;
}
