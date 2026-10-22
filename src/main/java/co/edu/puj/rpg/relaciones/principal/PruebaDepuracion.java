package co.edu.puj.rpg.relaciones.principal;

import co.edu.puj.rpg.relaciones.modelo.Arma;
import co.edu.puj.rpg.relaciones.modelo.CorazonRPG;
import co.edu.puj.rpg.relaciones.modelo.Escuadron;
import co.edu.puj.rpg.relaciones.modelo.Gremio;
import co.edu.puj.rpg.relaciones.modelo.Personaje;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

/**
 * Sección 5: reproduce de forma controlada los errores típicos de las relaciones entre clases
 * (StackOverflowError, NullPointerException, desincronización bidireccional y
 * ConcurrentModificationException) y comprueba que el modelo real los evita.
 * Sirve para colocar breakpoints y practicar Evaluate Expression (Alt + F8) en IntelliJ.
 */
public class PruebaDepuracion {

    private static int fallos = 0;

    public static void main(String[] args) {
        casoStackOverflow();
        casoNullPointer();
        casoDesincronizacion();
        casoConcurrentModification();
        casoComposicionYAgregacion();

        System.out.printf("%n=== RESULTADO ===%n");
        if (fallos == 0) {
            System.out.printf("Todas las comprobaciones pasaron.%n");
        } else {
            System.out.printf("Comprobaciones fallidas: %d%n", fallos);
        }
    }

    // ---------- 5.1 StackOverflowError por toString() circular ----------

    /** Versión INCORRECTA: cada toString() llama al toString() del otro lado. */
    private static class PersonajeMalo {
        String nombre = "Malo";
        GremioMalo gremio;

        @Override
        public String toString() {
            return nombre + " en " + gremio;       // llama a GremioMalo.toString()
        }
    }

    private static class GremioMalo {
        List<PersonajeMalo> miembros = new ArrayList<>();

        @Override
        public String toString() {
            return "Gremio" + miembros;            // llama a PersonajeMalo.toString() de cada miembro
        }
    }

    private static void casoStackOverflow() {
        System.out.printf("=== Caso 1: StackOverflowError (toString circular) ===%n");
        PersonajeMalo malo = new PersonajeMalo();
        GremioMalo gremioMalo = new GremioMalo();
        malo.gremio = gremioMalo;
        gremioMalo.miembros.add(malo);

        boolean desbordo = false;
        try {
            malo.toString();
        } catch (StackOverflowError e) {
            desbordo = true;
            System.out.printf("Capturado: StackOverflowError (Personaje.toString -> Gremio.toString -> Personaje.toString ...)%n");
        }
        verificar("La versión circular provoca StackOverflowError", desbordo);

        // Corrección: Gremio.toString() solo imprime los nombres de los miembros
        Gremio gremio = new Gremio("Dragones");
        Personaje p = new Personaje("Aragorn", 3);
        p.unirseAGremio(gremio);
        String texto;
        try {
            texto = p + " // " + gremio;
        } catch (StackOverflowError e) {
            texto = null;
        }
        verificar("La versión corregida imprime sin recursión: " + texto, texto != null);
    }

    // ---------- 5.2 NullPointerException ----------

    private static void casoNullPointer() {
        System.out.printf("%n=== Caso 2: NullPointerException (arma sin equipar) ===%n");
        Personaje p = new Personaje("Legolas", 2);

        boolean lanzo = false;
        try {
            Arma arma = p.getArmaEquipada();       // null: el personaje está desarmado
            int danio = arma.getDanioBase();       // error: se usa la referencia sin comprobarla
            System.out.printf("Daño: %d%n", danio);
        } catch (NullPointerException e) {
            lanzo = true;
            System.out.printf("Capturado: NullPointerException al leer el daño de un arma null%n");
        }
        verificar("Usar armaEquipada sin validar lanza NullPointerException", lanzo);

        // Corrección: calcularAtaqueTotal() comprueba que la referencia no sea null
        verificar("Desarmado: ataque total = nivel x 5 = 10", p.calcularAtaqueTotal() == 10);
        p.equiparArma(new Arma("Arco largo", 12));
        verificar("Con arma: 10 + 12 = 22", p.calcularAtaqueTotal() == 22);
        p.desequiparArma();
        p.desequiparArma();                        // segundo intento: no debe fallar
        verificar("Tras desequipar vuelve a 10", p.calcularAtaqueTotal() == 10);
        verificar("equiparArma(null) no cambia el estado", sinCambioAlEquiparNulo(p));
    }

