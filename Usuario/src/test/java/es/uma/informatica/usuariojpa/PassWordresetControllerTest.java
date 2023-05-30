package es.uma.informatica.usuariojpa;

import es.uma.informatica.usuariojpa.modelo.Rol;
import es.uma.informatica.usuariojpa.modelo.Token;
import es.uma.informatica.usuariojpa.modelo.TokenType;
import es.uma.informatica.usuariojpa.modelo.Usuario;
import es.uma.informatica.usuariojpa.repositories.TokenRepository;
import es.uma.informatica.usuariojpa.repositories.UsuarioRepository;
import es.uma.informatica.usuariojpa.schemas.PasswordResetRequest;
import es.uma.informatica.usuariojpa.schemas.ResetpasswordEmailRequest;
import es.uma.informatica.usuariojpa.services.JwtService;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.agent.VirtualMachine;
import org.antlr.v4.runtime.misc.LogManager;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("Testeo del servicio de usuario")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class PassWordresetControllerTest {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UsuarioRepository userRepo;

    @Autowired
    private TokenRepository tokenRepo;

    @Autowired
    private TestRestTemplate restTemplate;

    @Value(value="${local.server.port}")
    private int port;

    @Nested
    @DisplayName("Operaciones sin token de autorización")
    public class operacionesSinToken {

        @BeforeEach
        public void initUser() {
            // Crear usuario
            Usuario usuario = Usuario.builder()
                    .id(1L)
                    .nombre("asdf")
                    .contrasenia("reset")
                    .email("asdf@asdf.com")
                    .build();

            userRepo.save(usuario);
        }

        @Test
        @DisplayName("Reseteo de contraseña")
        public void resetPasswordSinToken() {
            // TODO: llamada al método
            var peticion = HttpUtils.post("http","localhost",port,"/passwordreset", new PasswordResetRequest("asdf@asdf.com"));
            var respuesta = restTemplate.exchange(peticion, new ParameterizedTypeReference<List<Usuario>>() {
            });

            // TODO: comprobación del resultado con el API
            assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
        }
    }

    @Nested
    @DisplayName("Operaciones con token de autorización")
    public class operacionesConToken {

        private String token;

        @BeforeEach
        public void initUser() {
            // Crear usuario
            Usuario usuario = Usuario.builder()
                    .id(1L)
                    .nombre("asdf")
                    .contrasenia("reset")
                    .email("asdf@asdf.com")
                    .build();
            // Crear Token
            Token token = new Token(1, jwtService.generateToken(Map.of("roles", List.of(Rol.RESPONSABLE)), usuario), TokenType.BEARER, false, false, usuario);
            usuario.setToken(token);
            // Añadir usuario a la aplicacion

            tokenRepo.save(token);
            userRepo.save(usuario);
            this.token = token.getToken();
        }


        @Test
        @DisplayName("Cambio de contraseña para un usuario valido")
        public void resestPassworEnCondicionesNormales() {
            // TODO: condiciones iniciales del método

            // TODO: llamada al método
            var peticion = HttpUtils.post("http","localhost",port,"/passwordreset", new PasswordResetRequest("asdf@asdf.com"), token);
            var respuesta = restTemplate.exchange(peticion, new ParameterizedTypeReference<List<Usuario>>() { });

            // TODO: comprobación del resultado con el API
            assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
        }
    }
}
