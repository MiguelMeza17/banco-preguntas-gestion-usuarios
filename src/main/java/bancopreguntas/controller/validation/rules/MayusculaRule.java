package bancopreguntas.controller.validation.rules;

import bancopreguntas.controller.validation.IPasswordRule;

public class MayusculaRule implements IPasswordRule {

    @Override
    public boolean esValida(String password) {
        if (password == null) {
            return false;
        }
        return password.chars().anyMatch(Character::isUpperCase);
    }

    @Override
    public String getMensajeError() {
        return "La contraseña debe incluir al menos una letra mayúscula.";
    }
}
