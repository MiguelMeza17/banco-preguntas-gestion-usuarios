package bancopreguntas.controller;

import bancopreguntas.conexion.UsuarioRepository;
import bancopreguntas.model.EstadoUsuario;
import bancopreguntas.model.Rol;
import bancopreguntas.model.Usuario;
import java.util.List;

/**
 * Lógica de negocio de gestión de usuarios.
 */
public class UsuarioController {

    private static final int LONGITUD_MINIMA_PASSWORD = 6;

    private final UsuarioRepository repository;

    public UsuarioController(UsuarioRepository repository) {
        this.repository = repository;
    }

    public Usuario registrarUsuario(String login, String nombreCompleto, Rol rol, String passwordPlano)
            throws Exception {
        String errorPassword = validarPassword(passwordPlano);
        if (errorPassword != null) {
            throw new Exception(errorPassword);
        }

        if (repository.existsByLogin(login)) {
            throw new Exception("Ya existe un usuario registrado con el login: " + login);
        }

        String passwordHash = PasswordUtil.encode(passwordPlano);
        Usuario usuario = new Usuario(0, login, nombreCompleto, rol, EstadoUsuario.ACTIVO, passwordHash);
        if (!repository.save(usuario)) {
            throw new Exception("No fue posible guardar el usuario en la base de datos.");
        }
        return usuario;
    }

    public List<Usuario> listarUsuarios() {
        return repository.list();
    }

    public boolean cambiarEstado(String login, EstadoUsuario nuevoEstado) {
        return repository.updateEstado(login, nuevoEstado);
    }

    private String validarPassword(String password) {
        if (password == null || password.length() < LONGITUD_MINIMA_PASSWORD) {
            return "La contraseña debe tener al menos " + LONGITUD_MINIMA_PASSWORD + " caracteres.";
        }
        if (password.chars().noneMatch(Character::isDigit)) {
            return "La contraseña debe incluir al menos un dígito.";
        }
        if (password.chars().noneMatch(Character::isUpperCase)) {
            return "La contraseña debe incluir al menos una letra mayúscula.";
        }
        if (password.chars().allMatch(Character::isLetterOrDigit)) {
            return "La contraseña debe incluir al menos un carácter especial.";
        }
        return null;
    }
}
