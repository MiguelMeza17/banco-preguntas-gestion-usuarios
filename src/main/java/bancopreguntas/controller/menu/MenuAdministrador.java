package bancopreguntas.controller.menu;

import java.util.List;

public class MenuAdministrador implements IMenuRol {

    @Override
    public String getNombreRol() {
        return "Administrador";
    }

    @Override
    public List<String> getOpciones() {
        // TODO: definir las opciones del menú de Administrador.
        return List.of();
    }
}
