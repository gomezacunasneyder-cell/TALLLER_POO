package com.educacion.ahorcado;

import java.util.Scanner;

/**
 * Fase 3: Bucle del juego y manipulación dinámica con StringBuilder (Unidades 1.3 y 1.4).
 * Usa break para victoria, agotamiento de intentos o rendición, continue para entradas
 * inválidas o letras repetidas, y el operador ternario para la retroalimentación.
 */
public class Fase3BucleJuego {

    static final String PALABRA_RENDICION = "RENDIRSE";
    private static final double PUNTOS_POR_ACIERTO = 10.0;
    private static final double PENALIZACION_POR_FALLO = 5.0;

    public static void ejecutar(Scanner scanner, EstadoPartida estado) {
        boolean pistaMostrada = false;
        System.out.printf("Escribe una letra por turno, o %s para abandonar la partida.%n", PALABRA_RENDICION);

        while (estado.intentosRestantes > 0 && !estado.juegoGanado) {
            System.out.printf("%nPalabra: %s%n", estado.mascara);
            System.out.printf("Letras usadas: %s%n", estado.historial.length() == 0 ? "-" : estado.historial);
            System.out.printf("Intentos restantes: %d / %d%n", estado.intentosRestantes, estado.MAX_INTENTOS);

            // Reto 1: pista con substring() cuando queda un único intento
            if (estado.intentosRestantes == 1 && !pistaMostrada) {
                System.out.printf("PISTA: la palabra contiene la letra %s.%n",
                        Fase5Retos.generarPista(estado.mascara, estado.palabraSecreta));
                pistaMostrada = true;
            }
            System.out.printf("Ingrese una letra: ");
            String entrada = Fase1MenuPrincipal.leerLinea(scanner).trim();

            // Validación: la entrada no puede estar vacía (protege charAt y substring)
            if (entrada.length() == 0) {
                System.out.printf("Error: no ingresó nada. Escriba una letra.%n");
                continue;
            }
            // Rendición: salida inmediata del bucle
            if (entrada.equalsIgnoreCase(PALABRA_RENDICION)) {
                estado.rendido = true;
                break;
            }
            // Validación: exactamente un carácter y debe ser una letra
            if (entrada.length() != 1 || !Character.isLetter(entrada.charAt(0))) {
                System.out.printf("Error: \"%s\" no es válido. Ingrese una sola letra.%n", entrada);
                continue;
            }

            char letra = Character.toUpperCase(entrada.charAt(0));

            // Letra repetida: se descarta el turno sin penalizar
            if (estado.historial.indexOf(String.valueOf(letra)) >= 0) {
                System.out.printf("La letra %c ya fue utilizada. Intente con otra.%n", letra);
                continue;
            }

            estado.ultimaLetra = letra;
            estado.historial.append(letra).append(' ');

            // Comparación carácter a carácter y actualización de la máscara
            boolean acierto = false;
            for (int i = 0; i < estado.palabraSecreta.length(); i++) {
                String candidata = String.valueOf(estado.palabraSecreta.charAt(i));
                if (candidata.equalsIgnoreCase(String.valueOf(letra))) {
                    estado.mascara.setCharAt(i * 2, letra);
                    acierto = true;
                }
            }

            if (acierto) {
                estado.aciertos++;
                estado.puntaje += PUNTOS_POR_ACIERTO;
            } else {
                estado.fallos++;
                estado.intentosRestantes--;  // solo se descuenta cuando la letra no pertenece a la palabra
                estado.puntaje = Math.max(0.0, estado.puntaje - PENALIZACION_POR_FALLO);
            }

            // Retroalimentación con operador ternario
            String mensaje = acierto
                    ? "¡Bien hecho! La letra " + estado.ultimaLetra + " está en la palabra."
                    : "Fallaste. La letra " + estado.ultimaLetra + " no está en la palabra.";
            System.out.printf("%s%n", mensaje);

            if (estado.mascara.indexOf("_") < 0) {   // ya no quedan guiones ocultos
                estado.juegoGanado = true;
                break;
            }
            if (estado.intentosRestantes <= 0) {     // se agotaron los intentos
                break;
            }
        }

        System.out.printf("%nPalabra: %s%n", estado.mascara);

        // Reto 3: multiplicador por velocidad (turnos) y por fallos (intentos que sobraron)
        if (estado.juegoGanado) {
            estado.puntaje += Fase5Retos.calcularBonoVictoria(estado.intentosRestantes, estado.turnos());
        }
    }
}
