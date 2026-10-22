package co.edu.puj.rpg.relaciones.modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Escuadrón táctico (AGREGACIÓN 0..*): agrupa Personajes que se crean FUERA de él y que siguen
 * existiendo, con su estado intacto, si el escuadrón se expulsa, se disuelve o se descarta.
 */
public class Escuadron {

    private final String nombre;
    private final List<Personaje> miembros;   // Agregación (0..*)

    public Escuadron(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El escuadrón necesita un nombre.");
        }
        this.nombre = nombre.trim();
        this.miembros = new ArrayList<>();    // inicialización de la colección
    }

    public void reclutarPersonaje(Personaje p) {
        if (p == null) {
            System.out.printf("No se puede reclutar un personaje nulo.%n");
            return;
        }
        if (miembros.contains(p)) {
            System.out.printf("%s ya pertenece al escuadrón %s.%n", p.getNombre(), nombre);
            return;
        }
        miembros.add(p);                      // el personaje se agrega tal cual fue creado
        System.out.printf("%s se une al escuadrón %s.%n", p.getNombre(), nombre);
    }

    public void expulsarPersonaje(Personaje p) {
        if (p != null && miembros.remove(p)) {
            System.out.printf("%s sale del escuadrón %s (conserva su estado).%n", p.getNombre(), nombre);
        } else {
            System.out.printf("Ese personaje no pertenece al escuadrón %s.%n", nombre);
        }
    }

    /** Disuelve el escuadrón: la lista se vacía, pero los personajes siguen intactos en memoria. */
    public void disolver() {
        miembros.clear();
    }

    public String getNombre() {
        return nombre;
    }

    public List<Personaje> getMiembros() {
        return Collections.unmodifiableList(miembros);
    }

    public int getCantidadMiembros() {
        return miembros.size();
    }

    /** Tabla de estadísticas del escuadrón construida con StringBuilder. */
    public String generarReporte() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("--- Escuadrón %s (%d miembros) ---%n", nombre, miembros.size()));
        if (miembros.isEmpty()) {
            sb.append(String.format("(sin miembros)%n"));
            return sb.toString();
        }
        sb.append(String.format("%-12s %5s %8s %10s %10s%n", "Nombre", "Nivel", "Ataque", "Salud", "Maná"));
        for (Personaje p : miembros) {
            sb.append(String.format("%-12s %5d %8d %4d/%-5d %4d/%-5d%n",
                    p.getNombre(), p.getNivel(), p.calcularAtaqueTotal(),
                    p.getSaludActual(), p.getSaludMaxima(), p.getManaActual(), p.getManaMaximo()));
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Escuadron[").append(nombre).append(", miembros (").append(miembros.size()).append("): ");
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
