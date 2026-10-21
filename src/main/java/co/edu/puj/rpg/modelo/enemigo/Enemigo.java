package co.edu.puj.rpg.modelo.enemigo;

import co.edu.puj.rpg.modelo.Personaje;

/**
 * Enemigo de la arena. Replica la estructura de atributos privados de Personaje y vive en un
 * subpaquete distinto (co.edu.puj.rpg.modelo.enemigo) para verificar el aislamiento de acceso
 * frente a Main (co.edu.puj.rpg.principal): los miembros private y los de acceso por defecto
 * (package-private) no son visibles desde otro paquete.
 */
public class Enemigo {

    private String nombre;
    private int puntosVida;
    private int vidaMaxima;
    private int puntosAtaque;
    private int nivel;
    private double oro;                 // oro que entrega al ser derrotado
    private boolean estaVivo;
    private int experienciaRecompensa;  // experiencia que entrega al ser derrotado

    public Enemigo(String nombre, int vidaMaxima, int puntosAtaque, int nivel,
                   double oro, int experienciaRecompensa) {
        this.nombre = nombre;
        this.vidaMaxima = vidaMaxima > 0 ? vidaMaxima : 1;
        this.puntosAtaque = puntosAtaque > 0 ? puntosAtaque : 1;
        this.nivel = nivel >= 1 ? nivel : 1;
        this.oro = oro >= 0.0 ? oro : 0.0;
        this.experienciaRecompensa = experienciaRecompensa >= 0 ? experienciaRecompensa : 0;
        setPuntosVida(this.vidaMaxima);   // el setter fija puntosVida y estaVivo: sin asignaciones directas
    }

    // ---------- Comportamiento ----------

    public void atacar(Personaje objetivo) {
        if (!estaVivo || objetivo == null || !objetivo.isEstaVivo()) {
            return;
        }
        objetivo.recibirDano(puntosAtaque);
    }

    public void recibirDano(int cantidadDano) {
        if (!esDanoValido(cantidadDano)) {
            return;
        }
        setPuntosVida(puntosVida - cantidadDano);
    }

    /**
     * Sin modificador de acceso (package-private): solo las clases de este paquete pueden invocarlo.
     * Main, ubicada en otro paquete, no lo ve.
     */
    boolean esDanoValido(int cantidadDano) {
        return cantidadDano > 0;
    }

    // ---------- Setter validado ----------

    public final void setPuntosVida(int vida) {
        if (vida <= 0) {
            this.puntosVida = 0;
            this.estaVivo = false;
        } else if (vida > this.vidaMaxima) {
            this.puntosVida = this.vidaMaxima;
            this.estaVivo = true;
        } else {
            this.puntosVida = vida;
            this.estaVivo = true;
        }
    }

    // ---------- Getters ----------

    public String getNombre() {
        return nombre;
    }

    public int getPuntosVida() {
        return puntosVida;
    }

    public int getVidaMaxima() {
        return vidaMaxima;
    }

    public int getPuntosAtaque() {
        return puntosAtaque;
    }

    public int getNivel() {
        return nivel;
    }

    public double getOro() {
        return oro;
    }

    public boolean isEstaVivo() {
        return estaVivo;
    }

    public int getExperienciaRecompensa() {
        return experienciaRecompensa;
    }
}
