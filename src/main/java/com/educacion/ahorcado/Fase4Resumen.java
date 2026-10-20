package com.educacion.ahorcado;

import java.util.Locale;

/**
 * Fase 4: Resumen de partida y formateo de resultados (Unidades 1.1 y 1.4).
 * Toda la salida se genera con System.out.printf (%s, %d, %.2f) y el porcentaje
 * usa casting a double para evitar la división entera.
 */
public class Fase4Resumen {

    // Locale.US garantiza el punto decimal del ejemplo del PDF (83.33%) sin importar la región del equipo
    private static final Locale LOCALE = Locale.US;
    private static final String SEPARADOR = "=".repeat(52);

    // ---------- Estadísticas acumuladas durante la sesión ----------
    private static int partidasJugadas = 0;
    private static int partidasGanadas = 0;
    private static int partidasPerdidas = 0;
    private static double mejorPuntaje = 0.0;
    private static double puntajeAcumulado = 0.0;

    public static void finalizarPartida(EstadoPartida estado) {
        registrarEstadisticas(estado.juegoGanado, estado.puntaje);
        mostrarResumen(estado);
    }

    static void mostrarResumen(EstadoPartida estado) {
        String estadoFinal = estado.juegoGanado ? "¡VICTORIA!" : (estado.rendido ? "RENDICIÓN" : "DERROTA");
        int turnos = estado.turnos();
        // Casting a double para evitar la división entera
        double porcentajeAciertos = turnos > 0 ? (double) estado.aciertos / turnos * 100 : 0.0;

        System.out.printf(LOCALE, "%n%s%n", SEPARADOR);
        System.out.printf(LOCALE, "RESUMEN DE LA PARTIDA%n");
        System.out.printf(LOCALE, "%s%n", SEPARADOR);
        System.out.printf(LOCALE, "Jugador: %s%n", estado.nombreJugador);
        System.out.printf(LOCALE, "Estado Final: %s%n", estadoFinal);
        System.out.printf(LOCALE, "Palabra Secreta: %s%n", estado.palabraSecreta);
        System.out.printf(LOCALE, "Intentos Restantes: %d / %d%n", estado.intentosRestantes, estado.MAX_INTENTOS);
        System.out.printf(LOCALE, "Porcentaje Aciertos: %.2f%%%n", porcentajeAciertos);
        System.out.printf(LOCALE, "Puntaje Final: %.2f pts%n", estado.puntaje);
        System.out.printf(LOCALE, "%s%n", SEPARADOR);
    }

    private static void registrarEstadisticas(boolean juegoGanado, double puntaje) {
        partidasJugadas++;
        if (juegoGanado) {
            partidasGanadas++;
        } else {
            partidasPerdidas++;
        }
        mejorPuntaje = Math.max(mejorPuntaje, puntaje);
        puntajeAcumulado += puntaje;
    }

    public static void mostrarEstadisticas() {
        if (partidasJugadas == 0) {
            System.out.printf("%nAún no hay partidas registradas en esta sesión. Elija 1 para jugar.%n");
            return;
        }
        double porcentajeVictorias = (double) partidasGanadas / partidasJugadas * 100;
        double puntajePromedio = puntajeAcumulado / partidasJugadas;

        System.out.printf(LOCALE, "%n%s%n", SEPARADOR);
        System.out.printf(LOCALE, "ESTADÍSTICAS DE LA SESIÓN%n");
        System.out.printf(LOCALE, "%s%n", SEPARADOR);
        System.out.printf(LOCALE, "Partidas Jugadas: %d%n", partidasJugadas);
        System.out.printf(LOCALE, "Victorias: %d%n", partidasGanadas);
        System.out.printf(LOCALE, "Derrotas: %d%n", partidasPerdidas);
        System.out.printf(LOCALE, "Porcentaje de Victorias: %.2f%%%n", porcentajeVictorias);
        System.out.printf(LOCALE, "Mejor Puntaje: %.2f pts%n", mejorPuntaje);
        System.out.printf(LOCALE, "Puntaje Promedio: %.2f pts%n", puntajePromedio);
        System.out.printf(LOCALE, "%s%n", SEPARADOR);
    }
}
