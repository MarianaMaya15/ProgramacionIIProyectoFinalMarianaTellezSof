package co.uniquindio.eventos.controller;

import co.edu.uniquindio.eventos.Navigator;
import co.edu.uniquindio.eventos.app.MainApp;
import co.edu.uniquindio.eventos.model.Asiento;
import co.edu.uniquindio.eventos.model.Compra;
import co.edu.uniquindio.eventos.model.Evento;
import co.edu.uniquindio.eventos.model.Incidencia;
import co.edu.uniquindio.eventos.model.PanelMetricas;
import co.edu.uniquindio.eventos.model.Recinto;
import co.edu.uniquindio.eventos.model.Usuario;
import co.edu.uniquindio.eventos.model.Zona;
import co.edu.uniquindio.eventos.patterns.creational.EventoFactory;
import co.edu.uniquindio.eventos.repository.DatosPrueba;
import co.edu.uniquindio.eventos.util.AlertUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.UUID;

public class AdminController {
    @FXML
    private ListView<Evento> eventosListView;
    @FXML
    private TextField eventoNombreField;
    @FXML
    private TextField eventoCategoriaField;
    @FXML
    private TextField eventoCiudadField;
    @FXML
    private TextField eventoFechaField;
    @FXML
    private TextField eventoDescripcionField;

    @FXML
    private ListView<Usuario> usuariosListView;
    @FXML
    private TextField usuarioNombreField;
    @FXML
    private TextField usuarioCorreoField;
    @FXML
    private TextField usuarioTelefonoField;

    @FXML
    private ListView<Recinto> recintosListView;
    @FXML
    private TextField recintoNombreField;
    @FXML
    private TextField recintoDireccionField;
    @FXML
    private TextField recintoCiudadField;
    @FXML
    private ListView<Zona> zonasListView;
    @FXML
    private TextField zonaNombreField;
    @FXML
    private TextField zonaCapacidadField;
    @FXML
    private TextField zonaPrecioField;

    @FXML
    private ComboBox<Evento> eventoAsientosComboBox;
    @FXML
    private ComboBox<Zona> zonaAsientosComboBox;
    @FXML
    private ListView<Asiento> asientosListView;

    @FXML
    private ListView<Compra> comprasListView;
    @FXML
    private TextArea detalleCompraArea;

    @FXML
    private ListView<Incidencia> incidenciasListView;
    @FXML
    private TextField tipoIncidenciaField;
    @FXML
    private TextArea descripcionIncidenciaArea;
    @FXML
    private ComboBox<String> entidadIncidenciaComboBox;

    @FXML
    private Label totalVentasLabel;
    @FXML
    private Label comprasLabel;
    @FXML
    private Label ingresosServiciosLabel;
    @FXML
    private Label publicadosLabel;
    @FXML
    private Label tasaCancelacionLabel;
    @FXML
    private Label ocupacionLabel;
    @FXML
    private BarChart<String, Number> ventasBarChart;
    @FXML
    private LineChart<String, Number> ventasLineChart;
    @FXML
    private PieChart ocupacionPieChart;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public void cargarDatos() {
        recargarListas();
        eventosListView.getSelectionModel().selectedItemProperty().addListener((obs, o, n) -> {
            if (n != null) {
                cargarEventoEnFormulario(n);
                cargarRecintoYZonasEvento(n);
            }
        });
        usuariosListView.getSelectionModel().selectedItemProperty().addListener((obs, o, n) -> cargarUsuarioEnFormulario(n));
        zonasListView.getSelectionModel().selectedItemProperty().addListener((obs, o, n) -> cargarZonaEnFormulario(n));
        eventoAsientosComboBox.setOnAction(e -> cargarZonasAsiento());
        entidadIncidenciaComboBox.setItems(FXCollections.observableArrayList("EVENTO", "COMPRA"));
    }

