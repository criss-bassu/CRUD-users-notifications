package es.uma.informatica.notificacion.modelo;

import jakarta.persistence.*;
import lombok.*;
//import lombok.Builder;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Builder
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class Notificacion {

    @Id
    @GeneratedValue
    private Long id;

    private String asunto;

    @Column(nullable = false)
    private String cuerpo; //Contenido del mensaje
    /* Se puede obtener el destinario mediante un Embedded o un idUsuario
        Se prefiere usar peticiones HTTP con los id de los usuario para obtener la informacion.
        De esta forma evitamos duplicidad en los datos. */

    private String emailDestino;

    private String telefonoDestino;

    @Temporal(TemporalType.TIMESTAMP)
    private Date programacionEnvio;   // Cuándo debe enviarse el mensaje -> 09/03/2023 14:33

    @Column(name = "Usuario_id")
    private Long idUsuario; // NECESARIO?

    @ElementCollection
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private List<Medio> medios;  // tlf, sms o ambas

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoNotificacion tipoNotificacion;

    @Enumerated(EnumType.STRING)
    private Estado estados = Estado.PENDIENTE;

    private String mensajeError;

    private Integer intentosRestantes = 5;

    @Temporal(TemporalType.TIMESTAMP)
    private Date momentoRealEnvio;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Notificacion that = (Notificacion) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Notificacion{" +
                "id=" + id +
                ", asunto='" + asunto + '\'' +
                ", cuerpo='" + cuerpo + '\'' +
                ", emailDestino='" + emailDestino + '\'' +
                ", telefonoDestino='" + telefonoDestino + '\'' +
                ", programacionEnvio=" + programacionEnvio +
                ", medios=" + medios +
                ", tipoNotificacion=" + tipoNotificacion +
                ", estados=" + estados +
                ", mensajeError='" + mensajeError + '\'' +
                ", momentoRealEnvio=" + momentoRealEnvio +
                '}';
    }
}
