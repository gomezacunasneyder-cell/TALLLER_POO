package co.edu.puj.rpg.relaciones.modelo;

/**
 * Personaje de la Arena de Héroes (Taller Unidad 2.5: Relaciones entre Clases).
 *
 *  - Arma:       asociación unidireccional 0..1 (puede ser null: combate desarmado).
 *  - Gremio:     asociación bidireccional, un personaje pertenece como máximo a un gremio.
 *  - CorazonRPG: composición 1..1; se crea en el constructor y no existe ningún setter ni getter
 *                que entregue o reemplace la instancia.
 */
public class Personaje {

    private static final int SALUD_BASE_INICIAL = 100;
    private static final int MANA_BASE_INICIAL = 50;
    private static final int SALUD_POR_NIVEL = 10;
    private static final int MANA_POR_NIVEL = 5;
    private static final int ATAQUE_POR_NIVEL = 5;

    private final String nombre;
    private final int nivel;
    private Arma armaEquipada;              // Asociación unidireccional (0..1)
    private Gremio gremio;                  // Asociación bidireccional (0..1 en este extremo)
    private final CorazonRPG corazon;       // Composición (1..1)

    public Personaje(String nombre, int nivel) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El personaje necesita un nombre.");
        }
        if (nivel < 1) {
            throw new IllegalArgumentException("El nivel mínimo es 1.");
        }
        this.nombre = nombre.trim();
        this.nivel = nivel;
        this.armaEquipada = null;           // Inicialmente desarmado
        this.gremio = null;                 // Inicialmente sin gremio
        // Composición: el contenedor crea su componente (nunca se recibe desde fuera)
        this.corazon = new CorazonRPG(
                SALUD_BASE_INICIAL + SALUD_POR_NIVEL * (nivel - 1),
                MANA_BASE_INICIAL + MANA_POR_NIVEL * (nivel - 1));
    }

    // ---------- 2.1 Asociación unidireccional: Personaje -> Arma ----------

    public void equiparArma(Arma nuevaArma) {
        if (nuevaArma == null) {
            System.out.printf("%s no puede equipar un arma nula.%n", nombre);
            return;
        }
        this.armaEquipada = nuevaArma;
        System.out.printf("%s equipa %s (+%d de daño).%n",
                nombre, nuevaArma.getNombre(), nuevaArma.getDanioBase());
    }

    public void desequiparArma() {
        if (this.armaEquipada != null) {
            Arma retirada = this.armaEquipada;
            this.armaEquipada = null;
            System.out.printf("%s guarda %s y queda desarmado.%n", nombre, retirada.getNombre());
        } else {
            System.out.printf("%s no tiene un arma para retirar.%n", nombre);
        }
    }

    public int calcularAtaqueTotal() {
        int ataqueBase = this.nivel * ATAQUE_POR_NIVEL;
        if (this.armaEquipada != null) {    // control obligatorio contra NullPointerException
            ataqueBase += this.armaEquipada.getDanioBase();
        }
        return ataqueBase;
    }

    // ---------- 2.2 Asociación bidireccional con sincronización cruzada: Personaje <-> Gremio ----------

    public void unirseAGremio(Gremio nuevoGremio) {
        if (nuevoGremio == null || nuevoGremio == this.gremio) {
            return;
        }
        if (this.gremio != null) {          // validación de permanencia previa
            abandonarGremio();
        }
        this.gremio = nuevoGremio;          // actualización de la referencia propia
        nuevoGremio.agregarMiembro(this);   // sincronización cruzada (el gremio evita la recursión)
        System.out.printf("%s se une al gremio %s.%n", nombre, nuevoGremio.getNombre());
    }

    public void abandonarGremio() {
        if (this.gremio == null) {
            return;
        }
        Gremio anterior = this.gremio;
        this.gremio = null;                 // primero se limpia el lado propio...
        anterior.retirarMiembro(this);      // ...y luego el gremio retira al personaje
        System.out.printf("%s abandona el gremio %s.%n", nombre, anterior.getNombre());
    }

    // ---------- Acciones que operan sobre el corazón (mediación del contenedor) ----------

    public void recibirDano(int cantidad) {
        corazon.recibirDano(cantidad);
    }

    public int curar(int cantidad) {
        return corazon.curar(cantidad);
    }

    public boolean gastarMana(int cantidad) {
        return corazon.gastarMana(cantidad);
    }

    public void recuperarMana(int cantidad) {
        corazon.recuperarMana(cantidad);
    }

    public boolean estaVivo() {
        return corazon.estaVivo();
    }

    // ---------- Accesores ----------

    public String getNombre() {
        return nombre;
    }

    public int getNivel() {
        return nivel;
    }

    public Arma getArmaEquipada() {
        return armaEquipada;
    }

    public Gremio getGremio() {
        return gremio;
    }

    public int getSaludActual() {
        return corazon.getSaludActual();
    }

    public int getSaludMaxima() {
        return corazon.getSaludBase();
    }

    public int getManaActual() {
        return corazon.getManaActual();
    }

    public int getManaMaximo() {
        return corazon.getManaBase();
    }

    // ---------- Reportes con StringBuilder ----------

    /** Resumen de una línea. Del gremio solo imprime su NOMBRE: así no hay recursión circular. */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(nombre).append(" (nivel ").append(nivel).append(")");
        sb.append(" | ").append(corazon);
        sb.append(" | Arma: ").append(armaEquipada != null ? armaEquipada.getNombre() : "Desarmado");
        sb.append(" | Gremio: ").append(gremio != null ? gremio.getNombre() : "Sin gremio");
        return sb.toString();
    }

    /** Estado detallado (opción 5 del menú): personaje, corazón, arma y gremio. */
    public String generarReporteDetallado() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("=== %s ===%n", nombre));
        sb.append(String.format("Nivel:          %d%n", nivel));
        sb.append(String.format("Ataque total:   %d%n", calcularAtaqueTotal()));
        sb.append(String.format("Estado:         %s%n", estaVivo() ? "Vivo" : "Caído"));
        sb.append(String.format("Corazón RPG:    salud %d/%d | maná %d/%d%n",
                corazon.getSaludActual(), corazon.getSaludBase(),
                corazon.getManaActual(), corazon.getManaBase()));
        if (armaEquipada != null) {
            sb.append(String.format("Arma:           %s (+%d de daño)%n",
                    armaEquipada.getNombre(), armaEquipada.getDanioBase()));
        } else {
            sb.append(String.format("Arma:           ninguna (desarmado)%n"));
        }
        if (gremio != null) {
            sb.append(String.format("Gremio:         %s (%d miembros)%n",
                    gremio.getNombre(), gremio.getCantidadMiembros()));
        } else {
            sb.append(String.format("Gremio:         sin gremio%n"));
        }
        return sb.toString();
    }
}
