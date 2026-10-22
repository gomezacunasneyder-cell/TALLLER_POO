package co.edu.puj.rpg.arena.tablero;

import co.edu.puj.rpg.arena.modelo.Personaje;

import java.util.List;

/**
 * Tablero de batalla 5x5 (sección 3.2) representado con una matriz de caracteres.
 * Se indexa como matriz[posicionX][posicionY]: X = fila, Y = columna.
 */
public class Tablero {

    public static final int TAMANO = 5;
    public static final char VACIO = '.';
    public static final char COFRE = 'C';

    private final char[][] matriz = new char[TAMANO][TAMANO];
    private final int cofreX;
    private final int cofreY;

    public Tablero(int cofreX, int cofreY) {
        if (!estaDentro(cofreX, cofreY)) {
            throw new IllegalArgumentException("El cofre debe estar dentro del tablero.");
        }
        this.cofreX = cofreX;
        this.cofreY = cofreY;
        limpiar();
    }

    /** Reconstruye la matriz desde cero para que ninguna celda conserve rastros de unidades caídas. */
    public void actualizar(List<? extends Personaje> heroes, List<? extends Personaje> enemigos) {
        limpiar();
        colocar(enemigos);
        colocar(heroes);   // los héroes se dibujan al final: si pisan el cofre, se ve el héroe
    }

    private void limpiar() {
        for (int i = 0; i < TAMANO; i++) {
            for (int j = 0; j < TAMANO; j++) {
                matriz[i][j] = VACIO;
            }
        }
        matriz[cofreX][cofreY] = COFRE;
    }

    private void colocar(List<? extends Personaje> unidades) {
        for (Personaje unidad : unidades) {
            if (unidad.estaVivo() && estaDentro(unidad.getPosicionX(), unidad.getPosicionY())) {
                matriz[unidad.getPosicionX()][unidad.getPosicionY()] = unidad.getSimbolo();
            }
        }
    }

    /** Evita IndexOutOfBounds: toda coordenada se valida antes de tocar la matriz. */
    public final boolean estaDentro(int x, int y) {
        return x >= 0 && x < TAMANO && y >= 0 && y < TAMANO;
    }

    /** Una celda está libre si es vacía o es el cofre (que puede pisarse). */
    public boolean estaLibre(int x, int y) {
        return estaDentro(x, y) && (matriz[x][y] == VACIO || matriz[x][y] == COFRE);
    }

    public boolean esCofre(int x, int y) {
        return x == cofreX && y == cofreY;
    }

    public char[][] getMatriz() {
        return matriz;
    }

    public void mostrar() {
        mostrarTablero(matriz);
    }

    /** Algoritmo de renderizado con bucles anidados (sección 3.2). */
    public void mostrarTablero(char[][] matriz) {
        System.out.print("      ");
        for (int j = 0; j < matriz[0].length; j++) {
            System.out.print(" " + j + " ");
        }
        System.out.println();
        for (int i = 0; i < matriz.length; i++) {
            System.out.print("  " + i + "   ");
            for (int j = 0; j < matriz[i].length; j++) {
                System.out.print("[" + matriz[i][j] + "]");
            }
            System.out.println();   // salto de línea por cada fila
        }
    }
}
