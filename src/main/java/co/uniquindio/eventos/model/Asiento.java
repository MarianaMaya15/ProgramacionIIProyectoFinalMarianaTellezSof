package co.edu.uniquindio.eventos.model;

import co.edu.uniquindio.eventos.model.enums.EstadoAsiento;

public class Asiento {
    private String idAsiento;
    private String fila;
    private int numero;
    private EstadoAsiento estado;

    public Asiento(String idAsiento, String fila, int numero) {
        this.idAsiento = idAsiento;
        this.fila = fila;
        this.numero = numero;
        this.estado = EstadoAsiento.DISPONIBLE;
    }

    public void reservar() {
        if (estado == EstadoAsiento.DISPONIBLE) {
            estado = EstadoAsiento.RESERVADO;
        }
    }

    public void vender() {
        if (estado == EstadoAsiento.RESERVADO || estado == EstadoAsiento.DISPONIBLE) {
            estado = EstadoAsiento.VENDIDO;
        }
    }

    public void bloquear() {
        estado = EstadoAsiento.BLOQUEADO;
    }

    public void liberar() {
        if (estado != EstadoAsiento.VENDIDO) {
            estado = EstadoAsiento.DISPONIBLE;
        }
    }

    public String getEtiqueta() {
        return fila + numero;
    }

    public String getIdAsiento() {
        return idAsiento;
    }

    public String getFila() {
        return fila;
    }

    public int getNumero() {
        return numero;
    }

    public EstadoAsiento getEstado() {
        return estado;
    }

    @Override
    public String toString() {
        return getEtiqueta() + " - " + estado;
    }
}
