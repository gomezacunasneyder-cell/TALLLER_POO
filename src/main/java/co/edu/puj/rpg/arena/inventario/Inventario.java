package co.edu.puj.rpg.arena.inventario;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Contenedor genérico (sección 3.1): una sola clase sirve para Inventario&lt;Pocion&gt;,
 * Inventario&lt;Equipamiento&gt;, etc. El compilador garantiza que solo entren objetos del tipo T
 * y elimina el casteo explícito al recuperarlos.
 */
public class Inventario<T> {

    private final List<T> items = new ArrayList<>();

    public void agregar(T item) {
        if (item == null) {
            throw new IllegalArgumentException("No se puede guardar un ítem nulo en el inventario.");
        }
        items.add(item);
    }

    public T obtener(int index) {
        return items.get(index);
    }

    /** Retira y devuelve el ítem de esa posición (por ejemplo, al consumir una poción). */
    public T quitar(int index) {
        return items.remove(index);
    }

    public int size() {
        return items.size();
    }

    public boolean estaVacio() {
        return items.isEmpty();
    }

    /** Vista de solo lectura: quien la reciba no puede alterar el inventario por fuera de la clase. */
    public List<T> getItems() {
        return Collections.unmodifiableList(items);
    }
}
