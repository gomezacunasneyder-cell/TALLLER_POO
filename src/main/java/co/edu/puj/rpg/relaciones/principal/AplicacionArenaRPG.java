package co.edu.puj.rpg.relaciones.principal;

import co.edu.puj.rpg.relaciones.modelo.Arma;
import co.edu.puj.rpg.relaciones.modelo.Escuadron;
import co.edu.puj.rpg.relaciones.modelo.Gremio;
import co.edu.puj.rpg.relaciones.modelo.Personaje;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.function.Function;

/**
 * Menú interactivo de la Arena de Héroes RPG (sección 4). Gestiona personajes, gremios,
 * escuadrones y armas con un ciclo do-while respaldado por un switch.
 */
public class AplicacionArenaRPG {

    private static final int NIVEL_MAXIMO = 99;

    private final Scanner scanner;
    // Colecciones globales: personajes libres, gremios y escuadrones
    private final List<Personaje> personajes = new ArrayList<>();
    private final List<Gremio> gremios = new ArrayList<>();
    private final List<Escuadron> escuadrones = new ArrayList<>();

    public AplicacionArenaRPG(Scanner scanner) {
        this.scanner = scanner;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        new AplicacionArenaRPG(scanner).ejecutar();
        scanner.close();
    }

    public void ejecutar() {
        int opcion;
        do {
            mostrarMenu();
            opcion = leerEntero();

            switch (opcion) {
                case 1:
                    crearPersonaje();
                    break;
                case 2:
                    gestionarGremios();
                    break;
                case 3:
                    gestionarEscuadrones();
                    break;
                case 4:
                    gestionarArmas();
                    break;
                case 5:
                    mostrarEstadoDetallado();
                    break;
                case 6:
                    System.out.printf("Cerrando la Arena de Héroes...%n");
                    break;
                default:
                    System.out.printf("Opción inválida.%n");
            }
        } while (opcion != 6);
    }

    private void mostrarMenu() {
        System.out.printf("%n=== MENÚ ARENA DE HÉROES RPG ===%n");
        System.out.printf("1. Crear Personaje%n");
        System.out.printf("2. Crear Gremio%n");
        System.out.printf("3. Reclutar Personaje a Escuadrón%n");
        System.out.printf("4. Equipar Arma a Personaje%n");
        System.out.printf("5. Mostrar Estado Detallado (Personaje y Corazón RPG)%n");
        System.out.printf("6. Salir%n");
        System.out.printf("Seleccione una opción: ");
    }

    // ---------- Opción 1: crear personaje ----------

    private void crearPersonaje() {
        String nombre = leerTexto("Nombre del personaje: ");
        if (nombre.isEmpty()) {
            System.out.printf("El nombre no puede estar vacío.%n");
            return;
        }
        if (buscarPorNombre(personajes, nombre, Personaje::getNombre)) {
            System.out.printf("Ya existe un personaje llamado %s.%n", nombre);
            return;
        }
        System.out.printf("Nivel (1-%d): ", NIVEL_MAXIMO);
        int nivel = leerEntero();
        if (nivel < 1 || nivel > NIVEL_MAXIMO) {
            System.out.printf("Nivel inválido.%n");
            return;
        }
        Personaje nuevo = new Personaje(nombre, nivel);   // el CorazonRPG se crea dentro del constructor
        personajes.add(nuevo);
        System.out.printf("Personaje creado: %s%n", nuevo);
    }

    // ---------- Opción 2: gremios (asociación bidireccional) ----------

    private void gestionarGremios() {
        System.out.printf("%n1. Crear gremio nuevo%n2. Inscribir personaje en un gremio%n3. Retirar personaje de su gremio%n0. Volver%nElige: ");
        int opcion = leerEntero();
        switch (opcion) {
            case 1:
                crearGremio();
                break;
            case 2:
                inscribirEnGremio();
                break;
            case 3:
                Personaje p = elegir(personajes, Personaje::getNombre, "Personaje");
                if (p != null) {
                    if (p.getGremio() == null) {
                        System.out.printf("%s no pertenece a ningún gremio.%n", p.getNombre());
                    } else {
                        p.abandonarGremio();
                    }
                }
                break;
            case 0:
                break;
            default:
                System.out.printf("Opción inválida.%n");
        }
    }

