package bancopreguntas.model;

/**
 * Entidad de dominio. No conoce cómo se persiste ni cómo se valida:
 * esas responsabilidades viven en otras clases (SRP).
 */
public class Usuario {

    private int id;
    private String login;
    private String nombreCompleto;
    private Rol rol;
    private EstadoUsuario estado;
    private String passwordHash;

    public Usuario() {
    }

    public Usuario(int id, String login, String nombreCompleto, Rol rol,
                    EstadoUsuario estado, String passwordHash) {
        this.id = id;
        this.login = login;
        this.nombreCompleto = nombreCompleto;
        this.rol = rol;
        this.estado = estado;
        this.passwordHash = passwordHash;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public EstadoUsuario getEstado() {
        return estado;
    }

    public void setEstado(EstadoUsuario estado) {
        this.estado = estado;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    @Override
    public String toString() {
        return "Usuario{" + "login=" + login + ", nombreCompleto=" + nombreCompleto
                + ", rol=" + rol + ", estado=" + estado + '}';
    }
}
