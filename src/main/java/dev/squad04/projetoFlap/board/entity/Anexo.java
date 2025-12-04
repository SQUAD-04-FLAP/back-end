package dev.squad04.projetoFlap.board.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "anexos")
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class Anexo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idAnexo;

    private String nomeOriginal;
    private String nomeArquivo;
    private String tipoArquivo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tarefa")
    private Tarefa tarefa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_setor")
    private Setor setor;
}
