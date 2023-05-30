package es.uma.informatica.usuariojpa;

// import es.uma.informatica.usuariojpa.controllers.UsuariosController;
import es.uma.informatica.usuariojpa.modelo.Rol;
import es.uma.informatica.usuariojpa.modelo.Token;
import es.uma.informatica.usuariojpa.modelo.TokenType;
import es.uma.informatica.usuariojpa.modelo.Usuario;
import es.uma.informatica.usuariojpa.repositories.TokenRepository;
import es.uma.informatica.usuariojpa.repositories.UsuarioRepository;
import es.uma.informatica.usuariojpa.schemas.UsuarioDTO;
import es.uma.informatica.usuariojpa.services.JwtService;
import es.uma.informatica.usuariojpa.services.excepciones.AccesoNoAutorizado;
import es.uma.informatica.usuariojpa.services.excepciones.UsuarioMismoCorreoElectronico;
import es.uma.informatica.usuariojpa.services.excepciones.UsuarioNoEncontrado;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriBuilderFactory;

import java.net.URI;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("Testeo del servicio de usuario")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UsuarioJpaApplicationTests {

    public static final String TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwicm9sZXMiOiJBTFVNTk8ifQ.4sxh7ph0tG_WRqqIJRFiIIa3sRUNGZlAVOzFputjYTA";

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TokenRepository tokenRepo;

    @Autowired
    private JwtService jwtService;

    @Value(value="${local.server.port}")
    private int port;

    @Autowired
    private UsuarioRepository usuarioRepo;

    @BeforeEach
    public void initializeDatabase() {
        usuarioRepo.deleteAll();
    }

    private URI uri(String scheme, String host, int port, String ...paths) {
        UriBuilderFactory ubf = new DefaultUriBuilderFactory();
        UriBuilder ub = ubf.builder()
                .scheme(scheme)
                .host(host).port(port);
        for (String path: paths) {
            ub = ub.path(path);
        }
        return ub.build();
    }

    private RequestEntity<Void> get(String scheme, String host, int port, String path) {
        URI uri = uri(scheme, host,port, path);
        var peticion = RequestEntity.get(uri)
                .accept(MediaType.APPLICATION_JSON)
                .build();
        return peticion;
    }

    private RequestEntity<Void> delete(String scheme, String host, int port, String path) {
        URI uri = uri(scheme, host,port, path);
        var peticion = RequestEntity.delete(uri)
                .build();
        return peticion;
    }

    private <T> RequestEntity<T> post(String scheme, String host, int port, String path, T object) {
        URI uri = uri(scheme, host,port, path);
        var peticion = RequestEntity.post(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .body(object);
        return peticion;
    }

    private <T> RequestEntity<T> put(String scheme, String host, int port, String path, T object) {
        URI uri = uri(scheme, host,port, path);
        var peticion = RequestEntity.put(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .body(object);
        return peticion;
    }

   /* @Nested
    @DisplayName("cuando NO hay usuario")
    public class ListaVacia {

        String token;
        public void config() {
            Usuario u = Usuario.builder()
                    .id(3L)
                    .nombre("Patricia")
                    .contrasenia("puesnosequeponer")
                    .email("0619993622@uma.es")
                    .build();
            // Creo el token, se añade a la bd junto al usuario que le corresponde (para el posterior test del post)
            Token token = new Token(1, jwtService.generateToken(Map.of("roles",List.of(Rol.ESTUDIANTE)),u), TokenType.BEARER, false, false, u);
            u.setToken(token);
            tokenRepo.save(token);
            usuarioRepo.save(u);
            this.token = token.getToken();
        }
*/

/*
        @Test
        @DisplayName("devuelve la lista de usuarios vacia")
        public void devuelveLista(){
            var peticion = HttpUtils.get("http","localhost",port,"/usuarios/3",token);
            var respuesta = restTemplate.exchange(peticion, new ParameterizedTypeReference<List<Usuario>>() {
            });
            assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
            assertThat(respuesta.getBody()).isEmpty();
        }
*/
        /*@Test
        @DisplayName("devuelve error cuando se elimina un usuario concreto")
        public void devuelveErrorAlEliminarContacto() {
            var peticion = delete("http", "localhost",port, "/api/agenda/contactos/1");

            var respuesta = restTemplate.exchange(peticion, Void.class);

            assertThat(respuesta.getStatusCode().value()).isEqualTo(404);
        }*/
/*
        @Test
        @DisplayName("acceso no autorizado")
        public void errorCuandoAccesoDenegado() {
            var peticion = HttpUtils.get("http", "localhost",port, "/usuarios",token);

            var respuesta = restTemplate.exchange(peticion,Void.class);

            assertThat(respuesta.getStatusCode().value()).isEqualTo(403);
            assertThat(respuesta.hasBody()).isEqualTo(false);
        }
    }
*/
  /*  @Nested
    @DisplayName("cuando existen usuarios")
    public class ListaConDatos {

        String token;
        public void config() {
            Usuario u = Usuario.builder()
                    .id(3L)
                    .nombre("Patricia")
                    .contrasenia("puesnosequeponer")
                    .email("0619993622@uma.es")
                    .build();
            // Creo el token, se añade a la bd junto al usuario que le corresponde (para el posterior test del post)
            Token token = new Token(1, jwtService.generateToken(Map.of("roles",List.of(Rol.ESTUDIANTE)),u), TokenType.BEARER, false, false, u);
            u.setToken(token);
            tokenRepo.save(token);
            usuarioRepo.save(u);
            this.token = token.getToken();
        }
*/
       /* @Test
        @DisplayName("devuelve la lista de usuarios correctamente")
        public void devuelveLista(){
            var peticion = HttpUtils.get("http","localhost",port,"/usuarios/3",token);
            var respuesta = restTemplate.exchange(peticion, new ParameterizedTypeReference<List<Usuario>>() {
            });
            assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
            assertThat(respuesta.getBody()).hasSize(2);
        }
*/

        @Nested
        @DisplayName("al consultar un usuario concreto")
        public class ObtenerUsuarios {

            String token;

            @BeforeEach
            public void config() {
                Usuario us = Usuario.builder()
                        .id(3L)
                        .nombre("Patricia")
                        .contrasenia("puesnosequeponer")
                        .email("0619993622@uma.es")
                        .build();
                // Creo el token, se añade a la bd junto al usuario que le corresponde (para el posterior test del get)
                Token token = new Token(1, jwtService.generateToken(Map.of("roles",List.of(Rol.ESTUDIANTE)),us), TokenType.BEARER, false, false, us);
                us.setToken(token);
                tokenRepo.save(token);
                usuarioRepo.save(us);
                this.token = token.getToken();
            }



            @Test
            @DisplayName("lo devuelve cuando existe")
            public void devuelveUsuario(){

                var peticion = HttpUtils.get("http","localhost",port,"/usuarios/1",this.token);
                var respuesta = restTemplate.exchange(peticion,UsuarioDTO.class);
                assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
                assertThat(respuesta.hasBody()).isEqualTo(true);
                assertThat(respuesta.getBody()).isNotNull();
            }

            @Test
            @DisplayName("acceso no autorizado")
            public void errorCuandoAccesoDenegado() {
                var peticion = HttpUtils.get("http", "localhost",port, "/usuarios","token.no.valido");

                var respuesta = restTemplate.exchange(peticion,Void.class);

                assertThat(respuesta.getStatusCode().value()).isEqualTo(403);
                assertThat(respuesta.hasBody()).isEqualTo(false);
            }


            @Test
            @DisplayName("da error cuando NO existe")
            public void errorCuandoUsuarioNoExiste() {
                var peticion = HttpUtils.get("http", "localhost",port, "/usuarios/28", token);

                var respuesta = restTemplate.exchange(peticion,
                        new ParameterizedTypeReference<List<Usuario>>() {});

                assertThat(respuesta.getStatusCode().value()).isEqualTo(404);
                assertThat(respuesta.hasBody()).isEqualTo(false);
            }

        }

        @Nested
        @DisplayName("al eliminar un usuario")
        public class EliminarUsuarios {

            String token;
            @BeforeEach
            public void initUser() {
                // Crear usuario
                Usuario usuario = Usuario.builder()
                        .id(1L)
                        .nombre("Cristina")
                        .contrasenia("contrasenia")
                        .email("cbaronsuarez@uma.es")
                        .build();

                // Crear Token
                Token token = new Token(1, jwtService.generateToken(Map.of("rol", Rol.ESTUDIANTE), usuario), TokenType.BEARER, false, false, usuario);
                usuario.setToken(token);
                // Añadir usuario a la aplicacion

                tokenRepo.save(token);
                usuarioRepo.save(usuario);
                this.token = token.getToken();
            }

            @Test
            @DisplayName("lo elimina cuando existe")
            public void eliminaCorrectamente() {
                List<Usuario> usuariosAntes = usuarioRepo.findAll();
                usuariosAntes.forEach(c->System.out.println(c));
                var peticion = HttpUtils.delete("http", "localhost",port, "/usuarios/1", this.token);

                var respuesta = restTemplate.exchange(peticion,Void.class);

                assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
                List<Usuario> usuariosDespues = usuarioRepo.findAll();
                assertThat(usuariosDespues).hasSize(usuariosAntes.size()-1);
                assertThat(usuariosDespues).allMatch(c->c.getId()!=9);
            }

            @Test
            @DisplayName("da error cuando NO existe")
            public void errorCuandoNoExiste() {
                var peticion = HttpUtils.delete("http", "localhost",port, "/usuarios/28", this.token);

                var respuesta = restTemplate.exchange(peticion,Void.class);

                assertThat(respuesta.getStatusCode().value()).isEqualTo(404);
                assertThat(respuesta.hasBody()).isEqualTo(false);
            }

            /*@Test
            @DisplayName("da error cuando NO se tiene acceso al usuario")
            public void errorCuandoAccesoDenegado() {
                var peticion = delete("http", "localhost",port, "/usuarios/1");

                var respuesta = restTemplate.exchange(peticion,Void.class);

                assertThat(respuesta.getStatusCode().value()).isEqualTo(403);
                assertThat(respuesta.hasBody()).isEqualTo(false);
            } */
        }
        @Nested
        @DisplayName("Al actualizar el usuario")
        public class actualizarUsuario {
            String token;
            @BeforeEach
            public void inicializarUsuario() {

                Usuario u = Usuario.builder().id(12L).
                        apellido1("nadal").
                        apellido2("parera").
                        nombre("rafael").
                        contrasenia("roland garros").
                        email("22gs@uma.es")
                        .build();
                // Creo el token, se añade a la bd junto al usuario que le corresponde (para el posterior test del post)
                Token token = new Token(1, jwtService.generateToken(Map.of("roles",List.of(Rol.ESTUDIANTE)),u), TokenType.BEARER, false, false, u);
                u.setToken(token);
                tokenRepo.save(token);
                usuarioRepo.save(u);
                this.token = token.getToken();
            }

            @Test
            @DisplayName("Se ha actualizado correctamente")
            public void actualizaCorrectamente() {
                Usuario u = Usuario.builder().id(12L).
                        apellido1("nadal").
                        apellido2("parera").
                        nombre("rafael").
                        contrasenia("roland garros").
                        email("22gs@uma.es")
                       .build();
                // Creo el token, se añade a la bd junto al usuario que le corresponde (para el posterior test del post)
                Token token = new Token(1, jwtService.generateToken(Map.of("roles",List.of(Rol.ESTUDIANTE)),u), TokenType.BEARER, false, false, u);
                u.setToken(token);
                tokenRepo.save(token);
                usuarioRepo.save(u);
                this.token = token.getToken();

                var peticion = HttpUtils.put("http","localhost",port,"/usuarios/12",u,this.token);
                var respuesta = restTemplate.exchange(peticion, Void.class);
                assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
            }

            @Test
            @DisplayName("Devuelve error cuando no encuentra el usuario")
            public void userNotFound() {
                Usuario u = Usuario.builder().id(12L).
                        apellido1("nadal").
                        apellido2("parera").
                        nombre("rafael").
                        contrasenia("roland garros").
                        email("22gs@uma.es")
                        .build();
                var peticion = HttpUtils.put("http","localhost",port,"/usuarios/28",u,this.token);
                var respuesta = restTemplate.exchange(peticion, Void.class);
                assertThat(respuesta.getStatusCode().value()).isEqualTo(404);
                assertThat(respuesta.hasBody()).isEqualTo(false);

                /*if(respuesta.getStatusCode().value()==404){
                    throw new UsuarioNoEncontrado();
                }*/
            }
        }
        @Nested
        @DisplayName("Al insertar el usuario")
        public class insertarUsuario {
            /*
            Preparo el usuario para la gestión del token del mismo
            Luego procedo a insertar los campos
             */
            String token;
            @BeforeEach
            public void cfg() {
                Usuario u = Usuario.builder()
                        .id(3L)
                        .nombre("Juan")
                        .contrasenia("kokunero")
                        .email("jmpguma@uma.es")
                        .build();
                // Creo el token, se añade a la bd junto al usuario que le corresponde (para el posterior test del post)
                Token token = new Token(1, jwtService.generateToken(Map.of("roles",List.of(Rol.ESTUDIANTE)),u), TokenType.BEARER, false, false, u);
                u.setToken(token);
                tokenRepo.save(token);
                usuarioRepo.save(u);
                this.token = token.getToken();
            }


            @Test
            @DisplayName("Se ha insertado correctamente")
            public void insertaCorrectamente() {
                Usuario u = Usuario.builder()
                        .id(1L)
                        .nombre("Juaniyo")
                        .contrasenia("kokunero")
                        .email("jmpgumo@uma.es")
                        .build();

                /*
                Gestión del token
                 */

                var peticion = HttpUtils.post("http","localhost",port,"/usuarios",u,token);
                var respuesta = restTemplate.exchange(peticion, Void.class);
                assertThat(respuesta.getStatusCode().value()).isEqualTo(201);

            }

            @Test
            @DisplayName("Da error por colisión de email")
            public void mismoCorreo() {
                Usuario u2 = Usuario.builder()
                        .id(2L)
                        .nombre("Juaniyo")
                        .contrasenia("kokunero21")
                        .email("jmpguma@uma.es")
                        .build();
                Usuario aux = usuarioRepo.findById(1L).get();

                var peticion = HttpUtils.post("http", "localhost", port, "/usuarios", u2, token);
                var respuesta = restTemplate.exchange(peticion, Void.class);
                assertThat(respuesta.getStatusCode().value()).isEqualTo(409); //CONFLICT


                /*if(u2.getEmail().equalsIgnoreCase(aux.getEmail())){
                    throw new UsuarioMismoCorreoElectronico();
                }*/
            }
            @Test
            @DisplayName("Da error por acceso no autorizado")
            public void accesoNoAutorizado() {
                Usuario u = new Usuario();
                u.setId(null);
                u.setNombre("Juaniyo");
                u.setApellido1("Pedrosaa");
                u.setApellido2("Garrido");
                u.setEmail("prueba2@uma.es");

                var peticion = HttpUtils.post("http", "localhost", port, "/usuarios",u,token);
                var respuesta = restTemplate.exchange(peticion, Void.class);
                assertThat(respuesta.getStatusCode().value()).isEqualTo(403);
                /*if(respuesta.getStatusCode().value()==403){
                    throw new AccesoNoAutorizado();
                }*/
            }
        }
   // }


}




















