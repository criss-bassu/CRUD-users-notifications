package es.uma.informatica.usuariojpa.components;

import org.springframework.stereotype.Component;

@Component
public class Mapper {

    /**
     *
     * TODO: DESCOMENTAR CUANDO ESTÉN TODOS LOS ESQUEMAS
     *
    public UsuarioDTO toDTO(Usuario usuario) {
        return UsuarioDTO.builder()
                .id(usuario.getId())
                .email(usuario.getEmail())
                .nombre(usuario.getNombre())
                .apellido1(usuario.getApellidos().split(" ")[0])
                .apellido2(usuario.getApellidos().split(" ")[1])
                .build();
    }

    public Usuario toUsuario(UsuarioDTO usuarioDTO) {
        return new Usuario(
                usuarioDTO.getId(),
                null,
                usuarioDTO.getNombre(),
                usuarioDTO.getApellido1() + " " + usuarioDTO.getApellido2(),
                null,
                usuarioDTO.getEmail(),
                null);
    }
     */
}
