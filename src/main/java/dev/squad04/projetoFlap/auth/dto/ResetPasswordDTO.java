package dev.squad04.projetoFlap.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record ResetPasswordDTO(
        @NotBlank String code,
        @NotBlank String newPassword) {}
