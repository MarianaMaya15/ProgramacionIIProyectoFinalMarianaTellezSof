package co.edu.uniquindio.eventos.model;

import co.edu.uniquindio.eventos.model.enums.EstadoPago;
import co.edu.uniquindio.eventos.patterns.behavioral.MetodoPago;

import java.time.LocalDateTime;

public class Pago {
    private String idPago;
    private double valor;
    private LocalDateTime fechaPago;
    private EstadoPago estado;
    private MetodoPago metodoPago;

    public Pago(String idPago, double valor, MetodoPago metodoPago) {
        this.idPago = idPago;
        this.valor = valor;
        this.metodoPago = metodoPago;
        this.estado = EstadoPago.PENDIENTE;
    }

    public boolean procesarPago() {
        boolean aprobado = metodoPago.procesar(valor);
        estado = aprobado ? EstadoPago.APROBADO : EstadoPago.RECHAZADO;
        if (aprobado) {
            fechaPago = LocalDateTime.now();
        }
        return aprobado;
    }

    public String generarComprobante() {
        return "Pago " + idPago + " - " + metodoPago.getNombre() + " - " + estado;
    }

    public void registrarReembolso() {
        estado = EstadoPago.REEMBOLSADO;
    }

    public String getIdPago() {
        return idPago;
    }

    public double getValor() {
        return valor;
    }

    public LocalDateTime getFechaPago() {
        return fechaPago;
    }

    public EstadoPago getEstado() {
        return estado;
    }

    public MetodoPago getMetodoPago() {
        return metodoPago;
    }
}
