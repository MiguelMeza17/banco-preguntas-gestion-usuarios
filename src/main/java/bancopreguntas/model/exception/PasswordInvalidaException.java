package bancopreguntas.model.exception;

import java.util.List;

public class PasswordInvalidaException extends Exception {

    private final List<String> errores;

    public PasswordInvalidaException(List<String> errores) {
        super("La contraseña no cumple los requisitos de seguridad.");
        this.errores = errores;
    }

    public List<String> getErrores() {
        return errores;
    }
}
