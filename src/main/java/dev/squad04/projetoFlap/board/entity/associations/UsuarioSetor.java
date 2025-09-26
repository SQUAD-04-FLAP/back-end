package dev.squad04.projetoFlap.board.entity.associations;

import dev.squad04.projetoFlap.auth.entity.User;
import dev.squad04.projetoFlap.board.entity.Setor;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "usuarios_setores")
@Getter @Setter
public class UsuarioSetor {
    @EmbeddedId
    private UsuarioSetorId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idUsuario")
    @JoinColumn(name = "id_usuario")
    private User usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idSetor")
    @JoinColumn(name = "id_setor")
    private Setor setor;

    private LocalDateTime associadoEm;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "associado_por")
    private User associadoPor;
}
