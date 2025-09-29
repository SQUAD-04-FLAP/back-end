package dev.squad04.projetoFlap.auth.dto.user;

import dev.squad04.projetoFlap.auth.enums.UserRole;
import jakarta.validation.constraints.NotBlank;

public record SetUserRoleDTO(
       @NotBlank UserRole role
) {}
