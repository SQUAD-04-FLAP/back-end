package dev.squad04.projetoFlap.auth.dto.register;

import dev.squad04.projetoFlap.auth.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegisterDTO(
        @NotBlank String nome,
        @NotBlank @Email String email,
        @NotBlank String senha,
        UserRole permissao) {

}
