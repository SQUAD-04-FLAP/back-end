package dev.squad04.projetoFlap.board.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "perfis")
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class Perfil {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idPerfil;

    private String nome;
    private String descricao;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
