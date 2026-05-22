package co.edu.uniquindio.eventos.model;

import co.edu.uniquindio.eventos.model.enums.EstadoEvento;

import java.time.LocalDateTime;
import java.util.List;

public class Evento {
    private String idEvento;
    private String nombre;
    private String categoria;
    private String descripcion;
    private String ciudad;
    private LocalDateTime fechaHora;
    private EstadoEvento estado;
    private String politicaCancelacion;
    private String politicaReembolso;
    private Recinto recinto;

    public Evento(String idEvento, String nombre, String categoria, String descripcion, String ciudad,
                  LocalDateTime fechaHora, Recinto recinto) {
        this.idEvento = idEvento;
        this.nombre = nombre;
        this.categoria = categoria;
        this.descripcion = descripcion;
        this.ciudad = ciudad;
        this.fechaHora = fechaHora;
        this.recinto = recinto;
        this.estado = EstadoEvento.BORRADOR;
        this.politicaCancelacion = "Cancelacion hasta 24 horas antes.";
        this.politicaReembolso = "Reembolso sujeto a revision.";
    }

    public void publicar() {
        estado = EstadoEvento.PUBLICADO;
    }

    public void pausar() {
        estado = EstadoEvento.PAUSADO;
    }

    public void cancelar() {
        estado = EstadoEvento.CANCELADO;
    }

    public void finalizar() {
        estado = EstadoEvento.FINALIZADO;
    }

    public void actualizarDatos(String nombre, String categoria, String descripcion, String ciudad, LocalDateTime fechaHora) {
        this.nombre = nombre;
        this.categoria = categoria;
        this.descripcion = descripcion;
        this.ciudad = ciudad;
        this.fechaHora = fechaHora;
    }

    public List<Zona> consultarDisponibilidad() {
        return recinto.getZonas();
    }

    public double getPrecioDesde() {
        return recinto.getZonas().stream()
                .mapToDouble(Zona::getPrecioBase)
                .min()
                .orElse(0);
    }

    public String getIdEvento() {
        return idEvento;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCategoria() {
        return categoria;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getCiudad() {
        return ciudad;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public EstadoEvento getEstado() {
        return estado;
    }

    public String getPoliticaCancelacion() {
        return politicaCancelacion;
    }

    public String getPoliticaReembolso() {
        return politicaReembolso;
    }

    public Recinto getRecinto() {
        return recinto;
    }

    public void asignarRecinto(Recinto recinto) {
        this.recinto = recinto;
    }

    @Override
    public String toString() {
        return nombre + " - " + estado;
    }
}
