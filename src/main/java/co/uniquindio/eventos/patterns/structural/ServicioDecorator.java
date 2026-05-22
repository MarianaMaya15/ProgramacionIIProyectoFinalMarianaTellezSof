package co.edu.uniquindio.eventos.patterns.structural;

// Patron Decorator aplicado para agregar servicios adicionales
public abstract class ServicioDecorator implements ServicioAdicional {
    protected ServicioAdicional servicioBase;

    protected ServicioDecorator(ServicioAdicional servicioBase) {
        this.servicioBase = servicioBase;
    }
}
