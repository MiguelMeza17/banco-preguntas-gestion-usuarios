package bancopreguntas.view;

import bancopreguntas.controller.AuthController;
import bancopreguntas.controller.UsuarioController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Pantalla de login. Ya recibe las dependencias que necesita (AuthController,
 * UsuarioController) por constructor, pero los botones todavía no llaman
 * nada — es solo la parte visual; falta conectar los manejadores de
 * eventos con AuthController.autenticar(...) y navegar según el resultado.
 */
public class LoginView {

    private final Stage stage;
    private final AuthController authService;
    private final UsuarioController usuarioService;

    public LoginView(Stage stage, AuthController authService, UsuarioController usuarioService) {
        this.stage = stage;
        this.authService = authService;
        this.usuarioService = usuarioService;
    }

    public Scene getScene() {
        Label titulo = new Label("Iniciar sesión");
        titulo.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        TextField campoLogin = new TextField();
        campoLogin.setPromptText("Nombre de usuario");

        PasswordField campoPassword = new PasswordField();
        campoPassword.setPromptText("Contraseña");

        Label labelError = new Label();

        Button botonIngresar = new Button("Ingresar");
        Hyperlink linkRegistro = new Hyperlink("¿No tienes cuenta? Regístrate");

        // TODO: botonIngresar.setOnAction -> llamar authService.autenticar(login, password)
        //       y navegar a DashboardView si es correcto, o mostrar el error en labelError.

        // TODO: linkRegistro.setOnAction -> navegar a RegistroUsuarioView.

        VBox contenedor = new VBox(12, titulo, campoLogin, campoPassword, botonIngresar, labelError, linkRegistro);
        contenedor.setPadding(new Insets(24));
        contenedor.setAlignment(Pos.CENTER_LEFT);

        return new Scene(contenedor, 380, 320);
    }
}
