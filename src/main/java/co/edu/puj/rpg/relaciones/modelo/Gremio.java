package co.edu.puj.rpg.relaciones.modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Gremio de la Arena: extremo "1" de la asociación bidireccional con Personaje (0..*).
 * Mantiene la lista explícita de sus miembros y la sincroniza con el atributo gremio de cada uno.
 */
public class Gremio {

    private final String nombre;
    private final List<Personaje> miembros = new ArrayList<>();

    public Gremio(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El gremio necesita un nombre.");
        }
        this.nombre = nombre.trim();
    }

    /** Registra al personaje y asegura que él también apunte a este gremio. */
    public void agregarMiembro(Personaje p) {
        if (p == null) {
            return;
        }
        if (miembros.contains(p)) {         // ya está: se corta aquí la recursión
            return;
        }
        miembros.add(p);
        if (p.getGremio() != this) {        // sincronización cruzada
            p.unirseAGremio(this);
        }
    }

    /** Retira al personaje y asegura que él deje de apuntar a este gremio. */
    public void retirarMiembro(Personaje p) {
        if (p == null) {
            return;
        }
        if (miembros.remove(p) && p.getGremio() == this) {
            p.abandonarGremio();
        }
    }

    /**
     * Disuelve el gremio. Recorre una COPIA de la lista porque retirar elementos mientras se
     * itera la original lanzaría ConcurrentModificationException.
     */
    public void disolver() {
        for (Personaje p : new ArrayList<>(miembros)) {
            p.abandonarGremio();
        }
    }

    public String getNombre() {
        return nombre;
    }

    /** Vista de solo lectura: los cambios deben pasar por agregarMiembro / retirarMiembro. */
    public List<Personaje> getMiembros() {
        return Collections.unmodifiableList(miembros);
    }

    public int getCantidadMiembros() {
        return miembros.size();
    }

    /**
     * Solo imprime los NOMBRES de los miembros. Llamar a Personaje.toString() aquí provocaría
     * StackOverflowError, porque Personaje.toString() también consulta al gremio.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Gremio[").append(nombre).append(", miembros (").append(miembros.size()).append("): ");
        for (int i = 0; i < miembros.size(); i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(miembros.get(i).getNombre());
        }
        if (miembros.isEmpty()) {
            sb.append("ninguno");
        }
        return sb.append("]").toString();
    }
}
