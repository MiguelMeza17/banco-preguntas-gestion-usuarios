package bancopreguntas.controller.validation.rules;

import bancopreguntas.controller.validation.IPasswordRule;

public class CaracterEspecialRule implements IPasswordRule {

    @Override
    public boolean esValida(String password) {
        if (password == null) {
            return false;
        }
        return password.chars().anyMatch(c -> !Character.isLetterOrDigit(c));
    }

    @Override
    public String getMensajeError() {
        return "La contraseña debe incluir al menos un carácter especial.";
    }
}
