package co.edu.uniquindio.eventos.model;

public class Administrador extends Usuario {
    private String codigoEmpleado;

    public Administrador(String idUsuario, String nombreCompleto, String correo, String telefono, String codigoEmpleado) {
        this(idUsuario, nombreCompleto, correo, telefono, "admin123", codigoEmpleado);
    }

    public Administrador(String idUsuario, String nombreCompleto, String correo, String telefono, String contrasena, String codigoEmpleado) {
        super(idUsuario, nombreCompleto, correo, telefono, contrasena);
        this.codigoEmpleado = codigoEmpleado;
    }

    public String gestionarUsuarios() {
        return "Gestion de usuarios habilitada";
    }

    public String gestionarEventos() {
        return "Gestion de eventos habilitada";
    }

    public String gestionarRecintos() {
        return "Gestion de recintos habilitada";
    }

    public String gestionarAsientos() {
        return "Gestion de asientos habilitada";
    }

    public String gestionarCompras() {
        return "Gestion de compras habilitada";
    }

    public String registrarIncidencia() {
        return "Incidencia registrada";
    }

    public String consultarMetricas() {
        return "Metricas consultadas";
    }

    public String getCodigoEmpleado() {
        return codigoEmpleado;
    }
}
