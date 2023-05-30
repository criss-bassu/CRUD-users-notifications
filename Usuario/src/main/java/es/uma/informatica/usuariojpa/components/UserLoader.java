package es.uma.informatica.usuariojpa.components;

import es.uma.informatica.usuariojpa.modelo.Rol;
import es.uma.informatica.usuariojpa.modelo.Token;
import es.uma.informatica.usuariojpa.modelo.TokenType;
import es.uma.informatica.usuariojpa.modelo.Usuario;
import es.uma.informatica.usuariojpa.repositories.TokenRepository;
import es.uma.informatica.usuariojpa.repositories.UsuarioRepository;
import es.uma.informatica.usuariojpa.services.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class UserLoader implements ApplicationRunner {

    private final UsuarioRepository userRepo;
    private final JwtService jwtService;
    private final TokenRepository tokenRepository;


    @Override
    public void run(ApplicationArguments args) throws Exception {
        Usuario initUser =Usuario.builder()
                .id(1L)
                .nombre("anonimous")
                .contrasenia("reset")
                .email("anonimous@anonimous.com")
                .roles(List.of(Rol.RESPONSABLE, Rol.VICERRECTORADO))
                .build();

        Token token = new Token(1, jwtService.generateToken(Map.of("roles", initUser.getRoles()), initUser), TokenType.BEARER, false, false, initUser);
        initUser.setToken(token);

        System.out.println("\n\n\tTOKEN DE ACCESO: " + token.getToken() + "\n");

        this.tokenRepository.save(token);
        this.userRepo.save(initUser);
    }
}
