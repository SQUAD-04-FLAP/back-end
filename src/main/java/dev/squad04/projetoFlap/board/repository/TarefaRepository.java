package dev.squad04.projetoFlap.board.repository;

import dev.squad04.projetoFlap.board.entity.Tarefa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TarefaRepository extends JpaRepository<Tarefa, Integer> {
    List<Tarefa> findByQuadroIdQuadro(Integer idQuadro);
    boolean existsByQuadroIdQuadro(Integer idQuadro);
    boolean existsByStatusIdStatus(Integer idStatus);
    List<Tarefa> findByResponsaveisIdUsuario(Integer idUsuario);
    List<Tarefa> findBySetorIdSetor(Integer idSetor);

    // Métodos para o dashboard
    List<Tarefa> findTop10ByAtivoTrueOrderByDtTerminoAsc();
    List<Tarefa> findTop10BySetorIdSetorAndAtivoTrueOrderByDtTerminoAsc(Integer idSetor);
    long countByStatusNome(String nomeStatus);
    long countBySetorIdSetor(Integer idSetor);
    long countBySetorIdSetorAndStatusNome(Integer idSetor, String nomeStatus);
    long countByDtTerminoBeforeAndStatusNomeNot(LocalDateTime dataHora, String nomeStatus);
    long countBySetorIdSetorAndDtTerminoBeforeAndStatusNomeNot(Integer idSetor, LocalDateTime dataHora, String nomeStatus);
}
