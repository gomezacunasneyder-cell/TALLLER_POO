package co.edu.puj.rpg.arena.modelo;

/**
 * Clase base abstracta de todos los combatientes (sección 1.2).
 *
 * Se usa clase abstracta y no interfaz porque los personajes comparten ESTADO (nombre, vida,
 * ataqueBase, posicionX, posicionY) y comportamiento concreto reutilizable (estaVivo, recibirDano).
 * Las interfaces no pueden tener atributos de instancia.
 *
 * Convención del tablero: posicionX es el índice de FILA y posicionY el de COLUMNA, de modo que
 * la celda del personaje es tablero[posicionX][posicionY] (como en la guía de depuración 5.2).
 */
public abstract class Personaje implements ICombatiente {

    private static final int PORCENTAJE_FURIA = 150;   // modo furia: 150 % del ataque base

    // ---------- Atributos protegidos exigidos por la guía ----------
    protected String nombre;
    protected int vida;
    protected int ataqueBase;
    protected int posicionX;
    protected int posicionY;

    // Apoyo: tope de curación (no cambia nunca después de crear el personaje)
    private final int vidaMaxima;

    protected Personaje(String nombre, int vida, int ataqueBase, int posicionX, int posicionY) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del personaje no puede estar vacío.");
        }
        if (vida <= 0) {
            throw new IllegalArgumentException("La vida inicial debe ser mayor que 0.");
        }
        if (ataqueBase < 0) {
            throw new IllegalArgumentException("El ataque base no puede ser negativo.");
        }
        this.nombre = nombre.trim();
        this.vida = vida;
        this.vidaMaxima = vida;
        this.ataqueBase = ataqueBase;
        this.posicionX = posicionX;
        this.posicionY = posicionY;
    }

    // ---------- Método abstracto: cada subclase define su ataque único ----------

    public abstract void realizarAtaqueEspecial(Personaje objetivo);

    /** Letra con la que se dibuja al personaje en el tablero (G, M, E...). */
    public abstract char getSimbolo();

    // ---------- Métodos concretos compartidos ----------

    public boolean estaVivo() {
        return vida > 0;
    }

    /** Descuenta salud garantizando que la vida nunca descienda de cero. */
    public void recibirDano(int cantidad) {
        if (cantidad <= 0) {
            return;
        }
        vida = Math.max(0, vida - cantidad);
    }

    /** Recupera salud sin superar la vida máxima. Devuelve los puntos realmente recuperados. */
    public int recuperarVida(int cantidad) {
        if (cantidad <= 0 || !estaVivo()) {
            return 0;
        }
        int antes = vida;
        vida = Math.min(vidaMaxima, vida + cantidad);
        return vida - antes;
    }

    /** Implementación base de ICombatiente: aplica la mitigación propia de la entidad. */
    @Override
    public void defender(int danoRecibido) {
        if (danoRecibido <= 0 || !estaVivo()) {
            return;
        }
        int danoFinal = Math.max(1, danoRecibido - calcularMitigacion());
        recibirDano(danoFinal);
        System.out.printf("%s recibe %d de daño (mitigado: %d). Vida: %d/%d%n",
                nombre, danoFinal, danoRecibido - danoFinal, vida, vidaMaxima);
    }

    /** Armadura base: 0. Las subclases con protección la redefinen. */
    protected int calcularMitigacion() {
        return 0;
    }

    // ---------- Sobrecarga de métodos (polimorfismo estático) ----------

    /** Ataque normal con el ataque base. */
    public void atacar(Personaje objetivo) {
        lanzarAtaque(objetivo, ataqueBase, "ataca");
    }

    /** Ataque con opción de modo furia (150 % del ataque base). */
    public void atacar(Personaje objetivo, boolean modoFuria) {
        if (modoFuria) {
            lanzarAtaque(objetivo, ataqueBase * PORCENTAJE_FURIA / 100, "ataca EN FURIA a");
        } else {
            atacar(objetivo);
        }
    }

    private void lanzarAtaque(Personaje objetivo, int dano, String accion) {
        if (!estaVivo() || objetivo == null || !objetivo.estaVivo()) {
            return;
        }
        System.out.printf("%s %s %s (%d de daño).%n", nombre, accion, objetivo.getNombre(), dano);
        objetivo.defender(dano);
    }

    /** Gancho que se ejecuta al inicio del turno de cada personaje (por defecto no hace nada). */
    public void iniciarTurno() {
        // Sin efecto; el Mago lo redefine para regenerar maná.
    }

    /** Línea de estado para el panel de la consola (cada subclase añade su recurso propio). */
    public String resumenEstado() {
        return String.format("[%c] %-8s Vida: %3d/%3d | Ataque: %2d | Pos: (%d,%d)",
                getSimbolo(), nombre, vida, vidaMaxima, ataqueBase, posicionX, posicionY);
    }

    // ---------- Accesores ----------

    public String getNombre() {
        return nombre;
    }

    public int getVida() {
        return vida;
    }

    public int getVidaMaxima() {
        return vidaMaxima;
    }

    public int getAtaqueBase() {
        return ataqueBase;
    }

    public int getPosicionX() {
        return posicionX;
    }

    public int getPosicionY() {
        return posicionY;
    }

    /** Actualiza las coordenadas; la validación de límites es responsabilidad del Tablero. */
    public void setPosicion(int posicionX, int posicionY) {
        this.posicionX = posicionX;
        this.posicionY = posicionY;
    }
}
