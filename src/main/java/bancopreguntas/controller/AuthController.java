package bancopreguntas.controller;

import bancopreguntas.conexion.UsuarioRepository;
import bancopreguntas.model.EstadoUsuario;
import bancopreguntas.model.Usuario;

/**
 * Valida credenciales de inicio de sesión.
 */
public class AuthController {

    private final UsuarioRepository repository;

    public AuthController(UsuarioRepository repository) {
        this.repository = repository;
    }

    public Usuario autenticar(String login, String passwordPlano) throws Exception {
        Usuario usuario = repository.findByLogin(login);
        if (usuario == null || !PasswordUtil.matches(passwordPlano, usuario.getPasswordHash())) {
            throw new Exception("Login o contraseña incorrectos.");
        }
        if (usuario.getEstado() != EstadoUsuario.ACTIVO) {
            throw new Exception("Login o contraseña incorrectos.");
        }
        return usuario;
    }
}
