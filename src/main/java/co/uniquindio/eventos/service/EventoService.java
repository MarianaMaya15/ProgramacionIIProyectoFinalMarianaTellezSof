package co.edu.uniquindio.eventos.service;

import co.edu.uniquindio.eventos.model.Evento;
import co.edu.uniquindio.eventos.model.enums.EstadoEvento;
import co.edu.uniquindio.eventos.repository.DatosPrueba;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EventoService {
    public List<Evento> listarEventos() {
        return DatosPrueba.getInstancia().getEventos();
    }

    public List<Evento> listarEventosPublicados() {
        return listarEventos().stream()
                .filter(evento -> evento.getEstado() == EstadoEvento.PUBLICADO)
                .toList();
    }

    public List<Evento> filtrarPorCiudad(String ciudad) {
        return listarEventos().stream()
                .filter(evento -> evento.getCiudad().equalsIgnoreCase(ciudad))
                .toList();
    }

    public List<Evento> filtrarPorCategoria(String categoria) {
        return listarEventos().stream()
                .filter(evento -> evento.getCategoria().equalsIgnoreCase(categoria))
                .toList();
    }

    public List<Evento> filtrarPorFecha(LocalDateTime desde, LocalDateTime hasta) {
        return listarEventos().stream()
                .filter(evento -> !evento.getFechaHora().isBefore(desde) && !evento.getFechaHora().isAfter(hasta))
                .toList();
    }

    public List<Evento> filtrarPorPrecioMaximo(double precioMaximo) {
        return listarEventos().stream()
                .filter(evento -> evento.getPrecioDesde() <= precioMaximo)
                .toList();
    }

    public void agregarEvento(Evento evento) {
        DatosPrueba.getInstancia().getEventos().add(evento);
    }

    public void actualizarEvento(Evento eventoActualizado) {
        List<Evento> eventos = DatosPrueba.getInstancia().getEventos();
        for (int i = 0; i < eventos.size(); i++) {
            if (eventos.get(i).getIdEvento().equals(eventoActualizado.getIdEvento())) {
                eventos.set(i, eventoActualizado);
                return;
            }
        }
    }

    public void eliminarEvento(String idEvento) {
        DatosPrueba.getInstancia().getEventos().removeIf(evento -> evento.getIdEvento().equals(idEvento));
    }

    public void publicarEvento(Evento evento) {
        evento.publicar();
    }

    public void pausarEvento(Evento evento) {
        evento.pausar();
    }

    public void cancelarEvento(Evento evento) {
        evento.cancelar();
    }

    public List<Evento> obtenerEventos() {
        return new ArrayList<>(listarEventos());
    }

    public List<Evento> obtenerEventosPublicados() {
        return new ArrayList<>(listarEventosPublicados());
    }
}
