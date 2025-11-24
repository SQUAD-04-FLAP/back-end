package dev.squad04.projetoFlap.auth.dto.register;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

public record RegisterDTO(
        @NotBlank String nome,
        @NotBlank @Email String email,
        @NotBlank String senha,
        LocalDate dtNascimento) {

}
