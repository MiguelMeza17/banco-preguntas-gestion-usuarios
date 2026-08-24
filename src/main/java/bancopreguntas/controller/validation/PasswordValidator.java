package bancopreguntas.controller.validation;

import bancopreguntas.controller.validation.rules.CaracterEspecialRule;
import bancopreguntas.controller.validation.rules.DigitoRule;
import bancopreguntas.controller.validation.rules.LongitudMinimaRule;
import bancopreguntas.controller.validation.rules.MayusculaRule;
import java.util.ArrayList;
import java.util.List;

/**
 * Aplica un conjunto de {@link IPasswordRule} sobre una contraseña.
 * No conoce las reglas concretas: recibe la lista por constructor
 * (inyección de dependencias).
 */
public class PasswordValidator {

    private final List<IPasswordRule> reglas;

    public PasswordValidator(List<IPasswordRule> reglas) {
        this.reglas = reglas;
    }

    /**
     * Conjunto de reglas por defecto exigido por la guía del taller
     * (mínimo 6 caracteres, un dígito, una mayúscula, un carácter especial).
     */
    public static PasswordValidator reglasPorDefecto() {
        return new PasswordValidator(List.of(
                new LongitudMinimaRule(),
                new DigitoRule(),
                new MayusculaRule(),
                new CaracterEspecialRule()
        ));
    }

    public boolean esValida(String password) {
        // TODO: implementar (típicamente: obtenerErrores(password).isEmpty()).
        return false;
    }

    public List<String> obtenerErrores(String password) {
        // TODO: recorrer "reglas" y acumular los mensajes de error de las que fallen.
        return new ArrayList<>();
    }
}
