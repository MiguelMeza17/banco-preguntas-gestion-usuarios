package bancopreguntas.conexion;

import bancopreguntas.model.EstadoUsuario;
import bancopreguntas.model.Usuario;
import java.util.List;

/**
 * Contrato de persistencia de usuarios. El resto del sistema depende de esta
 * abstracción y no de una implementación concreta (DIP).
 */
public interface IUsuarioRepository {

    boolean save(Usuario usuario);

    Usuario findByLogin(String login);

    boolean existsByLogin(String login);

    boolean updateEstado(String login, EstadoUsuario nuevoEstado);

    List<Usuario> list();
}
