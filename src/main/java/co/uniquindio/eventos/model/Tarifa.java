package co.edu.uniquindio.eventos.model;

public class Tarifa {
    private String idTarifa;
    private String nombre;
    private double valorBase;
    private double descuento;

    public Tarifa(String idTarifa, String nombre, double valorBase, double descuento) {
        this.idTarifa = idTarifa;
        this.nombre = nombre;
        this.valorBase = valorBase;
        this.descuento = descuento;
    }

    public double calcularPrecioFinal() {
        return valorBase - (valorBase * descuento);
    }

    public String getIdTarifa() {
        return idTarifa;
    }

    public String getNombre() {
        return nombre;
    }

    public double getValorBase() {
        return valorBase;
    }

    public double getDescuento() {
        return descuento;
    }
}
