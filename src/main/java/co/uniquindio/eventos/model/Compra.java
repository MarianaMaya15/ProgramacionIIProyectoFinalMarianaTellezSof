package co.edu.uniquindio.eventos.model;

import co.edu.uniquindio.eventos.model.enums.EstadoCompra;
import co.edu.uniquindio.eventos.patterns.behavioral.EstadoCompraInterface;
import co.edu.uniquindio.eventos.patterns.behavioral.EstadoCreada;
import co.edu.uniquindio.eventos.patterns.structural.ServicioAdicional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Compra {
    private String idCompra;
    private Usuario usuario;
    private Evento evento;
    private LocalDateTime fechaCreacion;
    private double total;
    private EstadoCompra estado;
    private List<Entrada> entradas;
    private List<ServicioAdicional> servicios;
    private double descuentoPromocional;
    private Pago pago;
    private EstadoCompraInterface estadoCompraActual;

    public Compra(String idCompra, Usuario usuario, Evento evento) {
        this.idCompra = idCompra;
        this.usuario = usuario;
        this.evento = evento;
        this.fechaCreacion = LocalDateTime.now();
        this.entradas = new ArrayList<>();
        this.servicios = new ArrayList<>();
        this.descuentoPromocional = 0;
        this.estadoCompraActual = new EstadoCreada();
        this.estado = EstadoCompra.CREADA;
        this.total = 0;
    }

    public void agregarEntrada(Entrada entrada) {
        entradas.add(entrada);
        calcularTotal();
    }

    public void quitarEntrada(Entrada entrada) {
        entradas.remove(entrada);
        calcularTotal();
    }

    public void agregarServicio(ServicioAdicional servicio) {
        servicios.add(servicio);
        calcularTotal();
    }

    public double calcularTotal() {
        double totalEntradas = entradas.stream().mapToDouble(Entrada::getPrecioFinal).sum();
        double totalServicios = servicios.stream().mapToDouble(ServicioAdicional::getCosto).sum();
        total = totalEntradas + totalServicios - descuentoPromocional;
        if (total < 0) {
            total = 0;
        }
        return total;
    }

    public void aplicarDescuentoPromocional(double descuento) {
        this.descuentoPromocional = Math.max(descuento, 0);
        calcularTotal();
    }

    public boolean pagar(Pago pago) {
        this.pago = pago;
        boolean aprobado = pago.procesarPago();
        if (aprobado) {
            estadoCompraActual.pagar(this);
        }
        return aprobado;
    }

    public void confirmar() {
        estadoCompraActual.confirmar(this);
    }

    public void cancelar() {
        estadoCompraActual.cancelar(this);
    }

    public void reembolsar() {
        estadoCompraActual.reembolsar(this);
    }

    public String generarComprobante() {
        StringBuilder builder = new StringBuilder();
        builder.append("COMPROBANTE DE COMPRA")
                .append("\nCompra: ").append(idCompra)
                .append("\nUsuario: ").append(usuario.getNombreCompleto())
                .append("\nCorreo: ").append(usuario.getCorreo())
                .append("\nEvento: ").append(evento.getNombre())
                .append("\nFecha compra: ").append(fechaCreacion)
                .append("\nEstado: ").append(estado)
                .append("\nEntradas:");
        for (Entrada entrada : entradas) {
            builder.append("\n- ").append(entrada.getZona().getNombre())
                    .append(" / Asiento ").append(entrada.getAsiento().getEtiqueta())
                    .append(" / $").append(String.format("%.0f", entrada.getPrecioFinal()));
        }
        if (servicios.isEmpty()) {
            builder.append("\nServicios: Sin adicionales");
        } else {
            builder.append("\nServicios:");
            for (ServicioAdicional servicio : servicios) {
                builder.append("\n- ").append(servicio.getNombre())
                        .append(" ($").append(String.format("%.0f", servicio.getCosto())).append(")");
            }
        }
        builder.append("\nDescuento promocional: -$").append(String.format("%.0f", descuentoPromocional))
                .append("\nTotal: $").append(String.format("%.0f", total));
        if (pago != null) {
            builder.append("\nMetodo de pago: ").append(pago.getMetodoPago().getNombre())
                    .append("\n").append(pago.generarComprobante());
        }
        return builder.toString();
    }

    public void setEstadoCompraActual(EstadoCompraInterface estadoCompraActual) {
        this.estadoCompraActual = estadoCompraActual;
        this.estado = estadoCompraActual.getEstado();
    }

    public String getIdCompra() {
        return idCompra;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public Evento getEvento() {
        return evento;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public double getTotal() {
        return total;
    }

    public EstadoCompra getEstado() {
        return estado;
    }

    public List<Entrada> getEntradas() {
        return entradas;
    }

    public List<ServicioAdicional> getServicios() {
        return servicios;
    }

    public double getDescuentoPromocional() {
        return descuentoPromocional;
    }

    public Pago getPago() {
        return pago;
    }

    public EstadoCompraInterface getEstadoCompraActual() {
        return estadoCompraActual;
    }

    @Override
    public String toString() {
        return idCompra + " - " + evento.getNombre() + " - " + estado;
    }
}
