package co.edu.uniquindio.eventos.patterns.behavioral;

// Patron Strategy aplicado para cambiar metodo de pago
public class PagoPSE implements MetodoPago {
    @Override
    public String getNombre() {
        return "PSE";
    }

    @Override
    public boolean procesar(double valor) {
        return valor > 0;
    }
}
