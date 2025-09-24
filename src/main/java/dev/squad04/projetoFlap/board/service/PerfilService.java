package dev.squad04.projetoFlap.board.service;

import dev.squad04.projetoFlap.board.dto.perfil.PerfilDTO;
import dev.squad04.projetoFlap.board.entity.Perfil;
import dev.squad04.projetoFlap.board.repository.PerfilRepository;
import dev.squad04.projetoFlap.exceptions.AppException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PerfilService {

    private final PerfilRepository perfilRepository;

    public PerfilService(PerfilRepository perfilRepository) {
        this.perfilRepository = perfilRepository;
    }

    public Perfil criarPerfil(PerfilDTO data) {
        if (perfilRepository.findByNome(data.nome()).isPresent()) {
            throw new AppException("Perfil já existe.", HttpStatus.CONFLICT);
        }
        Perfil novoPerfil = new Perfil();
        novoPerfil.setNome(data.nome());
        novoPerfil.setDescricao(data.descricao());
        novoPerfil.setCreatedAt(LocalDateTime.now());
        return perfilRepository.save(novoPerfil);
    }

    public Optional<Perfil> buscarPorId(Integer id) {
        return perfilRepository.findById(id);
    }

    public List<Perfil> listarTodos() {
        return perfilRepository.findAll();
    }

    public Perfil atualizarPerfil(Integer id, PerfilDTO data) {
        Perfil perfilExistente = perfilRepository.findById(id)
                .orElseThrow(() -> new AppException("Perfil não encontrado.", HttpStatus.NOT_FOUND));

        perfilExistente.setNome(data.nome());
        perfilExistente.setDescricao(data.descricao());
        perfilExistente.setUpdatedAt(LocalDateTime.now());
        return perfilRepository.save(perfilExistente);
    }

    public void deletarPerfil(Integer id) {
        if (!perfilRepository.existsById(id)) {
            throw new AppException("Perfil não encontrado.", HttpStatus.NOT_FOUND);
        } else {
            perfilRepository.deleteById(id);
        }
    }
}
