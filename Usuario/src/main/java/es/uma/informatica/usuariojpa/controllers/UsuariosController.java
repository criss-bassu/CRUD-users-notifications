package es.uma.informatica.usuariojpa.controllers;

import es.uma.informatica.usuariojpa.modelo.Usuario;
import es.uma.informatica.usuariojpa.schemas.UsuarioDTO;
import es.uma.informatica.usuariojpa.services.UsuarioService;
import es.uma.informatica.usuariojpa.services.excepciones.AccesoNoAutorizado;
import es.uma.informatica.usuariojpa.services.excepciones.UsuarioMismoCorreoElectronico;
import es.uma.informatica.usuariojpa.services.excepciones.UsuarioNoEncontrado;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuariosController {

    private final UsuarioService usuarioService;

    private final ModelMapper mapper;

    /**
     * Actualiza el usuario un cierto id
     *
     * @param id Id del usuario a actualizar
     * @param u
     * @return ResponseEntity de un UsuarioDTO acorde con el código de respuesta
     * @apiResponse El usuario se ha actualizado (200)
     * @apiResponse Acceso no autorizado (403)
     * @apiResponse El usuario no existe (404)
     */
    @PutMapping("/{id}")
    ResponseEntity<UsuarioDTO> actualizarUsuario(@PathVariable("id") Long id, @RequestBody Usuario u, UriComponentsBuilder builder) {
        u.setId(id);
        usuarioService.modificarUsuario(u);
        URI uri = builder
                .path("/usuarios")
                .path(String.format("/%d", id))
                .build()
                .toUri();
        System.out.println(uri.getRawQuery());
        return ResponseEntity.ok().build();
    }

    /*@GetMapping
    public List<Long> obtenerListasUsuarios(){
        return usuarioService.obtenerTodosUsuarios();
    }*/

    /**
     * Crea un usuario en el sistema
     *
     * @param u Usuario a añadir al sistema
     * @return ResponseEntity con el codigo de la respuesta
     * @apiResponse el usuario se crea correctamente (201)
     * @apiResponse Acceso no autorizado (403)
     * @apiResponse Hay un usuario con el mismo correo electrónico en el sistema (409)
     */
    @PostMapping
    public ResponseEntity<?> crearUsuario(@RequestBody Usuario u, UriComponentsBuilder builder) {
        Long id = usuarioService.aniadirUsuario(u);
        URI uri = builder
                .path("/usuarios")
                .path(String.format("/%d", id))
                .build()
                .toUri();
        System.out.println(uri.getRawQuery());
        return ResponseEntity.created(uri).build();
    }


    /**
     * Obtiene un usuario correcto
     *
     * @param id Id del usuario a consultar
     * @return ResponseEntity de un UsuarioDTO acorde con el código de respuesta
     * @apiResponse El usuario existe (200)
     * @apiResponse Acceso no autorizado (403)
     * @apiResponse El usuario no existe (404)
     */
    @GetMapping("/{id}")
    ResponseEntity<UsuarioDTO> getUsuario(@PathVariable("id") Long id) {
        //TODO
        Optional<UsuarioDTO> user = usuarioService.getUsuarioByID(id).map(x -> mapper.map(x, UsuarioDTO.class));

        return ResponseEntity.of(user);
    }

    /**
     * Obtiene la lista de usuarios del sistema
     *
     * @return Listado de usuarios de la aplicación
     * @apiResponse Devuelve la lista de usuarios (200)
     * @apiResponse Acceso no autorizado (403)
     */
    @GetMapping
    ResponseEntity<List<UsuarioDTO>> obtenerUsuarios1() {
        //TODO
        var res = usuarioService.getUsers();
        return new ResponseEntity<>(res.stream().map(x -> mapper.map(x, UsuarioDTO.class)).toList(), HttpStatus.OK);
    }

    // Método que elimina al usuario
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarUsuario(@PathVariable Long id) {
        if (usuarioService.getUsuarioByID(id).isPresent()) {
            usuarioService.eliminarUsuario(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(UsuarioNoEncontrado.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public void noEncontrado() {	}

    @ExceptionHandler(AccesoNoAutorizado.class)
    @ResponseStatus(code = HttpStatus.FORBIDDEN)
    public void noAutorizado() {	}

    //Tratamos la excepción de crear un usuario con un correo electrónico ya existente
    @ExceptionHandler(UsuarioMismoCorreoElectronico.class)
    @ResponseStatus(code = HttpStatus.CONFLICT)
    public void correoyaUsado(){   }
}
