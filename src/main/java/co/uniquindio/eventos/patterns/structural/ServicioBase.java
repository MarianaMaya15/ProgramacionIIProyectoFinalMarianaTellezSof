package co.edu.uniquindio.eventos.patterns.structural;

public class ServicioBase implements ServicioAdicional {
    @Override
    public String getNombre() {
        return "Base";
    }

    @Override
    public double getCosto() {
        return 0;
    }

    @Override
    public String getDescripcion() {
        return "Compra sin adicionales";
    }
}