    private void recargarListas() {
        eventosListView.setItems(FXCollections.observableArrayList(MainApp.getInstancia().getAdminService().obtenerEventos()));
        usuariosListView.setItems(FXCollections.observableArrayList(MainApp.getInstancia().getAdminService().obtenerUsuarios()));
        comprasListView.setItems(FXCollections.observableArrayList(MainApp.getInstancia().getAdminService().obtenerCompras()));
        incidenciasListView.setItems(FXCollections.observableArrayList(MainApp.getInstancia().getAdminService().listarIncidencias()));
        eventoAsientosComboBox.setItems(FXCollections.observableArrayList(MainApp.getInstancia().getAdminService().obtenerEventos()));
        recintosListView.setItems(FXCollections.observableArrayList(
                MainApp.getInstancia().getAdminService().obtenerRecintos().stream()
                        .sorted(Comparator.comparing(Recinto::getNombre))
                        .toList()));
        actualizarMetricas();
    }

    @FXML
    public void crearEvento() {
        try {
            Evento evento = EventoFactory.crearEvento(
                    "E-" + UUID.randomUUID().toString().substring(0, 5),
                    eventoNombreField.getText(),
                    eventoCategoriaField.getText(),
                    eventoDescripcionField.getText(),
                    eventoCiudadField.getText(),
                    LocalDateTime.parse(eventoFechaField.getText(), formatter),
                    crearRecintoBasico(eventoCiudadField.getText())
            );
            MainApp.getInstancia().getEventoService().agregarEvento(evento);
            recargarListas();
            AlertUtil.mostrarInfo("Eventos", "Evento creado.");
        } catch (Exception e) {
            AlertUtil.mostrarInfo("Eventos", "Revisa datos de evento. Fecha: yyyy-MM-dd HH:mm");
        }
    }

