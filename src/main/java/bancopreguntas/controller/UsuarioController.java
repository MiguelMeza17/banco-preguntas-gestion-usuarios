package bancopreguntas.controller;

import bancopreguntas.model.EstadoUsuario;
import bancopreguntas.model.Rol;
import bancopreguntas.model.Usuario;
import bancopreguntas.conexion.IUsuarioRepository;
import bancopreguntas.model.exception.PasswordInvalidaException;
import bancopreguntas.model.exception.UsuarioYaExisteException;
import bancopreguntas.controller.security.IPasswordEncoder;
import bancopreguntas.controller.validation.PasswordValidator;
import java.util.List;

/**
 * Lógica de negocio de gestión de usuarios. Depende solo de abstracciones
 * (IUsuarioRepository, IPasswordEncoder, PasswordValidator) inyectadas por
 * constructor (DIP).
 */
public class UsuarioController {

    private final IUsuarioRepository repository;
    private final IPasswordEncoder passwordEncoder;
    private final PasswordValidator passwordValidator;

    public UsuarioController(IUsuarioRepository repository, IPasswordEncoder passwordEncoder,
                           PasswordValidator passwordValidator) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.passwordValidator = passwordValidator;
    }

    public Usuario registrarUsuario(String login, String nombreCompleto, Rol rol, String passwordPlano)
            throws PasswordInvalidaException, UsuarioYaExisteException {
        List<String> errores = passwordValidator.obtenerErrores(passwordPlano);
        if (!errores.isEmpty()) {
            throw new PasswordInvalidaException(errores);
        }

        if (repository.existsByLogin(login)) {
            throw new UsuarioYaExisteException(login);
        }

        String passwordHash = passwordEncoder.encode(passwordPlano);
        Usuario usuario = new Usuario(0, login, nombreCompleto, rol, EstadoUsuario.ACTIVO, passwordHash);
        if (!repository.save(usuario)) {
            throw new IllegalStateException("No fue posible guardar el usuario en la base de datos.");
        }
        return usuario;
    }

    public List<Usuario> listarUsuarios() {
        return repository.list();
    }

    public boolean cambiarEstado(String login, EstadoUsuario nuevoEstado) {
        return repository.updateEstado(login, nuevoEstado);
    }
}
