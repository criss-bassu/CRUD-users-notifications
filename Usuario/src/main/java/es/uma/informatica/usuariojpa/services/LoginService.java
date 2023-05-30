package es.uma.informatica.usuariojpa.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.uma.informatica.usuariojpa.modelo.Token;
import es.uma.informatica.usuariojpa.modelo.TokenType;
import es.uma.informatica.usuariojpa.modelo.Usuario;
import es.uma.informatica.usuariojpa.repositories.TokenRepository;
import es.uma.informatica.usuariojpa.repositories.UsuarioRepository;
import es.uma.informatica.usuariojpa.schemas.Login;
import es.uma.informatica.usuariojpa.schemas.RespuestaJWT;
import es.uma.informatica.usuariojpa.schemas.UsuarioDTO;
import es.uma.informatica.usuariojpa.schemas.UsuarioNuevo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class LoginService {
    private final UsuarioRepository repository;
    private final TokenRepository tokenRepository;
    private final PasswordService passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public RespuestaJWT register(UsuarioNuevo request) {
        var user = Usuario.builder()
            .apellido1(request.getApellido1())
            .apellido2(request.getApellido2())
            .email(request.getEmail())
            .contrasenia(passwordEncoder.generatePassword())
            .build();
        var savedUser = repository.save(user);
        var jwtToken = jwtService.generateToken(user);
        saveUserToken(savedUser, jwtToken);
        return RespuestaJWT.builder()
            .jwt(jwtToken)
            .build();
    }

    public RespuestaJWT authenticate(Login request) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
            )
        );
        Usuario user = repository.findByEmail(request.getEmail())
            .orElseThrow();
        String jwtToken = jwtService.generateToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        return RespuestaJWT.builder()
            .jwt(jwtToken)
            .build();
    }

    private void saveUserToken(Usuario user, String jwtToken) {
        var token = Token.builder()
            .user(user)
            .token(jwtToken)
            .tokenType(TokenType.BEARER)
            .expired(false)
            .revoked(false)
            .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(Usuario user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(Math.toIntExact(user.getId()));
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
            if (authHeader == null ||!authHeader.startsWith("Bearer ", 0)) {
            return;
        }
        refreshToken = authHeader.substring("Bearer ".length());
        userEmail = jwtService.extractUsername(refreshToken);
        if (userEmail != null) {
            var user = this.repository.findByEmail(userEmail)
                    .orElseThrow();
            if (jwtService.isTokenValid(refreshToken, user)) {
                var accessToken = jwtService.generateToken(user);
                revokeAllUserTokens(user);
                saveUserToken(user, accessToken);
                var authResponse = RespuestaJWT.builder()
                        .jwt(accessToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }
}
