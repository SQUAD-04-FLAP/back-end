package dev.squad04.projetoFlap.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record ForgotPasswordDTO(
        @NotBlank String email) {}
