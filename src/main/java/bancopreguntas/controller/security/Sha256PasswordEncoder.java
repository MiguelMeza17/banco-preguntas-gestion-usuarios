package bancopreguntas.controller.security;

/**
 * Implementación de {@link IPasswordEncoder}. Estructura lista
 * (implementa el contrato); falta implementar el algoritmo de cifrado.
 */
public class Sha256PasswordEncoder implements IPasswordEncoder {

    @Override
    public String encode(String passwordPlano) {
        // TODO: implementar cifrado (p. ej. SHA-256 con sal aleatoria).
        return null;
    }

    @Override
    public boolean matches(String passwordPlano, String passwordHash) {
        // TODO: implementar verificación del hash.
        return false;
    }
}
