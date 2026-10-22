package co.edu.puj.rpg.arena.principal;

import co.edu.puj.rpg.arena.inventario.Inventario;
import co.edu.puj.rpg.arena.modelo.Enemigo;
import co.edu.puj.rpg.arena.modelo.Guerrero;
import co.edu.puj.rpg.arena.modelo.Mago;
import co.edu.puj.rpg.arena.modelo.Personaje;
import co.edu.puj.rpg.arena.modelo.Pocion;
import co.edu.puj.rpg.arena.tablero.Tablero;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Simulación del combate por turnos (sección 4). Mantiene el estado de la partida: escuadrón
 * héroe, escuadrón enemigo, inventario genérico y tablero 2D.
 */
public class Batalla {

    private static final int RONDA_ESPECIAL_ENEMIGA = 2;   // los enemigos usan su especial en rondas pares
    private static final int UMBRAL_FURIA_PORCENTAJE = 30; // con menos del 30 % de vida atacan en furia

    private final Scanner scanner;
    private final List<Personaje> heroes = new ArrayList<>();
    private final List<Enemigo> enemigos = new ArrayList<>();
    private final Inventario<Pocion> inventario = new Inventario<>();
    private final Tablero tablero = new Tablero(2, 2);
    private Personaje principal;
    private int ronda = 0;

    public Batalla(Scanner scanner) {
        this.scanner = scanner;
    }

    // ---------- 4.1 Flujo principal ----------

    public void iniciar() {
        inicializar();

        // Ciclo principal: mientras haya héroes vivos, enemigos activos y el cofre no se haya alcanzado
        do {
            ronda++;
            tablero.actualizar(heroes, enemigos);
            mostrarEstado();

            turnoJugador();

            if (hayEnemigosVivos() && !principalEnCofre()) {
                turnoEnemigo();
            }

            // Limpieza de bajas al cierre del turno
            heroes.removeIf(h -> !h.estaVivo());
            enemigos.removeIf(e -> !e.estaVivo());
            tablero.actualizar(heroes, enemigos);
        } while (!heroes.isEmpty() && !enemigos.isEmpty() && !principalEnCofre());

        mostrarResultado();
    }

    private void inicializar() {
        heroes.add(new Guerrero("Thorin", 120, 15, 0, 0, 10));
        heroes.add(new Mago("Gandalf", 80, 25, 0, 1, 50));
        principal = heroes.get(0);   // héroe principal: el que debe llegar al cofre

        enemigos.add(new Enemigo("Orco", 60, 12, 0, 4));
        enemigos.add(new Enemigo("Goblin", 40, 8, 4, 0));
        enemigos.add(new Enemigo("Troll", 90, 15, 4, 4));

        inventario.agregar(new Pocion("Poción menor", 30));
        inventario.agregar(new Pocion("Poción menor", 30));
        inventario.agregar(new Pocion("Poción mayor", 60));

        System.out.printf("=== ARENA RPG ===%n");
        System.out.printf("Escuadrón: %d héroes | Enemigos: %d | Pociones: %d%n",
                heroes.size(), enemigos.size(), inventario.size());
        System.out.printf("Objetivo: elimina a todos los enemigos o lleva a %s hasta el cofre [C].%n",
                principal.getNombre());
    }

    // ---------- Renderizado y estado ----------

    private void mostrarEstado() {
        System.out.printf("%n========== RONDA %d ==========%n", ronda);
        tablero.mostrar();
        System.out.printf("%n-- Héroes --%n");
        for (Personaje heroe : heroes) {
            System.out.println(heroe.resumenEstado());
        }
        System.out.printf("-- Enemigos --%n");
        for (Enemigo enemigo : enemigos) {
            System.out.println(enemigo.resumenEstado());
        }
        System.out.printf("Pociones disponibles: %d%n", inventario.size());
    }

    // ---------- Turno del jugador ----------

    private void turnoJugador() {
        System.out.printf("%n--- TURNO DEL JUGADOR ---%n");
        for (Personaje heroe : heroes) {
            if (!heroe.estaVivo()) {
                continue;
            }
            if (!hayEnemigosVivos() || principalEnCofre()) {
                break;   // la misión ya se cumplió: los demás héroes no necesitan actuar
            }
            heroe.iniciarTurno();
            ejecutarAccion(heroe);
        }
    }

