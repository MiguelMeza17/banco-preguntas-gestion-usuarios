package bancopreguntas.controller;

import bancopreguntas.model.Usuario;
import bancopreguntas.conexion.IUsuarioRepository;
import bancopreguntas.model.exception.CredencialesInvalidasException;
import bancopreguntas.controller.security.IPasswordEncoder;

/**
 * Valida credenciales de inicio de sesión. Al igual que
 * {@link UsuarioController}, depende de abstracciones, no de implementaciones
 * concretas (DIP). La conexión ya está armada; falta implementar la
 * verificación dentro de {@code autenticar}.
 */
public class AuthController {

    private final IUsuarioRepository repository;
    private final IPasswordEncoder passwordEncoder;

    public AuthController(IUsuarioRepository repository, IPasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public Usuario autenticar(String login, String passwordPlano) throws CredencialesInvalidasException {
        // TODO: 1) buscar el usuario por login (repository.findByLogin)
        //       2) verificar la password con passwordEncoder.matches
        //       3) verificar que el usuario esté ACTIVO
        //       4) si algo falla, lanzar CredencialesInvalidasException
        return null;
    }
}
