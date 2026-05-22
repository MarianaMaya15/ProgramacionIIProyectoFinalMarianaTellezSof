package co.edu.uniquindio.eventos.patterns.creational;

import co.edu.uniquindio.eventos.model.Compra;
import co.edu.uniquindio.eventos.model.Entrada;
import co.edu.uniquindio.eventos.model.Evento;
import co.edu.uniquindio.eventos.model.Usuario;
import co.edu.uniquindio.eventos.patterns.structural.ServicioAdicional;

// Patron Builder aplicado para construir una compra paso a paso
public class ReservaBuilder {
    private Compra compra;

    public ReservaBuilder conDatosBasicos(String idCompra, Usuario usuario, Evento evento) {
        this.compra = new Compra(idCompra, usuario, evento);
        return this;
    }

    public ReservaBuilder agregarEntrada(Entrada entrada) {
        compra.agregarEntrada(entrada);
        return this;
    }

    public ReservaBuilder agregarServicio(ServicioAdicional servicio) {
        compra.agregarServicio(servicio);
        return this;
    }

    public Compra construir() {
        compra.calcularTotal();
        return compra;
    }
}
