package co.edu.uniquindio.eventos.patterns.structural;

// Patron Decorator aplicado para agregar servicios adicionales
public class SeguroCancelacion extends ServicioDecorator {
    public SeguroCancelacion(ServicioAdicional servicioBase) {
        super(servicioBase);
    }

    @Override
    public String getNombre() {
        return servicioBase.getNombre() + ", Seguro";
    }

    @Override
    public double getCosto() {
        return servicioBase.getCosto() + 12000;
    }

    @Override
    public String getDescripcion() {
        return servicioBase.getDescripcion() + " + cobertura por cancelacion";
    }
}
