package co.edu.uniquindio.eventos.service;

import co.edu.uniquindio.eventos.model.Asiento;
import co.edu.uniquindio.eventos.model.Compra;
import co.edu.uniquindio.eventos.model.Evento;
import co.edu.uniquindio.eventos.model.Incidencia;
import co.edu.uniquindio.eventos.model.PanelMetricas;
import co.edu.uniquindio.eventos.model.Recinto;
import co.edu.uniquindio.eventos.model.Usuario;
import co.edu.uniquindio.eventos.model.Zona;
import co.edu.uniquindio.eventos.model.enums.EstadoCompra;
import co.edu.uniquindio.eventos.model.enums.EstadoEvento;
import co.edu.uniquindio.eventos.repository.DatosPrueba;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class AdminService {
    public List<Evento> obtenerEventos() {
        return DatosPrueba.getInstancia().getEventos();
    }

    public List<Recinto> obtenerRecintos() {
        return DatosPrueba.getInstancia().getRecintos();
    }

    public List<Usuario> obtenerUsuarios() {
        return DatosPrueba.getInstancia().getUsuarios();
    }

    public List<Compra> obtenerCompras() {
        return DatosPrueba.getInstancia().getCompras();
    }

    public void actualizarRecintoDeEvento(Evento evento, String nombre, String direccion, String ciudad) {
        if (evento != null && evento.getRecinto() != null) {
            evento.getRecinto().actualizarDatos(nombre, direccion, ciudad);
        }
    }

    public void crearZonaEnEvento(Evento evento, Zona zona) {
        if (evento != null && evento.getRecinto() != null && zona != null) {
            evento.getRecinto().agregarZona(zona);
        }
    }

    public void actualizarZonaEnEvento(Evento evento, String idZona, String nombre, int capacidad, double precioBase) {
        if (evento == null || evento.getRecinto() == null) {
            return;
        }
        evento.getRecinto().getZonas().stream()
                .filter(zona -> zona.getIdZona().equals(idZona))
                .findFirst()
                .ifPresent(zona -> zona.actualizarDatos(nombre, capacidad, precioBase));
    }

    public void eliminarZonaEnEvento(Evento evento, String idZona) {
        if (evento != null && evento.getRecinto() != null) {
            evento.getRecinto().eliminarZona(idZona);
        }
    }

    public void bloquearAsiento(Asiento asiento) {
        asiento.bloquear();
    }

    public void liberarAsiento(Asiento asiento) {
        asiento.liberar();
    }

    public Incidencia registrarIncidencia(String tipo, String descripcion, String entidadAfectada) {
        Incidencia incidencia = new Incidencia("INC-" + UUID.randomUUID().toString().substring(0, 5), tipo, descripcion, entidadAfectada);
        DatosPrueba.getInstancia().getIncidencias().add(incidencia);
        return incidencia;
    }

    public List<Incidencia> listarIncidencias() {
        return DatosPrueba.getInstancia().getIncidencias();
    }

    public PanelMetricas obtenerMetricasBasicas() {
        PanelMetricas panel = new PanelMetricas();
        List<Compra> compras = DatosPrueba.getInstancia().getCompras();
        panel.setCantidadCompras(compras.size());
        panel.setTotalVentas(compras.stream().mapToDouble(Compra::getTotal).sum());
        panel.setCantidadEventosPublicados((int) obtenerEventos().stream()
                .filter(evento -> evento.getEstado() == EstadoEvento.PUBLICADO)
                .count());
        panel.setIngresosServicios(compras.stream()
                .flatMap(compra -> compra.getServicios().stream())
                .mapToDouble(servicio -> servicio.getCosto())
                .sum());
        long canceladas = compras.stream().filter(compra -> compra.getEstado() == EstadoCompra.CANCELADA).count();
        panel.setTasaCancelacion(compras.isEmpty() ? 0 : (canceladas * 100.0) / compras.size());

        compras.stream()
                .collect(java.util.stream.Collectors.groupingBy(
                        compra -> compra.getEvento().getNombre(),
                        java.util.stream.Collectors.summingDouble(Compra::getTotal)))
                .entrySet().stream()
                .sorted(java.util.Map.Entry.<String, Double>comparingByValue(Comparator.reverseOrder()))
                .forEach(entry -> panel.getVentasPorEvento().put(entry.getKey(), entry.getValue()));

        compras.stream()
                .collect(Collectors.groupingBy(
                        compra -> compra.getFechaCreacion().toLocalDate().toString(),
                        Collectors.summingDouble(Compra::getTotal)))
                .entrySet().stream()
                .sorted(java.util.Map.Entry.comparingByKey())
                .forEach(entry -> panel.getVentasPorPeriodo().put(entry.getKey(), entry.getValue()));

        for (Evento evento : obtenerEventos()) {
            for (Zona zona : evento.getRecinto().getZonas()) {
                panel.getOcupacionPorZona().put(evento.getNombre() + " - " + zona.getNombre(), zona.consultarOcupacion());
            }
        }
        return panel;
    }

    public void cancelarCompra(Compra compra) {
        compra.cancelar();
    }

    public void registrarReembolso(Compra compra) {
        compra.reembolsar();
        if (compra.getPago() != null) {
            compra.getPago().registrarReembolso();
        }
    }

    public PanelMetricas consultarMetricas() {
        return obtenerMetricasBasicas();
    }
}
