package co.edu.uniquindio.eventos.repository;

import co.edu.uniquindio.eventos.model.Administrador;
import co.edu.uniquindio.eventos.model.Asiento;
import co.edu.uniquindio.eventos.model.Compra;
import co.edu.uniquindio.eventos.model.Entrada;
import co.edu.uniquindio.eventos.model.Evento;
import co.edu.uniquindio.eventos.model.Incidencia;
import co.edu.uniquindio.eventos.model.Pago;
import co.edu.uniquindio.eventos.model.Recinto;
import co.edu.uniquindio.eventos.model.Usuario;
import co.edu.uniquindio.eventos.model.Zona;
import co.edu.uniquindio.eventos.patterns.behavioral.NotificacionAdministrador;
import co.edu.uniquindio.eventos.patterns.behavioral.NotificacionUsuario;
import co.edu.uniquindio.eventos.patterns.behavioral.Notificador;
import co.edu.uniquindio.eventos.patterns.behavioral.PagoNequi;
import co.edu.uniquindio.eventos.patterns.behavioral.PagoPSE;
import co.edu.uniquindio.eventos.patterns.behavioral.PagoTarjeta;
import co.edu.uniquindio.eventos.patterns.creational.EventoFactory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DatosPrueba {
    private static DatosPrueba instancia;

    private List<Usuario> usuarios;
    private Administrador administrador;
    private List<Evento> eventos;
    private List<Compra> compras;
    private List<Incidencia> incidencias;
    private Notificador notificador;

    private DatosPrueba() {
        this.usuarios = new ArrayList<>();
        this.eventos = new ArrayList<>();
        this.compras = new ArrayList<>();
        this.incidencias = new ArrayList<>();
        this.notificador = new Notificador();
        this.notificador.agregarObservador(new NotificacionUsuario());
        this.notificador.agregarObservador(new NotificacionAdministrador());
        cargar();
    }

    public static DatosPrueba getInstancia() {
        if (instancia == null) {
            instancia = new DatosPrueba();
        }
        return instancia;
    }

    private void cargar() {
        Usuario u1 = new Usuario("U01", "Mariana Rodriguez", "mariana@eventosuq.com", "3001234567", "1234");
        Usuario u2 = new Usuario("U02", "Sofia Aviles", "sofia@eventosuq.com", "3014567890", "1234");
        Usuario u3 = new Usuario("U03", "Juan Tellez", "juan@eventosuq.com", "3021112233", "1234");
        u1.agregarMetodoPago(new PagoTarjeta());
        u1.agregarMetodoPago(new PagoNequi());
        u2.agregarMetodoPago(new PagoPSE());
        u3.agregarMetodoPago(new PagoTarjeta());
        usuarios.add(u1);
        usuarios.add(u2);
        usuarios.add(u3);

        administrador = new Administrador("A01", "Admin Eventos", "admin@eventosuq.com", "3000000000", "admin123", "EMP-2026");

        eventos.add(EventoFactory.crearEvento("E01", "Noche de Rock", "Concierto",
                "Concierto universitario con bandas locales.", "Armenia",
                LocalDateTime.now().plusDays(4).withHour(19).withMinute(30),
                crearRecintoConAsientos("R01-E01", "Coliseo UQ", "Cra 15 #12N", "Armenia")));
        eventos.add(EventoFactory.crearEvento("E02", "Festival de Danza", "Festival",
                "Presentaciones de grupos juveniles y academias.", "Armenia",
                LocalDateTime.now().plusDays(6).withHour(18).withMinute(0),
                crearRecintoConAsientos("R01-E02", "Coliseo UQ", "Cra 15 #12N", "Armenia")));
        eventos.add(EventoFactory.crearEvento("E03", "Obra La Casa", "Teatro",
                "Obra corta de drama y humor para publico general.", "Armenia",
                LocalDateTime.now().plusDays(8).withHour(20).withMinute(0),
                crearRecintoConAsientos("R02-E03", "Teatro Quindio", "Av Bolivar #20N", "Armenia")));
        eventos.add(EventoFactory.crearEvento("E04", "Stand Up PGII", "Comedia",
                "Noche de comedia estudiantil.", "Pereira",
                LocalDateTime.now().plusDays(10).withHour(19).withMinute(0),
                crearRecintoConAsientos("R02-E04", "Teatro Quindio", "Av Bolivar #20N", "Pereira")));
        eventos.add(EventoFactory.crearEvento("E05", "Muestra Coral", "Cultural",
                "Encuentro coral con invitados regionales.", "Manizales",
                LocalDateTime.now().plusDays(12).withHour(17).withMinute(30),
                crearRecintoConAsientos("R01-E05", "Coliseo UQ", "Cra 15 #12N", "Manizales")));
        eventos.get(3).publicar();
        eventos.get(4).publicar();

        Compra compra1 = new Compra("COM-001", u1, eventos.get(0));
        Zona zonaGeneral = eventos.get(0).getRecinto().getZonas().get(2);
        Asiento asiento1 = zonaGeneral.getAsientos().get(0);
        asiento1.reservar();
        compra1.agregarEntrada(new Entrada("ENT-001", zonaGeneral, asiento1, zonaGeneral.getPrecioBase()));
        compra1.pagar(new Pago("PAG-001", compra1.calcularTotal(), new PagoTarjeta()));
        compra1.confirmar();
        asiento1.vender();
        compras.add(compra1);
        u1.agregarCompra(compra1);

        Compra compra2 = new Compra("COM-002", u2, eventos.get(1));
        Zona zonaVip = eventos.get(1).getRecinto().getZonas().get(0);
        Asiento asiento2 = zonaVip.getAsientos().get(1);
        asiento2.reservar();
        compra2.agregarEntrada(new Entrada("ENT-002", zonaVip, asiento2, zonaVip.getPrecioBase()));
        compra2.pagar(new Pago("PAG-002", compra2.calcularTotal(), new PagoTarjeta()));
        asiento2.vender();
        compras.add(compra2);
        u2.agregarCompra(compra2);

        incidencias.add(new Incidencia("INC-001", "Silla bloqueada", "Asiento VIP en mantenimiento", eventos.get(0).getNombre()));
        incidencias.add(new Incidencia("INC-002", "Cambio de horario", "Revisar actualizacion del evento cultural", eventos.get(4).getNombre()));
    }

    private Recinto crearRecintoConAsientos(String idRecinto, String nombre, String direccion, String ciudad) {
        Recinto recinto = new Recinto(idRecinto, nombre, direccion, ciudad);
        Zona vip = new Zona(recinto.getIdRecinto() + "-Z1", "VIP", 12, 95000);
        Zona preferencial = new Zona(recinto.getIdRecinto() + "-Z2", "Preferencial", 12, 70000);
        Zona general = new Zona(recinto.getIdRecinto() + "-Z3", "General", 24, 45000);
        llenarAsientos(vip, "A", 12);
        llenarAsientos(preferencial, "B", 12);
        llenarAsientos(general, "C", 24);
        recinto.agregarZona(vip);
        recinto.agregarZona(preferencial);
        recinto.agregarZona(general);
        return recinto;
    }

    private void llenarAsientos(Zona zona, String fila, int total) {
        for (int i = 1; i <= total; i++) {
            zona.agregarAsiento(new Asiento(zona.getIdZona() + "-A" + i, fila, i));
        }
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public Administrador getAdministrador() {
        return administrador;
    }

    public List<Recinto> getRecintos() {
        return eventos.stream()
                .map(Evento::getRecinto)
                .distinct()
                .toList();
    }

    public List<Evento> getEventos() {
        return eventos;
    }

    public List<Compra> getCompras() {
        return compras;
    }

    public List<Incidencia> getIncidencias() {
        return incidencias;
    }

    public Notificador getNotificador() {
        return notificador;
    }
}
