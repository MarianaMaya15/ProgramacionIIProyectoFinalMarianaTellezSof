package co.uniquindio.eventos.controller;

import co.edu.uniquindio.eventos.Navigator;
import co.edu.uniquindio.eventos.app.MainApp;
import co.edu.uniquindio.eventos.model.Administrador;
import co.edu.uniquindio.eventos.model.Compra;
import co.edu.uniquindio.eventos.model.Evento;
import co.edu.uniquindio.eventos.model.Promocion;
import co.edu.uniquindio.eventos.model.SesionActual;
import co.edu.uniquindio.eventos.patterns.structural.AccesoPreferencial;
import co.edu.uniquindio.eventos.patterns.structural.Merchandising;
import co.edu.uniquindio.eventos.patterns.structural.Parqueadero;
import co.edu.uniquindio.eventos.patterns.structural.SeguroCancelacion;
import co.edu.uniquindio.eventos.patterns.structural.ServicioAdicional;
import co.edu.uniquindio.eventos.patterns.structural.ServicioBase;
import co.edu.uniquindio.eventos.patterns.structural.ServicioVIP;
import co.edu.uniquindio.eventos.repository.DatosPrueba;
import co.edu.uniquindio.eventos.util.AlertUtil;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class MainController {
    @FXML
    private StackPane contenidoPane;
    @FXML
    private Label usuarioLabel;
    @FXML
    private Button adminButton;
    @FXML
    private Label notificacionLabel;

    private MainApp mainApp;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        refrescarUsuarioLabel();
        configurarMenuPorRol();
        cargarCartelera();
    }

    @FXML
    public void mostrarCartelera() {
        Navigator.irCartelera();
    }

    public void cargarCartelera() {
        VBox root = new VBox(18);
        root.getStyleClass().add("section-wrapper");
        Label titulo = new Label("Estrenos / Cartelera");
        titulo.getStyleClass().add("section-title");
        Label subtitulo = new Label("Eventos destacados con una vista inspirada en cartelera.");
        subtitulo.getStyleClass().add("section-subtitle");

        FlowPane cards = new FlowPane();
        cards.setHgap(18);
        cards.setVgap(18);
        cards.setPadding(new Insets(10, 0, 10, 0));

        for (Evento evento : mainApp.getEventoService().listarEventosPublicados()) {
            cards.getChildren().add(crearTarjetaEvento(evento));
        }

        root.getChildren().addAll(titulo, subtitulo, cards);
        contenidoPane.getChildren().setAll(root);
        actualizarNotificacion();
    }

    private VBox crearTarjetaEvento(Evento evento) {
        VBox card = new VBox(10);
        card.getStyleClass().add("evento-card");
        card.setPrefWidth(250);

        VBox poster = new VBox();
        poster.getStyleClass().add("poster-box");
        poster.setPrefHeight(140);
        Label posterText = new Label(evento.getCategoria().toUpperCase());
        posterText.getStyleClass().add("poster-text");
        poster.getChildren().add(posterText);

        Label nombre = new Label(evento.getNombre());
        nombre.getStyleClass().add("card-title");
        Label ciudad = new Label(evento.getCiudad() + " | " + evento.getCategoria());
        ciudad.getStyleClass().add("card-text");
        Label fecha = new Label(evento.getFechaHora().format(formatter));
        fecha.getStyleClass().add("card-text");
        Label precio = new Label("Desde $" + String.format("%.0f", evento.getPrecioDesde()));
        precio.getStyleClass().add("card-price");
        Button comprar = new Button("Comprar");
        comprar.getStyleClass().add("primary-button");
        comprar.setOnAction(eventoClick -> abrirDetalleEvento(evento));

        card.getChildren().addAll(poster, nombre, ciudad, fecha, precio, comprar);
        return card;
    }

    private void abrirDetalleEvento(Evento eventoSeleccionado) {
        if (eventoSeleccionado == null) {
            AlertUtil.mostrarInfo("Error", "No se pudo abrir el detalle: el evento seleccionado es nulo.");
            return;
        }
        mainApp.mostrarDetalleEvento(eventoSeleccionado);
    }

    public void cargarDetalleEvento(Evento evento) {
        if (evento == null) {
            AlertUtil.mostrarInfo("Error", "No se pudo abrir el detalle: no hay evento seleccionado.");
            return;
        }
        try {
            FXMLLoader loader = Navigator.loadLoader("detalle-evento-view.fxml");
            Parent vista = loader.load();
            EventoController controller = loader.getController();
            controller.cargarEvento(evento);
            contenidoPane.getChildren().setAll(vista);
            actualizarNotificacion();
        } catch (Exception e) {
            e.printStackTrace();
            AlertUtil.mostrarInfo("Error", "No fue posible abrir el detalle del evento.");
        }
    }

    public void cargarDetalleEventoActual() {
        if (mainApp.getEventoSeleccionado() != null) {
            cargarDetalleEvento(mainApp.getEventoSeleccionado());
        } else {
            cargarCartelera();
        }
    }

    public void cargarCompra(Compra compra) {
        try {
            FXMLLoader loader = Navigator.loadLoader("compra-view.fxml");
            Parent vista = loader.load();
            CompraController controller = loader.getController();
            controller.cargarCompra(compra);
            contenidoPane.getChildren().setAll(vista);
            actualizarNotificacion();
        } catch (IOException e) {
            AlertUtil.mostrarInfo("Error", "No fue posible abrir la vista de compra.");
        }
    }

    public void cargarAdmin() {
        try {
            FXMLLoader loader = Navigator.loadLoader("admin-view.fxml");
            Parent vista = loader.load();
            AdminController controller = loader.getController();
            controller.cargarDatos();
            contenidoPane.getChildren().setAll(vista);
            actualizarNotificacion();
        } catch (IOException e) {
            AlertUtil.mostrarInfo("Error", "No fue posible abrir el panel de administrador.");
        }
    }

    @FXML
    public void mostrarPerfil() {
        try {
            FXMLLoader loader = Navigator.loadLoader("perfil-view.fxml");
            Parent vista = loader.load();
            PerfilController controller = loader.getController();
            controller.cargarUsuario(mainApp.getUsuarioActual());
            contenidoPane.getChildren().setAll(vista);
            actualizarNotificacion();
        } catch (Exception e) {
            e.printStackTrace();
            AlertUtil.mostrarInfo("Error", "No fue posible abrir el perfil.");
        }
    }

    @FXML
    public void mostrarHistorial() {
        try {
            FXMLLoader loader = Navigator.loadLoader("historial-view.fxml");
            Parent vista = loader.load();
            HistorialController controller = loader.getController();
            controller.cargarUsuario(mainApp.getUsuarioActual());
            contenidoPane.getChildren().setAll(vista);
            actualizarNotificacion();
        } catch (Exception e) {
            e.printStackTrace();
            AlertUtil.mostrarInfo("Error", "No fue posible abrir el historial.");
        }
    }

    @FXML
    public void mostrarServicios() {
        VBox box = new VBox(16);
        box.getStyleClass().add("section-wrapper");
        Label titulo = new Label("Servicios adicionales");
        titulo.getStyleClass().add("section-title");
        Label texto = new Label("Conoce los servicios disponibles. Se agregan cuando estes en la pantalla de compra.");
        texto.getStyleClass().add("section-subtitle");

        FlowPane cards = new FlowPane();
        cards.setHgap(16);
        cards.setVgap(16);

        List<ServicioAdicional> servicios = List.of(
                new ServicioVIP(new ServicioBase()),
                new SeguroCancelacion(new ServicioBase()),
                new Parqueadero(new ServicioBase()),
                new AccesoPreferencial(new ServicioBase()),
                new Merchandising(new ServicioBase())
        );
        List<String> condiciones = List.of(
                "Ideal cuando quieres experiencia premium.",
                "Util cuando exista riesgo de cambio.",
                "Recomendado para eventos en horario nocturno.",
                "Cuando buscas ingreso agil al recinto.",
                "Si quieres recuerdos oficiales del evento."
        );
        for (int i = 0; i < servicios.size(); i++) {
            cards.getChildren().add(crearTarjetaServicio(servicios.get(i), condiciones.get(i)));
        }

        box.getChildren().addAll(titulo, texto, cards);
        contenidoPane.getChildren().setAll(box);
        actualizarNotificacion();
    }

    @FXML
    public void mostrarPromociones() {
        VBox box = new VBox(16);
        box.getStyleClass().add("section-wrapper");
        Label titulo = new Label("Promociones");
        titulo.getStyleClass().add("section-title");
        Label texto = new Label("Promociones activas para tus compras de entradas.");
        texto.getStyleClass().add("section-subtitle");
        FlowPane cards = new FlowPane();
        cards.setHgap(16);
        cards.setVgap(16);

        for (Promocion promocion : mainApp.getPromocionService().listarPromociones()) {
            cards.getChildren().add(crearTarjetaPromocion(promocion));
        }

        box.getChildren().addAll(titulo, texto, cards);
        contenidoPane.getChildren().setAll(box);
        actualizarNotificacion();
    }

    private VBox crearTarjetaServicio(ServicioAdicional servicio, String condicion) {
        VBox card = new VBox(10);
        card.getStyleClass().add("evento-card");
        card.setPrefWidth(280);

        Label nombre = new Label(servicio.getNombre().replace("Base, ", ""));
        nombre.getStyleClass().add("card-title");
        Label descripcion = new Label(servicio.getDescripcion().replace("Compra sin adicionales + ", ""));
        descripcion.setWrapText(true);
        descripcion.getStyleClass().add("card-text");
        Label precio = new Label("Precio: $" + String.format("%.0f", servicio.getCosto()));
        precio.getStyleClass().add("card-price");
        Label aplica = new Label("Cuando aplica: " + condicion);
        aplica.setWrapText(true);
        aplica.getStyleClass().add("card-text");
        Button irCompra = new Button("Ver en compra");
        irCompra.getStyleClass().add("primary-button");
        irCompra.setOnAction(e -> Navigator.irCartelera());

        card.getChildren().addAll(nombre, descripcion, precio, aplica, irCompra);
        return card;
    }

    private VBox crearTarjetaPromocion(Promocion promocion) {
        VBox card = new VBox(10);
        card.getStyleClass().add("evento-card");
        card.setPrefWidth(280);

        Label nombre = new Label(promocion.getNombre());
        nombre.getStyleClass().add("card-title");
        Label descripcion = new Label(promocion.getDescripcion());
        descripcion.setWrapText(true);
        descripcion.getStyleClass().add("card-text");
        Label porcentaje = new Label("Descuento: " + String.format("%.0f%%", promocion.getPorcentajeDescuento() * 100));
        porcentaje.getStyleClass().add("card-price");
        Label condicion = new Label("Condicion: " + promocion.getCondicion());
        condicion.setWrapText(true);
        condicion.getStyleClass().add("card-text");
        Label estado = new Label(promocion.estaActiva() ? "Estado: ACTIVA" : "Estado: INACTIVA");
        estado.getStyleClass().add("detail-chip");
        Button verEventos = new Button("Ver eventos");
        verEventos.getStyleClass().add("primary-button");
        verEventos.setOnAction(e -> Navigator.irCartelera());

        card.getChildren().addAll(nombre, descripcion, porcentaje, condicion, estado, verEventos);
        return card;
    }

    @FXML
    public void mostrarAdmin() {
        if (!SesionActual.esAdministrador()) {
            AlertUtil.mostrarInfo("Permisos", "No tiene permisos para acceder al modo administrador.");
            return;
        }
        Navigator.irAdmin();
    }

    @FXML
    public void cerrarSesion() {
        mainApp.cerrarSesion();
    }

    private void actualizarNotificacion() {
        refrescarUsuarioLabel();
        String mensaje = DatosPrueba.getInstancia().getNotificador().getUltimoMensaje();
        notificacionLabel.setText(mensaje == null ? "Sin notificaciones nuevas." : mensaje);
    }

    private void refrescarUsuarioLabel() {
        if (SesionActual.esAdministrador()) {
            usuarioLabel.setText("Administrador: " + mainApp.getUsuarioActual().getNombreCompleto());
        } else {
            usuarioLabel.setText("Usuario: " + mainApp.getUsuarioActual().getNombreCompleto());
        }
    }

    private void configurarMenuPorRol() {
        boolean esAdmin = mainApp.getUsuarioActual() instanceof Administrador;
        adminButton.setVisible(esAdmin);
        adminButton.setManaged(esAdmin);
    }
}
