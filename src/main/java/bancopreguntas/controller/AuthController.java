package bancopreguntas.controller;

import bancopreguntas.model.EstadoUsuario;
import bancopreguntas.model.Usuario;
import bancopreguntas.conexion.IUsuarioRepository;
import bancopreguntas.model.exception.CredencialesInvalidasException;
import bancopreguntas.controller.security.IPasswordEncoder;

/**
 * Valida credenciales de inicio de sesión. Al igual que
 * {@link UsuarioController}, depende de abstracciones, no de implementaciones
 * concretas (DIP).
 */
public class AuthController {

    private final IUsuarioRepository repository;
    private final IPasswordEncoder passwordEncoder;

    public AuthController(IUsuarioRepository repository, IPasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public Usuario autenticar(String login, String passwordPlano) throws CredencialesInvalidasException {
        Usuario usuario = repository.findByLogin(login);
        if (usuario == null || !passwordEncoder.matches(passwordPlano, usuario.getPasswordHash())) {
            throw new CredencialesInvalidasException("Login o contraseña incorrectos.");
        }
        if (usuario.getEstado() != EstadoUsuario.ACTIVO) {
            throw new CredencialesInvalidasException("Login o contraseña incorrectos.");
        }
        return usuario;
    }
}