    @FXML
    public void actualizarEvento() {
        Evento seleccionado = eventosListView.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            AlertUtil.mostrarInfo("Eventos", "Selecciona un evento.");
            return;
        }
        try {
            seleccionado.actualizarDatos(
                    eventoNombreField.getText(),
                    eventoCategoriaField.getText(),
                    eventoDescripcionField.getText(),
                    eventoCiudadField.getText(),
                    LocalDateTime.parse(eventoFechaField.getText(), formatter)
            );
            eventosListView.refresh();
            AlertUtil.mostrarInfo("Eventos", "Evento actualizado.");
        } catch (Exception e) {
            AlertUtil.mostrarInfo("Eventos", "Formato de fecha invalido.");
        }
    }

    @FXML
    public void eliminarEvento() {
        Evento seleccionado = eventosListView.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            AlertUtil.mostrarInfo("Eventos", "Selecciona un evento.");
            return;
        }
        MainApp.getInstancia().getEventoService().eliminarEvento(seleccionado.getIdEvento());
        recargarListas();
    }

    @FXML
    public void publicarEvento() {
        cambiarEstadoEvento("PUBLICAR");
    }

    @FXML
    public void pausarEvento() {
        cambiarEstadoEvento("PAUSAR");
    }

    @FXML
    public void cancelarEvento() {
        cambiarEstadoEvento("CANCELAR");
    }

    @FXML
    public void crearUsuario() {
        Usuario usuario = new Usuario(
                "U-" + UUID.randomUUID().toString().substring(0, 5),
                usuarioNombreField.getText(),
                usuarioCorreoField.getText(),
                usuarioTelefonoField.getText()
        );
        MainApp.getInstancia().getUsuarioService().registrarUsuario(usuario);
        recargarListas();
    }

    @FXML
    public void actualizarUsuario() {
        Usuario usuario = usuariosListView.getSelectionModel().getSelectedItem();
        if (usuario == null) {
            AlertUtil.mostrarInfo("Usuarios", "Selecciona un usuario.");
            return;
        }
        MainApp.getInstancia().getUsuarioService().actualizarUsuario(
                usuario.getIdUsuario(),
                usuarioNombreField.getText(),
                usuarioCorreoField.getText(),
                usuarioTelefonoField.getText()
        );
        usuariosListView.refresh();
    }

    @FXML
    public void eliminarUsuario() {
        Usuario usuario = usuariosListView.getSelectionModel().getSelectedItem();
        if (usuario == null) {
            AlertUtil.mostrarInfo("Usuarios", "Selecciona un usuario.");
            return;
        }
        if (usuario.getIdUsuario().equals(MainApp.getInstancia().getUsuarioActual().getIdUsuario())) {
            AlertUtil.mostrarInfo("Usuarios", "No puedes eliminar el usuario actual.");
            return;
        }
        MainApp.getInstancia().getUsuarioService().eliminarUsuario(usuario.getIdUsuario());
        recargarListas();
    }

    @FXML
    public void actualizarRecinto() {
        Evento evento = eventosListView.getSelectionModel().getSelectedItem();
        if (evento == null) {
            AlertUtil.mostrarInfo("Recintos", "Selecciona un evento.");
            return;
        }
        MainApp.getInstancia().getAdminService().actualizarRecintoDeEvento(
                evento, recintoNombreField.getText(), recintoDireccionField.getText(), recintoCiudadField.getText());
        recargarListas();
    }

    @FXML
    public void crearZona() {
        Evento evento = eventosListView.getSelectionModel().getSelectedItem();
        if (evento == null) {
            AlertUtil.mostrarInfo("Zonas", "Selecciona un evento.");
            return;
        }
        try {
            Zona zona = new Zona(
                    evento.getRecinto().getIdRecinto() + "-Z-" + UUID.randomUUID().toString().substring(0, 3),
                    zonaNombreField.getText(),
                    Integer.parseInt(zonaCapacidadField.getText()),
                    Double.parseDouble(zonaPrecioField.getText())
            );
            MainApp.getInstancia().getAdminService().crearZonaEnEvento(evento, zona);
            cargarRecintoYZonasEvento(evento);
        } catch (Exception e) {
            AlertUtil.mostrarInfo("Zonas", "Capacidad y precio deben ser numericos.");
        }
    }

    @FXML
    public void actualizarZona() {
        Evento evento = eventosListView.getSelectionModel().getSelectedItem();
        Zona zona = zonasListView.getSelectionModel().getSelectedItem();
        if (evento == null || zona == null) {
            AlertUtil.mostrarInfo("Zonas", "Selecciona evento y zona.");
            return;
        }
        try {
            MainApp.getInstancia().getAdminService().actualizarZonaEnEvento(
                    evento, zona.getIdZona(), zonaNombreField.getText(),
                    Integer.parseInt(zonaCapacidadField.getText()),
                    Double.parseDouble(zonaPrecioField.getText()));
            zonasListView.refresh();
        } catch (Exception e) {
            AlertUtil.mostrarInfo("Zonas", "Capacidad y precio deben ser numericos.");
        }
    }

    @FXML
    public void eliminarZona() {
        Evento evento = eventosListView.getSelectionModel().getSelectedItem();
        Zona zona = zonasListView.getSelectionModel().getSelectedItem();
        if (evento == null || zona == null) {
            AlertUtil.mostrarInfo("Zonas", "Selecciona evento y zona.");
            return;
        }
        MainApp.getInstancia().getAdminService().eliminarZonaEnEvento(evento, zona.getIdZona());
        cargarRecintoYZonasEvento(evento);
    }

    @FXML
    public void cargarAsientosZona() {
        Zona zona = zonaAsientosComboBox.getValue();
        if (zona == null) {
            asientosListView.getItems().clear();
            return;
        }
        asientosListView.setItems(FXCollections.observableArrayList(zona.getAsientos()));
    }

    @FXML
    public void bloquearAsiento() {
        Asiento asiento = asientosListView.getSelectionModel().getSelectedItem();
        if (asiento != null) {
            MainApp.getInstancia().getAdminService().bloquearAsiento(asiento);
            asientosListView.refresh();
            actualizarMetricas();
        }
    }

    @FXML
    public void liberarAsiento() {
        Asiento asiento = asientosListView.getSelectionModel().getSelectedItem();
        if (asiento != null) {
            MainApp.getInstancia().getAdminService().liberarAsiento(asiento);
            asientosListView.refresh();
            actualizarMetricas();
        }
    }

    @FXML
    public void verDetalleCompra() {
        Compra compra = comprasListView.getSelectionModel().getSelectedItem();
        if (compra == null) {
            return;
        }
        detalleCompraArea.setText(compra.generarComprobante()
                + "\nUsuario: " + compra.getUsuario().getNombreCompleto()
                + "\nEntradas: " + compra.getEntradas().size());
    }

    @FXML
    public void cancelarCompraAdmin() {
        Compra compra = comprasListView.getSelectionModel().getSelectedItem();
        if (compra != null) {
            MainApp.getInstancia().getAdminService().cancelarCompra(compra);
            comprasListView.refresh();
            actualizarMetricas();
        }
    }

    @FXML
    public void reembolsarCompraAdmin() {
        Compra compra = comprasListView.getSelectionModel().getSelectedItem();
        if (compra != null) {
            MainApp.getInstancia().getAdminService().registrarReembolso(compra);
            comprasListView.refresh();
            actualizarMetricas();
        }
    }

    @FXML
    public void registrarIncidencia() {
        String tipo = tipoIncidenciaField.getText();
        String descripcion = descripcionIncidenciaArea.getText();
        String entidad = entidadIncidenciaComboBox.getValue();
        if (tipo == null || tipo.isBlank() || descripcion == null || descripcion.isBlank() || entidad == null) {
            AlertUtil.mostrarInfo("Incidencias", "Completa tipo, descripcion y entidad.");
            return;
        }
        Incidencia incidencia = MainApp.getInstancia().getAdminService()
                .registrarIncidencia(tipo, descripcion, entidad);
        DatosPrueba.getInstancia().getNotificador().notificar("Nueva incidencia: " + incidencia.consultarDetalle());
        tipoIncidenciaField.clear();
        descripcionIncidenciaArea.clear();
        incidenciasListView.setItems(FXCollections.observableArrayList(MainApp.getInstancia().getAdminService().listarIncidencias()));
    }

    @FXML
    public void volverCartelera() {
        Navigator.irCartelera();
    }

    private void cargarEventoEnFormulario(Evento evento) {
        eventoNombreField.setText(evento.getNombre());
        eventoCategoriaField.setText(evento.getCategoria());
        eventoCiudadField.setText(evento.getCiudad());
        eventoDescripcionField.setText(evento.getDescripcion());
        eventoFechaField.setText(evento.getFechaHora().format(formatter));
    }

    private void cargarUsuarioEnFormulario(Usuario usuario) {
        if (usuario == null) {
            return;
        }
        usuarioNombreField.setText(usuario.getNombreCompleto());
        usuarioCorreoField.setText(usuario.getCorreo());
        usuarioTelefonoField.setText(usuario.getTelefono());
    }

    private void cargarRecintoYZonasEvento(Evento evento) {
        recintosListView.setItems(FXCollections.observableArrayList(evento.getRecinto()));
        recintoNombreField.setText(evento.getRecinto().getNombre());
        recintoDireccionField.setText(evento.getRecinto().getDireccion());
        recintoCiudadField.setText(evento.getRecinto().getCiudad());
        zonasListView.setItems(FXCollections.observableArrayList(evento.getRecinto().getZonas()));
    }

    private void cargarZonaEnFormulario(Zona zona) {
        if (zona == null) {
            return;
        }
        zonaNombreField.setText(zona.getNombre());
        zonaCapacidadField.setText(String.valueOf(zona.getCapacidad()));
        zonaPrecioField.setText(String.valueOf((int) zona.getPrecioBase()));
    }

    private void cargarZonasAsiento() {
        Evento evento = eventoAsientosComboBox.getValue();
        if (evento == null) {
            zonaAsientosComboBox.getItems().clear();
            asientosListView.getItems().clear();
            return;
        }
        zonaAsientosComboBox.setItems(FXCollections.observableArrayList(evento.getRecinto().getZonas()));
        zonaAsientosComboBox.getSelectionModel().clearSelection();
        asientosListView.getItems().clear();
    }

    private void cambiarEstadoEvento(String accion) {
        Evento evento = eventosListView.getSelectionModel().getSelectedItem();
        if (evento == null) {
            AlertUtil.mostrarInfo("Eventos", "Selecciona un evento.");
            return;
        }
        switch (accion) {
            case "PUBLICAR" -> MainApp.getInstancia().getEventoService().publicarEvento(evento);
            case "PAUSAR" -> MainApp.getInstancia().getEventoService().pausarEvento(evento);
            case "CANCELAR" -> MainApp.getInstancia().getEventoService().cancelarEvento(evento);
            default -> {
            }
        }
        eventosListView.refresh();
        actualizarMetricas();
    }

    private Recinto crearRecintoBasico(String ciudad) {
        Recinto recinto = new Recinto("R-" + UUID.randomUUID().toString().substring(0, 5), "Recinto nuevo", "Direccion por definir", ciudad);
        Zona vip = new Zona(recinto.getIdRecinto() + "-Z1", "VIP", 12, 95000);
        Zona pref = new Zona(recinto.getIdRecinto() + "-Z2", "Preferencial", 12, 70000);
        Zona gen = new Zona(recinto.getIdRecinto() + "-Z3", "General", 24, 45000);
        recinto.agregarZona(vip);
        recinto.agregarZona(pref);
        recinto.agregarZona(gen);
        return recinto;
    }

    private void actualizarMetricas() {
        PanelMetricas metricas = MainApp.getInstancia().getAdminService().obtenerMetricasBasicas();
        totalVentasLabel.setText("$" + String.format("%.0f", metricas.getTotalVentas()));
        comprasLabel.setText(String.valueOf(metricas.getCantidadCompras()));
        ingresosServiciosLabel.setText("$" + String.format("%.0f", metricas.getIngresosServicios()));
        publicadosLabel.setText(String.valueOf(metricas.getCantidadEventosPublicados()));
        tasaCancelacionLabel.setText(String.format("%.0f%%", metricas.getTasaCancelacion()));
        StringBuilder ocupacion = new StringBuilder("Ocupacion por zona:\n");
        metricas.getOcupacionPorZona().forEach((nombre, valor) ->
                ocupacion.append(nombre).append(": ").append(String.format("%.0f%%", valor)).append("\n"));
        ocupacionLabel.setText(ocupacion.toString().trim());

        XYChart.Series<String, Number> serieVentas = new XYChart.Series<>();
        serieVentas.setName("Top eventos por ventas");
        metricas.getVentasPorEvento().forEach((evento, venta) ->
                serieVentas.getData().add(new XYChart.Data<>(evento, venta)));
        ventasBarChart.getData().setAll(serieVentas);

        XYChart.Series<String, Number> seriePeriodo = new XYChart.Series<>();
        seriePeriodo.setName("Ventas por periodo");
        metricas.getVentasPorPeriodo().forEach((periodo, venta) ->
                seriePeriodo.getData().add(new XYChart.Data<>(periodo, venta)));
        ventasLineChart.getData().setAll(seriePeriodo);

        ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList();
        metricas.getOcupacionPorZona().forEach((zona, valor) -> pieData.add(new PieChart.Data(zona, valor)));
        ocupacionPieChart.setData(pieData);
    }
}
