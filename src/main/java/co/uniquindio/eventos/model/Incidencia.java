package co.edu.uniquindio.eventos.model;

import co.edu.uniquindio.eventos.model.enums.EstadoIncidencia;

import java.time.LocalDateTime;

public class Incidencia {
    private String idIncidencia;
    private String tipo;
    private String descripcion;
    private LocalDateTime fecha;
    private EstadoIncidencia estado;
    private String entidadAfectada;

    public Incidencia(String idIncidencia, String tipo, String descripcion, String entidadAfectada) {
        this.idIncidencia = idIncidencia;
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.entidadAfectada = entidadAfectada;
        this.fecha = LocalDateTime.now();
        this.estado = EstadoIncidencia.ABIERTA;
    }

    public void registrar() {
        this.fecha = LocalDateTime.now();
        this.estado = EstadoIncidencia.ABIERTA;
    }

    public String consultarDetalle() {
        return tipo + ": " + descripcion + " (" + estado + ")";
    }

    public String getIdIncidencia() {
        return idIncidencia;
    }

    public String getTipo() {
        return tipo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public EstadoIncidencia getEstado() {
        return estado;
    }

    public String getEntidadAfectada() {
        return entidadAfectada;
    }

    @Override
    public String toString() {
        return idIncidencia + " - " + tipo + " - " + estado;
    }
}
