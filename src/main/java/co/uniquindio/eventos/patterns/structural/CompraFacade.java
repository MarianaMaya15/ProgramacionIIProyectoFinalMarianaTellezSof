package co.edu.uniquindio.eventos.patterns.structural;

import co.edu.uniquindio.eventos.model.Asiento;
import co.edu.uniquindio.eventos.model.Compra;
import co.edu.uniquindio.eventos.model.Entrada;
import co.edu.uniquindio.eventos.model.Evento;
import co.edu.uniquindio.eventos.model.Pago;
import co.edu.uniquindio.eventos.model.Usuario;
import co.edu.uniquindio.eventos.model.Zona;
import co.edu.uniquindio.eventos.model.enums.EstadoCompra;
import co.edu.uniquindio.eventos.patterns.behavioral.MetodoPago;
import co.edu.uniquindio.eventos.patterns.creational.GestorReservas;
import co.edu.uniquindio.eventos.patterns.creational.ReservaBuilder;

import java.util.UUID;

// Patron Facade aplicado para simplificar el proceso de compra
public class CompraFacade {
    private final GestorReservas gestorReservas;

    public CompraFacade() {
        this.gestorReservas = GestorReservas.getInstancia();
    }

    public Compra crearCompra(Usuario usuario, Evento evento, Zona zona, Asiento asiento) {
        gestorReservas.reservarAsiento(asiento);
        Entrada entrada = new Entrada("ENT-" + UUID.randomUUID().toString().substring(0, 5), zona, asiento, zona.getPrecioBase());
        return new ReservaBuilder()
                .conDatosBasicos("COM-" + UUID.randomUUID().toString().substring(0, 5), usuario, evento)
                .agregarEntrada(entrada)
                .construir();
    }

    public Compra crearCompra(Usuario usuario, Evento evento) {
        return new ReservaBuilder()
                .conDatosBasicos("COM-" + UUID.randomUUID().toString().substring(0, 5), usuario, evento)
                .construir();
    }

    public boolean agregarEntrada(Compra compra, Zona zona, Asiento asiento) {
        boolean reservado = gestorReservas.reservarAsiento(asiento);
        if (!reservado) {
            return false;
        }
        Entrada entrada = new Entrada("ENT-" + UUID.randomUUID().toString().substring(0, 5), zona, asiento, zona.getPrecioBase());
        compra.agregarEntrada(entrada);
        return true;
    }

    public void quitarEntrada(Compra compra, Entrada entrada) {
        compra.quitarEntrada(entrada);
        gestorReservas.liberarAsiento(entrada.getAsiento());
    }

    public void agregarServicio(Compra compra, ServicioAdicional servicio) {
        compra.agregarServicio(servicio);
    }

    public boolean pagarCompra(Compra compra, MetodoPago metodoPago) {
        Pago pago = new Pago("PAG-" + UUID.randomUUID().toString().substring(0, 5), compra.calcularTotal(), metodoPago);
        boolean pagado = compra.pagar(pago);
        if (pagado) {
            compra.getEntradas().forEach(entrada -> gestorReservas.venderAsiento(entrada.getAsiento()));
        }
        return pagado;
    }

    public void confirmarCompra(Compra compra) {
        compra.confirmar();
    }

    public void cancelarCompra(Compra compra) {
        compra.cancelar();
        compra.getEntradas().forEach(entrada -> {
            if (entrada.getAsiento().getEstado() != co.edu.uniquindio.eventos.model.enums.EstadoAsiento.VENDIDO) {
                gestorReservas.liberarAsiento(entrada.getAsiento());
            }
        });
    }

    public void reembolsarCompra(Compra compra) {
        if (compra.getEstado() == EstadoCompra.PAGADA || compra.getEstado() == EstadoCompra.CONFIRMADA) {
            compra.reembolsar();
            if (compra.getPago() != null) {
                compra.getPago().registrarReembolso();
            }
        }
    }
}
