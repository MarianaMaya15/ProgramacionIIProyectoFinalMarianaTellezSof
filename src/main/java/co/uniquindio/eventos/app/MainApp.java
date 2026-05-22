package co.edu.uniquindio.eventos.app;

import co.edu.uniquindio.eventos.Navigator;
import co.edu.uniquindio.eventos.controller.MainController;
import co.edu.uniquindio.eventos.model.Compra;
import co.edu.uniquindio.eventos.model.Entrada;
import co.edu.uniquindio.eventos.model.Evento;
import co.edu.uniquindio.eventos.model.SesionActual;
import co.edu.uniquindio.eventos.model.Usuario;
import co.edu.uniquindio.eventos.repository.DatosPrueba;
import co.edu.uniquindio.eventos.service.AdminService;
import co.edu.uniquindio.eventos.service.CompraService;
import co.edu.uniquindio.eventos.service.EventoService;
import co.edu.uniquindio.eventos.service.PromocionService;
import co.edu.uniquindio.eventos.service.UsuarioService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApp extends Application {
    private static MainApp instancia;

    private Stage primaryStage;
    private MainController mainController;
    private EventoService eventoService;
    private CompraService compraService;
    private UsuarioService usuarioService;
    private PromocionService promocionService;
    private AdminService adminService;
    private Usuario usuarioActual;
    private Evento eventoSeleccionado;
    private Compra compraActual;

    @Override
    public void start(Stage stage) throws IOException {
        instancia = this;
        DatosPrueba.getInstancia();
        this.primaryStage = stage;
        this.eventoService = new EventoService();
        this.compraService = new CompraService();
        this.usuarioService = new UsuarioService();
        this.promocionService = new PromocionService();
        this.adminService = new AdminService();
        this.usuarioActual = null;
        this.primaryStage.setTitle("EVENTOS UQ");
        mostrarLogin();
        this.primaryStage.show();
    }

    public static MainApp getInstancia() {
        return instancia;
    }

    public void mostrarCartelera() {
        Navigator.irCartelera();
    }

    public void mostrarLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/login-view.fxml"));
            Scene scene = new Scene(loader.load(), 760, 520);
            scene.getStylesheets().add(getClass().getResource("/styles/styles.css").toExternalForm());
            primaryStage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void mostrarRegistro() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/registro-view.fxml"));
            Scene scene = new Scene(loader.load(), 760, 560);
            scene.getStylesheets().add(getClass().getResource("/styles/styles.css").toExternalForm());
            primaryStage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void iniciarFlujoPrincipal(Usuario usuario) {
        try {
            this.usuarioActual = usuario;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/main-view.fxml"));
            Scene scene = new Scene(loader.load(), 1200, 760);
            scene.getStylesheets().add(getClass().getResource("/styles/styles.css").toExternalForm());
            mainController = loader.getController();
            mainController.setMainApp(this);
            Navigator.inicializar(mainController);
            primaryStage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cerrarSesion() {
        SesionActual.cerrarSesion();
        this.usuarioActual = null;
        this.eventoSeleccionado = null;
        this.compraActual = null;
        mostrarLogin();
    }

    public void mostrarDetalleEvento(Evento evento) {
        this.eventoSeleccionado = evento;
        Navigator.irDetalleEvento(evento);
    }

    public void mostrarCompra(java.util.List<Entrada> entradasSeleccionadas) {
        this.compraActual = compraService.crearCompra(usuarioActual, eventoSeleccionado);
        for (Entrada entrada : entradasSeleccionadas) {
            compraService.agregarEntrada(compraActual, entrada.getZona(), entrada.getAsiento());
        }
        Navigator.irCompra(compraActual);
    }

    public void mostrarAdmin() {
        Navigator.irAdmin();
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public EventoService getEventoService() {
        return eventoService;
    }

    public CompraService getCompraService() {
        return compraService;
    }

    public UsuarioService getUsuarioService() {
        return usuarioService;
    }

    public PromocionService getPromocionService() {
        return promocionService;
    }

    public AdminService getAdminService() {
        return adminService;
    }

    public Usuario getUsuarioActual() {
        return usuarioActual;
    }

    public Evento getEventoSeleccionado() {
        return eventoSeleccionado;
    }

    public Compra getCompraActual() {
        return compraActual;
    }

    public void actualizarCompraActual(Compra compra) {
        this.compraActual = compra;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
