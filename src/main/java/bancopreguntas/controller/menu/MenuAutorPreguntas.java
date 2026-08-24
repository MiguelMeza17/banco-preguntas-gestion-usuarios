package bancopreguntas.controller.menu;

import java.util.List;

public class MenuAutorPreguntas implements IMenuRol {

    @Override
    public String getNombreRol() {
        return "Autor de preguntas";
    }

    @Override
    public List<String> getOpciones() {
        // TODO: definir las opciones del menú de Autor de preguntas.
        return List.of();
    }
}
