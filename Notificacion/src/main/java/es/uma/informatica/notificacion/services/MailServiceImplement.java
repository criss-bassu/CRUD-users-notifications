package es.uma.informatica.notificacion.services;

import es.uma.informatica.notificacion.modelo.Estado;
import es.uma.informatica.notificacion.modelo.Notificacion;
import es.uma.informatica.notificacion.repositories.NotificacionRepository;
import es.uma.informatica.notificacion.services.excepciones.IntentosAgotadosException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.MailMessage;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;
import jakarta.mail.internet.MimeMessage;

import java.util.Date;
import java.util.List;
import java.util.Properties;

@Service
@RequiredArgsConstructor
public class MailServiceImplement {

    private final JavaMailSender javaMailSender;

    private final NotificacionRepository notificacionRepository;

    @Value("${spring.mail.username}")
    private static String srcMaildir;

    /**
     * Permite el envío de un correo electrónico desde la direción de correo de la aplicación a un destinatario completo.
     * La entrega del mensaje se lleva acabo mediante el protocolo SMTP, donde el cuerpo del mensaje se incluye con
     * el mismo formato con el que se pasa como parámetro.
     *
     * @param dest Destinatario del correo
     * @param asunto Asunto del mensaje
     * @param cuerpo Cuerpo de la notificación (email template)
     */
    public void sendMessage(String dest, String asunto, String cuerpo) {
        // TODO: Tratamiento apriori del envío del correo

        // Envio del correo
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(srcMaildir);
        message.setTo(dest);
        message.setSubject(asunto);
        message.setText(cuerpo);
        javaMailSender.send(message);

        // TODO: Tratamiento aposteriori de la notificación
    }

    public String sendMail(Notificacion x) {

        String response = "Error";

        try {
            if (x.getIntentosRestantes() == 0)
                throw new IntentosAgotadosException("No quedan intentos para la notificación");
            sendMessage(x.getEmailDestino(), x.getAsunto(), x.getCuerpo());
            x.setEstados(Estado.ENVIADO);
            x.setMomentoRealEnvio(new Date());
            response = "Enviado";
        } catch (IntentosAgotadosException e) {
            System.err.println("Fallo al enviar el correo electrónico, intentos agotados");
            x.setEstados(Estado.ERROR);
            x.setMensajeError(e.getMessage());
        } catch (MailException e ) {
            System.err.println("Fallo al enviar el correo electrónico, error en delivery del mail");
            x.setEstados(Estado.ERROR);
            x.setMensajeError(e.getMessage());
            x.setIntentosRestantes(x.getIntentosRestantes() - 1);
        } finally {
            notificacionRepository.save(x);
        }

        return response;
    }
}
