package dev.squad04.projetoFlap.board.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "quadros")
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class Quadro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idQuadro;

    private String nome;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_setor")
    private Setor setor;

    @OneToMany(mappedBy = "quadro", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<WorkflowStatus> workflowStatus;

    private Boolean ativo;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
