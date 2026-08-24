package bancopreguntas.controller.menu;

import java.util.List;

/**
 * Menú/tablero de opciones específico para un rol. Cualquier
 * implementación puede usarse donde se espere un IMenuRol sin romper
 * el comportamiento esperado (LSP).
 */
public interface IMenuRol {

    String getNombreRol();

    List<String> getOpciones();
}
