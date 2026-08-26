package bancopreguntas.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import bancopreguntas.conexion.UsuarioRepository;
import bancopreguntas.controller.security.Sha256PasswordEncoder;
import bancopreguntas.controller.validation.PasswordValidator;
import bancopreguntas.model.EstadoUsuario;
import bancopreguntas.model.Rol;
import bancopreguntas.model.Usuario;
import bancopreguntas.model.exception.CredencialesInvalidasException;
import bancopreguntas.model.exception.PasswordInvalidaException;
import bancopreguntas.model.exception.UsuarioYaExisteException;
import io.zonky.test.db.postgres.embedded.EmbeddedPostgres;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Cubre los criterios de aceptación de HU-05 (registro) y HU-06 (login) de
 * {@code docs/EPICAS_HISTORIAS_USUARIO.md}, contra un PostgreSQL real pero
 * desechable (mismo enfoque que {@code UsuarioRepositoryTest}).
 */
class AuthFlowTest {

    private static EmbeddedPostgres postgres;
    private static Connection conn;

    private UsuarioController usuarioController;
    private AuthController authController;

    @BeforeAll
    static void iniciarPostgres() throws Exception {
        postgres = EmbeddedPostgres.start();
        conn = postgres.getPostgresDatabase().getConnection();
    }

    @AfterAll
    static void detenerPostgres() throws Exception {
        conn.close();
        postgres.close();
    }

    @BeforeEach
    void prepararEscenario() throws SQLException {
        UsuarioRepository repository = new UsuarioRepository(conn);
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("TRUNCATE TABLE Usuario");
        }
        Sha256PasswordEncoder passwordEncoder = new Sha256PasswordEncoder();
        PasswordValidator passwordValidator = PasswordValidator.reglasPorDefecto();
        usuarioController = new UsuarioController(repository, passwordEncoder, passwordValidator);
        authController = new AuthController(repository, passwordEncoder);
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

        UsuarioYaExisteException ex = assertThrows(UsuarioYaExisteException.class,
                () -> usuarioController.registrarUsuario("jgarcia", "Otro Nombre", Rol.DOCENTE, "OtraClave1!"));

        assertTrue(ex.getMessage().contains("jgarcia"));
        assertEquals(1, usuarioController.listarUsuarios().size());
    }

    @Test
    void passwordInseguraListaTodasLasReglasIncumplidas() {
        PasswordInvalidaException ex = assertThrows(PasswordInvalidaException.class,
                () -> usuarioController.registrarUsuario("nuevo", "Nombre", Rol.ESTUDIANTE, "abc"));

        // "abc": incumple longitud mínima, dígito, mayúscula y carácter especial (las 4 reglas).
        assertEquals(4, ex.getErrores().size());
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
    void loginConPasswordIncorrectaLanzaCredencialesInvalidas() throws Exception {
        usuarioController.registrarUsuario("jgarcia", "Juan García", Rol.ESTUDIANTE, "Clave123!");

        assertThrows(CredencialesInvalidasException.class,
                () -> authController.autenticar("jgarcia", "ClaveIncorrecta1!"));
    }

    @Test
    void loginConLoginInexistenteLanzaCredencialesInvalidas() {
        assertThrows(CredencialesInvalidasException.class,
                () -> authController.autenticar("no-existe", "Clave123!"));
    }

    @Test
    void loginConLoginInexistenteYLoginConPasswordIncorrectaDanElMismoMensaje() throws Exception {
        usuarioController.registrarUsuario("jgarcia", "Juan García", Rol.ESTUDIANTE, "Clave123!");

        CredencialesInvalidasException porPasswordIncorrecta = assertThrows(CredencialesInvalidasException.class,
                () -> authController.autenticar("jgarcia", "Incorrecta1!"));
        CredencialesInvalidasException porLoginInexistente = assertThrows(CredencialesInvalidasException.class,
                () -> authController.autenticar("no-existe", "Clave123!"));

        assertEquals(porLoginInexistente.getMessage(), porPasswordIncorrecta.getMessage());
    }

    @Test
    void usuarioInactivoNoPuedeIniciarSesion() throws Exception {
        usuarioController.registrarUsuario("jgarcia", "Juan García", Rol.ESTUDIANTE, "Clave123!");
        usuarioController.cambiarEstado("jgarcia", EstadoUsuario.INACTIVO);

        assertThrows(CredencialesInvalidasException.class,
                () -> authController.autenticar("jgarcia", "Clave123!"));
    }

    // ---- Sha256PasswordEncoder ----

    @Test
    void elMismoPasswordProduceHashesDistintosPorLaSalAleatoria() {
        Sha256PasswordEncoder encoder = new Sha256PasswordEncoder();

        String hash1 = encoder.encode("Clave123!");
        String hash2 = encoder.encode("Clave123!");

        assertNotEquals(hash1, hash2);
        assertTrue(encoder.matches("Clave123!", hash1));
        assertTrue(encoder.matches("Clave123!", hash2));
        assertFalse(encoder.matches("Clave123!", "malformado-sin-separador"));
    }
}
