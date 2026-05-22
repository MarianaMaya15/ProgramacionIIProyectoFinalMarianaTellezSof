package co.edu.uniquindio.eventos.controller;

import co.edu.uniquindio.eventos.Navigator;
import co.edu.uniquindio.eventos.app.MainApp;
import co.edu.uniquindio.eventos.model.Compra;
import co.edu.uniquindio.eventos.model.Promocion;
import co.edu.uniquindio.eventos.patterns.behavioral.MetodoPago;
import co.edu.uniquindio.eventos.patterns.behavioral.PagoNequi;
import co.edu.uniquindio.eventos.patterns.behavioral.PagoPSE;
import co.edu.uniquindio.eventos.patterns.behavioral.PagoTarjeta;
import co.edu.uniquindio.eventos.patterns.structural.AccesoPreferencial;
import co.edu.uniquindio.eventos.patterns.structural.Merchandising;
import co.edu.uniquindio.eventos.model.enums.EstadoCompra;
import co.edu.uniquindio.eventos.patterns.structural.Parqueadero;
import co.edu.uniquindio.eventos.patterns.structural.SeguroCancelacion;
import co.edu.uniquindio.eventos.patterns.structural.ServicioAdicional;
import co.edu.uniquindio.eventos.patterns.structural.ServicioBase;
import co.edu.uniquindio.eventos.patterns.structural.ServicioVIP;
import co.edu.uniquindio.eventos.repository.DatosPrueba;
import co.edu.uniquindio.eventos.util.AlertUtil;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;

import java.util.ArrayList;
import java.util.List;

public class CompraController {
    @FXML
    private Label usuarioLabel;
    @FXML
    private Label eventoLabel;
    @FXML
    private ListView<String> entradasListView;
    @FXML
    private ComboBox<String> metodoPagoComboBox;
    @FXML
    private ComboBox<Promocion> promocionComboBox;
    @FXML
    private CheckBox vipCheckBox;
    @FXML
    private CheckBox seguroCheckBox;
    @FXML
    private CheckBox parqueaderoCheckBox;
    @FXML
    private CheckBox accesoPreferencialCheckBox;
    @FXML
    private CheckBox merchandisingCheckBox;
    @FXML
    private Label subtotalLabel;
    @FXML
    private Label serviciosLabel;
    @FXML
    private Label descuentoLabel;
    @FXML
    private Label totalLabel;
    @FXML
    private TextArea comprobanteArea;

    private Compra compra;
    private boolean serviciosAplicados;

    public void cargarCompra(Compra compra) {
        this.compra = compra;
        usuarioLabel.setText(compra.getUsuario().getNombreCompleto());
        eventoLabel.setText(compra.getEvento().getNombre());
        entradasListView.setItems(FXCollections.observableArrayList(
                compra.getEntradas().stream().map(entrada -> entrada.toString()).toList()));
        metodoPagoComboBox.setItems(FXCollections.observableArrayList("Tarjeta", "PSE", "Nequi"));
        metodoPagoComboBox.getSelectionModel().selectFirst();
        promocionComboBox.setItems(FXCollections.observableArrayList(
                MainApp.getInstancia().getPromocionService().listarPromocionesActivas()));
        actualizarTotal();
    }

    @FXML
    public void aplicarServicios() {
        if (serviciosAplicados) {
            actualizarTotal();
            return;
        }
        List<ServicioAdicional> servicios = construirServiciosSeleccionados();
        for (ServicioAdicional servicio : servicios) {
            MainApp.getInstancia().getCompraService().agregarServicio(compra, servicio);
        }
        serviciosAplicados = true;
        actualizarTotal();
    }

    private List<ServicioAdicional> construirServiciosSeleccionados() {
        List<ServicioAdicional> servicios = new ArrayList<>();
        if (vipCheckBox.isSelected()) {
            servicios.add(new ServicioVIP(new ServicioBase()));
        }
        if (seguroCheckBox.isSelected()) {
            servicios.add(new SeguroCancelacion(new ServicioBase()));
        }
        if (parqueaderoCheckBox.isSelected()) {
            servicios.add(new Parqueadero(new ServicioBase()));
        }
        if (accesoPreferencialCheckBox.isSelected()) {
            servicios.add(new AccesoPreferencial(new ServicioBase()));
        }
        if (merchandisingCheckBox.isSelected()) {
            servicios.add(new Merchandising(new ServicioBase()));
        }
        return servicios;
    }

