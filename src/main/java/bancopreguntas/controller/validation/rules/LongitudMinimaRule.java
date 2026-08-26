package bancopreguntas.controller.validation.rules;

import bancopreguntas.controller.validation.IPasswordRule;

public class LongitudMinimaRule implements IPasswordRule {

    private static final int LONGITUD_MINIMA = 6;

    @Override
    public boolean esValida(String password) {
        return password != null && password.length() >= LONGITUD_MINIMA;
    }

    @Override
    public String getMensajeError() {
        return "La contraseña debe tener al menos " + LONGITUD_MINIMA + " caracteres.";
    }
}
