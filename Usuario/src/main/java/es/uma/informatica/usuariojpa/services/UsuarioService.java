package es.uma.informatica.usuariojpa.services;

import es.uma.informatica.usuariojpa.modelo.Usuario;
import es.uma.informatica.usuariojpa.repositories.UsuarioRepository;
import es.uma.informatica.usuariojpa.schemas.PasswordResetRequest;
import es.uma.informatica.usuariojpa.schemas.ResetpasswordEmailRequest;
import es.uma.informatica.usuariojpa.services.excepciones.UsuarioMismoCorreoElectronico;
import es.uma.informatica.usuariojpa.services.excepciones.UsuarioNoEncontrado;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriBuilderFactory;

import java.net.URI;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository repo;
    private final JwtService jwtService;
    private final RestTemplate restTemplate;

    private final PasswordService passwordService;

    /**
     * Busca un usuario en la BD en base a su id, si este no existe se eleva UsuaruiNoEncontradoException
     * @param id
     *
     * @return Optional<Usuario> con el usuario en cuestión
     * @throws UsuarioNoEncontrado en caso de que el usuario no se encuentre en la Base de datos
     */
    public Optional<Usuario> getUsuarioByID(Long id) throws UsuarioNoEncontrado{
        Optional<Usuario> res = repo.findById(id);
        if (res.isEmpty())
            throw new UsuarioNoEncontrado("El usuario solicitado no está contenido en la base de datos. ID: " + id );


        return res;
    }

    public void eliminarUsuario(Long id) throws UsuarioNoEncontrado{
        if (repo.existsById(id)) {
            repo.deleteById(id);
        } else {
            throw new UsuarioNoEncontrado();
        }
    }

    public List<Long> obtenerTodosUsuarios(){
        return repo.findAll().stream().map(Usuario::getId).toList();
    }

    public void resetPassword(PasswordResetRequest request) throws UsuarioNoEncontrado{
        Optional<Usuario> aux = repo.findByEmail(request.getEmail());
        if (aux.isEmpty())
            throw new UsuarioNoEncontrado("El usuario solicitado no está contenido en la base de datos. EMAIL: " + request.getEmail() );

        Usuario usuario = aux.get();
        String email = usuario.getEmail();
        String rndPassword = passwordService.generatePassword();
        usuario.setContrasenia(rndPassword);

        repo.save(usuario);

        // Enviar petición de notificación por correo (http req) o enviar correo mualmente (JavaMail)
        URI uri = uri("http", "localhost", 8080, "/notificaciones");
        var notificacionReset = ResetpasswordEmailRequest.builder();

        notificacionReset.asunto("Reseteo de contraseña");

        // List.of("SMS", "EMAIL")
        if (usuario.getTelefono() != null) {
            notificacionReset
                    .medios(List.of("SMS", "EMAIL"))
                    .telefonoDestino(usuario.getTelefono());
        }

        notificacionReset
                .tipoNotificacion("PASSWORD_RESET")
                .cuerpo("Su nueva contraseña es: ".concat(rndPassword))
                .emailDestino(usuario.getEmail())
                .programacionEnvio(Date.from(Instant.now().plusSeconds(60)))
                .build();



        var peticionNotificacion = RequestEntity.post(uri)
            .accept(MediaType.APPLICATION_JSON)
            .body(notificacionReset);

        /*restTemplate.exchange(peticionNotificacion, new ParameterizedTypeReference<String>() {
        });*/
    }
    //Añadimos el usuario al repositorio
    public Long aniadirUsuario(Usuario u){
        if (repo.findByEmail(u.getEmail()).isPresent())
            throw new UsuarioMismoCorreoElectronico("Ya existe un usuario con ese correo");

        u.setId(null);

        if (u.getContrasenia() == null)
            u.setContrasenia(passwordService.generatePassword());

        repo.save(u);
        return u.getId();
    }

    public List<Usuario> getUsers() {
        return repo.findAll();
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

    public void modificarUsuario(Usuario u) throws UsuarioNoEncontrado{
        if (repo.existsById(u.getId())) {
            Optional<Usuario> usuario = repo.findById(u.getId());
            usuario.ifPresent(x -> {
                x.setNombre(u.getNombre());
                x.setApellido1(u.getApellido1());
                x.setApellido2(u.getApellido2());
                x.setEmail(u.getEmail());
                repo.save(x);
            });
        } else {
            throw new UsuarioNoEncontrado();
        }
    }
}
