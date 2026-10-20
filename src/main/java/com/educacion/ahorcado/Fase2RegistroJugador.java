package com.educacion.ahorcado;

import java.util.Scanner;

/**
 * Fase 2: Registro del jugador e inicialización de datos (Unidades 1.1 y 1.4).
 * Normaliza el nombre con trim(), toUpperCase() y length(), elige la palabra secreta
 * y crea el estado inicial de la partida.
 */
public class Fase2RegistroJugador {

    private static final String[] PALABRAS = {
            "JAVA", "CLASE", "BUCLE", "OBJETO", "CADENA",
            "VARIABLE", "CONSTANTE", "COMPILADOR", "PROGRAMACION", "INTELLIJ"
    };
    private static final double PUNTAJE_INICIAL = 100.0;

    public static EstadoPartida crearEstadoInicial(Scanner scanner) {
        String nombreJugador = leerNombre(scanner);
        String palabraSecreta = elegirPalabraSecreta();
        int maxIntentos = Fase5Retos.calcularMaxIntentos(palabraSecreta.length());  // Reto 2
        EstadoPartida estado = new EstadoPartida(nombreJugador, palabraSecreta, maxIntentos, PUNTAJE_INICIAL);
        mostrarBienvenida(estado);
        return estado;
    }

    /** Limpia con trim(), estandariza con toUpperCase() y valida con length(). */
    static String leerNombre(Scanner scanner) {
        String nombre = "";
        while (nombre.length() == 0) {
            System.out.printf("%nIngrese su nombre: ");
            nombre = Fase1MenuPrincipal.leerLinea(scanner).trim().toUpperCase();
            if (nombre.length() == 0) {
                System.out.printf("Error: el nombre no puede estar vacío. Intente de nuevo.%n");
            }
        }
        return nombre;
    }

    static String elegirPalabraSecreta() {
        return PALABRAS[(int) (Math.random() * PALABRAS.length)];
    }

    private static void mostrarBienvenida(EstadoPartida estado) {
        System.out.printf("%n¡Bienvenido, %s! La palabra secreta tiene %d letras y tienes %d intentos.%n",
                estado.nombreJugador, estado.palabraSecreta.length(), estado.MAX_INTENTOS);
    }
}
