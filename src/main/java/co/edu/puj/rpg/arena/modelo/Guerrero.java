package co.edu.puj.rpg.arena.modelo;

/** Especialización de combate físico: ataque potenciado por el escudo y mitigación de daño. */
public class Guerrero extends Personaje {

    protected int fuerzaEscudo;

    public Guerrero(String nombre, int vida, int ataqueBase, int posicionX, int posicionY, int fuerzaEscudo) {
        super(nombre, vida, ataqueBase, posicionX, posicionY);   // primero se inicializa el estado heredado
        this.fuerzaEscudo = Math.max(0, fuerzaEscudo);
    }

    /** Golpe de escudo: el daño suma el ataque base y la fuerza del escudo. */
    @Override
    public void realizarAtaqueEspecial(Personaje objetivo) {
        if (objetivo == null || !objetivo.estaVivo()) {
            return;
        }
        int dano = ataqueBase + fuerzaEscudo;
        System.out.printf("%s ejecuta GOLPE DE ESCUDO sobre %s (%d de daño).%n",
                nombre, objetivo.getNombre(), dano);
        objetivo.defender(dano);
    }

    /** El escudo absorbe la mitad de su fuerza en cada golpe recibido. */
    @Override
    protected int calcularMitigacion() {
        return fuerzaEscudo / 2;
    }

    @Override
    public char getSimbolo() {
        return 'G';
    }

    @Override
    public String resumenEstado() {
        return super.resumenEstado() + String.format(" | Escudo: %d", fuerzaEscudo);
    }

    public int getFuerzaEscudo() {
        return fuerzaEscudo;
    }
}
