package dev.squad04.projetoFlap.auth.dto.user;

import java.time.LocalDate;

public record UpdateUserDTO(String nome, boolean ativo, LocalDate dtNascimento) {
}
