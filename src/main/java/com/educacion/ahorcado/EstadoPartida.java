package com.educacion.ahorcado;

/**
 * Contenedor del estado de una partida: las variables de la tabla de la Fase 2
 * (MAX_INTENTOS, intentosRestantes, puntaje, juegoGanado, ultimaLetra, nombreJugador)
 * más los StringBuilder de la máscara y el historial (Unidades 1.1 y 1.4).
 * Se inicializa en la Fase 2, se modifica en la Fase 3 y se lee en la Fase 4.
 */
class EstadoPartida {

    final String nombreJugador;
    final String palabraSecreta;
    final int MAX_INTENTOS;          // constante inmutable por partida

    int intentosRestantes;
    double puntaje;
    boolean juegoGanado;
    boolean rendido;
    char ultimaLetra;
    int aciertos;
    int fallos;

    final StringBuilder mascara = new StringBuilder();    // ejemplo: "J _ V _"
    final StringBuilder historial = new StringBuilder();  // letras arriesgadas

    EstadoPartida(String nombreJugador, String palabraSecreta, int maxIntentos, double puntajeInicial) {
        this.nombreJugador = nombreJugador;
        this.palabraSecreta = palabraSecreta;
        this.MAX_INTENTOS = maxIntentos;
        this.intentosRestantes = maxIntentos;
        this.puntaje = puntajeInicial;
        this.juegoGanado = false;
        this.rendido = false;
        this.ultimaLetra = ' ';
        this.aciertos = 0;
        this.fallos = 0;

        // Máscara inicial: un guion bajo por letra, separados por espacios
        for (int i = 0; i < palabraSecreta.length(); i++) {
            if (i > 0) {
                this.mascara.append(' ');
            }
            this.mascara.append('_');
        }
    }

    int turnos() {
        return aciertos + fallos;
    }
}
