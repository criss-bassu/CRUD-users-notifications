package es.uma.informatica.usuariojpa.services.excepciones;

import java.util.function.Supplier;

public class EmailNotFoundException extends RuntimeException {
    public EmailNotFoundException(String s) {
        super(s);
    }
}
