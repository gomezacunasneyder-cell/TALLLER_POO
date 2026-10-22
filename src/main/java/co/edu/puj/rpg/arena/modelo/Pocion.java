package co.edu.puj.rpg.arena.modelo;

/** Ítem de soporte que se guarda en el Inventario&lt;Pocion&gt; y recupera vida. */
public class Pocion {

    private final String nombre;
    private final int curacion;

    public Pocion(String nombre, int curacion) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("La poción necesita un nombre.");
        }
        if (curacion <= 0) {
            throw new IllegalArgumentException("La curación debe ser mayor que 0.");
        }
        this.nombre = nombre.trim();
        this.curacion = curacion;
    }

    public String getNombre() {
        return nombre;
    }

    public int getCuracion() {
        return curacion;
    }
}
