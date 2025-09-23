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
@Table(name = "historico")
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class Historico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idHistorico;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tarefa")
    private Tarefa tarefa;

    private String acao;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "detalhe", columnDefinition = "jsonb")
    private Map<String, Object> detalhe;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feito_por")
    private User feitoPor;

    private LocalDateTime createdAt;
}
