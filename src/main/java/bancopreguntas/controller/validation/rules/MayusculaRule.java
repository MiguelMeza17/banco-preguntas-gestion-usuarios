package bancopreguntas.controller.validation.rules;

import bancopreguntas.controller.validation.IPasswordRule;

public class MayusculaRule implements IPasswordRule {

    @Override
    public boolean esValida(String password) {
        // TODO: validar que contenga al menos una letra mayúscula.
        return false;
    }

    @Override
    public String getMensajeError() {
        return "La contraseña debe incluir al menos una letra mayúscula.";
    }
}
