package co.edu.uniquindio.eventos.patterns.structural;

// Patron Decorator aplicado para agregar servicios adicionales
public class AccesoPreferencial extends ServicioDecorator {
    public AccesoPreferencial(ServicioAdicional servicioBase) {
        super(servicioBase);
    }

    @Override
    public String getNombre() {
        return servicioBase.getNombre() + ", Acceso preferencial";
    }

    @Override
    public double getCosto() {
        return servicioBase.getCosto() + 15000;
    }

    @Override
    public String getDescripcion() {
        return servicioBase.getDescripcion() + " + ingreso rapido sin fila";
    }
}
