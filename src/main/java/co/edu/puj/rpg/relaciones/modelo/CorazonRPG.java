package co.edu.puj.rpg.relaciones.modelo;

/**
 * Núcleo de atributos vitales de un Personaje (COMPOSICIÓN 1..1).
 *
 * El Personaje lo crea dentro de su propio constructor y nunca lo entrega al exterior. Los métodos
 * que modifican la salud o el maná tienen acceso de paquete: solo Personaje (mismo paquete) puede
 * invocarlos, así nadie más puede manipular el corazón sin pasar por la entidad principal.
 */
public class CorazonRPG {

    private final int saludBase;
    private final int manaBase;
    private int saludActual;
    private int manaActual;

    public CorazonRPG(int saludBase, int manaBase) {
        if (saludBase <= 0) {
            throw new IllegalArgumentException("La salud base debe ser mayor que 0.");
        }
        if (manaBase < 0) {
            throw new IllegalArgumentException("El maná base no puede ser negativo.");
        }
        this.saludBase = saludBase;
        this.manaBase = manaBase;
        this.saludActual = saludBase;
        this.manaActual = manaBase;
    }

    // ---------- Getters (lectura pública) ----------

    public int getSaludBase() {
        return saludBase;
    }

    public int getManaBase() {
        return manaBase;
    }

    public int getSaludActual() {
        return saludActual;
    }

    public int getManaActual() {
        return manaActual;
    }

    public boolean estaVivo() {
        return saludActual > 0;
    }

    // ---------- Manipulación interna de vida y maná (acceso de paquete) ----------

    /** Resta salud sin bajar de cero. */
    void recibirDano(int cantidad) {
        if (cantidad <= 0) {
            return;
        }
        saludActual = Math.max(0, saludActual - cantidad);
    }

    /** Recupera salud sin superar la salud base. Devuelve lo realmente recuperado. */
    int curar(int cantidad) {
        if (cantidad <= 0 || !estaVivo()) {
            return 0;
        }
        int antes = saludActual;
        saludActual = Math.min(saludBase, saludActual + cantidad);
        return saludActual - antes;
    }

    /** Gasta maná si alcanza; devuelve false (sin cambios) cuando no hay suficiente. */
    boolean gastarMana(int cantidad) {
        if (cantidad <= 0 || cantidad > manaActual) {
            return false;
        }
        manaActual -= cantidad;
        return true;
    }

    /** Recupera maná sin superar el maná base. */
    void recuperarMana(int cantidad) {
        if (cantidad <= 0) {
            return;
        }
        manaActual = Math.min(manaBase, manaActual + cantidad);
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append("Corazón[salud: ").append(saludActual).append('/').append(saludBase)
                .append(", maná: ").append(manaActual).append('/').append(manaBase).append("]")
                .toString();
    }
}
