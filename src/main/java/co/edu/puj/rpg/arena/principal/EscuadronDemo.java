package co.edu.puj.rpg.arena.principal;

import co.edu.puj.rpg.arena.modelo.Enemigo;
import co.edu.puj.rpg.arena.modelo.Guerrero;
import co.edu.puj.rpg.arena.modelo.Mago;
import co.edu.puj.rpg.arena.modelo.Personaje;

import java.util.ArrayList;
import java.util.List;

/**
 * Sección 2.2: operaciones de escuadrón con List&lt;Personaje&gt; y recorrido polimórfico.
 * Declaración, add, get/size, removeIf y for-each, usando solo las clases de la jerarquía.
 */
public class EscuadronDemo {

    public static void main(String[] args) {
        // 1. Declaración e inicialización con la referencia abstracta List
        List<Personaje> escuadron = new ArrayList<>();

        // 2. Adición de elementos polimórficos
        escuadron.add(new Guerrero("Thorin", 120, 15, 0, 0, 10));
        escuadron.add(new Mago("Gandalf", 80, 25, 0, 1, 50));

        // 3. Consulta e inspección
        System.out.printf("Unidades activas: %d%n", escuadron.size());
        System.out.printf("Primera unidad: %s%n", escuadron.get(0).getNombre());

        // 5. Recorrido polimórfico: cada subclase ejecuta su propio ataque especial
        Enemigo objetivo = new Enemigo("Orco", 60, 12, 0, 4);
        for (Personaje combatiente : escuadron) {
            combatiente.realizarAtaqueEspecial(objetivo);
        }

        // 4. Eliminación de caídos
        escuadron.add(objetivo);
        escuadron.removeIf(p -> !p.estaVivo());
        System.out.printf("Unidades tras la limpieza: %d%n", escuadron.size());
    }
}