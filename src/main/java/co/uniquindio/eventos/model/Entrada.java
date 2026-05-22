package co.edu.uniquindio.eventos.model;

import co.edu.uniquindio.eventos.model.enums.EstadoEntrada;

public class Entrada {
    private String idEntrada;
    private Zona zona;
    private Asiento asiento;
    private double precioFinal;
    private EstadoEntrada estado;

    public Entrada(String idEntrada, Zona zona, Asiento asiento, double precioFinal) {
        this.idEntrada = idEntrada;
        this.zona = zona;
        this.asiento = asiento;
        this.precioFinal = precioFinal;
        this.estado = EstadoEntrada.ACTIVA;
    }

    public void activar() {
        estado = EstadoEntrada.ACTIVA;
    }

    public void usar() {
        estado = EstadoEntrada.USADA;
    }

    public void anular() {
        estado = EstadoEntrada.ANULADA;
    }

    public String getIdEntrada() {
        return idEntrada;
    }

    public Zona getZona() {
        return zona;
    }

    public Asiento getAsiento() {
        return asiento;
    }

    public double getPrecioFinal() {
        return precioFinal;
    }

    public EstadoEntrada getEstado() {
        return estado;
    }

    @Override
    public String toString() {
        return zona.getNombre() + " - " + asiento.getEtiqueta() + " - $" + String.format("%.0f", precioFinal);
    }
}
