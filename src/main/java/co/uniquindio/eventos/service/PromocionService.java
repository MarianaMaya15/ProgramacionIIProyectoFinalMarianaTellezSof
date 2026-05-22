package co.edu.uniquindio.eventos.service;

import co.edu.uniquindio.eventos.model.Promocion;

import java.util.List;

public class PromocionService {
    private final List<Promocion> promociones = List.of(
            new Promocion("PRO-01", "Martes de cultura", "Descuento para eventos culturales.", 0.10, "Aplica para categoria Cultural.", true),
            new Promocion("PRO-02", "Compra grupal", "Descuento para compras de 3 o mas entradas.", 0.15, "Aplica con 3+ entradas.", true),
            new Promocion("PRO-03", "Estudiante UQ", "Promocion informativa para estudiantes UQ.", 0.08, "Aplica con validacion institucional.", true),
            new Promocion("PRO-04", "Preventa", "Descuento para compra anticipada.", 0.12, "Aplica para eventos proximos.", true)
    );

    public List<Promocion> listarPromociones() {
        return promociones;
    }

    public List<Promocion> listarPromocionesActivas() {
        return promociones.stream().filter(Promocion::estaActiva).toList();
    }

    public double aplicarPromocion(String idPromocion, double total) {
        Promocion promocion = promociones.stream()
                .filter(p -> p.getIdPromocion().equals(idPromocion))
                .findFirst()
                .orElse(null);
        if (promocion == null) {
            return total;
        }
        return promocion.aplicarDescuento(total);
    }
}
