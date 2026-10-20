package com.educacion.ahorcado;

/**
 * Fase 5: Retos de expansión (nivel avanzado).
 *  Reto 1: pista con substring().
 *  Reto 2: dificultad dinámica según length() de la palabra secreta.
 *  Reto 3: multiplicador de puntaje por velocidad (turnos) y por fallos (intentos sobrantes).
 */
public class Fase5Retos {

    /** Reto 2: las palabras más largas conceden más intentos. */
    static int calcularMaxIntentos(int longitudPalabra) {
        if (longitudPalabra <= 4) {
            return 6;
        } else if (longitudPalabra <= 7) {
            return 7;
        } else {
            return 8;
        }
    }

    /** Reto 1: devuelve, con substring(), la primera letra que aún está oculta en la máscara. */
    static String generarPista(StringBuilder mascara, String palabraSecreta) {
        int posicionOculta = mascara.indexOf("_") / 2;
        return palabraSecreta.substring(posicionOculta, posicionOculta + 1);
    }

    /** Reto 3: bono por intentos que sobraron (menos fallos) y por resolver en pocos turnos. */
    static double calcularBonoVictoria(int intentosRestantes, int turnos) {
        double bonoPorIntentos = intentosRestantes * 15.0;
        double bonoPorVelocidad = Math.max(0.0, 30.0 - 2.0 * turnos);
        return bonoPorIntentos + bonoPorVelocidad;
    }
}
