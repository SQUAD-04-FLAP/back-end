package dev.squad04.projetoFlap.board.entity.associations;

import dev.squad04.projetoFlap.auth.entity.User;
import dev.squad04.projetoFlap.board.entity.Tarefa;
import dev.squad04.projetoFlap.board.entity.WorkflowStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "tarefa_status_history")
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class TarefaStatusHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idHistory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tarefa")
    private Tarefa tarefa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "de_status_id")
    private WorkflowStatus deStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "para_status_id")
    private WorkflowStatus paraStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "alterado_por")
    private User alteradoPor;

    private LocalDateTime alteradoEm;
}
