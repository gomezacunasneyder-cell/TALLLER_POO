package co.edu.puj.rpg.relaciones.modelo;

/**
 * Arma que un Personaje puede tener equipada (asociación UNIDIRECCIONAL, 0..1).
 * El arma NO guarda ninguna referencia a su dueño: la flecha del diagrama solo va de Personaje a Arma.
 */
public class Arma {

    private final String nombre;
    private final int danioBase;

    public Arma(String nombre, int danioBase) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El arma necesita un nombre.");
        }
        if (danioBase < 0) {
            throw new IllegalArgumentException("El daño base no puede ser negativo.");
        }
        this.nombre = nombre.trim();
        this.danioBase = danioBase;
    }

    public String getNombre() {
        return nombre;
    }

    public int getDanioBase() {
        return danioBase;
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append("Arma[").append(nombre)
                .append(", daño base: ").append(danioBase).append("]")
                .toString();
    }
}
