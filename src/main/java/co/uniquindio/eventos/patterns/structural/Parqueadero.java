package co.edu.uniquindio.eventos.patterns.structural;

// Patron Decorator aplicado para agregar servicios adicionales
public class Parqueadero extends ServicioDecorator {
    public Parqueadero(ServicioAdicional servicioBase) {
        super(servicioBase);
    }

    @Override
    public String getNombre() {
        return servicioBase.getNombre() + ", Parqueadero";
    }

    @Override
    public double getCosto() {
        return servicioBase.getCosto() + 18000;
    }

    @Override
    public String getDescripcion() {
        return servicioBase.getDescripcion() + " + cupo de parqueadero";
    }
}
