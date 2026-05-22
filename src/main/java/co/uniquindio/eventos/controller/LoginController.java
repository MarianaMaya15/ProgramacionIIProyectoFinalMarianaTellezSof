package co.uniquindio.eventos.controller;

import co.edu.uniquindio.eventos.app.MainApp;
import co.edu.uniquindio.eventos.model.SesionActual;
import co.edu.uniquindio.eventos.model.Usuario;
import co.edu.uniquindio.eventos.util.AlertUtil;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {
    @FXML
    private TextField correoField;
    @FXML
    private PasswordField contrasenaField;

    @FXML
    public void iniciarSesion() {
        String correo = correoField.getText();
        String contrasena = contrasenaField.getText();
        if (correo == null || correo.isBlank() || contrasena == null || contrasena.isBlank()) {
            AlertUtil.mostrarInfo("Login", "Ingresa correo y contrasena.");
            return;
        }
        Usuario usuario = MainApp.getInstancia().getUsuarioService().iniciarSesion(correo, contrasena);
        if (usuario == null) {
            AlertUtil.mostrarInfo("Login", "Credenciales invalidas.");
            return;
        }
        SesionActual.iniciarSesion(usuario);
        MainApp.getInstancia().iniciarFlujoPrincipal(usuario);
    }

    @FXML
    public void irRegistro() {
        MainApp.getInstancia().mostrarRegistro();
    }
}
