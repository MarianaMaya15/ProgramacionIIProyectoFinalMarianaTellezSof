package co.edu.uniquindio.eventos.controller;

import co.edu.uniquindio.eventos.Navigator;
import co.edu.uniquindio.eventos.app.MainApp;
import co.edu.uniquindio.eventos.model.Asiento;
import co.edu.uniquindio.eventos.model.Entrada;
import co.edu.uniquindio.eventos.model.Evento;
import co.edu.uniquindio.eventos.model.Zona;
import co.edu.uniquindio.eventos.model.enums.EstadoAsiento;
import co.edu.uniquindio.eventos.util.AlertUtil;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventoController {
    @FXML
    private Label nombreEventoLabel;
    @FXML
    private Label categoriaLabel;
    @FXML
    private Label ciudadLabel;
    @FXML
    private Label fechaLabel;
    @FXML
    private Label descripcionLabel;
    @FXML
    private Label recintoLabel;
    @FXML
    private Label politicasLabel;
    @FXML
    private VBox mapaZonasContainer;
    @FXML
    private Label eventoResumenLabel;
    @FXML
    private ListView<String> resumenEntradasListView;
    @FXML
    private Label cantidadEntradasLabel;
    @FXML
    private Label subtotalEntradasLabel;

    private Evento evento;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private final List<Entrada> entradasSeleccionadas = new ArrayList<>();
    private final Map<Asiento, Button> botonesAsientos = new HashMap<>();

    public void cargarEvento(Evento evento) {
        this.evento = evento;
        nombreEventoLabel.setText(evento.getNombre());
        categoriaLabel.setText("Categoria: " + evento.getCategoria());
        ciudadLabel.setText("Ciudad: " + evento.getCiudad());
        fechaLabel.setText("Fecha: " + evento.getFechaHora().format(formatter));
        descripcionLabel.setText(evento.getDescripcion());
        recintoLabel.setText("Recinto: " + evento.getRecinto().getNombre() + " - " + evento.getRecinto().getDireccion());
        politicasLabel.setText(evento.getPoliticaCancelacion() + " " + evento.getPoliticaReembolso());
        eventoResumenLabel.setText(evento.getNombre());
        construirMapaZonas();
        actualizarResumen();
    }

    private void construirMapaZonas() {
        mapaZonasContainer.getChildren().clear();
        botonesAsientos.clear();

        List<Zona> zonasOrdenadas = evento.getRecinto().getZonas().stream()
                .sorted(Comparator.comparingInt(this::ordenZona))
                .toList();

        for (Zona zona : zonasOrdenadas) {
            VBox tarjetaZona = new VBox(10);
            tarjetaZona.getStyleClass().addAll("detail-panel", claseZona(zona.getNombre()));

            Label tituloZona = new Label(zona.getNombre() + " - $" + String.format("%.0f", zona.getPrecioBase()));
            tituloZona.getStyleClass().add("panel-title");

            FlowPane grillaAsientos = new FlowPane();
            grillaAsientos.setHgap(8);
            grillaAsientos.setVgap(8);
            grillaAsientos.setPrefWrapLength(360);

            for (Asiento asiento : zona.getAsientos()) {
                Button btn = crearBotonAsiento(zona, asiento);
                botonesAsientos.put(asiento, btn);
                grillaAsientos.getChildren().add(btn);
            }

            tarjetaZona.getChildren().addAll(tituloZona, grillaAsientos);
            mapaZonasContainer.getChildren().add(tarjetaZona);
            VBox.setVgrow(tarjetaZona, Priority.NEVER);
        }
    }

    private Button crearBotonAsiento(Zona zona, Asiento asiento) {
        Button btn = new Button(String.format("%02d", asiento.getNumero()));
        btn.getStyleClass().addAll("seat-button", claseEstadoAsiento(asiento.getEstado()));
        btn.setMinSize(44, 34);
        btn.setPrefSize(44, 34);
        btn.setMaxSize(44, 34);
        btn.setOnAction(event -> alternarSeleccionAsiento(zona, asiento));
        return btn;
    }

    private void alternarSeleccionAsiento(Zona zona, Asiento asiento) {
        if (asiento.getEstado() != EstadoAsiento.DISPONIBLE) {
            AlertUtil.mostrarInfo("Asiento no disponible", "Este asiento esta " + asiento.getEstado().name().toLowerCase() + ".");
            return;
        }
        Entrada entradaExistente = buscarEntradaPorAsiento(asiento);
        if (entradaExistente != null) {
            entradasSeleccionadas.remove(entradaExistente);
            refrescarEstiloAsiento(asiento);
            actualizarResumen();
            return;
        }

        Entrada entrada = new Entrada("TEMP-" + asiento.getIdAsiento(), zona, asiento, zona.getPrecioBase());
        entradasSeleccionadas.add(entrada);
        refrescarEstiloAsiento(asiento);
        actualizarResumen();
    }

    private Entrada buscarEntradaPorAsiento(Asiento asiento) {
        return entradasSeleccionadas.stream()
                .filter(entrada -> entrada.getAsiento().getIdAsiento().equals(asiento.getIdAsiento()))
                .findFirst()
                .orElse(null);
    }

    private void refrescarEstiloAsiento(Asiento asiento) {
        Button btn = botonesAsientos.get(asiento);
        if (btn == null) {
            return;
        }
        btn.getStyleClass().removeAll("seat-available", "seat-reservado", "seat-vendido", "seat-bloqueado", "seat-seleccionado");
        if (buscarEntradaPorAsiento(asiento) != null) {
            btn.getStyleClass().add("seat-seleccionado");
        } else {
            btn.getStyleClass().add(claseEstadoAsiento(asiento.getEstado()));
        }
    }

    private String claseEstadoAsiento(EstadoAsiento estadoAsiento) {
        return switch (estadoAsiento) {
            case DISPONIBLE -> "seat-available";
            case RESERVADO -> "seat-reservado";
            case VENDIDO -> "seat-vendido";
            case BLOQUEADO -> "seat-bloqueado";
        };
    }

    private String claseZona(String nombreZona) {
        String zonaNormalizada = nombreZona.toLowerCase();
        if (zonaNormalizada.contains("vip")) {
            return "zona-vip";
        }
        if (zonaNormalizada.contains("prefer")) {
            return "zona-preferencial";
        }
        return "zona-general";
    }

    private int ordenZona(Zona zona) {
        String nombre = zona.getNombre().toLowerCase();
        if (nombre.contains("vip")) return 0;
        if (nombre.contains("prefer")) return 1;
        return 2;
    }

    private void actualizarResumen() {
        resumenEntradasListView.setItems(FXCollections.observableArrayList(
                entradasSeleccionadas.stream().map(Entrada::toString).toList()
        ));
        cantidadEntradasLabel.setText(String.valueOf(entradasSeleccionadas.size()));
        double subtotal = entradasSeleccionadas.stream().mapToDouble(Entrada::getPrecioFinal).sum();
        subtotalEntradasLabel.setText("$" + String.format("%.0f", subtotal));
    }

    @FXML
    public void continuarCompra() {
        if (entradasSeleccionadas.isEmpty()) {
            AlertUtil.mostrarInfo("Seleccion incompleta", "Selecciona al menos un asiento disponible.");
            return;
        }
        MainApp.getInstancia().mostrarCompra(new ArrayList<>(entradasSeleccionadas));
    }

    @FXML
    public void limpiarSeleccion() {
        entradasSeleccionadas.clear();
        for (Asiento asiento : botonesAsientos.keySet()) {
            refrescarEstiloAsiento(asiento);
        }
        actualizarResumen();
    }

    @FXML
    public void volverCartelera() {
        Navigator.irCartelera();
    }
}
