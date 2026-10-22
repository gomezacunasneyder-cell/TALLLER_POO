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

/**
 * Sección 5.1: reproduce de forma controlada los errores de la matriz de excepciones y muestra
 * la corrección de cada uno. Sirve para colocar breakpoints y practicar Step Over / Step Into /
 * Evaluate Expression (Alt + F8) en IntelliJ.
 */
public class PruebaDepuracion {

    private static int fallos = 0;

    public static void main(String[] args) {
        casoIndexOutOfBounds();
        casoClassCast();
        casoInstanciacionAbstracta();
        casoInvariantes();

        System.out.printf("%n=== RESULTADO ===%n");
        System.out.printf(fallos == 0
                ? "Todas las comprobaciones pasaron.%n"
                : "Comprobaciones fallidas: " + fallos + "%n");
    }

    /** IndexOutOfBoundsException: índice fuera del ArrayList o de la matriz 5x5. */
    private static void casoIndexOutOfBounds() {
        System.out.printf("=== Caso 1: IndexOutOfBoundsException ===%n");
        Tablero tablero = new Tablero(2, 2);
        char[][] matriz = tablero.getMatriz();

        try {
            char celda = matriz[5][5];   // error: los índices válidos son 0..4
            System.out.printf("Celda leída: %c%n", celda);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.printf("Capturado: %s%n", e.getMessage());
        }

        List<Personaje> escuadron = new ArrayList<>();
        escuadron.add(new Guerrero("Thorin", 120, 15, 0, 0, 10));
        try {
            escuadron.get(3);   // error: la lista solo tiene el índice 0
        } catch (IndexOutOfBoundsException e) {
            System.out.printf("Capturado: %s%n", e.getMessage());
        }

        // Corrección: validar antes de acceder
        verificar("estaDentro(5,5) es false", !tablero.estaDentro(5, 5));
        verificar("estaDentro(-1,0) es false", !tablero.estaDentro(-1, 0));
        verificar("estaDentro(4,4) es true", tablero.estaDentro(4, 4));
        verificar("índice 3 no es válido con size()==1", !(3 < escuadron.size()));
    }

    /** ClassCastException: castear un Guerrero a Mago. */
    private static void casoClassCast() {
        System.out.printf("%n=== Caso 2: ClassCastException ===%n");
        Personaje p = new Guerrero("Thorin", 120, 15, 0, 0, 10);
        try {
            Mago mago = (Mago) p;   // error: la instancia real es un Guerrero
            System.out.printf("Mago: %s%n", mago.getNombre());
        } catch (ClassCastException e) {
            System.out.printf("Capturado: ClassCastException al castear Guerrero a Mago%n");
        }

        // Corrección: comprobar el tipo real antes de castear (o, mejor, usar polimorfismo)
        boolean esMago = p instanceof Mago;
        verificar("Un Guerrero no es instancia de Mago", !esMago);

        // Con genéricos explícitos el compilador impide mezclar tipos: no hace falta castear
        Inventario<Pocion> inventario = new Inventario<>();
        inventario.agregar(new Pocion("Poción menor", 30));
        Pocion pocion = inventario.obtener(0);
        verificar("Inventario<Pocion> devuelve Pocion sin casteo", pocion.getCuracion() == 30);
    }

    /**
     * Error de compilación: instanciar la clase abstracta. La línea siguiente NO compila, por eso
     * está comentada; descoméntala en IntelliJ para ver el subrayado rojo:
     *
     *     Personaje p = new Personaje("X", 10, 1, 0, 0);   // 'Personaje' is abstract; cannot be instantiated
     *
     * Corrección: instanciar una subclase concreta.
     */
    private static void casoInstanciacionAbstracta() {
        System.out.printf("%n=== Caso 3: instanciación directa de una clase abstracta ===%n");
        Personaje p1 = new Guerrero("Thorin", 120, 15, 0, 0, 10);
        Personaje p2 = new Mago("Gandalf", 80, 25, 0, 1, 50);
        verificar("Guerrero como Personaje: " + p1.getNombre(), p1.estaVivo());
        verificar("Mago como Personaje: " + p2.getNombre(), p2.estaVivo());
    }

    /** Invariantes del modelo: la vida nunca baja de 0 ni sube del máximo. */
    private static void casoInvariantes() {
        System.out.printf("%n=== Caso 4: invariantes del modelo ===%n");
        Personaje guerrero = new Guerrero("Thorin", 120, 15, 0, 0, 10);
        Enemigo orco = new Enemigo("Orco", 60, 12, 0, 4);

        orco.recibirDano(-5);
        verificar("recibirDano(-5) no cambia la vida", orco.getVida() == 60);
        orco.recibirDano(1000);
        verificar("recibirDano(1000) deja la vida en 0 (no negativa)", orco.getVida() == 0 && !orco.estaVivo());

        guerrero.defender(30);   // escudo 10 -> mitiga 5 -> 25 de daño
        verificar("Guerrero mitiga la mitad de su escudo: vida 95", guerrero.getVida() == 95);
        guerrero.recuperarVida(1000);
        verificar("recuperarVida no supera la vida máxima", guerrero.getVida() == guerrero.getVidaMaxima());

        Mago mago = new Mago("Gandalf", 80, 25, 0, 1, 50);
        Enemigo troll = new Enemigo("Troll", 90, 15, 4, 4);
        mago.realizarAtaqueEspecial(troll);
        verificar("Bola de fuego: 50 de daño y 15 de maná", troll.getVida() == 40 && mago.getPuntosMana() == 35);

        Inventario<Pocion> vacio = new Inventario<>();
        verificar("Un inventario nuevo está vacío", vacio.estaVacio());

        List<Enemigo> enemigos = new ArrayList<>(List.of(orco, troll));
        enemigos.removeIf(e -> !e.estaVivo());
        verificar("removeIf elimina solo a los caídos", enemigos.size() == 1);
    }

    private static void verificar(String descripcion, boolean correcto) {
        if (!correcto) {
            fallos++;
        }
        System.out.printf("[%s] %s%n", correcto ? "OK" : "FALLA", descripcion);
    }
}
