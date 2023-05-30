package es.uma.informatica.notificacion.services.excepciones;

public class IntentosAgotadosException extends RuntimeException {
    public IntentosAgotadosException(String s) {
        super(s);
    }
}
