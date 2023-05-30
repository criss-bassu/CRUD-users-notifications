package es.uma.informatica.notificacion.config;

import es.uma.informatica.notificacion.modelo.Medio;
import es.uma.informatica.notificacion.modelo.Notificacion;
import es.uma.informatica.notificacion.services.MailServiceImplement;
import es.uma.informatica.notificacion.services.NotificacionDBservice;
import es.uma.informatica.notificacion.services.SMSService;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.FixedDelayTask;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Planificacador customizable para el envío eficiente de notificaciones dentro de la aplicación
 */
@Configuration
@EnableScheduling // Habilita la funcionalidad de programación de tareas
@RequiredArgsConstructor
public class PlanificadorNotificaciones {

    private final SMSService smsService;
    private final MailServiceImplement mailService;
    private final NotificacionDBservice notificacionDBservice;

    /**
     * Define un bean para un Executor que se utilizará para programar tareas. En este caso, un único hilo programado
     */
    @Bean
    public Executor taskExecutor() {
        return Executors.newSingleThreadScheduledExecutor();
    }

    @Scheduled(fixedDelay = 60 * 60 * 1000) // Cada hora se recolectan y envían notificaciones
    public void tramitarNoticiones() {
        List<Notificacion> notificaciones = notificacionDBservice.getNotificacionesPendientes();


        for (Notificacion x :
                notificaciones) {
            if (x.getMedios().contains(Medio.EMAIL)) {
                // Enviar email
                mailService.sendMail(x);
            }
            if (x.getMedios().contains(Medio.SMS)) {
                // Enviar sms
                smsService.sendSMS(x);
            }
        }
    }

    /**
     * Implementa el método configureTasks de SchedulingConfigurer
     * Este método configura las tareas programadas, proramando el temporizador (trigger) y asignano el manejador (Runnable)
     *
     * @param taskRegistrar the registrar to be configured.
     */
    /*@Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        // Establece el Executor definido anteriormente como el programador de tareas para esta tarea
        taskRegistrar.setScheduler(taskExecutor());

        // Agrega una tarea programada que ejecutará el método tick() en la instancia de TickService
        // cada vez que se desencadene
        taskRegistrar.addTriggerTask(
                () -> {
                    // Definir la lógica del manejador (envío de notificaciones sms o email,  o ambos)


                    // Puede ser útil actualizar valores de la clase para simplificar el código del trigger
                },


                 // Configura un Trigger que se utilizará para calcular cuándo se ejecutará la tarea

                 // TODO: el trigger se ha de programar con la diferencia entre la hora actual y la siguiente notificación en la lista

                triggerContext -> {
                    // Obtiene el tiempo de finalización de la tarea anterior si está disponible


                    // Calcula la próxima fecha y hora de ejecución de la tarea sumando el retraso
                    // devuelto por el método getDelay() de TickService al tiempo de finalización
                    // de la tarea anterior (o la hora actual si no se ha ejecutado antes)

                    // Devuelve la próxima fecha y hora de ejecución como un objeto Date
                    return null;
                }


        );
    }*/
}
