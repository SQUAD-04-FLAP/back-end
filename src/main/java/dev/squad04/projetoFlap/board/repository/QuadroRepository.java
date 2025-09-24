package dev.squad04.projetoFlap.board.repository;

import dev.squad04.projetoFlap.board.entity.Quadro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuadroRepository extends JpaRepository<Quadro, Integer> {
    List<Quadro> findBySetorIdSetor(Integer idSetor);
}
