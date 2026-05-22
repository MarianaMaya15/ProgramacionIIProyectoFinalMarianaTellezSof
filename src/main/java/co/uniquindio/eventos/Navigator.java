package co.edu.uniquindio.eventos;

import co.edu.uniquindio.eventos.controller.MainController;
import co.edu.uniquindio.eventos.model.Compra;
import co.edu.uniquindio.eventos.model.Evento;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayDeque;
import java.util.Deque;

public class Navigator {
    private static final String VIEW_PATH = "/views/";
    private static MainController mainController;
    private static final Deque<String> historial = new ArrayDeque<>();

    private Navigator() {
    }

    public static void inicializar(MainController controller) {
        mainController = controller;
    }

    public static Node loadView(String fxmlName) {
        try {
            URL url = Navigator.class.getResource(VIEW_PATH + fxmlName);
            if (url == null) {
                throw new IllegalArgumentException("No se encontro la vista: " + fxmlName);
            }
            return FXMLLoader.load(url);
        } catch (IOException e) {
            throw new RuntimeException("Error al cargar vista: " + fxmlName, e);
        }
    }

    public static FXMLLoader loadLoader(String fxmlName) {
        URL url = Navigator.class.getResource(VIEW_PATH + fxmlName);
        if (url == null) {
            throw new IllegalArgumentException("No se encontro la vista: " + fxmlName);
        }
        return new FXMLLoader(url);
    }

    public static void irCartelera() {
        historial.clear();
        mainController.cargarCartelera();
    }

    public static void irDetalleEvento(Evento evento) {
        guardarPaso("cartelera");
        mainController.cargarDetalleEvento(evento);
    }

    public static void irCompra(Compra compra) {
        guardarPaso("detalle");
        mainController.cargarCompra(compra);
    }

    public static void irAdmin() {
        guardarPaso("cartelera");
        mainController.cargarAdmin();
    }

    public static void volver() {
        if (historial.isEmpty()) {
            mainController.cargarCartelera();
            return;
        }
        String destino = historial.pop();
        switch (destino) {
            case "detalle" -> mainController.cargarDetalleEventoActual();
            default -> mainController.cargarCartelera();
        }
    }

    private static void guardarPaso(String vista) {
        if (historial.isEmpty() || !historial.peek().equals(vista)) {
            historial.push(vista);
        }
    }
}
