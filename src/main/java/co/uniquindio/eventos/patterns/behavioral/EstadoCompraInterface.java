package co.edu.uniquindio.eventos.patterns.behavioral;

import co.edu.uniquindio.eventos.model.Compra;
import co.edu.uniquindio.eventos.model.enums.EstadoCompra;

public interface EstadoCompraInterface {
    void pagar(Compra compra);

    void confirmar(Compra compra);

    void cancelar(Compra compra);

    void reembolsar(Compra compra);

    void reportarIncidencia(Compra compra);

    EstadoCompra getEstado();
}