    private static boolean sinCambioAlEquiparNulo(Personaje p) {
        p.equiparArma(null);
        return p.getArmaEquipada() == null;
    }

    // ---------- 5.2 Desincronización bidireccional ----------

    private static class PersonajeDesincronizado {
        GremioDesincronizado gremio;

        void unirse(GremioDesincronizado g) {
            this.gremio = g;                       // error: solo actualiza UN lado
        }
    }

    private static class GremioDesincronizado {
        List<PersonajeDesincronizado> miembros = new ArrayList<>();
    }

    private static void casoDesincronizacion() {
        System.out.printf("%n=== Caso 3: desincronización bidireccional ===%n");
        PersonajeDesincronizado malo = new PersonajeDesincronizado();
        GremioDesincronizado gremioMalo = new GremioDesincronizado();
        malo.unirse(gremioMalo);
        boolean incoherente = malo.gremio == gremioMalo && !gremioMalo.miembros.contains(malo);
        verificar("La versión incorrecta deja los dos lados incoherentes", incoherente);

        // Corrección: ambos caminos mantienen la coherencia
        Gremio alfa = new Gremio("Alfa");
        Gremio beta = new Gremio("Beta");
        Personaje a = new Personaje("Gimli", 4);
        Personaje b = new Personaje("Boromir", 4);

        a.unirseAGremio(alfa);
        verificar("Personaje.unirseAGremio sincroniza ambos lados", coherente(a, alfa));

        alfa.agregarMiembro(b);
        verificar("Gremio.agregarMiembro sincroniza ambos lados", coherente(b, alfa));
        verificar("El gremio tiene 2 miembros sin duplicados", alfa.getCantidadMiembros() == 2);

        a.unirseAGremio(alfa);
        verificar("Unirse dos veces al mismo gremio no duplica", alfa.getCantidadMiembros() == 2);

        a.unirseAGremio(beta);
        verificar("Cambiar de gremio lo retira del anterior", !alfa.getMiembros().contains(a) && coherente(a, beta));

        alfa.retirarMiembro(b);
        verificar("Gremio.retirarMiembro limpia ambos lados", b.getGremio() == null && alfa.getCantidadMiembros() == 0);

        a.unirseAGremio(null);
        verificar("unirseAGremio(null) se ignora", a.getGremio() == beta);

        beta.disolver();
        verificar("disolver() deja a todos sin gremio", a.getGremio() == null && beta.getCantidadMiembros() == 0);
    }

    /** Equivale a evaluar personaje.getGremio() y gremio.getMiembros() con Alt + F8. */
    private static boolean coherente(Personaje p, Gremio g) {
        return p.getGremio() == g && g.getMiembros().contains(p);
    }

    // ---------- 5.2 ConcurrentModificationException ----------

    private static void casoConcurrentModification() {
        System.out.printf("%n=== Caso 4: ConcurrentModificationException ===%n");
        List<Personaje> lista = new ArrayList<>();
        lista.add(new Personaje("Uno", 1));
        lista.add(new Personaje("Dos", 1));
        lista.add(new Personaje("Tres", 1));

        boolean lanzo = false;
        try {
            for (Personaje p : lista) {
                if (p.getNombre().equals("Uno")) {
                    lista.remove(p);               // error: se modifica la lista mientras se recorre
                }
            }
        } catch (ConcurrentModificationException e) {
            lanzo = true;
            System.out.printf("Capturado: ConcurrentModificationException%n");
        }
        verificar("Remover dentro del for-each lanza ConcurrentModificationException", lanzo);

        // Corrección: recorrer una copia o usar removeIf
        List<Personaje> otra = new ArrayList<>(List.of(new Personaje("Uno", 1), new Personaje("Dos", 1)));
        for (Personaje p : new ArrayList<>(otra)) {
            if (p.getNombre().equals("Uno")) {
                otra.remove(p);
            }
        }
        verificar("Recorrer una copia permite remover", otra.size() == 1);
        otra.removeIf(p -> p.getNombre().equals("Dos"));
        verificar("removeIf también es seguro", otra.isEmpty());
    }

