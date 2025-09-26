package dev.squad04.projetoFlap.board.repository;

import dev.squad04.projetoFlap.board.entity.WorkflowStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkflowStatusRepository extends JpaRepository<WorkflowStatus, Integer> {
}
