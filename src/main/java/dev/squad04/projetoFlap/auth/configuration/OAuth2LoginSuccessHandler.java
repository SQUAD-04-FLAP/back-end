package dev.squad04.projetoFlap.auth.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.squad04.projetoFlap.auth.dto.LoggedDTO;
import dev.squad04.projetoFlap.auth.entity.User;
import dev.squad04.projetoFlap.auth.enums.AuthProvider;
import dev.squad04.projetoFlap.auth.enums.UserRole;
import dev.squad04.projetoFlap.auth.repository.UserRepository;
import dev.squad04.projetoFlap.auth.service.TokenService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final ObjectMapper objectMapper;

    public OAuth2LoginSuccessHandler(UserRepository userRepository, TokenService tokenService, ObjectMapper objectMapper) {
        this.userRepository = userRepository;
        this.tokenService = tokenService;
        this.objectMapper = objectMapper;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        DefaultOAuth2User oAuth2User = (DefaultOAuth2User) authentication.getPrincipal();
        String nome = oAuth2User.getAttribute("name");
        String email = oAuth2User.getAttribute("email");

        Optional<User> optionalUser = userRepository.findByEmail(email);
        User finalUser;

        if (optionalUser.isPresent()) {
            finalUser = optionalUser.get();
        } else {
            User newUser = new User();
            newUser.setNome(nome);
            newUser.setEmail(email);
            newUser.setPermissao(UserRole.USER);
            newUser.setSenha(null);
            newUser.setProvedor(AuthProvider.GOOGLE);
            finalUser = userRepository.save(newUser);
        }

        String jwtToken = tokenService.generateToken(finalUser);

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        LoggedDTO loggedDTO = new LoggedDTO(jwtToken);
        response.getWriter().write(objectMapper.writeValueAsString(loggedDTO));
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request) {
        var session = request.getSession(false);
        if (session == null) {
            return;
        }
        session.removeAttribute("SPRING_SECURITY_SAVED_REQUEST");
    }
}
