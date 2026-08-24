package bancopreguntas.controller.validation;

/**
 * Una regla individual de validación de contraseña. Agregar una nueva
 * regla no requiere modificar {@link PasswordValidator} (OCP): solo se
 * crea una nueva clase que implemente esta interfaz.
 */
public interface IPasswordRule {

    boolean esValida(String password);

    String getMensajeError();
}
