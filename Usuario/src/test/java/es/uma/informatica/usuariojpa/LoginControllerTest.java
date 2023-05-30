package es.uma.informatica.usuariojpa;

import es.uma.informatica.usuariojpa.controllers.LoginController;
import es.uma.informatica.usuariojpa.schemas.Login;
import es.uma.informatica.usuariojpa.schemas.RespuestaJWT;
import es.uma.informatica.usuariojpa.services.LoginService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class LoginControllerTest {

    @InjectMocks
    private LoginController loginController;

    @Mock
    private LoginService loginService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testLogin_WithCorrectCredentials_ReturnsJwtToken() {
        // Arrange
        Login login = new Login("username", "password");
        RespuestaJWT respuesta = new RespuestaJWT("jwtToken");
        when(loginService.authenticate(login)).thenReturn(respuesta);

        // Act
        ResponseEntity<RespuestaJWT> response = loginController.login(login);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(respuesta, response.getBody());
    }

    @Test
    public void testLogin_WithIncorrectCredentials_ReturnsForbidden() {
        // Arrange
        Login login = new Login("username", "wrongPassword");
        when(loginService.authenticate(login)).thenReturn(null);

        // Act
        ResponseEntity<RespuestaJWT> response = loginController.login(login);

        // Assert
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals(null, response.getBody());
    }
}
