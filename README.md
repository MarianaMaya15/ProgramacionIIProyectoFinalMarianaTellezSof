# Proyecto Final PGII

## Plataforma de Gestion de Eventos y Venta de Entradas

Aplicacion de escritorio construida en `Java 17`, `JavaFX 17` y `Maven` para administrar eventos, recintos, zonas, asientos, compras, pagos e incidencias. El sistema incluye flujo para usuarios compradores y un panel administrativo con metricas operativas, gestion de disponibilidad y generacion de reportes.

## Integrantes

- Juan Jose Tellez Sanchez
- Sofia Aviles Diaz
- Mariana Rodriguez Maya

## Diagrama de clases

![Diagrama de clases](./Diagrama_Clases_Eventos_UQ.png)

## Descripcion breve del proyecto

El proyecto modela una plataforma de eventos donde un usuario puede iniciar sesion, consultar la cartelera, seleccionar entradas, agregar servicios adicionales, pagar y consultar su historial de compras. En paralelo, el administrador puede crear y actualizar eventos, gestionar recintos, zonas y asientos, registrar incidencias y consultar metricas de ventas, ocupacion y cancelaciones.

## Tecnologias

- Java 17
- JavaFX 17.0.10
- Maven
- FXML + CSS
- JUnit 5

## Estructura general del proyecto

- `src/main/java/co/edu/uniquindio/eventos/model`: entidades principales del sistema.
- `src/main/java/co/edu/uniquindio/eventos/model/enums`: enumeraciones de estados.
- `src/main/java/co/edu/uniquindio/eventos/service`: logica principal de la aplicacion.
- `src/main/java/co/edu/uniquindio/eventos/controller`: controladores de las vistas JavaFX.
- `src/main/java/co/edu/uniquindio/eventos/patterns`: implementacion de los patrones de diseno.
- `src/main/java/co/edu/uniquindio/eventos/repository/DatosPrueba.java`: datos iniciales de prueba.
- `src/main/resources/views`: vistas FXML.
- `src/main/resources/styles`: estilos CSS.

## Compilacion y ejecucion

### Guia de ejecucion en IntelliJ IDEA

La forma recomendada de ejecutar este proyecto es desde `IntelliJ IDEA`, ya que el entorno reconoce automaticamente la estructura Maven y facilita la configuracion de JavaFX.

### Requisitos para ejecutar

Para ejecutar el proyecto se necesita:

- IntelliJ IDEA Community o Ultimate.
- JDK 17 o superior.
- Maven configurado en el proyecto.
- JavaFX configurado mediante las dependencias del `pom.xml`.

### Como abrir el proyecto en IntelliJ IDEA

1. Abrir IntelliJ IDEA.
2. Seleccionar `Open`.
3. Buscar la carpeta raiz del proyecto.
4. Seleccionar la carpeta donde esta el archivo `pom.xml`.
5. Esperar a que IntelliJ cargue las dependencias de Maven.
6. Verificar que el proyecto reconozca el JDK.

### Configurar el JDK

En IntelliJ IDEA:

1. Ir a `File`.
2. Entrar a `Project Structure`.
3. En `Project SDK`, seleccionar `JDK 17` o superior.
4. Aplicar los cambios.

Si IntelliJ muestra un aviso como `JDK missing`, se debe seleccionar un JDK instalado o descargar uno desde el mismo IntelliJ.

### Configurar la clase principal

Para ejecutar la aplicacion correctamente:

1. Ir a la parte superior derecha de IntelliJ.
2. Abrir el menu de configuraciones.
3. Seleccionar `Edit Configurations`.
4. Presionar el boton `+`.
5. Elegir `Application`.
6. En `Main class`, colocar:

```text
co.edu.uniquindio.eventos.app.Launcher
```

7. En `Working directory`, dejar la carpeta raiz del proyecto.
8. Guardar la configuracion con `Apply` y luego `OK`.
9. Presionar el boton verde de ejecutar.

### Clase principal del proyecto

La clase que se debe ejecutar es:

```text
co.edu.uniquindio.eventos.app.Launcher
```

Esta clase llama internamente a `MainApp`.

No se recomienda ejecutar directamente `MainApp`, porque en algunos entornos JavaFX puede generar errores relacionados con el runtime.

### Ejecucion con Maven

Tambien se puede ejecutar desde consola con:

```bash
mvn clean compile
mvn javafx:run
```

Y si se desea correr las pruebas:

```bash
mvn test
```

