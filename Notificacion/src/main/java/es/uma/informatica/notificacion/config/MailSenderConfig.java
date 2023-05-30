package es.uma.informatica.notificacion.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailSenderConfig {

    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);

        mailSender.setUsername("xxxxx@gmail.com");
        mailSender.setPassword("xxxxx");

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");


        return mailSender;
    }

    @Bean
    public SimpleMailMessage getSMTPTemplate() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setText(
                "Aquí se define el cuerpo de la plantilla para cada mensaje\n" +
                "Usar String format para introducir argumentos o varialbes:\n%s\n");
        return message;
    }
}
