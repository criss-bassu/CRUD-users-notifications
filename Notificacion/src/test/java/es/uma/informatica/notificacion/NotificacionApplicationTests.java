package es.uma.informatica.notificacion;

import es.uma.informatica.notificacion.modelo.Estado;
import es.uma.informatica.notificacion.modelo.Medio;
import es.uma.informatica.notificacion.modelo.Notificacion;
import es.uma.informatica.notificacion.modelo.TipoNotificacion;
import es.uma.informatica.notificacion.repositories.NotificacionRepository;
import es.uma.informatica.notificacion.schemas.NotificacionDTO;
import es.uma.informatica.notificacion.schemas.NotificacionNueva;
import es.uma.informatica.notificacion.services.excepciones.NotificacionNoEncontrada;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriBuilderFactory;

import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("En el servicio de Notificacion")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class NotificacionApplicationTests {

    public static final String BEARER = "Bearer ";
    public static final String AUTHORIZATION = "Authorization";
    private static final String TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwicm9sZXMiOlsiQUxVTU5PIiwiQ09SUkVDVE9SIl19.P0kuh5dTBU7zl_7L_wt6tJSPXrc7LjXGrq_FhTdhubc";

    @Autowired
    private TestRestTemplate restTemplate;

    @Value(value="${local.server.port}")
    private int port;

    @Autowired
    private NotificacionRepository repo;

    @BeforeEach
    public void initializeDatabase() {
        repo.deleteAll();
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

    private <T> RequestEntity.BodyBuilder post(String scheme, String host, int port, String path) {
        URI uri = uri(scheme, host,port, path);
        return RequestEntity.post(uri)
                .contentType(MediaType.APPLICATION_JSON);
    }

    private <T> RequestEntity<T> put(String scheme, String host, int port, String path, T object) {
        URI uri = uri(scheme, host,port, path);
        var peticion = RequestEntity.put(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .body(object);
        return peticion;
    }

    /* private void compruebaCampos(Notificacion expected, Notificacion actual) {
        assertThat(actual.getAsunto().equals(expected.getAsunto()));    // isEqualTo?
        assertThat(actual.getEstados().equals(expected.getEstados()));    // isEqualTo?
        assertThat(actual.getCuerpo().equals(expected.getCuerpo()));  // isEqualTo?
        assertThat(actual.getProgramacionEnvio()).isEqualTo(expected.getProgramacionEnvio());
        assertThat(actual.getIdUsuario()).isEqualTo(expected.getIdUsuario());
        assertThat(actual.getMedios()).isEqualTo(expected.getMedios());
    } */

    //@Nested
    //@DisplayName("cuando NO hay notificaciones")
    //public class ListaVacia {

        @Nested
        @DisplayName("crea una notificacion")
        public class crearNotificacion {
            @Test
            @DisplayName("sin ID")
            public void sinID() {
                NotificacionNueva notificacion = NotificacionNueva.builder()
                        .cuerpo("Creando una notificaciones con ID")
                        .emailDestino("cbaronsuarez@uma.es")
                        .asunto("Nueva notificacion con ID")
                        .programacionEnvio(new Date())
                        .medios(new ArrayList<>(Medio.EMAIL.ordinal()))
                        .tipoNotificacion(TipoNotificacion.ANUNCIO_NOTA_ESTUDIANTE)
                        .build();

                var peticion = HttpUtils.post("http", "localhost", port, "/notificaciones", notificacion, TOKEN);
                var respuesta = restTemplate.exchange(peticion, Void.class);

                compruebaRespuesta(notificacion, respuesta);
            }

            private void compruebaRespuesta(NotificacionNueva notificacion, ResponseEntity<Void> respuesta) {
                assertThat(respuesta.getStatusCode().value()).isEqualTo(201);
                assertThat(respuesta.getHeaders().get("Location").get(0))
                        .startsWith("http://localhost:"+port+"/notificaciones");

                List<Notificacion> notificaciones = repo.findAll();
                assertThat(notificaciones).hasSize(1);
                assertThat(respuesta.getHeaders().get("Location").get(0))
                        .endsWith("/"+notificaciones.get(0).getId());
                // compruebaCampos(notificacion, notificaciones.get(0));
            }

            /*@Test
            @DisplayName("sin ID")
            public void sinID() {
                Notificacion notificacion = new Notificacion();
                notificacion.setId(null);
                notificacion.setEstado(Estado.ENVIADO);
                notificacion.setAsunto("prueba");
                notificacion.setFechaEnvio(new Date());
                notificacion.setMedios(Medio.EMAIL);
                notificacion.setIdUsuario(1L);
                notificacion.setMensaje("Este es el contenido de la prueba 2 que tiene ID");
                // notificacion.setIntentos(5);
                var peticion = post("http", "localhost", port, "/notificaciones", notificacion);

                var respuesta = restTemplate.exchange(peticion, Void.class);

                compruebaRespuesta(notificacion, respuesta);
            }*/
        }

    @Nested
    @DisplayName("obtiene una notificacion")
    public class obtenerNotificacion {
        @BeforeEach
        public void setUp(){
            Notificacion notificacion = Notificacion.builder()
                    .cuerpo("Creando una notificaciones con ID")
                    .emailDestino("cbaronsuarez@uma.es")
                    .asunto("Nueva notificacion con ID")
                    .programacionEnvio(new Date())
                    .medios(new ArrayList<>(Medio.EMAIL.ordinal()))
                    .idUsuario(1L)
                    .tipoNotificacion(TipoNotificacion.ANUNCIO_NOTA_ESTUDIANTE)
                    .build();
            repo.save(notificacion);
        }

            @Test
        @DisplayName("cuando existe")
        public void devuelveNotificacion() {
            var peticion = HttpUtils.get("http", "localhost",port, "/notificaciones/1", TOKEN);

            var respuesta = restTemplate.exchange(peticion,Notificacion.class);

            assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
            assertThat(respuesta.hasBody()).isEqualTo(true);
            assertThat(respuesta.getBody()).isNotNull();
        }
        /*@Test
        @DisplayName("la devuelve cuando existe")
        public void obtenerNotificacionExistente() {
            ResponseEntity<Notificacion> responseEntity = restTemplate.getForEntity("/notificaciones/{id}", Notificacion.class, 1L);

            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        }*/

        @Test
        @DisplayName("da error cuando NO existe")
        public void errorCuandoNotificacionNoExiste() {
            var peticion = HttpUtils.get("http", "localhost",port, "/notificaciones/28", TOKEN);

            var respuesta = restTemplate.exchange(peticion,
                    new ParameterizedTypeReference<List<Notificacion>>() {});

            assertThat(respuesta.getStatusCode().value()).isEqualTo(404);
            assertThat(respuesta.hasBody()).isEqualTo(false);
        }
        @Test
        @DisplayName("devuelve la lista completa de notificaciones")
        public void devuelveListaNotificacionesPorTipoYEstado() {
            var peticion = HttpUtils.get("http", "localhost",port, "/notificaciones", TOKEN);

            var respuesta = restTemplate.exchange(peticion,
                    new ParameterizedTypeReference<List<Notificacion>>() {});

            assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
            assertThat(respuesta.hasBody()).isEqualTo(true);
            assertThat(respuesta.getBody()).isNotNull();
        }
    }

        /*@Test
        @DisplayName("devuelve error cuando se pide una notificacion que no existe")
        public void devuelveErrorAlConsultarNotificacionInexistente() {
            var peticion = get("http", "localhost",port, "/notificaciones/28");

            var respuesta = restTemplate.exchange(peticion, Void.class);

            assertThat(respuesta.getStatusCode().value()).isEqualTo(404);
            assertThat(respuesta.hasBody()).isEqualTo(false);
        }*/




    // }

    //@Nested
    //@DisplayName("cuando existen notificaciones")
    //public class ListaConDatos {
        /*@BeforeEach
        public void introduceDatos() {
            Notificacion notificacion1 = new Notificacion();
            notificacion1.setId(null);
            notificacion1.setEstado(Estado.PENDIENTE);
            notificacion1.setAsunto(null);
            notificacion1.setFechaEnvio(new Date());
            notificacion1.setMedios(Medio.SMS);
            notificacion1.setIdUsuario(1L);
            notificacion1.setMensaje("Este es el contenido de la prueba 3 que NO tiene ID");
            // notificacion1.setIntentos(5);
            repo.save(notificacion1);

            Notificacion notificacion2 = new Notificacion();
            notificacion2.setId(null);
            notificacion2.setEstado(Estado.ENVIADO);
            notificacion2.setAsunto("HolaDeNuevo");
            notificacion2.setFechaEnvio(new Date());
            notificacion2.setMedios(Medio.EMAIL);
            notificacion2.setIdUsuario(1L);
            notificacion2.setMensaje("Este es el contenido de la prueba 4 que NO tiene ID");
            // notificacion2.setIntentos(5);
            repo.save(notificacion2);
        }*/

        /*@Test
        @DisplayName("devuelve la lista de notificaciones correctamente")
        public void devuelveLista() {
            var peticion = get("http", "localhost", port, "/notificaciones");

            var respuesta = restTemplate.exchange(peticion, new ParameterizedTypeReference<List<Notificacion>>() {
            });

            assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
            assertThat(respuesta.getBody()).hasSize(1);
        }*/

        //@Nested
        //@DisplayName("crea una notificacion")
        //public class InsertaNotificaciones {
            /*@Test
            @DisplayName("sin ID establecido")
            public void sinID() {
                Notificacion notificacion = new Notificacion();
                notificacion.setId(null);
                notificacion.setEstado(Estado.PENDIENTE);
                notificacion.setAsunto("");
                notificacion.setFechaEnvio(new Date());
                notificacion.setMedios(Medio.SMS);
                notificacion.setIdUsuario(1L);
                notificacion.setMensaje("Este es el contenido de la prueba 5 que NO tiene ID");
                // notificacion.setIntentos(5);
                var peticion = post("http", "localhost", port, "/notificaciones", notificacion);

                var respuesta = restTemplate.exchange(peticion, Void.class);

                compruebaRespuesta(notificacion, respuesta);
            }*/
            /*@Test
            @DisplayName("cuando NO existe otra con el ID asignado")
            public void conID() {
                Notificacion notificacion = Notificacion.builder()
                        .id(2L)
                        .cuerpo("Creando una notificaciones con ID v2")
                        .emailDestino("cbaronsuarez@uma.es")
                        .asunto("Nueva notificacion con ID 2")
                        .programacionEnvio(new Date())
                        .medios(new ArrayList<>(Medio.EMAIL.ordinal()))
                        .idUsuario(1L)
                        .tipoNotificacion(TipoNotificacion.ANUNCIO_NOTA_ESTUDIANTE)
                        .build();
                repo.save(notificacion);

                var peticion = post("http", "localhost", port, "/notificaciones", notificacion);

                var respuesta = restTemplate.exchange(peticion, Void.class);

                compruebaRespuesta(notificacion, respuesta);
            }*/
            /*@Test
            @DisplayName("a pesar de que el ID coincida con uno existente")
            public void conIDExistente() {
                Notificacion notificacion = new Notificacion();
                notificacion.setId(1L);
                notificacion.setEstado(Estado.ENVIADO);
                notificacion.setAsunto("Hola");
                notificacion.setProgramacionEnvio(new Date());
                notificacion.setMedios(Medio.EMAIL);
                // notificacion.setIntentos(5);
                var peticion = post("http", "localhost", port, "/notificaciones", notificacion);

                var respuesta = restTemplate.exchange(peticion, Void.class);

                compruebaRespuesta(notificacion, respuesta);
            }*/

            /*private void compruebaRespuesta(Notificacion notificacion, ResponseEntity<Void> respuesta) {
                assertThat(respuesta.getStatusCode().value()).isEqualTo(201);
                assertThat(respuesta.getHeaders().get("Location").get(0))
                        .startsWith("http://localhost:"+port+"/notificaciones");

                List<Notificacion> notificaciones = repo.findAll();
                assertThat(notificaciones).hasSize(2);
                assertThat(respuesta.getHeaders().get("Location").get(0))
                        .endsWith("/"+notificaciones.get(0).getId());
                // compruebaCampos(notificacion, notificaciones.get(0));
            }*/
        // }

        /*@Nested
        @DisplayName("al consultar una notificacion en concreto")
        public class ObtenerNotificaciones {
            @Test
             @DisplayName("lo devuelve cuando existe")
             public void devuelveNotificacion() {
                 var peticion = get("http", "localhost",port, "/notificaciones/1");

                 var respuesta = restTemplate.exchange(peticion,Notificacion.class);

                 assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
                 assertThat(respuesta.hasBody()).isEqualTo(true);
                 assertThat(respuesta.getBody()).isNotNull();
             }
            @Test
            @DisplayName("la devuelve cuando existe")
            public void obtenerNotificacionExistente() {
                ResponseEntity<Notificacion> responseEntity = restTemplate.getForEntity("/notificaciones/{id}", Notificacion.class, 1L);

                assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
            }


            @Test
            @DisplayName("da error cuando NO existe")
            public void errorCuandoNotificacionNoExiste() {
                var peticion = get("http", "localhost",port, "/notificaciones/28");

                var respuesta = restTemplate.exchange(peticion,
                        new ParameterizedTypeReference<List<Notificacion>>() {});

                assertThat(respuesta.getStatusCode().value()).isEqualTo(404);
                assertThat(respuesta.hasBody()).isEqualTo(false);
            }

            @Test
            @DisplayName("da error cuando no se tiene acceso")
            public void errorAccesoNoAutorizado() {
                Notificacion notificacion = new Notificacion();
                notificacion.setId(14L);
                notificacion.setEstado(Estado.PENDIENTE);
                notificacion.setAsunto("");
                notificacion.setFechaEnvio(new Date());
                notificacion.setMedios(Medio.SMS);
                notificacion.setIdUsuario(2L); // ID del usuario al que se destina la notificación
                notificacion.setMensaje("Mensaje");

                Assertions.assertThrows(RuntimeException.class, () -> {
                    verificarAcceso(notificacion);
                });
            }

            @Test
            @DisplayName("Se puede acceder si se tiene acceso")
            public void accesoAutorizado() {
                Notificacion notificacion = new Notificacion();
                notificacion.setId(15L);
                notificacion.setEstado(Estado.PENDIENTE);
                notificacion.setAsunto("");
                notificacion.setFechaEnvio(new Date());
                notificacion.setMedios(Medio.SMS);
                notificacion.setIdUsuario(1L); // ID del usuario al que se destina la notificación
                notificacion.setMensaje("Mensaje");

                Assertions.assertDoesNotThrow(() -> {
                    verificarAcceso(notificacion);
                });
            }

            private void verificarAcceso(Notificacion notificacion) {
                long idUsuarioActual = 1L;
                if (notificacion.getIdUsuario() != idUsuarioActual) {
                    throw new RuntimeException("No se tiene acceso a la notificación.");
                }
            }
        }
        */

        @Nested
        @DisplayName("actualizar notificacion")
        public class actualizarNotificaciones {



            @BeforeEach
            public void config(){
                Notificacion n = Notificacion.builder()
                        .cuerpo("Actualizando notificacion correctamente")
                        .emailDestino("0619993622@uma.es")
                        .asunto("Actualizacion notificacion")
                        .telefonoDestino("744483726")
                        .id(1L)
                        .programacionEnvio(new Date())
                        .medios(new ArrayList<>(Medio.EMAIL.ordinal()))
                        .tipoNotificacion(TipoNotificacion.ANUNCIO_NOTA_ESTUDIANTE)
                        .build();
                repo.save(n);
            }

            @Test
            @DisplayName("la notificacion se ha actualizado")
            @DirtiesContext
            public void actualizarCorrectamente() {
                NotificacionDTO not = new NotificacionDTO().builder()
                        .cuerpo("Actualizando notificacion correctamente")
                        .emailDestino("0619993622@uma.es")
                        .asunto("Actualizacion notificacion")
                        .telefonoDestino("744483726")
                        .programacionEnvio(new Date())
                        .medios(new ArrayList<>(Medio.EMAIL.ordinal()))
                        .tipoNotificacion(TipoNotificacion.ANUNCIO_NOTA_ESTUDIANTE)
                        .build();
                var peticion = HttpUtils.put("http", "localhost", port, "/notificaciones/1", not, TOKEN);

                var respuesta = restTemplate.exchange(peticion, Void.class);
                assertThat(respuesta.getStatusCode().value()).isEqualTo(200);

            }

            @Test
            @DisplayName("la notificacion no existe")
            public void errorCuandoNoExiste() {
                NotificacionDTO not = new NotificacionDTO().builder()
                        .cuerpo("Error, la notificacion no existe")
                        .emailDestino("0619993622@uma.es")
                        .asunto("Actualizacion notificacion")
                        .telefonoDestino("744483726")
                        .programacionEnvio(new Date())
                        .medios(new ArrayList<>(Medio.EMAIL.ordinal()))
                        .tipoNotificacion(TipoNotificacion.ANUNCIO_NOTA_ESTUDIANTE)
                        .build();
                var peticion = HttpUtils.put("http", "localhost", port, "/notificaciones/2", not, TOKEN);
                var respuesta = restTemplate.exchange(peticion, Void.class);
                assertThat(respuesta.getStatusCode().value()).isEqualTo(404);
                assertThat(respuesta.hasBody()).isEqualTo(false);
                /*if(respuesta.getStatusCode().value()==404){
                    throw new NotificacionNoEncontrada();
                }*/
            }
           /* @Test
            @DisplayName("devuelve error cuando se actualiza una notificacion  concreta")
            public void devuelveErrorAlActualizarNotificacion() {
                NotificacionDTO notificacion = new NotificacionDTO().builder()
                        .cuerpo("Error al actualizar notificacion")
                        .emailDestino("0619993622@uma.es")
                        .id(1L)
                        .asunto("Actualizacion notificacion")
                        .telefonoDestino("744483726")
                        .programacionEnvio(new Date())
                        .medios(new ArrayList<>(Medio.EMAIL.ordinal()))
                        .tipoNotificacion(TipoNotificacion.ANUNCIO_NOTA_ESTUDIANTE)
                        .build();

                var peticion = HttpUtils.put("http", "localhost",port, "/notificacion/2", notificacion,TOKEN);

                var respuesta = restTemplate.exchange(peticion, Void.class);

                assertThat(respuesta.getStatusCode().value()).isEqualTo(404);
            }*/
        }

            /*
            private void verificarAcceso(Notificacion notificacion) {
                long idUsuarioActual = 1L;
                if (notificacion.getIdUsuario() != idUsuarioActual) {
                    throw new RuntimeException("No se tiene acceso a la notificación.");
                }
            }
            @Test
            @DisplayName("acceso no autorizado")
            public void errorAccesoNoAutorizado() {
                Notificacion notificacion = new Notificacion();
                notificacion.setId(14L);
                notificacion.setEstado(Estado.PENDIENTE);
                notificacion.setAsunto("");
                notificacion.setFechaEnvio(new Date());
                notificacion.setMedios(Medio.SMS);
                notificacion.setIdUsuario(2L); // ID del usuario al que se destina la notificación
                notificacion.setMensaje("Mensaje");

                Assertions.assertThrows(RuntimeException.class, () -> {
                    verificarAcceso(notificacion);
                });
            }



        }*/

        @Nested
        @DisplayName("Al eliminar notificación")
        public class eliminarNotificacion{
            @BeforeEach
            public void prepararNoti(){
                Notificacion n = Notificacion.builder()
                        .cuerpo("Actualizando notificacion correctamente")
                        .emailDestino("jmpguma@uma.es")
                        .asunto("Actualizacion notificacion")
                        .telefonoDestino("744483726")
                        .id(1L)
                        .programacionEnvio(new Date())
                        //.medios(new ArrayList<>(Medio.EMAIL.ordinal()))
                        .tipoNotificacion(TipoNotificacion.ANUNCIO_NOTA_ESTUDIANTE)
                        .build();
                repo.save(n);
            }
            @Test
            @DisplayName("Se ha eliminado correctamente")
            public void eliminarCorrectamente(){
                var peticion = HttpUtils.delete("http","localhost",port,"/notificaciones/1",TOKEN);
                var respuesta = restTemplate.exchange(peticion, Void.class);
                assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
            }
        }

    //}


}