    private void crearGremio() {
        String nombre = leerTexto("Nombre del gremio: ");
        if (nombre.isEmpty()) {
            System.out.printf("El nombre no puede estar vacío.%n");
            return;
        }
        if (buscarPorNombre(gremios, nombre, Gremio::getNombre)) {
            System.out.printf("Ya existe un gremio llamado %s.%n", nombre);
            return;
        }
        Gremio nuevo = new Gremio(nombre);
        gremios.add(nuevo);
        System.out.printf("Gremio creado: %s%n", nuevo);
    }

    private void inscribirEnGremio() {
        Gremio gremio = elegir(gremios, Gremio::getNombre, "Gremio");
        if (gremio == null) {
            return;
        }
        Personaje personaje = elegir(personajes, Personaje::getNombre, "Personaje");
        if (personaje != null) {
            personaje.unirseAGremio(gremio);   // sincroniza ambos extremos de la relación
        }
    }

    // ---------- Opción 3: escuadrones (agregación) ----------

    private void gestionarEscuadrones() {
        if (personajes.isEmpty()) {
            System.out.printf("Primero crea al menos un personaje (opción 1).%n");
            return;
        }
        Escuadron escuadron = elegirOCrearEscuadron();
        if (escuadron == null) {
            return;
        }
        System.out.printf("%n1. Reclutar personaje%n2. Expulsar personaje%n0. Volver%nElige: ");
        int opcion = leerEntero();
        if (opcion == 1) {
            Personaje p = elegir(personajes, Personaje::getNombre, "Personaje a reclutar");
            if (p != null) {
                escuadron.reclutarPersonaje(p);
            }
        } else if (opcion == 2) {
            Personaje p = elegir(escuadron.getMiembros(), Personaje::getNombre, "Personaje a expulsar");
            if (p != null) {
                escuadron.expulsarPersonaje(p);
            }
        } else if (opcion != 0) {
            System.out.printf("Opción inválida.%n");
        }
    }

    private Escuadron elegirOCrearEscuadron() {
        if (!escuadrones.isEmpty()) {
            System.out.printf("%nEscuadrones:%n");
            for (int i = 0; i < escuadrones.size(); i++) {
                System.out.printf("%d. %s (%d miembros)%n", i + 1, escuadrones.get(i).getNombre(),
                        escuadrones.get(i).getCantidadMiembros());
            }
            System.out.printf("0. Crear un escuadrón nuevo%nElige: ");
            int eleccion = leerEntero();
            if (eleccion >= 1 && eleccion <= escuadrones.size()) {
                return escuadrones.get(eleccion - 1);
            }
            if (eleccion != 0) {
                System.out.printf("Escuadrón inválido.%n");
                return null;
            }
        }
        String nombre = leerTexto("Nombre del nuevo escuadrón: ");
        if (nombre.isEmpty()) {
            System.out.printf("El nombre no puede estar vacío.%n");
            return null;
        }
        if (buscarPorNombre(escuadrones, nombre, Escuadron::getNombre)) {
            System.out.printf("Ya existe un escuadrón llamado %s.%n", nombre);
            return null;
        }
        Escuadron nuevo = new Escuadron(nombre);
        escuadrones.add(nuevo);
        System.out.printf("Escuadrón creado: %s%n", nuevo);
        return nuevo;
    }

    // ---------- Opción 4: armas (asociación unidireccional) ----------

