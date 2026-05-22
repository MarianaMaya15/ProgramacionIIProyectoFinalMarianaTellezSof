package co.edu.uniquindio.eventos.controller;

import co.edu.uniquindio.eventos.Navigator;
import co.edu.uniquindio.eventos.app.MainApp;
import co.edu.uniquindio.eventos.model.Compra;
import co.edu.uniquindio.eventos.model.Usuario;
import co.edu.uniquindio.eventos.model.enums.EstadoCompra;
import co.edu.uniquindio.eventos.patterns.structural.ReporteCSV;
import co.edu.uniquindio.eventos.patterns.structural.ReportePDF;
import co.edu.uniquindio.eventos.util.AlertUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.beans.property.SimpleStringProperty;

import java.util.List;

public class HistorialController {
    @FXML
    private TextField eventoFiltroField;
    @FXML
    private ComboBox<EstadoCompra> estadoFiltroComboBox;
    @FXML
    private DatePicker fechaFiltroDatePicker;
    @FXML
    private TableView<Compra> comprasTableView;
    @FXML
    private TableColumn<Compra, String> eventoColumn;
    @FXML
    private TableColumn<Compra, String> fechaColumn;
    @FXML
    private TableColumn<Compra, String> estadoColumn;
    @FXML
    private TableColumn<Compra, String> entradasColumn;
    @FXML
    private TableColumn<Compra, String> totalColumn;
    @FXML
    private TextArea detalleArea;

    private Usuario usuarioActual;

    public void cargarUsuario(Usuario usuario) {
        this.usuarioActual = usuario;
        estadoFiltroComboBox.setItems(FXCollections.observableArrayList(EstadoCompra.values()));
        eventoColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getEvento().getNombre()));
        fechaColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getFechaCreacion().toString()));
        estadoColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getEstado().name()));
        entradasColumn.setCellValueFactory(c -> new SimpleStringProperty(String.valueOf(c.getValue().getEntradas().size())));
        totalColumn.setCellValueFactory(c -> new SimpleStringProperty("$" + String.format("%.0f", c.getValue().getTotal())));
        cargarCompras(usuario.getCompras());
    }

    @FXML
    public void filtrarCompras() {
        List<Compra> filtradas = MainApp.getInstancia().getCompraService().filtrarComprasUsuario(
                usuarioActual,
                eventoFiltroField.getText(),
                estadoFiltroComboBox.getValue(),
                fechaFiltroDatePicker.getValue()
        );
        cargarCompras(filtradas);
    }

    @FXML
    public void limpiarFiltros() {
        eventoFiltroField.clear();
        estadoFiltroComboBox.getSelectionModel().clearSelection();
        fechaFiltroDatePicker.setValue(null);
        cargarCompras(usuarioActual.getCompras());
    }

    @FXML
    public void verDetalle() {
        Compra compra = comprasTableView.getSelectionModel().getSelectedItem();
        if (compra == null) {
            AlertUtil.mostrarInfo("Historial", "Selecciona una compra para ver detalle.");
            return;
        }
        detalleArea.setText(compra.generarComprobante()
                + "\nEntradas: " + compra.getEntradas().size()
                + "\nServicios: " + compra.getServicios().size());
    }

    @FXML
    public void descargarCsv() {
        String resultado = usuarioActual.descargarReporteCompras(new ReporteCSV());
        AlertUtil.mostrarInfo("Reporte CSV", resultado);
    }

    @FXML
    public void generarPdfSimulado() {
        String resultado = usuarioActual.descargarReporteCompras(new ReportePDF());
        AlertUtil.mostrarInfo("Reporte PDF", resultado);
    }

    @FXML
    public void volverCartelera() {
        Navigator.irCartelera();
    }

    private void cargarCompras(List<Compra> compras) {
        ObservableList<Compra> items = FXCollections.observableArrayList(compras);
        comprasTableView.setItems(items);
    }
}
