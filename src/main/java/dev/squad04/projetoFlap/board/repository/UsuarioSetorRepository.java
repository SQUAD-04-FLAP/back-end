package dev.squad04.projetoFlap.board.repository;

import dev.squad04.projetoFlap.board.entity.associations.UsuarioSetor;
import dev.squad04.projetoFlap.board.entity.associations.UsuarioSetorId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioSetorRepository extends JpaRepository<UsuarioSetor, UsuarioSetorId> {
}
