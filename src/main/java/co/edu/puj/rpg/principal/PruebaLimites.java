package co.edu.puj.rpg.principal;

import co.edu.puj.rpg.modelo.Personaje;

/**
 * Pruebas manuales de la guía de verificación (6.2, punto 3):
 * valores límite (-10, 0, 100000) a través de los setters validados.
 */
public class PruebaLimites {

    public static void main(String[] args) {
        Personaje heroe = new Personaje("Aragorn", 100, 15, 50.0);

        System.out.printf("=== setPuntosVida (vidaMaxima = %d) ===%n", heroe.getVidaMaxima());
        int[] valoresVida = {-10, 0, 50, 100000};
        for (int valor : valoresVida) {
            heroe.setPuntosVida(valor);
            System.out.printf("setPuntosVida(%d) -> vida: %d | estaVivo: %b%n",
                    valor, heroe.getPuntosVida(), heroe.isEstaVivo());
        }

        System.out.printf("%n=== setOro ===%n");
        double[] valoresOro = {-10.0, 0.0, 100000.0};
        for (double valor : valoresOro) {
            double antes = heroe.getOro();
            boolean aplicado = heroe.setOro(valor);
            System.out.printf("setOro(%.2f) -> aplicado: %b | oro antes: %.2f | oro ahora: %.2f%n",
                    valor, aplicado, antes, heroe.getOro());
        }

        System.out.printf("%nNota: setNivel es protected, por eso Main no puede invocarlo.%n");
        System.out.printf("El nivel solo cambia vía ganarExperiencia(): nivel actual = %d%n", heroe.getNivel());
        heroe.ganarExperiencia(50);
        System.out.printf("Tras ganarExperiencia(50): nivel = %d | vidaMaxima = %d | ataque = %d%n",
                heroe.getNivel(), heroe.getVidaMaxima(), heroe.getPuntosAtaque());
    }
}
