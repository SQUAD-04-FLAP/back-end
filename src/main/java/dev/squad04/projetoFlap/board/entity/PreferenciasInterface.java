package dev.squad04.projetoFlap.board.entity;

import dev.squad04.projetoFlap.auth.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Table(name = "preferencias_interface")
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class PreferenciasInterface {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idPreferencia;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario")
    private User usuario;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "configuracoes", columnDefinition = "jsonb")
    private Map<String, Object> configuracoes;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
