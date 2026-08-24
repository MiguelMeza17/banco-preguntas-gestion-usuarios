package bancopreguntas.conexion;

/**
 * Fábrica (singleton) que instancia la implementación concreta de
 * {@link IUsuarioRepository} que se debe usar. Quien la consume no
 * necesita conocer la clase concreta.
 */
public class Factory {

    private static Factory instance;

    private Factory() {
    }

    public static Factory getInstance() {
        if (instance == null) {
            instance = new Factory();
        }
        return instance;
    }

    /**
     * @param tipo "default" (única implementación disponible: PostgreSQL)
     */
    public IUsuarioRepository getUsuarioRepository(String tipo) {
        IUsuarioRepository result = null;

        switch (tipo) {
            case "default":
                result = new UsuarioRepository();
                break;
        }

        return result;
    }
}
