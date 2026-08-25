package bancopreguntas.view;

import bancopreguntas.conexion.Factory;
import bancopreguntas.conexion.IUsuarioRepository;
import bancopreguntas.controller.security.IPasswordEncoder;
import bancopreguntas.controller.security.Sha256PasswordEncoder;
import bancopreguntas.controller.AuthController;
import bancopreguntas.controller.UsuarioController;
import bancopreguntas.controller.validation.PasswordValidator;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

/**
 * Punto de entrada de la aplicación. Arma (composition root) las
 * dependencias concretas y las inyecta en los servicios; el resto de la
 * app solo conoce abstracciones. La conexión entre todas las capas ya
 * está armada — lo que falta es implementar la lógica dentro de cada
 * clase marcada con TODO.
 */
public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        IUsuarioRepository repository;
        try {
            repository = Factory.getInstance().getUsuarioRepository("default");
        } catch (IllegalStateException ex) {
            mostrarErrorConexion();
            return;
        }
        IPasswordEncoder passwordEncoder = new Sha256PasswordEncoder();
        PasswordValidator passwordValidator = PasswordValidator.reglasPorDefecto();

        AuthController authService = new AuthController(repository, passwordEncoder);
        UsuarioController usuarioService = new UsuarioController(repository, passwordEncoder, passwordValidator);

        primaryStage.setTitle("Banco de Preguntas Saber Pro - Gestión de Usuarios");

        LoginView loginView = new LoginView(primaryStage, authService, usuarioService);
        primaryStage.setScene(loginView.getScene());
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void mostrarErrorConexion() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("No se pudo conectar a la base de datos");
        alert.setHeaderText("PostgreSQL no está disponible en localhost:5432");
        alert.setContentText("Verifica que Docker/WSL estén corriendo (ver SETUP.md) y vuelve a intentar.");
        alert.showAndWait();
        Platform.exit();
    }
}