## Datos de prueba inicializados

Los datos se cargan automaticamente desde `DatosPrueba.getInstancia()` al iniciar `MainApp`.

### Usuarios

- `U01` Mariana Rodriguez - `mariana@eventosuq.com` - clave `1234`
- `U02` Sofia Aviles - `sofia@eventosuq.com` - clave `1234`
- `U03` Juan Tellez - `juan@eventosuq.com` - clave `1234`

### Administrador

- `A01` Admin Eventos - `admin@eventosuq.com` - clave `admin123`

### Eventos iniciales

- `E01` Noche de Rock
- `E02` Festival de Danza
- `E03` Obra La Casa
- `E04` Stand Up PGII
- `E05` Muestra Coral

### Recintos, zonas y asientos

- Cada evento se crea con un recinto asociado.
- Cada recinto inicializa 3 zonas: `VIP`, `Preferencial` y `General`.
- Las capacidades base son `12`, `12` y `24` asientos respectivamente.
- Los asientos se generan automaticamente por zona en `DatosPrueba`.

### Compras y pagos de prueba

- `COM-001`: compra confirmada para Mariana con pago `PAG-001` por tarjeta.
- `COM-002`: compra pagada para Sofia con pago `PAG-002`.

### Incidencias iniciales

- `INC-001`: silla bloqueada.
- `INC-002`: cambio de horario.

## Reportes operativos

El sistema incluye generacion de reportes desde el historial del usuario:

- `ReporteCSV`: exporta el historial de compras a un archivo `.csv`.
- `ReportePDF`: genera un reporte PDF simulado en un archivo `.txt`.
- `ReporteExternoAdapter`: adapta un servicio externo de reporte plano al contrato interno `Reporte`.

Los reportes se disparan desde `HistorialController` y se generan en archivos temporales del sistema mediante `Files.createTempFile(...)`.

## Patrones de diseno implementados

### 1. Singleton

- Requisito que resuelve: control centralizado de disponibilidad y reservas de asientos.
- Problema: varias instancias administrando el mismo asiento podrian provocar estados inconsistentes.
- Proposito: garantizar un unico punto de acceso al gestor de reservas.
- Solucion: `GestorReservas` expone `getInstancia()` y concentra `reservarAsiento`, `liberarAsiento` y `venderAsiento`.

```java
public static GestorReservas getInstancia() {
    if (instancia == null) {
        instancia = new GestorReservas();
    }
    return instancia;
}
```

### 2. Factory Method

- Requisito que resuelve: creacion estandarizada de eventos.
- Problema: la logica de creacion y publicacion inicial del evento no debe repetirse en controladores y servicios.
- Proposito: encapsular la construccion de eventos segun su categoria.
- Solucion: `EventoFactory.crearEvento(...)` crea el objeto y decide si debe publicarse de inmediato.

### 3. Builder

- Requisito que resuelve: construccion progresiva de una compra.
- Problema: una compra se arma por pasos con datos basicos, entradas y servicios.
- Proposito: simplificar la construccion de objetos complejos.
- Solucion: `ReservaBuilder` permite encadenar `conDatosBasicos`, `agregarEntrada`, `agregarServicio` y `construir`.

### 4. Decorator

- Requisito que resuelve: adicion dinamica de beneficios a una compra.
- Problema: extras como VIP, seguro, parqueadero o merchandising no deben inflar la clase `Compra`.
- Proposito: agregar comportamiento y costo adicional sin modificar la clase base.
- Solucion: `ServicioBase` se envuelve con decoradores como `ServicioVIP`, `SeguroCancelacion`, `Parqueadero`, `AccesoPreferencial` y `Merchandising`.

### 5. Facade

- Requisito que resuelve: simplificacion del proceso de compra.
- Problema: comprar implica reservar asientos, crear entradas, calcular total, pagar, confirmar o cancelar.
- Proposito: ofrecer una interfaz de alto nivel para el flujo de compra.
- Solucion: `CompraFacade` coordina `GestorReservas`, `ReservaBuilder`, `Pago` y transiciones de la compra.

```java
public boolean pagarCompra(Compra compra, MetodoPago metodoPago) {
    Pago pago = new Pago("PAG-" + UUID.randomUUID().toString().substring(0, 5),
            compra.calcularTotal(), metodoPago);
    boolean pagado = compra.pagar(pago);
    if (pagado) {
        compra.getEntradas().forEach(entrada -> gestorReservas.venderAsiento(entrada.getAsiento()));
    }
    return pagado;
}
```