    private void ejecutarAccion(Personaje heroe) {
        boolean turnoGastado = false;
        while (!turnoGastado) {
            mostrarMenu(heroe);
            int opcion = leerEntero();
            switch (opcion) {
                case 1:
                    turnoGastado = atacar(heroe);
                    break;
                case 2:
                    turnoGastado = usarInventario(heroe);
                    break;
                case 3:
                    turnoGastado = mover(heroe);
                    break;
                case 4:
                    System.out.printf("%s pasa su turno.%n", heroe.getNombre());
                    turnoGastado = true;
                    break;
                default:
                    System.out.printf("Opción inválida. Elige un número del 1 al 4.%n");
            }
        }
    }

    private void mostrarMenu(Personaje heroe) {
        System.out.printf("%n>> Turno de %s (%d/%d de vida)%n", heroe.getNombre(), heroe.getVida(), heroe.getVidaMaxima());
        System.out.printf("1. Atacar (ataque especial)%n");
        System.out.printf("2. Usar Inventario%n");
        System.out.printf("3. Mover en Tablero%n");
        System.out.printf("4. Pasar turno%n");
        System.out.printf("Elige una opción: ");
    }

    /** Opción 1: elige un enemigo vivo y le aplica el ataque especial del héroe (polimorfismo dinámico). */
    private boolean atacar(Personaje heroe) {
        List<Enemigo> vivos = enemigosVivos();
        System.out.printf("%nObjetivos:%n");
        for (int i = 0; i < vivos.size(); i++) {
            Enemigo e = vivos.get(i);
            System.out.printf("%d. %s (Vida: %d/%d, Pos: %d,%d)%n",
                    i + 1, e.getNombre(), e.getVida(), e.getVidaMaxima(), e.getPosicionX(), e.getPosicionY());
        }
        System.out.printf("0. Cancelar%nElige un objetivo: ");
        int eleccion = leerEntero();
        if (eleccion == 0) {
            return false;
        }
        if (eleccion < 1 || eleccion > vivos.size()) {
            System.out.printf("Objetivo inválido.%n");
            return false;
        }
        Enemigo objetivo = vivos.get(eleccion - 1);
        heroe.realizarAtaqueEspecial(objetivo);
        if (!objetivo.estaVivo()) {
            System.out.printf("¡%s ha sido derrotado!%n", objetivo.getNombre());
        }
        return true;
    }

    /** Opción 2: gasta una poción del Inventario&lt;Pocion&gt; para recuperar vida (sin casteo). */
    private boolean usarInventario(Personaje heroe) {
        if (inventario.estaVacio()) {
            System.out.printf("El inventario está vacío.%n");
            return false;
        }
        if (heroe.getVida() == heroe.getVidaMaxima()) {
            System.out.printf("%s ya tiene la vida al máximo.%n", heroe.getNombre());
            return false;
        }
        System.out.printf("%nInventario:%n");
        for (int i = 0; i < inventario.size(); i++) {
            Pocion p = inventario.obtener(i);   // el genérico devuelve Pocion directamente
            System.out.printf("%d. %s (+%d de vida)%n", i + 1, p.getNombre(), p.getCuracion());
        }
        System.out.printf("0. Cancelar%nElige una poción: ");
        int eleccion = leerEntero();
        if (eleccion == 0) {
            return false;
        }
        if (eleccion < 1 || eleccion > inventario.size()) {
            System.out.printf("Poción inválida.%n");
            return false;
        }
        Pocion pocion = inventario.quitar(eleccion - 1);
        int recuperado = heroe.recuperarVida(pocion.getCuracion());
        System.out.printf("%s usa %s y recupera %d de vida. Vida: %d/%d%n",
                heroe.getNombre(), pocion.getNombre(), recuperado, heroe.getVida(), heroe.getVidaMaxima());
        return true;
    }

