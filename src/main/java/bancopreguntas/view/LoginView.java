package bancopreguntas.view;

import bancopreguntas.controller.AuthController;
import bancopreguntas.controller.UsuarioController;
import bancopreguntas.model.Usuario;
import bancopreguntas.model.exception.CredencialesInvalidasException;
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
 * Pantalla de login. Delega la autenticación en {@link AuthController} y
 * navega a {@link DashboardView} si las credenciales son válidas, o a
 * {@link RegistroUsuarioView} desde el enlace de registro.
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
        labelError.setStyle("-fx-text-fill: red;");
        labelError.setWrapText(true);

        Button botonIngresar = new Button("Ingresar");
        Hyperlink linkRegistro = new Hyperlink("¿No tienes cuenta? Regístrate");

        botonIngresar.setOnAction(evento -> {
            try {
                Usuario usuario = authService.autenticar(campoLogin.getText(), campoPassword.getText());
                labelError.setText("");
                DashboardView dashboardView = new DashboardView(stage, usuario, authService, usuarioService);
                stage.setScene(dashboardView.getScene());
            } catch (CredencialesInvalidasException ex) {
                labelError.setText(ex.getMessage());
            }
        });

        linkRegistro.setOnAction(evento -> {
            RegistroUsuarioView registroView = new RegistroUsuarioView(stage, authService, usuarioService);
            stage.setScene(registroView.getScene());
        });

        VBox contenedor = new VBox(12, titulo, campoLogin, campoPassword, botonIngresar, labelError, linkRegistro);
        contenedor.setPadding(new Insets(24));
        contenedor.setAlignment(Pos.CENTER_LEFT);

        return new Scene(contenedor, 380, 320);
    }
}
