package dev.squad04.projetoFlap.auth.repository;

import dev.squad04.projetoFlap.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(String email);

    Optional<User> findByResetCode(String code);

    UserDetails findUserDetailsByEmail(String email);
}