    /** Opción 3: mueve al héroe una celda, actualizando posicionX/posicionY y la matriz 2D. */
    private boolean mover(Personaje heroe) {
        System.out.printf("%n1. Arriba  2. Abajo  3. Izquierda  4. Derecha  0. Cancelar%nElige una dirección: ");
        int direccion = leerEntero();
        int nuevoX = heroe.getPosicionX();
        int nuevoY = heroe.getPosicionY();
        switch (direccion) {
            case 0:
                return false;
            case 1:
                nuevoX--;
                break;
            case 2:
                nuevoX++;
                break;
            case 3:
                nuevoY--;
                break;
            case 4:
                nuevoY++;
                break;
            default:
                System.out.printf("Dirección inválida.%n");
                return false;
        }
        if (!tablero.estaDentro(nuevoX, nuevoY)) {
            System.out.printf("Movimiento imposible: (%d,%d) está fuera del tablero.%n", nuevoX, nuevoY);
            return false;
        }
        if (!tablero.estaLibre(nuevoX, nuevoY)) {
            System.out.printf("La celda (%d,%d) está ocupada.%n", nuevoX, nuevoY);
            return false;
        }
        heroe.setPosicion(nuevoX, nuevoY);
        tablero.actualizar(heroes, enemigos);
        System.out.printf("%s se mueve a (%d,%d).%n", heroe.getNombre(), nuevoX, nuevoY);
        tablero.mostrar();
        if (principalEnCofre()) {
            System.out.printf("¡%s ha alcanzado el cofre!%n", principal.getNombre());
        }
        return true;
    }

    // ---------- Turno enemigo (IA automática) ----------

    private void turnoEnemigo() {
        System.out.printf("%n--- TURNO ENEMIGO ---%n");
        for (Enemigo enemigo : enemigos) {
            if (!enemigo.estaVivo()) {
                continue;
            }
            Personaje objetivo = heroeMasDebil();
            if (objetivo == null) {
                break;
            }
            boolean enFuria = enemigo.getVida() * 100 < enemigo.getVidaMaxima() * UMBRAL_FURIA_PORCENTAJE;
            if (enFuria) {
                enemigo.atacar(objetivo, true);              // sobrecarga con modo furia
            } else if (ronda % RONDA_ESPECIAL_ENEMIGA == 0) {
                enemigo.realizarAtaqueEspecial(objetivo);    // polimorfismo dinámico
            } else {
                enemigo.atacar(objetivo);                    // sobrecarga básica
            }
            if (!objetivo.estaVivo()) {
                System.out.printf("¡%s ha caído en combate!%n", objetivo.getNombre());
            }
        }
    }

    private Personaje heroeMasDebil() {
        Personaje masDebil = null;
        for (Personaje heroe : heroes) {
            if (heroe.estaVivo() && (masDebil == null || heroe.getVida() < masDebil.getVida())) {
                masDebil = heroe;
            }
        }
        return masDebil;
    }

    // ---------- 4.2 Condiciones de victoria y derrota ----------

    private boolean hayEnemigosVivos() {
        for (Enemigo enemigo : enemigos) {
            if (enemigo.estaVivo()) {
                return true;
            }
        }
        return false;
    }

    private List<Enemigo> enemigosVivos() {
        List<Enemigo> vivos = new ArrayList<>();
        for (Enemigo enemigo : enemigos) {
            if (enemigo.estaVivo()) {
                vivos.add(enemigo);
            }
        }
        return vivos;
    }

    private boolean principalEnCofre() {
        return principal.estaVivo() && tablero.esCofre(principal.getPosicionX(), principal.getPosicionY());
    }

    private int sumaVidaHeroes() {
        int suma = 0;
        for (Personaje heroe : heroes) {
            suma += heroe.getVida();
        }
        return suma;
    }

    private void mostrarResultado() {
        System.out.printf("%n=== FIN DE LA BATALLA (rondas: %d) ===%n", ronda);
        tablero.mostrar();
        if (heroes.isEmpty() || sumaVidaHeroes() <= 0) {
            System.out.printf("DERROTA: el escuadrón héroe ha caído.%n");
        } else if (enemigos.isEmpty()) {
            System.out.printf("¡VICTORIA! Todos los enemigos han sido eliminados.%n");
        } else {
            System.out.printf("¡VICTORIA! %s alcanzó el cofre del tesoro.%n", principal.getNombre());
        }
        System.out.printf("Héroes en pie: %d | Vida total: %d | Pociones sin usar: %d%n",
                heroes.size(), sumaVidaHeroes(), inventario.size());
    }

    // ---------- Entrada ----------

    /** Lee una línea y la convierte a entero; si no es un número devuelve -1 (opción inválida). */
    private int leerEntero() {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        } catch (NoSuchElementException e) {
            System.out.printf("%nEntrada finalizada. Saliendo del juego.%n");
            System.exit(0);
            return -1;
        }
    }
}