    // ---------- Reglas de composición, agregación y asociación unidireccional ----------

    private static void casoComposicionYAgregacion() {
        System.out.printf("%n=== Caso 5: composición, agregación y asociación unidireccional ===%n");

        // Composición: ningún método público entrega o reemplaza el CorazonRPG
        boolean exponeCorazon = false;
        for (Method m : Personaje.class.getMethods()) {
            if (m.getReturnType() == CorazonRPG.class) {
                exponeCorazon = true;
            }
            for (Class<?> parametro : m.getParameterTypes()) {
                if (parametro == CorazonRPG.class) {
                    exponeCorazon = true;
                }
            }
        }
        verificar("Personaje no tiene getter ni setter de CorazonRPG", !exponeCorazon);

        boolean corazonFinalYPrivado = false;
        try {
            Field campo = Personaje.class.getDeclaredField("corazon");
            corazonFinalYPrivado = Modifier.isPrivate(campo.getModifiers()) && Modifier.isFinal(campo.getModifiers());
        } catch (NoSuchFieldException e) {
            corazonFinalYPrivado = false;
        }
        verificar("El atributo corazon es private final", corazonFinalYPrivado);

        boolean mutadoresNoPublicos = true;
        for (String nombre : new String[]{"recibirDano", "curar", "recuperarMana"}) {
            try {
                Method m = CorazonRPG.class.getDeclaredMethod(nombre, int.class);
                mutadoresNoPublicos &= !Modifier.isPublic(m.getModifiers());
            } catch (NoSuchMethodException e) {
                mutadoresNoPublicos = false;
            }
        }
        verificar("Los métodos que mutan el corazón no son públicos", mutadoresNoPublicos);

        Personaje p1 = new Personaje("Thorin", 5);
        Personaje p2 = new Personaje("Thorin", 5);
        p1.recibirDano(30);
        verificar("Cada personaje tiene su propio corazón (p1: 110, p2: 140)",
                p1.getSaludActual() == 110 && p2.getSaludActual() == 140);

        // Asociación unidireccional: Arma no referencia a Personaje
        boolean armaConoceDueno = false;
        for (Field campo : Arma.class.getDeclaredFields()) {
            if (campo.getType() == Personaje.class) {
                armaConoceDueno = true;
            }
        }
        verificar("Arma no guarda ninguna referencia a Personaje", !armaConoceDueno);

        // Agregación: los personajes sobreviven a su escuadrón
        Escuadron escuadron = new Escuadron("Vanguardia");
        Personaje gandalf = new Personaje("Gandalf", 10);
        gandalf.equiparArma(new Arma("Bastón", 20));
        gandalf.recibirDano(15);
        escuadron.reclutarPersonaje(gandalf);
        escuadron.reclutarPersonaje(gandalf);
        escuadron.reclutarPersonaje(null);
        verificar("Reclutar duplicado o null no altera el escuadrón", escuadron.getCantidadMiembros() == 1);
        escuadron.disolver();
        escuadron = null;
        verificar("Tras disolver el escuadrón, el personaje conserva vida y arma",
                gandalf.getSaludActual() == 175 && gandalf.getArmaEquipada() != null);
    }

    private static void verificar(String descripcion, boolean correcto) {
        if (!correcto) {
            fallos++;
        }
        System.out.printf("[%s] %s%n", correcto ? "OK" : "FALLA", descripcion);
    }
}
