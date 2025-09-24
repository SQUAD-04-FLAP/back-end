package dev.squad04.projetoFlap.board.entity.associations;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@Embeddable
public class UsuarioSetorId implements Serializable {
    private Integer idUsuario;
    private Integer idSetor;
}
