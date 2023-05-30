package es.uma.informatica.notificacion.services;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import es.uma.informatica.notificacion.modelo.Estado;
import es.uma.informatica.notificacion.modelo.Notificacion;
import es.uma.informatica.notificacion.repositories.NotificacionRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SMSService {

    private final NotificacionRepository notificacionRepository;

    @Value("${twilio.auth.token}")
    private String TWILIO_AUTH_TOKEN;

    @Value("${twilio.account.sid}")
    private String TWILIO_ACCOUNT_SID;

    @Value("${twilio.outgoing.number}")
    private String TWILIO_OUT_GOING_NUMBER;

    /**
     * Proceso de autenticación de la aplicación para el servicio del API
     */
    @PostConstruct
    public void initSMSService() {
        Twilio.init(TWILIO_ACCOUNT_SID, TWILIO_AUTH_TOKEN);
    }

    /**
     * Método para el envío de sms, desde un número de teléfono asignado por twilio. IMPORTANTE solo se envían sms a teléfonos verificados por la aplicación.
     *
     * @param dstNumber Telefon de destino
     * @param text Cuerpo del mensaje
     * @return Cadena de texto con el estado del mensaje
     */
    public String sendSMS(String dstNumber, String text) {
        Message sms = Message.creator(
                new PhoneNumber(dstNumber),
                new PhoneNumber(TWILIO_OUT_GOING_NUMBER),
                text
        ).create();

        return sms.getSid();
    }

    public String sendSMS(Notificacion x) {
        // Preprocesing de la notificación
        String response = "";
        // Envío del sms
        try {
            response = sendSMS(x.getTelefonoDestino(), x.getAsunto() + "\n" + x.getCuerpo());
            x.setEstados(Estado.ENVIADO);
            x.setMomentoRealEnvio(new Date());
        } catch (Exception e ) {
            System.err.println("Fallo al enviar sms: " + e.getMessage());
            x.setEstados(Estado.ERROR);
            x.setMensajeError(e.getMessage());
            x.setIntentosRestantes(x.getIntentosRestantes() - 1);
            response = "Error";
        } finally {
            notificacionRepository.save(x);
        }

        return response;
    }


}
