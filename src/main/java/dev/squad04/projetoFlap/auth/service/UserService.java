package dev.squad04.projetoFlap.auth.service;

import dev.squad04.projetoFlap.auth.entity.User;
import dev.squad04.projetoFlap.auth.repository.UserRepository;
import dev.squad04.projetoFlap.exceptions.AppException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
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
}