### 6. Adapter

- Requisito que resuelve: integracion con servicios externos de reporte.
- Problema: un proveedor externo no expone la misma interfaz usada dentro del sistema.
- Proposito: hacer compatible un servicio legado o externo con el contrato interno.
- Solucion: `ReporteExternoAdapter` implementa `Reporte` y delega en `ServicioReporteExterno`.

### 7. Strategy

- Requisito que resuelve: soporte para distintos metodos de pago.
- Problema: cada medio de pago tiene una forma distinta de procesar la transaccion.
- Proposito: intercambiar algoritmos de pago sin cambiar la clase `Pago`.
- Solucion: la interfaz `MetodoPago` es implementada por `PagoTarjeta`, `PagoPSE` y `PagoNequi`.

```java
public boolean procesarPago() {
    boolean aprobado = metodoPago.procesar(valor);
    estado = aprobado ? EstadoPago.APROBADO : EstadoPago.RECHAZADO;
    return aprobado;
}
```

### 8. Observer

- Requisito que resuelve: notificacion de cambios importantes.
- Problema: usuarios y administradores deben enterarse de incidencias o novedades sin acoplarse a cada modulo.
- Proposito: desacoplar el emisor de eventos de los receptores.
- Solucion: `Notificador` mantiene una lista de `Observador` y propaga mensajes a `NotificacionUsuario` y `NotificacionAdministrador`.

### 9. State

- Requisito que resuelve: manejo del ciclo de vida de la compra.
- Problema: una compra cambia su comportamiento segun el estado actual.
- Proposito: evitar condicionales extensos y mover la logica de transicion a objetos estado.
- Solucion: `Compra` delega en `EstadoCompraInterface` y clases concretas como `EstadoCreada`, `EstadoPagada`, `EstadoConfirmada`, `EstadoCancelada`, `EstadoReembolsada` y `EstadoIncidenciaCompra`.

## Principios SOLID aplicados

### S - Single Responsibility Principle

- `CompraService` coordina operaciones de compra, mientras `AdminService` concentra tareas administrativas y `UsuarioService` gestiona autenticacion y CRUD de usuarios.
- `ReporteCSV` y `ReportePDF` solo se ocupan de exportar reportes.

### O - Open/Closed Principle

- Para agregar un nuevo metodo de pago basta con crear otra implementacion de `MetodoPago`.
- Para agregar un nuevo tipo de reporte basta con implementar `Reporte`.
- Para agregar un nuevo servicio adicional basta con extender el esquema de decoradores.

### L - Liskov Substitution Principle

- `Administrador` puede usarse donde se espera un `Usuario`.
- `ReporteCSV`, `ReportePDF` y `ReporteExternoAdapter` sustituyen correctamente la abstraccion `Reporte`.
- `PagoTarjeta`, `PagoPSE` y `PagoNequi` sustituyen a `MetodoPago`.

### I - Interface Segregation Principle

- El proyecto usa interfaces pequenas y enfocadas: `MetodoPago`, `Reporte` y `Observador`.
- Cada cliente depende solo del contrato que realmente necesita.

### D - Dependency Inversion Principle

- `Usuario.descargarReporteCompras(Reporte reporte)` depende de la abstraccion `Reporte`, no de una clase concreta.
- `Pago` depende de `MetodoPago`, no de `PagoTarjeta`, `PagoPSE` o `PagoNequi`.
- `Compra` depende de `EstadoCompraInterface` para cambiar su comportamiento segun el estado.

## Flujo funcional resumido

1. El usuario inicia sesion desde `login-view.fxml`.
2. `MainApp` carga `DatosPrueba` y abre la cartelera.
3. El usuario selecciona evento, entradas y servicios adicionales.
4. `CompraService` delega el flujo a `CompraFacade`.
5. El pago se procesa con una estrategia concreta.
6. El historial permite ver detalle y descargar reportes.
7. El administrador puede crear eventos, gestionar zonas y asientos, registrar incidencias y revisar metricas.

## Historial de Git

Para la revision del proyecto se debe evidenciar:

- commits propios por integrante,
- commits frecuentes,
- mensajes descriptivos,
- trabajo en ramas individuales antes de integrar a `main`.

Comandos utiles para revisar el historial:

```bash
git log --oneline --graph --all
git branch -a
```

En el repositorio existen ramas remotas de trabajo y la revision final se apoyara en el historial de Git.
