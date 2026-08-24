package bancopreguntas.controller.menu;

import java.util.List;

public class MenuDocente implements IMenuRol {

    @Override
    public String getNombreRol() {
        return "Docente";
    }

    @Override
    public List<String> getOpciones() {
        // TODO: definir las opciones del menú de Docente.
        return List.of();
    }
}
