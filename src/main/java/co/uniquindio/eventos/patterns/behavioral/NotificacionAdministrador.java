package co.edu.uniquindio.eventos.patterns.behavioral;

public class NotificacionAdministrador implements Observador {
    private String ultimoMensaje;

    @Override
    public void actualizar(String mensaje) {
        this.ultimoMensaje = "Admin: " + mensaje;
    }

    public String getUltimoMensaje() {
        return ultimoMensaje;
    }
}
