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
        String email = oAuth2User.getAttribute("email");

        User user = (User) userRepository.findByLogin(email);

        if (user == null) {
            user = new User();
            user.setLogin(email);
            user.setRole(UserRole.USER);
            user.setPassword(null);
            user.setProvider(AuthProvider.GOOGLE);
            userRepository.save(user);
        }

        String jwtToken = tokenService.generateToken(user);

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
