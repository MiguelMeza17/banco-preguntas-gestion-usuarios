package bancopreguntas.controller.validation.rules;

import bancopreguntas.controller.validation.IPasswordRule;

public class DigitoRule implements IPasswordRule {

    @Override
    public boolean esValida(String password) {
        // TODO: validar que contenga al menos un dígito.
        return false;
    }

    @Override
    public String getMensajeError() {
        return "La contraseña debe incluir al menos un dígito.";
    }
}
