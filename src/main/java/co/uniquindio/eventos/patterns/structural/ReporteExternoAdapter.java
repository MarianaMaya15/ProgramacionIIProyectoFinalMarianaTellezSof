package co.edu.uniquindio.eventos.patterns.structural;

import co.edu.uniquindio.eventos.model.Usuario;

// Patron Adapter aplicado para adaptar un servicio externo de reportes
public class ReporteExternoAdapter implements Reporte {
    private ServicioReporteExterno servicioReporteExterno;

    public ReporteExternoAdapter(ServicioReporteExterno servicioReporteExterno) {
        this.servicioReporteExterno = servicioReporteExterno;
    }

    @Override
    public String generar(Usuario usuario) {
        return servicioReporteExterno.exportarFormatoPlano(usuario);
    }
}
