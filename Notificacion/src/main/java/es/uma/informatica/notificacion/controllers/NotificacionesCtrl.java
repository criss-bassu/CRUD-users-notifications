package es.uma.informatica.notificacion.controllers;

import es.uma.informatica.notificacion.repositories.NotificacionRepository;
import es.uma.informatica.notificacion.schemas.NotificacionDTO;
import es.uma.informatica.notificacion.services.NotificacionDBservice;
import es.uma.informatica.notificacion.services.excepciones.AccesoNoAutorizado;
import es.uma.informatica.notificacion.services.excepciones.NotificacionNoEncontrada;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import es.uma.informatica.notificacion.modelo.Notificacion;
import es.uma.informatica.notificacion.schemas.NotificacionNueva;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notificaciones")
public class NotificacionesCtrl {

    private final NotificacionDBservice servicio;

    private final ModelMapper mapper;

    /**
     * Actualiza una notificación
     *
     * @param id Id de la notificación a actualizar
     * @return ResponseEntity con la notificación actualizada
     * @apiResponse La notificación se ha actualizado (200)
     * @apiResponse Acceso no autorizado (403)
     * @apiResponse La notificación no existe (404)
     */


    @PutMapping("{id}")
    public ResponseEntity<?> actualizarNotificacion(@PathVariable Long id, @RequestBody NotificacionDTO notif){
        //notif.setId(id);
        servicio.actualizarLista(this.mapper.map(notif, Notificacion.class));
        return ResponseEntity.ok().build();
    }


    /**
     * Crea una nueva notificación en el sistema
     *
     * @param notificacion Notificación a añadir al sistema
     * @return ResponseEntity con el codigo de la respuesta
     * @apiResponse la notificación se crea correctamente (201)
     * @apiResponse Acceso no autorizado (403)
     */
    // Método que crea una nueva notificación en el sistema
    @PostMapping
    public ResponseEntity<?> crearNotificacion(@RequestBody NotificacionNueva notificacion, UriComponentsBuilder builder) {
        Long id = servicio.crearNotificacion(mapper.map(notificacion, Notificacion.class));
        URI uri = builder
                .path("/notificaciones")
                .path(String.format("/%d", id))
                .build()
                .toUri();
        return ResponseEntity.created(uri).build();
    }

    /**
     * Elimina la notificación
     *
     * @param id Id de la notificación a eliminar
     * @return ResponseEntity con el código de respuesta
     * @apiResponse La notificación se ha eliminado (200)
     * @apiResponse Acceso no autorizado (403)
     * @apiResponse La notificación no existe (404)
     */
   /* @DeleteMapping("/{id}")
    ResponseEntity<Void> eliminarNotificacion(@PathVariable("id") Long id) {
        return null;
    }*/

    /**
     * Obtiene una notificación concreta
     *
     * @param id Id de la notifación a consultar
     * @return ResponseEntity con el código de respuesta
     * @apiResponse La notificación existente
     * @apiResponse Acceso no autorizado (403)
     * @apiResponse La notificación no existe
     */
    //Método que obtiene una notificación concreta
    @GetMapping("{id}")
    public ResponseEntity<Notificacion> obtenerNotificacion(@PathVariable Long id) {
        Optional<Notificacion> n = servicio.getNotificacionByID(id);
        return ResponseEntity.of(n);
    }

    @GetMapping("/all")
    public ResponseEntity<List<NotificacionDTO>> getAllNotifications() {
        var notificaciones = this.servicio.obtenerTodasNotificaciones().stream().map(x -> mapper.map(x, NotificacionDTO.class)).toList();
        return  ResponseEntity.ofNullable(notificaciones);
    }


    /**
     * Obtiene la lista de notificaciones del sistema, filtrada de acuerdo con los parámetros de consulta (si se usan)
     *
     * @param tipo   Tipo de la notificación para la consultar {ANUNCIO_NOTA_ESTUDIANTE, ANUNCIO_AULA_ESTUDIANTE, ANUNCIO_AULA_VIGILANTE, PASSWORD_RESET}
     * @param estado Estado de notificación para la consulta {PENDIENTE, ENVIADO, ABORTADA, ERROR}
     * @return Listado de las notificaciones con el tipo y estado asociado
     * @apiResponse Devuelve la lista de notificaciones (200)
     * @apiResponse Acceso no autorizado (403)
     */
    @GetMapping()
    public ResponseEntity<List<NotificacionDTO>> obtenerNotificaciones(@RequestParam() String tipo, @RequestParam() String estado) {
        var res = this.servicio.getNotificacionesByTipo(tipo).map(x -> mapper.map(x, NotificacionDTO.class)).toList();

        return ResponseEntity.ofNullable(res);
    }

    /*
    *
    * public List<Notificacion> obtenerNotificaciones(String tipo, String estado) {
    // Crear una instancia de NotificacionesDBService
    NotificacionesDBService dbService = new NotificacionesDBService();

    // Llamar a los métodos correspondientes en NotificacionesDBService
    List<Notificacion> notificaciones = dbService.obtenerNotificaciones();
    List<Notificacion> notificacionesFiltradas = new ArrayList<>();

    // Filtrar las notificaciones por tipo y estado
    for (Notificacion notificacion : notificaciones) {
        if (notificacion.getTipo().equals(tipo) && notificacion.getEstado().equals(estado)) {
            notificacionesFiltradas.add(notificacion);
        }
    }

    // Devolver la lista de notificaciones filtradas
    return notificacionesFiltradas;
}

    * */

    @ExceptionHandler(NotificacionNoEncontrada.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public void noEncontrado() {	}

    /*@ExceptionHandler(AccesoNoAutorizado.class)
    @ResponseStatus(code = HttpStatus.FORBIDDEN)
    public void noAutorizado() {	}*/

    //Eliminamos una notificación en concreto
    @DeleteMapping("{id}")
    public ResponseEntity<?> eliminarNotificacion(@PathVariable Long id){
        if(servicio.getNotificacionByID(id).isPresent()){
            servicio.eliminar(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
