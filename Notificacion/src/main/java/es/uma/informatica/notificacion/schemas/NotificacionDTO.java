package es.uma.informatica.notificacion.schemas;
import es.uma.informatica.notificacion.modelo.Estado;
import es.uma.informatica.notificacion.modelo.Medio;
import es.uma.informatica.notificacion.modelo.TipoNotificacion;
import lombok.*;

import java.util.Date;
import java.util.List;

//@Data
@Builder
//@RequiredArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotificacionDTO {

    private Long id;

    private String asunto;

    private String cuerpo;

    private String emailDestino;

    private String telefonoDestino;

    private Date programacionEnvio;

    private List<Medio> medios;

    private TipoNotificacion tipoNotificacion;


}
