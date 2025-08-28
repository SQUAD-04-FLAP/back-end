package dev.squad04.projetoFlap.auth.dto;

import dev.squad04.projetoFlap.auth.enums.AuthProvider;
import dev.squad04.projetoFlap.auth.enums.UserRole;
import jakarta.validation.constraints.NotBlank;

public record RegisterDTO(@NotBlank String login, @NotBlank String password, UserRole role) {

}
