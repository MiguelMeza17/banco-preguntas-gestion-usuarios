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
 * Tablero mostrado tras un login exitoso.
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
        Label bienvenida = new Label("Bienvenido, " + usuario.getNombreCompleto());
        Label rol = new Label("Rol: " + usuario.getRol().getNombreVisible());

        VBox contenedor = new VBox(12, bienvenida, rol);
        contenedor.setPadding(new Insets(24));
        contenedor.setAlignment(Pos.CENTER_LEFT);

        return new Scene(contenedor, 420, 420);
    }
}
