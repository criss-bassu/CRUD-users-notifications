package es.uma.informatica.notificacion.schemas;
import es.uma.informatica.notificacion.modelo.Medio;
import es.uma.informatica.notificacion.modelo.TipoNotificacion;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.*;

import java.util.Date;
import java.util.List;


@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotificacionNueva {

    private String asunto;

    private String cuerpo;

    private String emailDestino;

    private String telefonoDestino;

    private Date programacionEnvio;

    private List<Medio> medios;

    private TipoNotificacion tipoNotificacion;


}
