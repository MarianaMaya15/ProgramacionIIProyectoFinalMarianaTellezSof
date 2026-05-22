package co.edu.uniquindio.eventos.patterns.behavioral;

public class NotificacionUsuario implements Observador {
    private String ultimoMensaje;

    @Override
    public void actualizar(String mensaje) {
        this.ultimoMensaje = "Usuario: " + mensaje;
    }

    public String getUltimoMensaje() {
        return ultimoMensaje;
    }
}
