package co.edu.uniquindio.eventos.patterns.behavioral;

public interface MetodoPago {
    String getNombre();

    boolean procesar(double valor);
}
