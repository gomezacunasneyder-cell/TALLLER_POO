package co.edu.puj.rpg.principal;

import co.edu.puj.rpg.modelo.Personaje;
import co.edu.puj.rpg.modelo.enemigo.Enemigo;

/**
 * Pruebas manuales de la guía de verificación (sección 6.2, punto 3): valores límite
 * (-10, 0, 100000) a través de los setters, comprobando que el objeto conserve su coherencia interna.
 */
public class PruebaLimites {

    private static int fallos = 0;

    public static void main(String[] args) {
        probarPuntosVida();
        probarOro();
        probarNivel();

        System.out.printf("%n=== RESULTADO ===%n");
        if (fallos == 0) {
            System.out.printf("Todas las comprobaciones pasaron: el objeto conservó su coherencia.%n");
        } else {
            System.out.printf("Comprobaciones fallidas: %d%n", fallos);
        }
    }

    private static void probarPuntosVida() {
        Personaje heroe = new Personaje("Aragorn", 100, 15, 50.0);
        System.out.printf("=== setPuntosVida (vidaMaxima = %d) ===%n", heroe.getVidaMaxima());

        int[] valores = {-10, 0, 1, 50, 100, 100000, Integer.MIN_VALUE, Integer.MAX_VALUE};
        for (int valor : valores) {
            heroe.setPuntosVida(valor);
            boolean enRango = heroe.getPuntosVida() >= 0 && heroe.getPuntosVida() <= heroe.getVidaMaxima();
            boolean vivoCoherente = heroe.isEstaVivo() == (heroe.getPuntosVida() > 0);
            verificar(String.format("setPuntosVida(%d) -> vida: %d | estaVivo: %b",
                    valor, heroe.getPuntosVida(), heroe.isEstaVivo()), enRango && vivoCoherente);
        }
    }

    private static void probarOro() {
        Personaje heroe = new Personaje("Aragorn", 100, 15, 50.0);
        System.out.printf("%n=== setOro (oro inicial = %.2f) ===%n", heroe.getOro());

        double[] valores = {-10.0, 0.0, 100000.0, -0.01};
        for (double valor : valores) {
            double antes = heroe.getOro();
            boolean aplicado = heroe.setOro(valor);
            boolean coherente = valor < 0.0
                    ? (!aplicado && heroe.getOro() == antes)    // rechazado: el atributo no cambia
                    : (aplicado && heroe.getOro() == valor);    // aceptado: se asigna el valor
            verificar(String.format("setOro(%.2f) -> aplicado: %b | oro antes: %.2f | oro ahora: %.2f",
                    valor, aplicado, antes, heroe.getOro()), coherente);
        }
    }

    /**
     * setNivel es protected: desde este paquete no se puede invocar. El nivel solo sube derrotando
     * enemigos (experiencia), y el valor nunca baja de 1.
     */
    private static void probarNivel() {
        Personaje heroe = new Personaje("Aragorn", 100, 15, 50.0);
        System.out.printf("%n=== Nivel (setNivel es protected) ===%n");
        verificar(String.format("Nivel inicial: %d", heroe.getNivel()), heroe.getNivel() == 1);

        // Enemigo débil que otorga 60 de experiencia: supera el umbral de 50 y sube un nivel
        Enemigo debil = new Enemigo("Slime de prueba", 1, 1, 1, 10.0, 60);
        heroe.atacar(debil);
        verificar(String.format("Tras derrotar al enemigo -> nivel: %d | experiencia: %d | oro: %.2f",
                heroe.getNivel(), heroe.getExperiencia(), heroe.getOro()),
                !debil.isEstaVivo() && heroe.getNivel() == 2 && heroe.getNivel() >= 1);

        // Con experiencia que NO supera el umbral, el nivel se mantiene
        Personaje novato = new Personaje("Novato", 100, 15, 0.0);
        novato.atacar(new Enemigo("Slime menor", 1, 1, 1, 0.0, 30));
        verificar(String.format("Experiencia que no supera el umbral -> nivel: %d | experiencia: %d",
                novato.getNivel(), novato.getExperiencia()), novato.getNivel() == 1);
    }

    private static void verificar(String descripcion, boolean correcto) {
        if (!correcto) {
            fallos++;
        }
        System.out.printf("[%s] %s%n", correcto ? "OK" : "FALLA", descripcion);
    }
}
