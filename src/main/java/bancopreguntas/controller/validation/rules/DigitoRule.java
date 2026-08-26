package bancopreguntas.controller.validation.rules;

import bancopreguntas.controller.validation.IPasswordRule;

public class DigitoRule implements IPasswordRule {

    @Override
    public boolean esValida(String password) {
        if (password == null) {
            return false;
        }
        return password.chars().anyMatch(Character::isDigit);
    }

    @Override
    public String getMensajeError() {
        return "La contraseña debe incluir al menos un dígito.";
    }
}
