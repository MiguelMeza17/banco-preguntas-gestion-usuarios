package bancopreguntas.model;

/**
 * Roles soportados por el sistema de Banco de Preguntas Saber Pro.
 */
public enum Rol {
    ADMINISTRADOR("Administrador"),
    AUTOR_PREGUNTAS("Autor de preguntas"),
    REVISOR("Revisor"),
    DOCENTE("Docente"),
    ESTUDIANTE("Estudiante");

    private final String nombreVisible;

    Rol(String nombreVisible) {
        this.nombreVisible = nombreVisible;
    }

    public String getNombreVisible() {
        return nombreVisible;
    }
}
