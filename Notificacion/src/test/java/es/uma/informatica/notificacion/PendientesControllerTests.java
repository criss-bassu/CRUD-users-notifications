package es.uma.informatica.notificacion;

import es.uma.informatica.notificacion.modelo.Estado;
import es.uma.informatica.notificacion.modelo.Notificacion;
import es.uma.informatica.notificacion.modelo.TipoNotificacion;
import es.uma.informatica.notificacion.repositories.NotificacionRepository;

import org.antlr.v4.runtime.Token;
import org.apache.http.auth.AUTH;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriBuilderFactory;


import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("En el servicio de Notificacion")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class PendientesControllerTests {

    public static final String BEARER = "Bearer ";
    public static final String AUTHORIZATION = "Authorization";
    @Autowired
    private NotificacionRepository notificacionRepo;

    @Autowired
    private TestRestTemplate restTemplate;

    @Value(value="${local.server.port}")
    private int port;

    @BeforeEach
    public void initNotificaciones() {
        for (int i = 0; i < 5; i++) {
            generaNotificacion(TipoNotificacion.PASSWORD_RESET, Estado.PENDIENTE);
            generaNotificacion(TipoNotificacion.ANUNCIO_AULA_VIGILANTE, Estado.PENDIENTE);
            generaNotificacion(TipoNotificacion.ANUNCIO_AULA_ESTUDIANTE, Estado.PENDIENTE);
            generaNotificacion(TipoNotificacion.ANUNCIO_NOTA_ESTUDIANTE, Estado.PENDIENTE);
        }
    }

    @Nested
    @DisplayName("Caso en el que SI se dispone de un token de acceso válido")
    public class ValidJwtToken {

        // Para este caso es suficiente con un token valido, no tiene porque estar dado de alta https://jwt.io
        private static final String TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwicm9sZXMiOlsiQUxVTU5PIiwiQ09SUkVDVE9SIl19.P0kuh5dTBU7zl_7L_wt6tJSPXrc7LjXGrq_FhTdhubc";

        @Test
        @DisplayName("Aborta notificacion/es en condiciones normales. Token de acceso válido (concuerdan la \"firma\" y el cuerpo), y tipo de notificación válido")
        public void abortaTokenEnCondicionesNormales() {
            var uri = new DefaultUriBuilderFactory().builder()
                    .scheme("http")
                    .host("localhost")
                    .port(port)
                    .path("/pendientes/abortar")
                    .queryParam("tipo", "ANUNCIO_NOTA_ESTUDIANTE")
                    .build();
            var peticion = RequestEntity.post(uri)
                    .header(AUTHORIZATION, BEARER + TOKEN)
                    .build();
            var respuesta = restTemplate.exchange(peticion, ResponseEntity.class);
            // TODO: comprobación del resultado con el API

            assertThat(respuesta.getStatusCode().value() == 200);
        }

    }

    @Nested
    @DisplayName("Caso en el que NO se dispone de un token de acceso o este no es váido")
    public class NonValidJwtToken {

        @Test
        @DisplayName("")
        public void abortaNotificacionSinToken() throws IOException, URISyntaxException {
            /*
            curl -X 'POST' \
              'http://localhost:8080/pendientes/abortar?tipo=ANUNCIO_NOTA_ESTUDIANTE' \
              -H 'accept:
              -d
             */
            /*URL url = new URL("http://localhost:" + port + "/pendientes/abortar?tipo=ANUNCIO_NOTA_ESTUDIANTE");


            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set(AUTHORIZATION, BEARER + "token_nulo");

            RequestEntity<String> request = new RequestEntity<>(headers, HttpMethod.POST, url.toURI());
            RestTemplate restTemplate = new RestTemplate();
            var response = restTemplate.exchange(request, Void.class);*/
            var uri = new DefaultUriBuilderFactory().builder()
                    .scheme("http")
                    .host("localhost")
                    .port(port)
                    .path("/pendientes/abortar")
                    .queryParam("tipo", "ANUNCIO_NOTA_ESTUDIANTE")
                    .build();

            var peticion = RequestEntity.post(uri).header(AUTHORIZATION, BEARER + "token.no.valido").build();
            var respuesta = restTemplate.exchange(peticion, ResponseEntity.class);
            // TODO: comprobación del resultado con el API

            assertThat(respuesta.getStatusCode().value() == 403);
        }
    }

    private static URI getPostWithParamsUri(String tipo) {
        UriBuilderFactory ubf = new DefaultUriBuilderFactory();
        URI uri= ubf.builder()
                .scheme("http")
                .host("localhost")
                .port(8080)
                .path("/pendientes/abortar")
                .queryParam("tipo", tipo)
                .build();
        return uri;
    }

    private void generaNotificacion(TipoNotificacion tipoNotificacion, Estado estado) {
        Notificacion notificacion = Notificacion.builder()
                .asunto("asdfadsf")
                .estados(estado)
                .tipoNotificacion(tipoNotificacion)
                .cuerpo("adsfasdfasdf")
            .build();

        notificacionRepo.save(notificacion);
    }

}
