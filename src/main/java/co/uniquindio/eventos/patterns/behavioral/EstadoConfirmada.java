package co.edu.uniquindio.eventos.patterns.behavioral;

import co.edu.uniquindio.eventos.model.Compra;
import co.edu.uniquindio.eventos.model.enums.EstadoCompra;

public class EstadoConfirmada implements EstadoCompraInterface {
    @Override
    public void pagar(Compra compra) {
    }

    @Override
    public void confirmar(Compra compra) {
    }

    @Override
    public void cancelar(Compra compra) {
        compra.setEstadoCompraActual(new EstadoCancelada());
    }

    @Override
    public void reembolsar(Compra compra) {
        compra.setEstadoCompraActual(new EstadoReembolsada());
    }

    @Override
    public void reportarIncidencia(Compra compra) {
        compra.setEstadoCompraActual(new EstadoIncidenciaCompra());
    }

    @Override
    public EstadoCompra getEstado() {
        return EstadoCompra.CONFIRMADA;
    }
}
