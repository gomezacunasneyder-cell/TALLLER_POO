package co.edu.puj.rpg.arena.modelo;

/** Especialización de combate mágico: su ataque especial consume maná. */
public class Mago extends Personaje {

    private static final int COSTO_BOLA_DE_FUEGO = 15;
    private static final int MANA_POR_TURNO = 5;

    protected int puntosMana;
    private final int manaMaximo;

    public Mago(String nombre, int vida, int ataqueBase, int posicionX, int posicionY, int puntosMana) {
        super(nombre, vida, ataqueBase, posicionX, posicionY);
        this.puntosMana = Math.max(0, puntosMana);
        this.manaMaximo = this.puntosMana;
    }

    /**
     * Bola de fuego: cuesta maná y hace el doble del ataque base. Sin maná suficiente el mago
     * recurre al ataque básico (sobrecarga atacar(Personaje) heredada).
     */
    @Override
    public void realizarAtaqueEspecial(Personaje objetivo) {
        if (objetivo == null || !objetivo.estaVivo()) {
            return;
        }
        if (puntosMana < COSTO_BOLA_DE_FUEGO) {
            System.out.printf("%s no tiene maná suficiente (%d/%d) y usa un ataque básico.%n",
                    nombre, puntosMana, COSTO_BOLA_DE_FUEGO);
            atacar(objetivo);
            return;
        }
        puntosMana -= COSTO_BOLA_DE_FUEGO;
        int dano = ataqueBase * 2;
        System.out.printf("%s lanza BOLA DE FUEGO sobre %s (%d de daño, maná restante: %d).%n",
                nombre, objetivo.getNombre(), dano, puntosMana);
        objetivo.defender(dano);
    }

    /** Regenera un poco de maná al comenzar cada turno. */
    @Override
    public void iniciarTurno() {
        puntosMana = Math.min(manaMaximo, puntosMana + MANA_POR_TURNO);
    }

    @Override
    public char getSimbolo() {
        return 'M';
    }

    @Override
    public String resumenEstado() {
        return super.resumenEstado() + String.format(" | Maná: %d/%d", puntosMana, manaMaximo);
    }

    public int getPuntosMana() {
        return puntosMana;
    }
}