    @FXML
    public void pagarCompra() {
        aplicarServicios();
        MetodoPago metodoPago = switch (metodoPagoComboBox.getValue()) {
            case "PSE" -> new PagoPSE();
            case "Nequi" -> new PagoNequi();
            default -> new PagoTarjeta();
        };
        boolean pagoExitoso = MainApp.getInstancia().getCompraService().pagarCompra(compra, metodoPago);
        if (pagoExitoso) {
            DatosPrueba.getInstancia().getNotificador().notificar("La compra " + compra.getIdCompra() + " fue pagada.");
            actualizarTotal();
            comprobanteArea.setText(compra.generarComprobante());
            AlertUtil.mostrarInfo("Pago", "Pago simulado aprobado con " + metodoPago.getNombre());
        } else {
            AlertUtil.mostrarInfo("Pago", "El pago no pudo ser procesado.");
        }
    }

    @FXML
    public void confirmarCompra() {
        MainApp.getInstancia().getCompraService().confirmarCompra(compra);
        DatosPrueba.getInstancia().getNotificador().notificar("Compra confirmada para " + compra.getEvento().getNombre());
        comprobanteArea.setText(compra.generarComprobante());
        AlertUtil.mostrarInfo("Compra confirmada", "La compra quedo confirmada correctamente.");
    }

    @FXML
    public void cancelarCompra() {
        if (compra.getEstado() == EstadoCompra.PAGADA || compra.getEstado() == EstadoCompra.CONFIRMADA) {
            AlertUtil.mostrarInfo("Cancelacion", "La compra ya fue pagada/confirmada. Usa flujo de reembolso en administracion.");
            return;
        }
        MainApp.getInstancia().getCompraService().cancelarCompra(compra);
        DatosPrueba.getInstancia().getNotificador().notificar("Compra cancelada: " + compra.getIdCompra());
        actualizarTotal();
        comprobanteArea.setText(compra.generarComprobante());
        AlertUtil.mostrarInfo("Compra cancelada", "La compra fue cancelada antes del pago.");
    }

    @FXML
    public void aplicarPromocion() {
        Promocion promocion = promocionComboBox.getValue();
        if (promocion == null) {
            AlertUtil.mostrarInfo("Promocion", "Selecciona una promocion activa.");
            return;
        }
        if (promocion.getIdPromocion().equals("PRO-02") && compra.getEntradas().size() < 3) {
            AlertUtil.mostrarInfo("Promocion no aplica", "Compra grupal requiere 3 o mas entradas.");
            return;
        }
        double base = compra.getEntradas().stream().mapToDouble(entrada -> entrada.getPrecioFinal()).sum()
                + compra.getServicios().stream().mapToDouble(servicio -> servicio.getCosto()).sum();
        double totalConPromo = MainApp.getInstancia().getPromocionService()
                .aplicarPromocion(promocion.getIdPromocion(), base);
        compra.aplicarDescuentoPromocional(base - totalConPromo);
        actualizarTotal();
        AlertUtil.mostrarInfo("Promocion aplicada", promocion.getNombre() + " aplicada correctamente.");
    }

    @FXML
    public void volverDetalle() {
        Navigator.volver();
    }

    private void actualizarTotal() {
        double subtotal = compra.getEntradas().stream().mapToDouble(entrada -> entrada.getPrecioFinal()).sum();
        double servicios = compra.getServicios().stream().mapToDouble(servicio -> servicio.getCosto()).sum();
        subtotalLabel.setText("$" + String.format("%.0f", subtotal));
        serviciosLabel.setText("$" + String.format("%.0f", servicios));
        descuentoLabel.setText("-$" + String.format("%.0f", compra.getDescuentoPromocional()));
        totalLabel.setText("$" + String.format("%.0f", compra.calcularTotal()));
    }
}
