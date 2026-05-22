package co.edu.uniquindio.eventos.service;

import co.edu.uniquindio.eventos.model.Compra;
import co.edu.uniquindio.eventos.model.Entrada;
import co.edu.uniquindio.eventos.model.Evento;
import co.edu.uniquindio.eventos.model.Usuario;
import co.edu.uniquindio.eventos.model.Zona;
import co.edu.uniquindio.eventos.model.Asiento;
import co.edu.uniquindio.eventos.model.enums.EstadoCompra;
import co.edu.uniquindio.eventos.patterns.behavioral.MetodoPago;
import co.edu.uniquindio.eventos.patterns.structural.CompraFacade;
import co.edu.uniquindio.eventos.patterns.structural.ServicioAdicional;
import co.edu.uniquindio.eventos.repository.DatosPrueba;

import java.time.LocalDate;
import java.util.List;

public class CompraService {
    private final CompraFacade compraFacade;

    public CompraService() {
        this.compraFacade = new CompraFacade();
    }

    public Compra crearCompra(Usuario usuario, Evento evento, Zona zona, Asiento asiento) {
        Compra compra = compraFacade.crearCompra(usuario, evento, zona, asiento);
        agregarCompra(compra);
        return compra;
    }

    public Compra crearCompra(Usuario usuario, Evento evento) {
        Compra compra = compraFacade.crearCompra(usuario, evento);
        agregarCompra(compra);
        return compra;
    }

    public boolean agregarEntrada(Compra compra, Zona zona, Asiento asiento) {
        return compraFacade.agregarEntrada(compra, zona, asiento);
    }

    public void quitarEntrada(Compra compra, Entrada entrada) {
        compraFacade.quitarEntrada(compra, entrada);
    }

    public void agregarCompra(Compra compra) {
        compra.getUsuario().agregarCompra(compra);
        DatosPrueba.getInstancia().getCompras().add(compra);
    }

    public void agregarServicio(Compra compra, ServicioAdicional servicio) {
        compraFacade.agregarServicio(compra, servicio);
    }

    public boolean pagarCompra(Compra compra, MetodoPago metodoPago) {
        return compraFacade.pagarCompra(compra, metodoPago);
    }

    public void confirmarCompra(Compra compra) {
        compraFacade.confirmarCompra(compra);
    }

    public void cancelarCompra(Compra compra) {
        compraFacade.cancelarCompra(compra);
    }

    public void reembolsarCompra(Compra compra) {
        compraFacade.reembolsarCompra(compra);
    }

    public List<Compra> listarCompras() {
        return DatosPrueba.getInstancia().getCompras();
    }

    public List<Compra> listarComprasPorUsuario(Usuario usuario) {
        return listarCompras().stream()
                .filter(compra -> compra.getUsuario().getIdUsuario().equals(usuario.getIdUsuario()))
                .toList();
    }

    public List<Compra> filtrarComprasUsuario(Usuario usuario, String nombreEvento, EstadoCompra estado, LocalDate fecha) {
        return listarComprasPorUsuario(usuario).stream()
                .filter(compra -> nombreEvento == null || nombreEvento.isBlank()
                        || compra.getEvento().getNombre().toLowerCase().contains(nombreEvento.toLowerCase()))
                .filter(compra -> estado == null || compra.getEstado() == estado)
                .filter(compra -> fecha == null || compra.getFechaCreacion().toLocalDate().equals(fecha))
                .toList();
    }

    public double calcularTotalCompra(Compra compra) {
        return compra.calcularTotal();
    }

    public List<Compra> obtenerCompras() {
        return listarCompras();
    }
}
