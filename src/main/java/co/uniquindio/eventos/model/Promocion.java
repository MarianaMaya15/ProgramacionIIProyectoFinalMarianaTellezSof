package co.edu.uniquindio.eventos.model;

public class Promocion {
    private String idPromocion;
    private String nombre;
    private String descripcion;
    private double porcentajeDescuento;
    private String condicion;
    private boolean activa;

    public Promocion(String idPromocion, String nombre, String descripcion, double porcentajeDescuento, String condicion, boolean activa) {
        this.idPromocion = idPromocion;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.porcentajeDescuento = porcentajeDescuento;
        this.condicion = condicion;
        this.activa = activa;
    }

    public double aplicarDescuento(double total) {
        if (!activa) {
            return total;
        }
        return total - (total * porcentajeDescuento);
    }

    public boolean estaActiva() {
        return activa;
    }

    public String getIdPromocion() {
        return idPromocion;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public double getPorcentajeDescuento() {
        return porcentajeDescuento;
    }

    public String getCondicion() {
        return condicion;
    }

    public boolean isActiva() {
        return activa;
    }

    @Override
    public String toString() {
        return nombre + " (" + String.format("%.0f%%", porcentajeDescuento * 100) + ")";
    }
}
