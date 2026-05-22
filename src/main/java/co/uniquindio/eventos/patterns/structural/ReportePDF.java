package co.edu.uniquindio.eventos.patterns.structural;

import co.edu.uniquindio.eventos.model.Usuario;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;

public class ReportePDF implements Reporte {
    @Override
    public String generar(Usuario usuario) {
        StringBuilder contenido = new StringBuilder();
        contenido.append("REPORTE DE COMPRAS (PDF SIMULADO)\n")
                .append("Usuario: ").append(usuario.getNombreCompleto()).append("\n")
                .append("Correo: ").append(usuario.getCorreo()).append("\n")
                .append("Fecha generacion: ").append(LocalDateTime.now()).append("\n\n");
        double total = 0;
        for (var compra : usuario.getCompras()) {
            contenido.append("Compra ").append(compra.getIdCompra())
                    .append(" | Evento: ").append(compra.getEvento().getNombre())
                    .append(" | Estado: ").append(compra.getEstado())
                    .append(" | Total: $").append(String.format("%.0f", compra.getTotal()))
                    .append("\n");
            total += compra.getTotal();
        }
        contenido.append("\nTOTAL GENERAL: $").append(String.format("%.0f", total)).append("\n");
        try {
            Path archivo = Files.createTempFile("reporte-compras-" + usuario.getIdUsuario() + "-", ".txt");
            Files.writeString(archivo, contenido.toString());
            return "Reporte PDF simulado generado en: " + archivo.toAbsolutePath();
        } catch (IOException e) {
            return "No fue posible generar el reporte PDF simulado: " + e.getMessage();
        }
    }
}
