package bancopreguntas.view;

import bancopreguntas.conexion.UsuarioRepository;
import bancopreguntas.controller.AuthController;
import bancopreguntas.controller.UsuarioController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

/**
 * Punto de entrada de la aplicación. Arma las dependencias concretas y las
 * inyecta en los controladores.
 */
public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        UsuarioRepository repository;
        try {
            repository = new UsuarioRepository();
        } catch (IllegalStateException ex) {
            mostrarErrorConexion();
            return;
        }

        AuthController authService = new AuthController(repository);
        UsuarioController usuarioService = new UsuarioController(repository);

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
        alert.setHeaderText("No se pudo abrir la base de datos SQLite");
        alert.setContentText("Verifica los permisos de escritura en el directorio del proyecto y vuelve a intentar.");
        alert.showAndWait();
        Platform.exit();
    }
}
