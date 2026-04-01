package dev.squad04.projetoFlap.auth.configuration;

import dev.squad04.projetoFlap.auth.entity.User;
import dev.squad04.projetoFlap.auth.enums.AuthProvider;
import dev.squad04.projetoFlap.auth.enums.UserRole;
import dev.squad04.projetoFlap.auth.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

@Configuration
public class AdminUserInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminUserInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (userRepository.findByEmail("admin@flap.com").isEmpty()) {
            User admin = new User(
                    "Administrador",
                    "admin@flap.com",
                    passwordEncoder.encode("admin123"),
                    UserRole.ADMIN,
                    AuthProvider.CREDENTIALS,
                    LocalDate.of(2000, 1, 1)
            );
            userRepository.save(admin);
            
            System.out.println("=======================================================================");
            System.out.println("  USUÁRIO DE TESTE (ADMIN) CRIADO COM SUCESSO! ");
            System.out.println("  E-mail: admin@flap.com");
            System.out.println("  Senha:  admin123");
            System.out.println("=======================================================================");
        }
    }
}
