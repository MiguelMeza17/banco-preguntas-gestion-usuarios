package bancopreguntas.view;

import bancopreguntas.model.Rol;
import bancopreguntas.controller.AuthController;
import bancopreguntas.controller.UsuarioController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Formulario de registro de usuarios. Ya recibe AuthController/UsuarioController
 * por constructor; falta conectar el botón "Registrar" con
 * usuarioService.registrarUsuario(...) y mostrar el resultado.
 */
public class RegistroUsuarioView {

    private final Stage stage;
    private final AuthController authService;
    private final UsuarioController usuarioService;

    public RegistroUsuarioView(Stage stage, AuthController authService, UsuarioController usuarioService) {
        this.stage = stage;
        this.authService = authService;
        this.usuarioService = usuarioService;
    }

    public Scene getScene() {
        Label titulo = new Label("Registro de usuario");
        titulo.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        TextField campoLogin = new TextField();
        campoLogin.setPromptText("Nombre de usuario (login)");

        TextField campoNombreCompleto = new TextField();
        campoNombreCompleto.setPromptText("Nombre completo");

        ComboBox<Rol> comboRol = new ComboBox<>();
        comboRol.getItems().addAll(Rol.values());
        comboRol.setPromptText("Rol");

        PasswordField campoPassword = new PasswordField();
        campoPassword.setPromptText("Contraseña");

        Label labelMensaje = new Label();
        labelMensaje.setWrapText(true);

        Button botonRegistrar = new Button("Registrar");
        Hyperlink linkVolver = new Hyperlink("Volver a iniciar sesión");

        // TODO: botonRegistrar.setOnAction -> llamar usuarioService.registrarUsuario(...)
        //       y mostrar éxito o los errores (PasswordInvalidaException / UsuarioYaExisteException) en labelMensaje.

        // TODO: linkVolver.setOnAction -> volver a LoginView.

        VBox contenedor = new VBox(12, titulo, campoLogin, campoNombreCompleto, comboRol,
                campoPassword, botonRegistrar, labelMensaje, linkVolver);
        contenedor.setPadding(new Insets(24));
        contenedor.setAlignment(Pos.CENTER_LEFT);

        return new Scene(contenedor, 400, 420);
    }
}
