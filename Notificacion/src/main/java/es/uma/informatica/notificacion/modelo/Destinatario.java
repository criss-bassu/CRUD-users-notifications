package es.uma.informatica.notificacion.modelo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Destinatario {
    private String nombre;
    private String apellidos;
    private String email;
    private String tlf;
}
