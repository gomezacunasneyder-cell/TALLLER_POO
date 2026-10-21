package co.edu.puj.rpg.arena.modelo;

/** Enemigo controlado por la IA de la arena (aparece como [E] en el tablero). */
public class Enemigo extends Personaje {

    private static final int BONO_EMBESTIDA = 5;

    public Enemigo(String nombre, int vida, int ataqueBase, int posicionX, int posicionY) {
        super(nombre, vida, ataqueBase, posicionX, posicionY);
    }

    /** Embestida: ataque base más un bono fijo. */
    @Override
    public void realizarAtaqueEspecial(Personaje objetivo) {
        if (objetivo == null || !objetivo.estaVivo()) {
            return;
        }
        int dano = ataqueBase + BONO_EMBESTIDA;
        System.out.printf("%s lanza una EMBESTIDA sobre %s (%d de daño).%n",
                nombre, objetivo.getNombre(), dano);
        objetivo.defender(dano);
    }

    @Override
    public char getSimbolo() {
        return 'E';
    }
}
