package dev.squad04.projetoFlap.board.repository;

import dev.squad04.projetoFlap.board.entity.Setor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SetorRepository extends JpaRepository<Setor, Integer> {
    Optional<Setor> findByNome(String nome);
}
