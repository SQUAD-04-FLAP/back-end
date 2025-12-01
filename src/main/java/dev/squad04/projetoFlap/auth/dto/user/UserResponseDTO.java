package dev.squad04.projetoFlap.auth.dto.user;

import dev.squad04.projetoFlap.auth.enums.AuthProvider;
import dev.squad04.projetoFlap.auth.enums.UserRole;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

public record UserResponseDTO(
        Integer idUsuario,
        String nome,
        String email,
        Boolean ativo,
        UserRole permissao,
        Set<UserSetorDTO> setores,
        AuthProvider provedor,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDate dtNascimento,
        String fotoUrl
) {}
