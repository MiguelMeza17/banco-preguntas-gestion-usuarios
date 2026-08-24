package bancopreguntas.controller.menu;

import java.util.List;

public class MenuRevisor implements IMenuRol {

    @Override
    public String getNombreRol() {
        return "Revisor";
    }

    @Override
    public List<String> getOpciones() {
        // TODO: definir las opciones del menú de Revisor.
        return List.of();
    }
}
