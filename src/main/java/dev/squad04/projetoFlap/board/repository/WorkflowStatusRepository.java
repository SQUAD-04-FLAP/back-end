package dev.squad04.projetoFlap.board.repository;

import dev.squad04.projetoFlap.board.entity.WorkflowStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WorkflowStatusRepository extends JpaRepository<WorkflowStatus, Integer> {
    @Query("SELECT MAX(ws.ordem) FROM WorkflowStatus ws WHERE ws.quadro.idQuadro = :idQuadro")
    Optional<Integer> findMaxOrdemByQuadroId(Integer idQuadro);

    Optional<WorkflowStatus> findByQuadroIdQuadroAndOrdem(Integer idQuadro, Integer ordem);
}
