package es.uma.informatica.usuariojpa.schemas;

import lombok.*;

@Data
//@Builder
@RequiredArgsConstructor
public class UsuarioDTO {
    private int id;
    private String nombre;
    private String apellido1;
    private String apellido2;
    private String email;
}
