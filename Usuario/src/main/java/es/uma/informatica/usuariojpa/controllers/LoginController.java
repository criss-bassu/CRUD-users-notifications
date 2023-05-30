package es.uma.informatica.usuariojpa.controllers;

import es.uma.informatica.usuariojpa.schemas.Login;
import es.uma.informatica.usuariojpa.schemas.RespuestaJWT;
import es.uma.informatica.usuariojpa.services.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/login")
public class LoginController {

    private final LoginService loginService;

    /**
     * Comprueba las credenciales del usuario y devuelve un JWT
     *
     * @apiResponse Devuelve un JWT para el usuario si las credenciales son correctas (200)
     * @apiResponse Credenciales no correctas (403)
     * @return ResponseEntity con el token JWT y el código de respuesta
     */
    @PostMapping(value = "/login")
    public ResponseEntity<RespuestaJWT> login(@RequestBody Login login) {
        RespuestaJWT respuesta = loginService.authenticate(login);
        if (respuesta == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(respuesta, HttpStatus.OK);
    }
}
