package bancopreguntas.controller.menu;

import bancopreguntas.model.Rol;

/**
 * Crea el menú correspondiente al rol de un usuario autenticado.
 */
public class MenuFactory {

    private MenuFactory() {
    }

    public static IMenuRol obtenerMenu(Rol rol) {
        switch (rol) {
            case ADMINISTRADOR:
                return new MenuAdministrador();
            case AUTOR_PREGUNTAS:
                return new MenuAutorPreguntas();
            case REVISOR:
                return new MenuRevisor();
            case DOCENTE:
                return new MenuDocente();
            case ESTUDIANTE:
                return new MenuEstudiante();
            default:
                throw new IllegalArgumentException("Rol no soportado: " + rol);
        }
    }
}
