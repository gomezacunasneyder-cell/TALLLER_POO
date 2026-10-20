package com.educacion.ahorcado;

import java.util.Scanner;

/**
 * Taller Práctico Integrador: "Adivina la Palabra Secreta / Ahorcado de Consola".
 * Punto de entrada. Cada fase del taller vive en su propia clase:
 *
 *   Fase1MenuPrincipal      - menú, switch y validación de entradas
 *   Fase2RegistroJugador    - registro del jugador y variables de estado
 *   Fase3BucleJuego         - bucle de juego con StringBuilder
 *   Fase4Resumen            - resumen con printf y estadísticas
 *   Fase5Retos              - pistas, dificultad dinámica y multiplicador de puntaje
 */
public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Fase1MenuPrincipal.ejecutar(scanner);
        scanner.close();
    }
}
