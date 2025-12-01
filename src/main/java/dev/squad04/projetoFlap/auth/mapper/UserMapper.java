package dev.squad04.projetoFlap.auth.mapper;

import dev.squad04.projetoFlap.auth.dto.user.UserResponseDTO;
import dev.squad04.projetoFlap.auth.dto.user.UserSetorDTO;
import dev.squad04.projetoFlap.auth.entity.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserMapper {
    public UserResponseDTO toDTO(User usuario) {
        Set<UserSetorDTO> setoresDto = usuario.getAssociacoesSetor().stream()
                .map(associacao -> new UserSetorDTO(
                        associacao.getSetor().getIdSetor(),
                        associacao.getSetor().getNome()
                ))
                .collect(Collectors.toSet());

        return new UserResponseDTO(
                usuario.getIdUsuario(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.isAtivo(),
                usuario.getPermissao(),
                setoresDto,
                usuario.getProvedor(),
                usuario.getCreatedAt(),
                usuario.getUpdatedAt(),
                usuario.getDtNascimento(),
                usuario.getFotoUrl()
        );
    }

    public List<UserResponseDTO> toDTOList(List<User> usuarios) {
        return usuarios.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}
