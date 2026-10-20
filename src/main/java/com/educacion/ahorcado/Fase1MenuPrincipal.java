package com.educacion.ahorcado;

import java.util.Scanner;

/**
 * Fase 1: Arquitectura del menú principal y control de flujo (Unidades 1.2 y 1.3).
 * Menú con do-while + switch y validación repetitiva de la opción con while.
 */
public class Fase1MenuPrincipal {

    public static void ejecutar(Scanner scanner) {
        int opcion;

        do {
            mostrarMenu();
            opcion = leerOpcionMenu(scanner);

            switch (opcion) {
                case 1: {
                    EstadoPartida estado = Fase2RegistroJugador.crearEstadoInicial(scanner);
                    Fase3BucleJuego.ejecutar(scanner, estado);
                    System.out.printf("%n[Fase 4] El resumen de la partida se implementa en la siguiente fase.%n");
                    break;
                }
                case 2:
                    System.out.printf("%n[Fase 4] Las estadísticas se implementan en una fase posterior.%n");
                    break;
                case 3:
                    System.out.printf("%n¡Gracias por jugar! Hasta pronto.%n");
                    break;
                default:
                    break;  // inalcanzable: leerOpcionMenu solo devuelve 1, 2 o 3
            }
        } while (opcion != 3);
    }

    static void mostrarMenu() {
        System.out.printf("%n=== AHORCADO DE CONSOLA ===%n");
        System.out.printf("1. Jugar%n");
        System.out.printf("2. Ver Estadísticas%n");
        System.out.printf("3. Salir%n");
    }

    /**
     * Lee la opción del menú con un bucle while que repite la lectura hasta recibir 1, 2 o 3.
     * Lee siempre la línea completa y la convierte con Integer.parseInt para evitar residuos en el búfer.
     */
    static int leerOpcionMenu(Scanner scanner) {
        int opcion = -1;
        boolean opcionValida = false;

        while (!opcionValida) {
            System.out.printf("Seleccione una opción (1-3): ");
            String linea = leerLinea(scanner).trim();
            try {
                opcion = Integer.parseInt(linea);
                if (opcion >= 1 && opcion <= 3) {
                    opcionValida = true;
                } else {
                    System.out.printf("Error: la opción %d está fuera de rango. Elija 1, 2 o 3.%n", opcion);
                }
            } catch (NumberFormatException e) {
                System.out.printf("Error: \"%s\" no es un número válido. Ingrese 1, 2 o 3.%n", linea);
            }
        }
        return opcion;
    }

    /** Lee una línea completa; si la entrada se cierra, termina el programa de forma ordenada. */
    static String leerLinea(Scanner scanner) {
        if (!scanner.hasNextLine()) {
            System.out.printf("%nEntrada finalizada. Saliendo del juego.%n");
            System.exit(0);
        }
        return scanner.nextLine();
    }
}
