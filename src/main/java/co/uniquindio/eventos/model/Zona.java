package co.edu.uniquindio.eventos.model;

import co.edu.uniquindio.eventos.model.enums.EstadoAsiento;

import java.util.ArrayList;
import java.util.List;

public class Zona {
    private String idZona;
    private String nombre;
    private int capacidad;
    private double precioBase;
    private List<Asiento> asientos;

    public Zona(String idZona, String nombre, int capacidad, double precioBase) {
        this.idZona = idZona;
        this.nombre = nombre;
        this.capacidad = capacidad;
        this.precioBase = precioBase;
        this.asientos = new ArrayList<>();
    }

    public void agregarAsiento(Asiento asiento) {
        asientos.add(asiento);
    }

    public double consultarOcupacion() {
        if (asientos.isEmpty()) {
            return 0;
        }
        long ocupados = asientos.stream()
                .filter(asiento -> asiento.getEstado() == EstadoAsiento.VENDIDO || asiento.getEstado() == EstadoAsiento.RESERVADO)
                .count();
        return (ocupados * 100.0) / asientos.size();
    }

    public void definirPrecioBase(double nuevoPrecio) {
        this.precioBase = nuevoPrecio;
    }

    public void actualizarDatos(String nombre, int capacidad, double precioBase) {
        this.nombre = nombre;
        this.capacidad = capacidad;
        this.precioBase = precioBase;
    }

    public List<Asiento> consultarAsientosDisponibles() {
        return asientos.stream()
                .filter(asiento -> asiento.getEstado() == EstadoAsiento.DISPONIBLE)
                .toList();
    }

    public String getIdZona() {
        return idZona;
    }

    public String getNombre() {
        return nombre;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public double getPrecioBase() {
        return precioBase;
    }

    public List<Asiento> getAsientos() {
        return asientos;
    }

    @Override
    public String toString() {
        return nombre + " - $" + String.format("%.0f", precioBase);
    }
}
