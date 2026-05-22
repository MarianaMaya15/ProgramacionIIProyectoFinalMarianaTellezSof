package co.edu.uniquindio.eventos.patterns.structural;

import co.edu.uniquindio.eventos.model.Usuario;

public class ServicioReporteExterno {
    public String exportarFormatoPlano(Usuario usuario) {
        return "Servicio externo: historial de " + usuario.getNombreCompleto();
    }
}
