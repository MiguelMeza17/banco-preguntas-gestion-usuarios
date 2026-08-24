package bancopreguntas.controller.validation.rules;

import bancopreguntas.controller.validation.IPasswordRule;

public class CaracterEspecialRule implements IPasswordRule {

    @Override
    public boolean esValida(String password) {
        // TODO: validar que contenga al menos un carácter especial.
        return false;
    }

    @Override
    public String getMensajeError() {
        return "La contraseña debe incluir al menos un carácter especial.";
    }
}
