package co.edu.uniquindio.eventos.controller;

import co.edu.uniquindio.eventos.Navigator;
import co.edu.uniquindio.eventos.app.MainApp;
import co.edu.uniquindio.eventos.model.Usuario;
import co.edu.uniquindio.eventos.util.AlertUtil;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class PerfilController {
    @FXML
    private TextField nombreField;
    @FXML
    private TextField correoField;
    @FXML
    private TextField telefonoField;

    public void cargarUsuario(Usuario usuario) {
        nombreField.setText(usuario.getNombreCompleto());
        correoField.setText(usuario.getCorreo());
        telefonoField.setText(usuario.getTelefono());
    }

    @FXML
    public void guardarPerfil() {
        String nombre = nombreField.getText();
        String correo = correoField.getText();
        String telefono = telefonoField.getText();
        if (nombre == null || nombre.isBlank() || correo == null || correo.isBlank() || telefono == null || telefono.isBlank()) {
            AlertUtil.mostrarInfo("Datos incompletos", "Nombre, correo y telefono son obligatorios.");
            return;
        }
        Usuario actual = MainApp.getInstancia().getUsuarioActual();
        MainApp.getInstancia().getUsuarioService().actualizarUsuario(actual.getIdUsuario(), nombre, correo, telefono);
        AlertUtil.mostrarInfo("Perfil", "Perfil actualizado correctamente.");
        Navigator.irCartelera();
    }

    @FXML
    public void volverCartelera() {
        Navigator.irCartelera();
    }
}
