package co.edu.puj.rpg.modelo;

import co.edu.puj.rpg.modelo.enemigo.Enemigo;

/**
 * Héroe del simulador de batalla (Taller Unidad 2.2: Encapsulamiento y Modificadores de Acceso).
 *
 * Reglas de diseño aplicadas:
 *  - Todos los atributos son private (sección 2.1 de la guía).
 *  - Después de crear el objeto, el estado solo cambia a través de setters con validación (sección 3.1);
 *    el constructor también delega en ellos para no repetir el error del Caso B.
 *  - La API pública se limita a getters, setters y métodos de comportamiento.
 */
public class Personaje {

    // ---------- Constantes de reglas de juego ----------
    private static final double PROBABILIDAD_CRITICO = 0.15;   // Reto 2: 15% de golpe crítico
    private static final int MULTIPLICADOR_CRITICO = 2;         // el ataque duplica su magnitud
    private static final int EXPERIENCIA_POR_NIVEL = 50;        // umbral = nivel x 50
    private static final int BONO_VIDA_POR_NIVEL = 10;
    private static final int BONO_ATAQUE_POR_NIVEL = 2;
    private static final int POCIONES_NORMALES_INICIALES = 2;

    // ---------- Atributos privados exigidos por la guía (sección 2.1) ----------
    private String nombre;
    private int puntosVida;
    private int vidaMaxima;
    private int puntosAtaque;
    private int nivel;
    private double oro;
    private boolean estaVivo;

    // ---------- Atributos de apoyo para los retos de expansión (también privados) ----------
    private int experiencia;
    private int pocionesNormales;
    private int pocionesGrandes;
    private int ultimoDano;               // daño de la última acción de atacar() (para el historial)
    private boolean ultimoGolpeCritico;   // si la última acción de atacar() fue un golpe crítico

    // ---------- Constructores (sección 2.2) ----------

    /**
     * Constructor completo. Usa this para distinguir cada parámetro del atributo homónimo y
     * delega la inicialización en los setters validados, de modo que ningún valor inválido
     * pueda entrar al objeto (lección del Caso B).
     */
    public Personaje(String nombre, int vidaMaxima, int puntosAtaque, double oro) {
        this.nombre = nombre;
        setVidaMaxima(vidaMaxima);
        setPuntosAtaque(puntosAtaque);
        setNivel(1);
        setOro(oro < 0.0 ? 0.0 : oro);        // un saldo inicial negativo se normaliza a 0.0
        setPuntosVida(this.vidaMaxima);       // el setter fija también estaVivo = true
        this.pocionesNormales = POCIONES_NORMALES_INICIALES;
    }

    /** Constructor por defecto: reutiliza el completo mediante this(...). */
    public Personaje() {
        this("Héroe", 100, 10, 0.0);
    }

    // ---------- API pública de comportamiento (firmas exigidas por la guía) ----------

    /**
     * Ataca a un enemigo. Con 15% de probabilidad el golpe es crítico y su magnitud se duplica
     * antes de aplicarse al enemigo. Al derrotarlo, el héroe cobra su botín y gana experiencia.
     */
    public void atacar(Enemigo enemigo) {
        ultimoDano = 0;
        ultimoGolpeCritico = false;
        if (!estaVivo || enemigo == null || !enemigo.isEstaVivo()) {
            return;
        }

        int dano = puntosAtaque;
        if (Math.random() < PROBABILIDAD_CRITICO) {
            dano *= MULTIPLICADOR_CRITICO;
            ultimoGolpeCritico = true;
        }
        ultimoDano = dano;
        enemigo.recibirDano(dano);

        if (!enemigo.isEstaVivo()) {
            setOro(oro + enemigo.getOro());                 // botín por el setter validado
            ganarExperiencia(enemigo.getExperienciaRecompensa());
        }
    }

    /** Recupera salud; el setter impide superar vidaMaxima. */
    public void usarPocion(int cantidadRestaurada) {
        if (!estaVivo || cantidadRestaurada <= 0) {
            return;
        }
        setPuntosVida(puntosVida + cantidadRestaurada);
    }

    /** Reduce la vida a través del setter correspondiente. */
    public void recibirDano(int cantidadDano) {
        if (cantidadDano <= 0) {
            return;
        }
        setPuntosVida(puntosVida - cantidadDano);
    }

    // ---------- Inventario de pociones (apoyo de la tienda y del menú) ----------

