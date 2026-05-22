package co.edu.uniquindio.eventos.model;

public class SesionActual {
    private static Usuario usuarioActual;

    private SesionActual() {
    }

    public static void iniciarSesion(Usuario usuario) {
        usuarioActual = usuario;
    }

    public static Usuario getUsuarioActual() {
        return usuarioActual;
    }

    public static boolean esAdministrador() {
        return usuarioActual instanceof Administrador;
    }

    public static void cerrarSesion() {
        usuarioActual = null;
    }
}
