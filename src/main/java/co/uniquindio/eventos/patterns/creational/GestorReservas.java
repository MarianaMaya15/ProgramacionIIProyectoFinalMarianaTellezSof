package co.edu.uniquindio.eventos.patterns.creational;

import co.edu.uniquindio.eventos.model.Asiento;
import co.edu.uniquindio.eventos.model.enums.EstadoAsiento;

// Patron Singleton aplicado para controlar reservas
public class GestorReservas {
    private static GestorReservas instancia;

    private GestorReservas() {
    }

    public static GestorReservas getInstancia() {
        if (instancia == null) {
            instancia = new GestorReservas();
        }
        return instancia;
    }

    public boolean reservarAsiento(Asiento asiento) {
        if (asiento.getEstado() == EstadoAsiento.DISPONIBLE) {
            asiento.reservar();
            return true;
        }
        return false;
    }

    public void liberarAsiento(Asiento asiento) {
        asiento.liberar();
    }

    public void venderAsiento(Asiento asiento) {
        asiento.vender();
    }
}