    private void gestionarArmas() {
        Personaje personaje = elegir(personajes, Personaje::getNombre, "Personaje");
        if (personaje == null) {
            return;
        }
        System.out.printf("%n1. Equipar un arma nueva%n2. Desequipar el arma actual%n0. Volver%nElige: ");
        int opcion = leerEntero();
        if (opcion == 1) {
            String nombreArma = leerTexto("Nombre del arma: ");
            System.out.printf("Daño base del arma: ");
            int danio = leerEntero();
            if (nombreArma.isEmpty() || danio < 0) {
                System.out.printf("Datos del arma inválidos.%n");
                return;
            }
            personaje.equiparArma(new Arma(nombreArma, danio));   // el arma no conoce a su dueño
        } else if (opcion == 2) {
            personaje.desequiparArma();
        } else if (opcion != 0) {
            System.out.printf("Opción inválida.%n");
        }
    }

    // ---------- Opción 5: estado detallado ----------

    private void mostrarEstadoDetallado() {
        if (personajes.isEmpty()) {
            System.out.printf("Aún no hay personajes. Crea uno con la opción 1.%n");
            return;
        }
        System.out.printf("%n%-12s %5s %-14s %-12s %9s %9s %7s%n",
                "Personaje", "Nivel", "Arma", "Gremio", "Salud", "Maná", "Ataque");
        for (Personaje p : personajes) {
            System.out.printf("%-12s %5d %-14s %-12s %4d/%-4d %4d/%-4d %7d%n",
                    p.getNombre(), p.getNivel(),
                    p.getArmaEquipada() != null ? p.getArmaEquipada().getNombre() : "Desarmado",
                    p.getGremio() != null ? p.getGremio().getNombre() : "Sin gremio",
                    p.getSaludActual(), p.getSaludMaxima(), p.getManaActual(), p.getManaMaximo(),
                    p.calcularAtaqueTotal());
        }

        Personaje elegido = elegir(personajes, Personaje::getNombre, "Ver detalle de");
        if (elegido != null) {
            System.out.printf("%n%s", elegido.generarReporteDetallado());
        }

        for (Escuadron e : escuadrones) {
            System.out.printf("%n%s", e.generarReporte());
        }
        for (Gremio g : gremios) {
            System.out.printf("%n%s%n", g);
        }
    }

    // ---------- Utilidades de entrada ----------

    /** Muestra una lista numerada y devuelve el elemento elegido (null si se cancela o no es válido). */
    private <T> T elegir(List<T> opciones, Function<T, String> etiqueta, String titulo) {
        if (opciones.isEmpty()) {
            System.out.printf("No hay elementos disponibles para elegir.%n");
            return null;
        }
        System.out.printf("%n%s:%n", titulo);
        for (int i = 0; i < opciones.size(); i++) {
            System.out.printf("%d. %s%n", i + 1, etiqueta.apply(opciones.get(i)));
        }
        System.out.printf("0. Cancelar%nElige: ");
        int eleccion = leerEntero();
        if (eleccion == 0) {
            return null;
        }
        if (eleccion < 1 || eleccion > opciones.size()) {
            System.out.printf("Selección inválida.%n");
            return null;
        }
        return opciones.get(eleccion - 1);
    }

    private <T> boolean buscarPorNombre(List<T> lista, String nombre, Function<T, String> obtenerNombre) {
        for (T elemento : lista) {
            if (obtenerNombre.apply(elemento).equalsIgnoreCase(nombre)) {
                return true;
            }
        }
        return false;
    }

    private String leerTexto(String mensaje) {
        System.out.printf("%s", mensaje);
        return leerLinea().trim();
    }

    /** Lee una línea con nextLine (no deja saltos pendientes) y la convierte a entero; -1 si no es número. */
    private int leerEntero() {
        try {
            return Integer.parseInt(leerLinea().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private String leerLinea() {
        try {
            return scanner.nextLine();
        } catch (NoSuchElementException e) {
            System.out.printf("%nEntrada finalizada. Saliendo del juego.%n");
            System.exit(0);
            return "";
        }
    }
}
