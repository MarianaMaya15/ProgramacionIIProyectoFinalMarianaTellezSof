package co.edu.uniquindio.eventos.patterns.creational;

import co.edu.uniquindio.eventos.model.Evento;
import co.edu.uniquindio.eventos.model.Recinto;

import java.time.LocalDateTime;

// Patron Factory Method aplicado para crear eventos segun categoria
public class EventoFactory {
    public static Evento crearEvento(String idEvento, String nombre, String categoria, String descripcion,
                                     String ciudad, LocalDateTime fechaHora, Recinto recinto) {
        Evento evento = new Evento(idEvento, nombre, categoria, descripcion, ciudad, fechaHora, recinto);
        if ("Concierto".equalsIgnoreCase(categoria) || "Teatro".equalsIgnoreCase(categoria)
                || "Festival".equalsIgnoreCase(categoria)) {
            evento.publicar();
        }
        return evento;
    }
}
