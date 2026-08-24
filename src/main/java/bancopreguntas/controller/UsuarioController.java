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
 * (IUsuarioRepository, IPasswordEncoder) inyectadas por constructor (DIP).
 * La conexión con el repositorio y las demás dependencias ya está armada;
 * falta implementar las reglas de negocio dentro de cada método.
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
        // TODO: 1) validar la password con passwordValidator
        //       2) verificar que el login no exista (repository.existsByLogin)
        //       3) cifrar la password con passwordEncoder
        //       4) construir el Usuario (estado ACTIVO) y guardarlo con repository.save
        return null;
    }

    public List<Usuario> listarUsuarios() {
        return repository.list();
    }

    public boolean cambiarEstado(String login, EstadoUsuario nuevoEstado) {
        return repository.updateEstado(login, nuevoEstado);
    }
}
