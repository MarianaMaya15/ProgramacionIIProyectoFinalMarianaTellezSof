package co.edu.uniquindio.eventos.model;

import java.util.LinkedHashMap;
import java.util.Map;

public class PanelMetricas {
    private double totalVentas;
    private double ingresosServicios;
    private double tasaCancelacion;
    private int cantidadCompras;
    private int cantidadEventosPublicados;
    private Map<String, Double> ocupacionPorZona;
    private Map<String, Double> ventasPorEvento;
    private Map<String, Double> ventasPorPeriodo;

    public PanelMetricas() {
        this.ocupacionPorZona = new LinkedHashMap<>();
        this.ventasPorEvento = new LinkedHashMap<>();
        this.ventasPorPeriodo = new LinkedHashMap<>();
    }

    public double getTotalVentas() {
        return totalVentas;
    }

    public void setTotalVentas(double totalVentas) {
        this.totalVentas = totalVentas;
    }

    public double getIngresosServicios() {
        return ingresosServicios;
    }

    public void setIngresosServicios(double ingresosServicios) {
        this.ingresosServicios = ingresosServicios;
    }

    public double getTasaCancelacion() {
        return tasaCancelacion;
    }

    public void setTasaCancelacion(double tasaCancelacion) {
        this.tasaCancelacion = tasaCancelacion;
    }

    public int getCantidadCompras() {
        return cantidadCompras;
    }

    public void setCantidadCompras(int cantidadCompras) {
        this.cantidadCompras = cantidadCompras;
    }

    public int getCantidadEventosPublicados() {
        return cantidadEventosPublicados;
    }

    public void setCantidadEventosPublicados(int cantidadEventosPublicados) {
        this.cantidadEventosPublicados = cantidadEventosPublicados;
    }

    public Map<String, Double> getOcupacionPorZona() {
        return ocupacionPorZona;
    }

    public Map<String, Double> getVentasPorEvento() {
        return ventasPorEvento;
    }

    public Map<String, Double> getVentasPorPeriodo() {
        return ventasPorPeriodo;
    }
}
