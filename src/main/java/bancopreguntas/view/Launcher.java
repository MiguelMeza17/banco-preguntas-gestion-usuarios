package bancopreguntas.view;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.swing.JOptionPane;

/**
 * Punto de entrada real del .jar / de VS Code. No extiende
 * {@code javafx.application.Application}: eso evita el error
 * "JavaFX runtime components are missing" que Java lanza cuando la
 * clase con el método main() ejecutada directamente sí extiende
 * Application y JavaFX no está en el module-path.
 */
public class Launcher {

    private static final String WSL_DISTRO = "Ubuntu";

    public static void main(String[] args) {
        asegurarBaseDeDatos();
        MainApp.main(args);
    }

    /**
     * Levanta (o confirma que ya está sana) la base de datos vía
     * "docker compose up -d --wait" en WSL antes de abrir la UI. Sin
     * Docker Desktop, WSL2 apaga la distro Ubuntu (y con ella el
     * contenedor) cuando queda inactiva o cuando la PC duerme/reinicia,
     * así que sin este paso la app se revienta con un stack trace crudo
     * de "Connection refused" en el primer intento de conexión.
     */
    private static void asegurarBaseDeDatos() {
        String proyecto = System.getProperty("user.dir");
        String composeFile = aRutaWsl(proyecto) + "/docker-compose.yml";

        List<String> comando = List.of(
                "wsl", "-d", WSL_DISTRO, "--",
                "docker", "compose",
                "-f", composeFile,
                "--project-directory", aRutaWsl(proyecto),
                "up", "-d", "--wait"
        );

        try {
            Process proceso = new ProcessBuilder(comando)
                    .redirectOutput(ProcessBuilder.Redirect.INHERIT)
                    .redirectError(ProcessBuilder.Redirect.INHERIT)
                    .start();
            boolean termino = proceso.waitFor(90, TimeUnit.SECONDS);
            if (!termino || proceso.exitValue() != 0) {
                JOptionPane.showMessageDialog(null,
                        "No se pudo levantar la base de datos (docker compose up -d --wait).\n"
                        + "Revisa que WSL/Docker estén instalados y funcionando (ver SETUP.md) e inténtalo de nuevo.",
                        "Base de datos no disponible", JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
        } catch (IOException ex) {
            // wsl.exe no se encontró en esta máquina: seguimos igual, y si
            // de verdad hace falta la base de datos, MainApp mostrará su
            // propio mensaje al intentar conectar.
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

    /** Convierte una ruta de Windows (p. ej. "D:\\foo\\bar") a su ruta montada en WSL2 ("/mnt/d/foo/bar"). */
    private static String aRutaWsl(String rutaWindows) {
        String ruta = rutaWindows.replace('\\', '/');
        char unidad = Character.toLowerCase(ruta.charAt(0));
        return "/mnt/" + unidad + ruta.substring(2);
    }
}
