package bancopreguntas.controller.security;

/**
 * Contrato para cifrar y verificar contraseñas. El servicio depende de
 * esta abstracción, no de un algoritmo concreto (DIP) — permite cambiar
 * el algoritmo de cifrado sin tocar la lógica de negocio.
 */
public interface IPasswordEncoder {

    String encode(String passwordPlano);

    boolean matches(String passwordPlano, String passwordHash);
}
