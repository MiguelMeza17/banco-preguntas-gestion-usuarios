package bancopreguntas.conexion;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.zonky.test.db.postgres.embedded.EmbeddedPostgres;
import java.sql.Connection;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Prueba de la capa de conexión: usa un PostgreSQL real pero desechable
 * (embedded-postgres) para no depender de tener un servidor instalado
 * solo para correr las pruebas.
 */
class UsuarioRepositoryTest {

    private static EmbeddedPostgres postgres;
    private static Connection conn;

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

    @Test
    void seConectaYCreaLaTablaUsuario() throws Exception {
        UsuarioRepository repository = new UsuarioRepository(conn);

        assertNotNull(repository);
        assertTrue(repository.list().isEmpty());
    }
}
