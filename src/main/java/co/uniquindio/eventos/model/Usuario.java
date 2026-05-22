package co.edu.uniquindio.eventos.model;

import co.edu.uniquindio.eventos.patterns.behavioral.MetodoPago;
import co.edu.uniquindio.eventos.patterns.structural.Reporte;

import java.util.ArrayList;
import java.util.List;

public class Usuario {
    private String idUsuario;
    private String nombreCompleto;
    private String correo;
    private String telefono;
    private String contrasena;
    private List<MetodoPago> metodosPago;
    private List<Compra> compras;

    public Usuario(String idUsuario, String nombreCompleto, String correo, String telefono) {
        this(idUsuario, nombreCompleto, correo, telefono, "1234");
    }

    public Usuario(String idUsuario, String nombreCompleto, String correo, String telefono, String contrasena) {
        this.idUsuario = idUsuario;
        this.nombreCompleto = nombreCompleto;
        this.correo = correo;
        this.telefono = telefono;
        this.contrasena = contrasena;
        this.metodosPago = new ArrayList<>();
        this.compras = new ArrayList<>();
    }

    public String registrarse() {
        return nombreCompleto + " registrado correctamente";
    }

    public boolean iniciarSesion() {
        return correo != null && !correo.isBlank();
    }

    public void actualizarPerfil(String nuevoTelefono) {
        this.telefono = nuevoTelefono;
    }

    public void actualizarPerfil(String nuevoNombre, String nuevoCorreo, String nuevoTelefono) {
        this.nombreCompleto = nuevoNombre;
        this.correo = nuevoCorreo;
        this.telefono = nuevoTelefono;
    }

    public List<Evento> consultarEventos(List<Evento> eventos) {
        return eventos;
    }

    public List<Compra> consultarHistorialCompras() {
        return compras;
    }

    public String descargarReporteCompras(Reporte reporte) {
        return reporte.generar(this);
    }

    public void agregarMetodoPago(MetodoPago metodo) {
        metodosPago.add(metodo);
    }

    public void agregarCompra(Compra compra) {
        compras.add(compra);
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public String getCorreo() {
        return correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public List<MetodoPago> getMetodosPago() {
        return metodosPago;
    }

    public List<Compra> getCompras() {
        return compras;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    @Override
    public String toString() {
        return nombreCompleto;
    }
}
