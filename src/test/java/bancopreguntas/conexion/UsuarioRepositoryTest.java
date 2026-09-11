package bancopreguntas.conexion;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.junit.jupiter.api.Test;

/**
 * Prueba de la capa de conexión: usa SQLite en memoria para no tocar
 * el archivo banco_preguntas.db real.
 */
class UsuarioRepositoryTest {

    @Test
    void seConectaYCreaLaTablaUsuario() throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:sqlite::memory:");
        UsuarioRepository repository = new UsuarioRepository(conn);

        assertNotNull(repository);
        assertTrue(repository.list().isEmpty());
    }
}
