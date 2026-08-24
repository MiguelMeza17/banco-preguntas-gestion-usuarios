package bancopreguntas.conexion;

import bancopreguntas.model.EstadoUsuario;
import bancopreguntas.model.Rol;
import bancopreguntas.model.Usuario;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implementación concreta de {@link IUsuarioRepository} sobre PostgreSQL.
 * Podría reemplazarse por otra tecnología sin afectar a quien consume
 * la interfaz (DIP).
 */
public class UsuarioRepository implements IUsuarioRepository {

    private static final String URL_POR_DEFECTO = "jdbc:postgresql://localhost:5432/banco_preguntas";
    private static final String USUARIO_POR_DEFECTO = "postgres";
    private static final String PASSWORD_POR_DEFECTO = "postgres";

    private final Connection conn;

    /** Constructor de producción: se conecta a la instancia local configurada arriba. */
    public UsuarioRepository() {
        this(conectar(URL_POR_DEFECTO, USUARIO_POR_DEFECTO, PASSWORD_POR_DEFECTO));
    }

    /** Constructor para pruebas: reutiliza una conexión ya abierta (p. ej. Postgres embebido). */
    public UsuarioRepository(Connection conn) {
        this.conn = conn;
        initDatabase();
    }

    private static Connection conectar(String url, String usuario, String password) {
        try {
            return DriverManager.getConnection(url, usuario, password);
        } catch (SQLException ex) {
            throw new IllegalStateException("No fue posible conectar a PostgreSQL en " + url
                    + ". Verifica que el servidor esté corriendo y las credenciales en UsuarioRepository.", ex);
        }
    }

    @Override
    public boolean save(Usuario usuario) {
        if (usuario == null || usuario.getLogin() == null || usuario.getLogin().isBlank()) {
            return false;
        }

        String sql = "INSERT INTO Usuario (Login, NombreCompleto, Rol, Estado, PasswordHash) "
                + "VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, usuario.getLogin());
            pstmt.setString(2, usuario.getNombreCompleto());
            pstmt.setString(3, usuario.getRol().name());
            pstmt.setString(4, usuario.getEstado().name());
            pstmt.setString(5, usuario.getPasswordHash());
            pstmt.executeUpdate();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(UsuarioRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @Override
    public Usuario findByLogin(String login) {
        String sql = "SELECT Id, Login, NombreCompleto, Rol, Estado, PasswordHash "
                + "FROM Usuario WHERE Login = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, login);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapUsuario(rs);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(UsuarioRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public boolean existsByLogin(String login) {
        return findByLogin(login) != null;
    }

    @Override
    public boolean updateEstado(String login, EstadoUsuario nuevoEstado) {
        String sql = "UPDATE Usuario SET Estado = ? WHERE Login = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nuevoEstado.name());
            pstmt.setString(2, login);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            Logger.getLogger(UsuarioRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @Override
    public List<Usuario> list() {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT Id, Login, NombreCompleto, Rol, Estado, PasswordHash FROM Usuario";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                usuarios.add(mapUsuario(rs));
            }
        } catch (SQLException ex) {
            Logger.getLogger(UsuarioRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
        return usuarios;
    }

    private Usuario mapUsuario(ResultSet rs) throws SQLException {
        Usuario usuario = new Usuario();
        usuario.setId(rs.getInt("Id"));
        usuario.setLogin(rs.getString("Login"));
        usuario.setNombreCompleto(rs.getString("NombreCompleto"));
        usuario.setRol(Rol.valueOf(rs.getString("Rol")));
        usuario.setEstado(EstadoUsuario.valueOf(rs.getString("Estado")));
        usuario.setPasswordHash(rs.getString("PasswordHash"));
        return usuario;
    }

    private void initDatabase() {
        String sql = "CREATE TABLE IF NOT EXISTS Usuario (\n"
                + "    Id SERIAL PRIMARY KEY,\n"
                + "    Login TEXT NOT NULL UNIQUE,\n"
                + "    NombreCompleto TEXT NOT NULL,\n"
                + "    Rol TEXT NOT NULL,\n"
                + "    Estado TEXT NOT NULL,\n"
                + "    PasswordHash TEXT NOT NULL\n"
                + ");";

        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException ex) {
            Logger.getLogger(UsuarioRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void disconnect() {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(UsuarioRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
