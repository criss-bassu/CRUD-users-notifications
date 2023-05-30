package es.uma.informatica.notificacion.schemas;

import es.uma.informatica.notificacion.modelo.Notificacion;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


/**
 * Clase para cambiar el tipo de notificación. De un Objeto de esquema a un objeto de modelo
 * Ver: https://www.baeldung.com/java-dto-pattern
 */
@Component
public class NotificacionMapper {

    /**
     * Obtener una notificación apartir de un DTO
     * @param notificacionDTO dto con la información para el mapeo
     * @return Notificación del modelo con la información del DTO
     */
    public static Notificacion mapFromDTO(NotificacionDTO notificacionDTO) {
        return null;
    }

    /**
     * Obtener una notificación a partir de la petición de nueva Notificación (DTO)
     * @param notificacionNueva dto con la información para el mapeo
     * @return Notificación del modelo con la información del DTO
     */
    public static Notificacion mapFromDTO(NotificacionNueva notificacionNueva) {
        return null;
    }

    /**
     * Obtener una objeto DTO con la información asociada una entidad del modelo (Notificacion.class)
     * @param notificacion entidad del modelo con la informacion a consultar
     * @return DTO representativo de la notificacion pasada como argumento
     */
    public static NotificacionDTO mapFromModel(Notificacion notificacion) {
        return null;
    }

}
