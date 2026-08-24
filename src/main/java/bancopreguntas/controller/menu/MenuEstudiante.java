package bancopreguntas.controller.menu;

import java.util.List;

public class MenuEstudiante implements IMenuRol {

    @Override
    public String getNombreRol() {
        return "Estudiante";
    }

    @Override
    public List<String> getOpciones() {
        // TODO: definir las opciones del menú de Estudiante.
        return List.of();
    }
}
