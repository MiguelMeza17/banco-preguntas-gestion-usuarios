package bancopreguntas.model.exception;

public class UsuarioYaExisteException extends Exception {

    public UsuarioYaExisteException(String login) {
        super("Ya existe un usuario registrado con el login: " + login);
    }
}