    /** Consume una poción del inventario. Devuelve false si no queda ninguna de ese tipo. */
    public boolean consumirPocion(boolean grande) {
        if (grande) {
            if (pocionesGrandes > 0) {
                pocionesGrandes--;
                return true;
            }
            return false;
        }
        if (pocionesNormales > 0) {
            pocionesNormales--;
            return true;
        }
        return false;
    }

    /** Entrega una Poción Grande. La tienda solo la invoca después de que setOro() aprobó el pago. */
    public void agregarPocionGrande() {
        pocionesGrandes++;
    }

    // ---------- Experiencia y nivel (Reto 2). Es privado: solo se gana derrotando enemigos ----------

    private void ganarExperiencia(int puntos) {
        if (puntos <= 0) {
            return;
        }
        experiencia += puntos;
        // Sube de nivel cuando la experiencia SUPERA el umbral (estrictamente mayor)
        while (experiencia > experienciaParaSiguienteNivel()) {
            experiencia -= experienciaParaSiguienteNivel();
            setNivel(nivel + 1);                           // método protegido, invocado dentro de la clase
            setVidaMaxima(vidaMaxima + BONO_VIDA_POR_NIVEL);
            setPuntosAtaque(puntosAtaque + BONO_ATAQUE_POR_NIVEL);
            setPuntosVida(vidaMaxima);                     // al subir de nivel recupera toda la vida
        }
    }

    public int experienciaParaSiguienteNivel() {
        return nivel * EXPERIENCIA_POR_NIVEL;
    }

    // ---------- Setters con lógica de validación (sección 3.1) ----------

    /**
     * Valor menor o igual a 0: la vida queda en 0 y estaVivo pasa a false.
     * Valor mayor que vidaMaxima: la vida queda en vidaMaxima.
     * Valor intermedio: se asigna tal cual y estaVivo permanece en true.
     */
    public final void setPuntosVida(int vida) {
        if (vida <= 0) {
            this.puntosVida = 0;
            this.estaVivo = false;
        } else if (vida > this.vidaMaxima) {
            this.puntosVida = this.vidaMaxima;
            this.estaVivo = true;
        } else {
            this.puntosVida = vida;
            this.estaVivo = true;
        }
    }

    /**
     * El oro nunca puede ser negativo. Si la transacción dejaría un saldo menor a 0.0, se rechaza,
     * se indica "saldo insuficiente" y el atributo no se modifica. Devuelve true si se aplicó el
     * cambio, para que la tienda sepa si puede entregar el artículo.
     */
    public final boolean setOro(double oro) {
        if (oro < 0.0) {
            System.out.printf("Saldo insuficiente: la operación dejaría el oro en %.2f. Operación rechazada.%n", oro);
            return false;
        }
        this.oro = oro;
        return true;
    }

    /**
     * Protegido (Reto 2): solo la propia clase y sus subclases pueden cambiar el nivel.
     * Un valor inferior a 1 se ignora y el nivel conserva su valor actual.
     */
    protected final void setNivel(int nivel) {
        if (nivel < 1) {
            return;
        }
        this.nivel = nivel;
    }

    // Setters privados: cubren los demás atributos para que toda escritura pase por validación
    private void setVidaMaxima(int vidaMaxima) {
        this.vidaMaxima = vidaMaxima > 0 ? vidaMaxima : 1;
    }

    private void setPuntosAtaque(int puntosAtaque) {
        this.puntosAtaque = puntosAtaque > 0 ? puntosAtaque : 1;
    }

    // ---------- Getters: API de lectura oficial (sección 3.2) ----------

    public String getNombre() {
        return nombre;
    }

    public int getPuntosVida() {
        return puntosVida;
    }

    public int getVidaMaxima() {
        return vidaMaxima;
    }

    public int getPuntosAtaque() {
        return puntosAtaque;
    }

    public int getNivel() {
        return nivel;
    }

    public double getOro() {
        return oro;
    }

    public boolean isEstaVivo() {
        return estaVivo;
    }

    // Getters de los atributos de apoyo
    public int getExperiencia() {
        return experiencia;
    }

    public int getPocionesNormales() {
        return pocionesNormales;
    }

    public int getPocionesGrandes() {
        return pocionesGrandes;
    }

    public int getUltimoDano() {
        return ultimoDano;
    }

    public boolean isUltimoGolpeCritico() {
        return ultimoGolpeCritico;
    }
}
