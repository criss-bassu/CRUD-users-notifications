package es.uma.informatica.notificacion.services;

import es.uma.informatica.notificacion.modelo.Estado;
import es.uma.informatica.notificacion.modelo.Medio;
import es.uma.informatica.notificacion.modelo.Notificacion;
import es.uma.informatica.notificacion.modelo.TipoNotificacion;
import es.uma.informatica.notificacion.repositories.NotificacionRepository;
import es.uma.informatica.notificacion.schemas.NotificacionDTO;
import es.uma.informatica.notificacion.services.excepciones.NotificacionNoEncontrada;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Stream;

@Service
@Transactional
@RequiredArgsConstructor
public class NotificacionDBservice {
    private final NotificacionRepository notificacionRepository;

    // TODO: Operaciones de salvado/registro de notifiaciones, para la auditaoría de mensajes enviados y no enviados
    /*


    @Autowired
    public NotificacionDBservice(NotificacionRepository repo) {
        this.notificacionRepository = repo;
    }
     */

    public Optional<Notificacion> getNotificacionByID(Long id) {
        return notificacionRepository.findById(id);
    }

    public Long crearNotificacion(Notificacion noti) {
        noti.setId(null);
        notificacionRepository.save(noti);
        return noti.getId();
    }

    public void actualizarLista(Notificacion noti){
        if(notificacionRepository.existsById(noti.getId())){
            /*Optional<Notificacion> n = notificacionRepository.findById(noti.getId());
            n.ifPresent(x -> {
                x.setAsunto(noti.getAsunto());
                x.setCuerpo(noti.getCuerpo());
                x.setEmailDestino(noti.getEmailDestino());
                x.setTelefonoDestino(noti.getTelefonoDestino());
                x.setProgramacionEnvio(noti.getProgramacionEnvio());
                x.setMedios(noti.getMedios());
                x.setTipoNotificacion(noti.getTipoNotificacion());
                this.notificacionRepository.save(x);
            });*/


            this.notificacionRepository.save(noti);
        }else {
            throw new NotificacionNoEncontrada();
        }
    }

    /**
     * Aborta todas las notificaciones que coincidan con el tipo indicado
     * Presupone que tipo toma un valor válido para el API
     *
     * @param tipo Notificaciones a abortar
     */
    public void abortaNotificacion(TipoNotificacion tipo) {
        if (tipo == null || tipo.equals(""))
            abortaNotificaciones();

        for (Notificacion x : filtraTipo(tipo, notificacionRepository)) {
            aborta(x, notificacionRepository);
        }

    }

    public static Iterable<? extends Notificacion> filtraTipo(TipoNotificacion tipo, NotificacionRepository repository) {
        /*return repository
                .findAll()
                .stream()
                .filter(x -> !x.getTipoNotificacion().contains(tipo))
                .toList();*/
        // tipo.equalsIgnoreCase(x.getTipoNotificacion().name())
        List<Notificacion> res = new ArrayList<>();

        for (Notificacion x:
             repository.findAll()) {
            if (x.getTipoNotificacion().equals(tipo))
                res.add(x);
        }

        return res;
    }

    /*public static void main(String[] args) {
        List<Notificacion> contenidoDB =  List.of(
            new Notificacion(0L,"Notificación 1","asdf","asdf@asdf.com","PENDIENTE", null, null,"SMS", null, null, null, "+12341234","ANUNCIO_NOTAS"),
            new Notificacion(1L,"Notificación 1","asdf","asdf@asdf.com","PENDIENTE", null, null,"SMS", null, null, null, "+12341234","ANUNCIO_NOTAS"),
            new Notificacion(2L,"Notificación 1","asdf","asdf@asdf.com","PENDIENTE", null, null,"SMS", null, null, null,"+12341234","ANUNCIO_NOTAS"),
            new Notificacion(3L,"Notificación 1","asdf","asdf@asdf.com","PENDIENTE", null, null,"SMS", null, null, null,"+12341234","ANUNCIO_NOTAS")
        );


    }*/

    public void abortaNotificaciones() {
        for (Notificacion x : notificacionRepository.findAll()) {
            aborta(x, notificacionRepository);
        }
    }

    private static void aborta(Notificacion x, NotificacionRepository notificacionRepository) {
        // TODO: ¿Qué se considera abortar una "Notificación"? -> actualizar el atributo estado. Comprobar que save sobreescribe la entrada de la BD
        x.setEstados(Estado.ABORTADA);
        notificacionRepository.save(x);
    }

    //Elimina "Notificación"
    public void eliminar(Long id){
        if(notificacionRepository.existsById(id)){
            notificacionRepository.deleteById(id);
        }else{
            throw new NotificacionNoEncontrada();
        }
    }


    public List<Notificacion> getNotificacionesPendientes() {
        List<Notificacion> result = notificacionRepository.findAll();

        return result.stream().filter( x -> x.getEstados() != null && x.getEstados().equals(Estado.PENDIENTE)).toList();
    }

    public List<Notificacion> obtenerTodasNotificaciones() {
        return notificacionRepository.findAll().stream().toList();
    }

    public Stream<Notificacion> getNotificacionesByTipo(String tipo) {
        return this.notificacionRepository.findAll().stream().filter(x -> x.getTipoNotificacion().equals(TipoNotificacion.valueOf(tipo)));
    }
}
