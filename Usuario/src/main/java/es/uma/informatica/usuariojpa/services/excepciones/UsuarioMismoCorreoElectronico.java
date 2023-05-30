package es.uma.informatica.usuariojpa.services.excepciones;

public class UsuarioMismoCorreoElectronico extends RuntimeException{
    //public UsuarioMismoCorreoElectronico() {
    //}

    public UsuarioMismoCorreoElectronico(String message) {
        super("Hay un usuario con el mismo correo electrónico");
    }
}
