package es.uma.informatica.usuariojpa.controllers;

import es.uma.informatica.usuariojpa.schemas.PasswordResetRequest;
import es.uma.informatica.usuariojpa.services.UsuarioService;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/passwordreset")
@RequiredArgsConstructor
public class PasswordResetController {

    private final UsuarioService usuarioService;

    /**
     * Pide al servicio que se genere una nueva contraseña para un usuario. El servicio debe enviar un e-mail a través del servicio de notificaciones con la nueva contraseña para el uusario.
     * Si el usuario no existe, la respuesta debe ser la misma que cuando existe, para no dar información acerca de la existencia del usuario en el sistema.
     *
     * @apiResponse Si el usuario existe cambia contraseña y envía notificación (200). En caso contrario no hace nada
     */
    @PostMapping()
    ResponseEntity<Void> resetearPassword(@RequestBody PasswordResetRequest request) {
        usuarioService.resetPassword(request);
        return ResponseEntity.ok().build();
    }
}
