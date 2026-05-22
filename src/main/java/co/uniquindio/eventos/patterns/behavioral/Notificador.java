package co.edu.uniquindio.eventos.patterns.behavioral;

import java.util.ArrayList;
import java.util.List;

// Patron Observer aplicado para notificar cambios de compra o evento
public class Notificador {
    private List<Observador> observadores;
    private String ultimoMensaje;

    public Notificador() {
        this.observadores = new ArrayList<>();
    }

    public void agregarObservador(Observador observador) {
        observadores.add(observador);
    }

    public void eliminarObservador(Observador observador) {
        observadores.remove(observador);
    }

    public void notificar(String mensaje) {
        this.ultimoMensaje = mensaje;
        for (Observador observador : observadores) {
            observador.actualizar(mensaje);
        }
    }

    public String getUltimoMensaje() {
        return ultimoMensaje;
    }

    public int getCantidadObservadores() {
        return observadores.size();
    }
}
