package dev.squad04.projetoFlap.board.repository;

import dev.squad04.projetoFlap.board.entity.Tarefa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TarefaRepository extends JpaRepository<Tarefa, Integer> {
    List<Tarefa> findByQuadroIdQuadro(Integer idQuadro);
    boolean existsByQuadroIdQuadro(Integer idQuadro);
    boolean existsByStatusIdStatus(Integer idStatus);
    List<Tarefa> findByResponsaveisIdUsuario(Integer idUsuario);
    List<Tarefa> findBySetorIdSetor(Integer idSetor);
    List<Tarefa> findTop10ByAtivoTrueOrderByDtTerminoAsc();
    List<Tarefa> findTop10BySetorIdSetorAndAtivoTrueOrderByDtTerminoAsc(Integer idSetor);
}
