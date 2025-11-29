package dev.squad04.projetoFlap.auth.service;

import dev.squad04.projetoFlap.auth.dto.user.UpdateUserDTO;
import dev.squad04.projetoFlap.auth.entity.User;
import dev.squad04.projetoFlap.auth.repository.UserRepository;
import dev.squad04.projetoFlap.exceptions.AppException;
import dev.squad04.projetoFlap.file.service.FileStorageService;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final FileStorageService fileStorageService;

    public UserService(UserRepository userRepository, FileStorageService fileStorageService) {
        this.userRepository = userRepository;
        this.fileStorageService = fileStorageService;
    }

    public User findUserById(Integer id) {
        Optional<User> user = userRepository.findById(id);

        if (user.isPresent()) {
            return user.get();
        } else {
            throw new AppException("Usuário não encontrado.", HttpStatus.NOT_FOUND);
        }
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public User updateUser(Integer idUsuario, UpdateUserDTO data) {
        User user = findUserById(idUsuario);

        if (data.nome() != null && !data.nome().isBlank()) {
            user.setNome(data.nome());
        }

        user.setAtivo(data.ativo());
        user.setUpdatedAt(LocalDateTime.now());
        user.setDtNascimento(data.dtNascimento());

        return userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Integer idUsuario) {
        User user = findUserById(idUsuario);
        userRepository.delete(user);
    }

    @Transactional
    public User saveProfilePhoto(Integer idUsuario, MultipartFile file) {
        User user = findUserById(idUsuario);

        if (user.getFotoUrl() != null) {
            fileStorageService.deletarArquivo(user.getFotoUrl());
        }

        String nomeArquivo = fileStorageService.salvarArquivo(file);
        user.setFotoUrl(nomeArquivo);
        return userRepository.save(user);
    }
}
