package es.uma.informatica.usuariojpa.schemas;

import lombok.*;

@Data
//@Builder
@RequiredArgsConstructor
public class UsuarioNuevo {

    private String nombre;
    private String apellido1;

    private String apellido2;

    private String email;


}
