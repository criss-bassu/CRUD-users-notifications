package es.uma.informatica.notificacion;

import es.uma.informatica.notificacion.modelo.Medio;
import es.uma.informatica.notificacion.modelo.Notificacion;
import es.uma.informatica.notificacion.modelo.TipoNotificacion;
import es.uma.informatica.notificacion.services.MailServiceImplement;
import es.uma.informatica.notificacion.services.SMSService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;


@SpringBootTest
@DisplayName("En el servicio de Notificacion. Pruebas unitarias para el testeo del envío de sms y email")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class EmailSMSServicesTests {

    @Autowired
    private SMSService smsService;

    @Autowired
    private MailServiceImplement mailService;

    @Nested
    @DisplayName("Pruebas para el envío de SMS")
    public class SMSTests {

        @Test
        @DisplayName("Envío de SMS a un número de teléfono válido")
        public void envioSMSEnCondicionesNormales() {
            String respuesta = smsService.sendSMS(Notificacion.builder()
                    .intentosRestantes(5)
                    .asunto("Test: ")
                    .cuerpo("Envío de sms en condiciones normales")
                    .tipoNotificacion(TipoNotificacion.ANUNCIO_AULA_ESTUDIANTE)
                    .medios(Collections.singletonList(Medio.SMS))
                    .telefonoDestino("+34684332722")
                    .build());

            assertThat(respuesta.equalsIgnoreCase("Delivered"));
        }

        @Test
        @DisplayName("Envío de SMS a un número de teléfono no válido")
        public void envioSMSParaDestinoNoValido() {
            String respuesta = smsService.sendSMS(Notificacion.builder()
                    .intentosRestantes(5)
                    .asunto("Test: ")
                    .cuerpo("Envío de sms a un número no verificado")
                    .tipoNotificacion(TipoNotificacion.ANUNCIO_AULA_ESTUDIANTE)
                    .medios(Collections.singletonList(Medio.SMS))
                    .telefonoDestino("+34123456789") // Número de teléfono no
                    .build());

            assertThat(respuesta.equalsIgnoreCase("Error"));
        }
    }

    @Nested
    @DisplayName("Pruebas para el envío de Email")
    public class EmailTests {

        @Test
        @DisplayName("Envio de una notificación intentos igual a 0")
        public void envioDeCorreoElectrónicoDeNotificacionNoValida() {
            Notificacion notificacion = Notificacion.builder()
                    .id(1L)
                    .intentosRestantes(0)
                    .medios(Collections.singletonList(Medio.EMAIL))
                    .asunto("Test:")
                    .cuerpo("Noticiación no válida")
                    .emailDestino("asdf@asdf.com")
                    .tipoNotificacion(TipoNotificacion.PASSWORD_RESET)
                    .build();

            String response = mailService.sendMail(notificacion);

            assertThat(response.equalsIgnoreCase("error"));


        }

        @Test
        @DisplayName("Envio de una notificación intentos superior a 0")
        public void envioDeCorreoElectrónicoEnCondicionesNormales() {
            Notificacion notificacion = Notificacion.builder()
                    .id(1L)
                    .intentosRestantes(5)
                    .medios(Collections.singletonList(Medio.EMAIL))
                    .asunto("Test:")
                    .cuerpo("Noticiación válida")
                    .emailDestino("asdf@asdf.com")
                    .tipoNotificacion(TipoNotificacion.PASSWORD_RESET)
                    .build();

            String response = mailService.sendMail(notificacion);

            assertThat(response.equalsIgnoreCase("error"));

        }
    }

}
