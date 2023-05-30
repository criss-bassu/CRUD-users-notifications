package es.uma.informatica.notificacion.repositories;

import es.uma.informatica.notificacion.modelo.Medio;
import es.uma.informatica.notificacion.modelo.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.*;
import java.util.List;

@Repository
public interface NotificacionRepository extends JpaRepository<Notificacion,Long> {

    //public List<Notificacion> findAllByTipoEqualsIgnoreCase(TipoNotificacion tipo);


    // List<Notificacion> findAllByMediosContains(List<Medio> reference);
}
