package co.edu.puj.rpg.modelo;

import co.edu.puj.rpg.modelo.enemigo.Enemigo;

/**
 * Héroe del simulador de batalla.
 * Todos los atributos son private: el estado solo cambia mediante la API pública.
 */
public class Personaje {

    // ---------- Atributos privados (encapsulamiento) ----------
    private String nombre;
    private int puntosVida;
    private int vidaMaxima;
    private int puntosAtaque;
    private int nivel;
    private double oro;
    private boolean estaVivo;

    // Atributos de apoyo para los retos de expansión
    private int experiencia;
    private int pocionesNormales;
    private int pocionesGrandes;
    private int ultimoDano;
    private boolean ultimoGolpeCritico;

    // ---------- Constructores ----------

    /** Constructor completo: usa this para distinguir parámetros de atributos. */
    public Personaje(String nombre, int vidaMaxima, int puntosAtaque, double oro) {
        this.nombre = nombre;
        this.vidaMaxima = vidaMaxima > 0 ? vidaMaxima : 1;
        this.puntosAtaque = puntosAtaque > 0 ? puntosAtaque : 1;
        this.nivel = 1;
        this.experiencia = 0;
        this.pocionesNormales = 2;
        this.pocionesGrandes = 0;
        this.estaVivo = true;
        this.oro = 0.0;
        setOro(oro);                       // el oro inicial también pasa por validación
        setPuntosVida(this.vidaMaxima);    // la vida inicial pasa por el setter validado
    }

    /** Constructor por defecto: delega en el completo con valores base. */
    public Personaje() {
        this("Héroe", 100, 10, 0.0);
    }

    // ---------- API pública de comportamiento ----------

    /** Ataca a un enemigo. Con 15% de probabilidad el golpe es crítico (daño x2). */
    public void atacar(Enemigo enemigo) {
        ultimoDano = 0;
        ultimoGolpeCritico = false;
        if (!estaVivo || enemigo == null || !enemigo.isEstaVivo()) {
            return;
        }
        int dano = puntosAtaque;
        if (Math.random() < 0.15) {
            dano *= 2;
            ultimoGolpeCritico = true;
        }
        ultimoDano = dano;
        enemigo.recibirDano(dano);

        if (!enemigo.isEstaVivo()) {       // recompensas al derrotar al enemigo
            setOro(oro + enemigo.getOro());
            ganarExperiencia(enemigo.getExperienciaRecompensa());
        }
    }

    /** Recupera salud; el setter se encarga de no superar vidaMaxima. */
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

    /** Suma experiencia y sube de nivel cada vez que supera el umbral (nivel x 50). */
    public void ganarExperiencia(int puntos) {
        if (puntos <= 0) {
            return;
        }
        experiencia += puntos;
        while (experiencia >= experienciaParaSiguienteNivel()) {
            experiencia -= experienciaParaSiguienteNivel();
            setNivel(nivel + 1);           // setNivel es protected: solo se usa dentro de la clase
            vidaMaxima += 10;
            puntosAtaque += 2;
            setPuntosVida(vidaMaxima);     // al subir de nivel recupera toda la vida
        }
    }

    public int experienciaParaSiguienteNivel() {
        return nivel * 50;
    }

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

    /** Entrega una poción grande al inventario (se llama solo tras una compra aprobada). */
    public void agregarPocionGrande() {
        pocionesGrandes++;
    }

    // ---------- Setters con lógica de validación ----------

    public void setPuntosVida(int vida) {
        if (vida <= 0) {
            this.puntosVida = 0;
            this.estaVivo = false;
        } else if (vida > vidaMaxima) {
            this.puntosVida = vidaMaxima;
            this.estaVivo = true;
        } else {
            this.puntosVida = vida;
            this.estaVivo = true;
        }
    }

    /** Rechaza saldos negativos sin alterar el atributo. Devuelve true si se aplicó. */
    public boolean setOro(double oro) {
        if (oro < 0.0) {
            return false;
        }
        this.oro = oro;
        return true;
    }

    /** Protegido: solo el propio personaje (o una subclase) puede cambiar el nivel. */
    protected void setNivel(int nivel) {
        if (nivel < 1) {
            return;                        // valor inválido: conserva el nivel actual
        }
        this.nivel = nivel;
    }

    // ---------- Getters (API de lectura) ----------

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
