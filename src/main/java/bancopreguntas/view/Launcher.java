package bancopreguntas.view;

/**
 * Punto de entrada real del .jar / de VS Code. No extiende
 * {@code javafx.application.Application}: eso evita el error
 * "JavaFX runtime components are missing" que Java lanza cuando la
 * clase con el método main() ejecutada directamente sí extiende
 * Application y JavaFX no está en el module-path.
 */
public class Launcher {

    public static void main(String[] args) {
        MainApp.main(args);
    }
}
