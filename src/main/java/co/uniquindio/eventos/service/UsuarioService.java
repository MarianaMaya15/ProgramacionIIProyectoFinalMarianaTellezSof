package co.edu.uniquindio.eventos.service;

import co.edu.uniquindio.eventos.model.Usuario;
import co.edu.uniquindio.eventos.repository.DatosPrueba;

import java.util.List;

public class UsuarioService {
    public Usuario registrarUsuario(Usuario usuario) {
        if (buscarPorCorreo(usuario.getCorreo()) != null) {
            return null;
        }
        DatosPrueba.getInstancia().getUsuarios().add(usuario);
        return usuario;
    }

    public void actualizarUsuario(String idUsuario, String nombre, String correo, String telefono) {
        Usuario usuario = listarUsuarios().stream()
                .filter(u -> u.getIdUsuario().equals(idUsuario))
                .findFirst()
                .orElse(null);
        if (usuario != null) {
            usuario.actualizarPerfil(nombre, correo, telefono);
        }
    }

    public void eliminarUsuario(String idUsuario) {
        DatosPrueba.getInstancia().getUsuarios().removeIf(usuario -> usuario.getIdUsuario().equals(idUsuario));
    }

    public Usuario buscarPorCorreo(String correo) {
        Usuario usuario = DatosPrueba.getInstancia().getUsuarios().stream()
                .filter(u -> u.getCorreo().equalsIgnoreCase(correo))
                .findFirst()
                .orElse(null);
        if (usuario != null) {
            return usuario;
        }
        if (DatosPrueba.getInstancia().getAdministrador().getCorreo().equalsIgnoreCase(correo)) {
            return DatosPrueba.getInstancia().getAdministrador();
        }
        return null;
    }

    public Usuario iniciarSesion(String correo, String contrasena) {
        Usuario usuario = buscarPorCorreo(correo);
        if (usuario == null) {
            return null;
        }
        if (usuario.getContrasena() == null) {
            return null;
        }
        if (!usuario.getContrasena().equals(contrasena)) {
            return null;
        }
        return usuario;
    }

    public List<Usuario> listarUsuarios() {
        return DatosPrueba.getInstancia().getUsuarios();
    }

    public Usuario obtenerUsuarioSimulado() {
        return listarUsuarios().get(0);
    }
}
