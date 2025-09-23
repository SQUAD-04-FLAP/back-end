package dev.squad04.projetoFlap.board.repository;

import dev.squad04.projetoFlap.board.entity.Tarefa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TarefaRepository extends JpaRepository<Tarefa, Integer> {
}
