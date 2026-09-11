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
 * Acceso a datos de Usuario sobre SQLite.
 */
public class UsuarioRepository {

    private static final String URL_POR_DEFECTO = "jdbc:sqlite:banco_preguntas.db";

    private final Connection conn;

    /** Abre (o crea) el archivo banco_preguntas.db en el directorio de trabajo. */
    public UsuarioRepository() {
        this(conectar(URL_POR_DEFECTO));
    }

    /** Para pruebas: reutiliza una conexión ya abierta (p. ej. SQLite en memoria). */
    public UsuarioRepository(Connection conn) {
        this.conn = conn;
        initDatabase();
    }

    private static Connection conectar(String url) {
        try {
            return DriverManager.getConnection(url);
        } catch (SQLException ex) {
            throw new IllegalStateException("No fue posible abrir la base de datos SQLite en " + url + ".", ex);
        }
    }

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

    public boolean existsByLogin(String login) {
        return findByLogin(login) != null;
    }

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
                + "    Id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
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
