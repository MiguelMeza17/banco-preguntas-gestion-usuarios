package bancopreguntas.view;

import bancopreguntas.model.Usuario;
import bancopreguntas.controller.AuthController;
import bancopreguntas.controller.UsuarioController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Tablero mostrado tras un login exitoso. Ya recibe el Usuario autenticado
 * y las dependencias necesarias; falta usar MenuFactory.obtenerMenu(usuario.getRol())
 * para pintar las opciones según el rol.
 */
public class DashboardView {

    private final Stage stage;
    private final Usuario usuario;
    private final AuthController authService;
    private final UsuarioController usuarioService;

    public DashboardView(Stage stage, Usuario usuario, AuthController authService,
                          UsuarioController usuarioService) {
        this.stage = stage;
        this.usuario = usuario;
        this.authService = authService;
        this.usuarioService = usuarioService;
    }

    public Scene getScene() {
        Label bienvenida = new Label("Bienvenido");

        // TODO: usar MenuFactory.obtenerMenu(usuario.getRol()) para mostrar el
        //       nombre del rol y agregar un botón por cada opción del menú.

        VBox contenedor = new VBox(12, bienvenida);
        contenedor.setPadding(new Insets(24));
        contenedor.setAlignment(Pos.CENTER_LEFT);

        return new Scene(contenedor, 420, 420);
    }
}
