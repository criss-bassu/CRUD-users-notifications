package es.uma.informatica.usuariojpa.services.excepciones;

public class UsuarioNoEncontrado extends RuntimeException{
    public UsuarioNoEncontrado(String s) {
        super(s);
    }

    public UsuarioNoEncontrado(){
        super();
    }
}
