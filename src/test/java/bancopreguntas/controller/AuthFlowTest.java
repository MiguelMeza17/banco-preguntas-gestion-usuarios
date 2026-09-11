package bancopreguntas.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import bancopreguntas.conexion.UsuarioRepository;
import bancopreguntas.model.EstadoUsuario;
import bancopreguntas.model.Rol;
import bancopreguntas.model.Usuario;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Cubre los criterios de aceptación de HU-05 (registro) y HU-06 (login) de
 * {@code docs/EPICAS_HISTORIAS_USUARIO.md}, contra SQLite en memoria (se
 * crea y se descarta en cada prueba, no toca banco_preguntas.db).
 */
class AuthFlowTest {

    private UsuarioController usuarioController;
    private AuthController authController;

    @BeforeEach
    void prepararEscenario() throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:sqlite::memory:");
        UsuarioRepository repository = new UsuarioRepository(conn);
        usuarioController = new UsuarioController(repository);
        authController = new AuthController(repository);
    }

    // ---- HU-05 · Registro ----

    @Test
    void registroExitosoCreaUsuarioActivoConPasswordCifrada() throws Exception {
        Usuario creado = usuarioController.registrarUsuario(
                "jgarcia", "Juan García", Rol.ESTUDIANTE, "Clave123!");

        assertEquals(EstadoUsuario.ACTIVO, creado.getEstado());
        assertNotEquals("Clave123!", creado.getPasswordHash());
    }

    @Test
    void loginDuplicadoNoCreaSegundoUsuario() throws Exception {
        usuarioController.registrarUsuario("jgarcia", "Juan García", Rol.ESTUDIANTE, "Clave123!");

        Exception ex = assertThrows(Exception.class,
                () -> usuarioController.registrarUsuario("jgarcia", "Otro Nombre", Rol.DOCENTE, "OtraClave1!"));

        assertTrue(ex.getMessage().contains("jgarcia"));
        assertEquals(1, usuarioController.listarUsuarios().size());
    }

    @Test
    void passwordInseguraNoCreaElUsuario() {
        assertThrows(Exception.class,
                () -> usuarioController.registrarUsuario("nuevo", "Nombre", Rol.ESTUDIANTE, "abc"));

        assertTrue(usuarioController.listarUsuarios().isEmpty());
    }

    // ---- HU-06 · Login ----

    @Test
    void loginExitosoDevuelveElUsuarioAutenticado() throws Exception {
        usuarioController.registrarUsuario("jgarcia", "Juan García", Rol.ESTUDIANTE, "Clave123!");

        Usuario autenticado = authController.autenticar("jgarcia", "Clave123!");

        assertEquals("jgarcia", autenticado.getLogin());
    }

    @Test
    void loginConPasswordIncorrectaLanzaError() throws Exception {
        usuarioController.registrarUsuario("jgarcia", "Juan García", Rol.ESTUDIANTE, "Clave123!");

        assertThrows(Exception.class,
                () -> authController.autenticar("jgarcia", "ClaveIncorrecta1!"));
    }

    @Test
    void loginConLoginInexistenteLanzaError() {
        assertThrows(Exception.class,
                () -> authController.autenticar("no-existe", "Clave123!"));
    }

    @Test
    void loginConLoginInexistenteYLoginConPasswordIncorrectaDanElMismoMensaje() throws Exception {
        usuarioController.registrarUsuario("jgarcia", "Juan García", Rol.ESTUDIANTE, "Clave123!");

        Exception porPasswordIncorrecta = assertThrows(Exception.class,
                () -> authController.autenticar("jgarcia", "Incorrecta1!"));
        Exception porLoginInexistente = assertThrows(Exception.class,
                () -> authController.autenticar("no-existe", "Clave123!"));

        assertEquals(porLoginInexistente.getMessage(), porPasswordIncorrecta.getMessage());
    }

    @Test
    void usuarioInactivoNoPuedeIniciarSesion() throws Exception {
        usuarioController.registrarUsuario("jgarcia", "Juan García", Rol.ESTUDIANTE, "Clave123!");
        usuarioController.cambiarEstado("jgarcia", EstadoUsuario.INACTIVO);

        assertThrows(Exception.class,
                () -> authController.autenticar("jgarcia", "Clave123!"));
    }

    // ---- PasswordUtil ----

    @Test
    void elMismoPasswordProduceHashesDistintosPorLaSalAleatoria() {
        String hash1 = PasswordUtil.encode("Clave123!");
        String hash2 = PasswordUtil.encode("Clave123!");

        assertNotEquals(hash1, hash2);
        assertTrue(PasswordUtil.matches("Clave123!", hash1));
        assertTrue(PasswordUtil.matches("Clave123!", hash2));
        assertFalse(PasswordUtil.matches("Clave123!", "malformado-sin-separador"));
    }
}
