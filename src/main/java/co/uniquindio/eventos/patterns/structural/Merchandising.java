package co.edu.uniquindio.eventos.patterns.structural;

// Patron Decorator aplicado para agregar servicios adicionales
public class Merchandising extends ServicioDecorator {
    public Merchandising(ServicioAdicional servicioBase) {
        super(servicioBase);
    }

    @Override
    public String getNombre() {
        return servicioBase.getNombre() + ", Merchandising";
    }

    @Override
    public double getCosto() {
        return servicioBase.getCosto() + 22000;
    }

    @Override
    public String getDescripcion() {
        return servicioBase.getDescripcion() + " + kit con recuerdos del evento";
    }
}
