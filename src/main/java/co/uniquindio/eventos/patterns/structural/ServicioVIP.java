package co.edu.uniquindio.eventos.patterns.structural;

// Patron Decorator aplicado para agregar servicios adicionales
public class ServicioVIP extends ServicioDecorator {
    public ServicioVIP(ServicioAdicional servicioBase) {
        super(servicioBase);
    }

    @Override
    public String getNombre() {
        return servicioBase.getNombre() + ", VIP";
    }

    @Override
    public double getCosto() {
        return servicioBase.getCosto() + 35000;
    }

    @Override
    public String getDescripcion() {
        return servicioBase.getDescripcion() + " + ingreso preferencial";
    }
}
