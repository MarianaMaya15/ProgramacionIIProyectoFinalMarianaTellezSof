package co.edu.uniquindio.eventos.patterns.structural;

import co.edu.uniquindio.eventos.model.Usuario;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ReporteCSV implements Reporte {
    @Override
    public String generar(Usuario usuario) {
        StringBuilder csv = new StringBuilder("idCompra,usuario,evento,fecha,estado,cantidadEntradas,servicios,total\n");
        double totalGeneral = 0;
        for (var compra : usuario.getCompras()) {
            totalGeneral += compra.getTotal();
            String servicios = compra.getServicios().isEmpty() ? "Sin servicios"
                    : compra.getServicios().stream().map(s -> s.getNombre().replace(",", " ")).reduce((a, b) -> a + " | " + b).orElse("Sin servicios");
            csv.append(compra.getIdCompra()).append(",")
                    .append(usuario.getNombreCompleto()).append(",")
                    .append(compra.getEvento().getNombre()).append(",")
                    .append(compra.getFechaCreacion()).append(",")
                    .append(compra.getEstado()).append(",")
                    .append(compra.getEntradas().size()).append(",")
                    .append(servicios).append(",")
                    .append(String.format("%.0f", compra.getTotal()))
                    .append("\n");
        }
        csv.append(",,,,,,,TOTAL_GENERAL,").append(String.format("%.0f", totalGeneral)).append("\n");
        try {
            Path archivo = Files.createTempFile("reporte-compras-" + usuario.getIdUsuario() + "-", ".csv");
            Files.writeString(archivo, csv.toString());
            return "Reporte CSV generado en: " + archivo.toAbsolutePath();
        } catch (IOException e) {
            return "No fue posible generar el CSV: " + e.getMessage();
        }
    }
}
