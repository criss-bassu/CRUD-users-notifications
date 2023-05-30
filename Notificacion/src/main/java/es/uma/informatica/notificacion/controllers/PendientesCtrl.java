package es.uma.informatica.notificacion.controllers;

import es.uma.informatica.notificacion.modelo.TipoNotificacion;
import es.uma.informatica.notificacion.services.NotificacionDBservice;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.RequiredTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/pendientes")
public class PendientesCtrl {

    private final NotificacionDBservice notificacionDBservice;

    /**
     * Permite abortar las notificaciones pendientes de envío de un determinado tipo. Si no se especifica el tipo se abortan todas las pendientes.
     *
     * @param tipo Tipo de las notificaciones a abortar. Si no se indican se abortan todas las pendientes  { ANUNCIO_NOTA_ESTUDIANTE, ANUNCIO_AULA_ESTUDIANTE, ANUNCIO_AULA_VIGILANTE, PASSWORD_RESET }
     * @apiResponse Se han abortado las notificaciones pendientes indicadas (403)
     * @apiResponse Acceso no autorizado (403)
     */
    @PostMapping("/abortar")
    public ResponseEntity<Void> abortarNotificacionesPendientes(@RequestParam() String tipo) {
        // TODO: control de errores de entrada ( valor de tipo ) -> Excepción
        notificacionDBservice.abortaNotificacion(TipoNotificacion.valueOf(tipo));
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
