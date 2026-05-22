package co.edu.uniquindio.eventos.controller;

import co.edu.uniquindio.eventos.app.MainApp;
import co.edu.uniquindio.eventos.model.Usuario;
import co.edu.uniquindio.eventos.util.AlertUtil;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField; 

import java.util.UUID;

public class RegistroController {
    @FXML
    private TextField nombreField;
    @FXML
    private TextField correoField;
    @FXML
    private TextField telefonoField;
    @FXML
    private PasswordField contrasenaField;
    @FXML
    private PasswordField confirmarContrasenaField;

    @FXML
    public void registrar() {
        String nombre = nombreField.getText();
        String correo = correoField.getText();
        String telefono = telefonoField.getText();
        String contrasena = contrasenaField.getText();
        String confirmar = confirmarContrasenaField.getText();

        if (nombre == null || nombre.isBlank() || correo == null || correo.isBlank()
                || telefono == null || telefono.isBlank() || contrasena == null || contrasena.isBlank()
                || confirmar == null || confirmar.isBlank()) {
            AlertUtil.mostrarInfo("Registro", "Completa todos los campos.");
            return;
        }
        if (!contrasena.equals(confirmar)) {
            AlertUtil.mostrarInfo("Registro", "Las contrasenas no coinciden.");
            return;
        }
        if (MainApp.getInstancia().getUsuarioService().buscarPorCorreo(correo) != null) {
            AlertUtil.mostrarInfo("Registro", "El correo ya esta registrado.");
            return;
        }
        Usuario usuario = new Usuario("U-" + UUID.randomUUID().toString().substring(0, 5), nombre, correo, telefono, contrasena);
        MainApp.getInstancia().getUsuarioService().registrarUsuario(usuario);
        AlertUtil.mostrarInfo("Registro", "Usuario registrado correctamente.");
        MainApp.getInstancia().mostrarLogin();
    }

    @FXML
    public void volverLogin() {
        MainApp.getInstancia().mostrarLogin();
    }
}
