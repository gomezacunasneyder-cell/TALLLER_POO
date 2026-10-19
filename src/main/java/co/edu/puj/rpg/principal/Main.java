package co.edu.puj.rpg.principal;

import co.edu.puj.rpg.modelo.Personaje;
import co.edu.puj.rpg.modelo.enemigo.Enemigo;

import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Simulador de Batalla / Arena de Héroes.
 * Main solo usa la API pública (getters, setters y métodos de comportamiento):
 * no puede tocar ningún atributo privado de Personaje ni de Enemigo.
 */
public class Main {

    private static final double COSTO_POCION_GRANDE = 40.0;
    private static final int CURACION_POCION_NORMAL = 30;
    private static final int CURACION_POCION_GRANDE = 60;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        Personaje heroe = new Personaje("Aragorn", 100, 15, 50.0);
        Enemigo[] enemigos = {
                new Enemigo("Goblin", 40, 6, 1, 30.0, 40),
                new Enemigo("Orco", 70, 10, 2, 60.0, 70),
                new Enemigo("Dragón", 120, 16, 3, 150.0, 150)
        };

        StringBuilder historial = new StringBuilder();
        boolean huyo = false;

        System.out.printf("=== ARENA DE HÉROES ===%n");
        System.out.printf("Bienvenido, %s. Te esperan %d enemigos.%n", heroe.getNombre(), enemigos.length);

        for (int i = 0; i < enemigos.length && heroe.isEstaVivo() && !huyo; i++) {
            Enemigo enemigo = enemigos[i];
            System.out.printf("%n>>> COMBATE %d: %s (Nivel %d) aparece en la arena <<<%n",
                    i + 1, enemigo.getNombre(), enemigo.getNivel());
            historial.append(String.format("-- Combate %d contra %s --%n", i + 1, enemigo.getNombre()));

            // Bucle principal: continúa mientras ambos sigan vivos (y el jugador no huya)
            do {
                mostrarMenu();
                int opcion = leerEntero(scanner);
                boolean turnoConsumido = false;

                if (opcion < 1 || opcion > 5) {
                    System.out.printf("Opción inválida. Elige un número del 1 al 5.%n");
                } else {
                    switch (opcion) {
                        case 1:
                            turnoConsumido = atacar(heroe, enemigo, historial);
                            break;
                        case 2:
                            turnoConsumido = usarPocion(heroe, historial);
                            break;
                        case 3:
                            mostrarEstado(heroe, enemigo);
                            break;
                        case 4:
                            huyo = true;
                            historial.append(String.format("%s huye del combate.%n", heroe.getNombre()));
                            break;
                        case 5:
                            abrirTienda(scanner, heroe, historial);
                            break;
                    }
                }

                // Turno del enemigo, solo si el jugador gastó su turno y el enemigo sigue vivo
                if (turnoConsumido && enemigo.isEstaVivo()) {
                    int vidaAntes = heroe.getPuntosVida();
                    enemigo.atacar(heroe);
                    historial.append(String.format("%s ataca y causa %d de daño a %s.%n",
                            enemigo.getNombre(), vidaAntes - heroe.getPuntosVida(), heroe.getNombre()));
                    if (!heroe.isEstaVivo()) {
                        historial.append(String.format("%s ha sido derrotado.%n", heroe.getNombre()));
                    }
                }

                // Al final de cada ronda se imprime el historial acumulado
                if (turnoConsumido) {
                    System.out.printf("%n--- HISTORIAL DE BATALLA ---%n%s", historial);
                    System.out.printf("Vida: %d/%d | Oro: %.2f | Nivel: %d%n",
                            heroe.getPuntosVida(), heroe.getVidaMaxima(), heroe.getOro(), heroe.getNivel());
                }
            } while (heroe.isEstaVivo() && enemigo.isEstaVivo() && !huyo);
        }

        // ---------- Resumen final ----------
        System.out.printf("%n=== FIN DEL JUEGO ===%n");
        if (!heroe.isEstaVivo()) {
            System.out.printf("Has sido derrotado en la arena.%n");
        } else if (huyo) {
            System.out.printf("Huiste del combate. La arena sigue esperándote.%n");
        } else {
            System.out.printf("¡Victoria! Derrotaste a todos los enemigos.%n");
        }
        System.out.printf("%s | Nivel: %d | Vida: %d/%d | Oro: %.2f%n",
                heroe.getNombre(), heroe.getNivel(), heroe.getPuntosVida(),
                heroe.getVidaMaxima(), heroe.getOro());

        scanner.close();
    }

    // ---------- Acciones de combate ----------

    private static boolean atacar(Personaje heroe, Enemigo enemigo, StringBuilder historial) {
        int nivelAntes = heroe.getNivel();
        double oroAntes = heroe.getOro();

        heroe.atacar(enemigo);

        if (heroe.isUltimoGolpeCritico()) {
            historial.append(String.format("¡GOLPE CRÍTICO! %s inflige %d de daño a %s.%n",
                    heroe.getNombre(), heroe.getUltimoDano(), enemigo.getNombre()));
        } else {
            historial.append(String.format("%s inflige %d de daño a %s.%n",
                    heroe.getNombre(), heroe.getUltimoDano(), enemigo.getNombre()));
        }

        if (!enemigo.isEstaVivo()) {
            historial.append(String.format("%s ha sido derrotado. Botín: %.2f de oro.%n",
                    enemigo.getNombre(), heroe.getOro() - oroAntes));
            if (heroe.getNivel() > nivelAntes) {
                historial.append(String.format("¡%s sube al nivel %d!%n", heroe.getNombre(), heroe.getNivel()));
            }
        }
        return true;
    }

    private static boolean usarPocion(Personaje heroe, StringBuilder historial) {
        if (heroe.getPuntosVida() == heroe.getVidaMaxima()) {
            System.out.printf("Ya tienes la vida al máximo. No gastas la poción.%n");
            return false;
        }

        boolean grande;
        if (heroe.getPocionesNormales() > 0) {
            grande = false;
        } else if (heroe.getPocionesGrandes() > 0) {
            grande = true;
        } else {
            System.out.printf("No te quedan pociones. Visita la tienda (opción 5).%n");
            return false;
        }

        heroe.consumirPocion(grande);
        int vidaAntes = heroe.getPuntosVida();
        heroe.usarPocion(grande ? CURACION_POCION_GRANDE : CURACION_POCION_NORMAL);
        historial.append(String.format("%s usa una poción %s y recupera %d de vida.%n",
                heroe.getNombre(), grande ? "grande" : "normal", heroe.getPuntosVida() - vidaAntes));
        return true;
    }

    // ---------- Reto 1: Tienda ----------

    private static void abrirTienda(Scanner scanner, Personaje heroe, StringBuilder historial) {
        int opcion;
        do {
            System.out.printf("%n=== TIENDA ===%n");
            System.out.printf("Oro disponible: %.2f%n", heroe.getOro());
            System.out.printf("1. Poción Grande de Salud (restaura %d de vida) - %.2f de oro%n",
                    CURACION_POCION_GRANDE, COSTO_POCION_GRANDE);
            System.out.printf("2. Salir de la tienda%n");
            System.out.printf("Elige una opción: ");
            opcion = leerEntero(scanner);

            if (opcion == 1) {
                double nuevoSaldo = heroe.getOro() - COSTO_POCION_GRANDE;
                if (heroe.setOro(nuevoSaldo)) {
                    heroe.agregarPocionGrande();
                    System.out.printf("Compra exitosa. Saldo restante: %.2f%n", heroe.getOro());
                    historial.append(String.format("%s compra una Poción Grande de Salud.%n", heroe.getNombre()));
                } else {
                    System.out.printf("Saldo insuficiente: necesitas %.2f y tienes %.2f.%n",
                            COSTO_POCION_GRANDE, heroe.getOro());
                }
            } else if (opcion != 2) {
                System.out.printf("Opción inválida. Elige 1 o 2.%n");
            }
        } while (opcion != 2);
    }

    // ---------- Pantallas y entrada ----------

    private static void mostrarMenu() {
        System.out.printf("%n=== MENÚ DE COMBATE ===%n");
        System.out.printf("1. Atacar%n");
        System.out.printf("2. Usar Poción%n");
        System.out.printf("3. Ver Estado%n");
        System.out.printf("4. Huir%n");
        System.out.printf("5. Tienda%n");
        System.out.printf("Elige una opción: ");
    }

    private static void mostrarEstado(Personaje heroe, Enemigo enemigo) {
        System.out.printf("%n--- ESTADO ---%n");
        System.out.printf("%s | Nivel: %d | Vida: %d/%d | Ataque: %d | Oro: %.2f%n",
                heroe.getNombre(), heroe.getNivel(), heroe.getPuntosVida(), heroe.getVidaMaxima(),
                heroe.getPuntosAtaque(), heroe.getOro());
        System.out.printf("Experiencia: %d/%d | Pociones: %d normales, %d grandes | Vivo: %b%n",
                heroe.getExperiencia(), heroe.experienciaParaSiguienteNivel(),
                heroe.getPocionesNormales(), heroe.getPocionesGrandes(), heroe.isEstaVivo());
        System.out.printf("%s (Nivel %d) | Vida: %d/%d | Ataque: %d%n",
                enemigo.getNombre(), enemigo.getNivel(), enemigo.getPuntosVida(),
                enemigo.getVidaMaxima(), enemigo.getPuntosAtaque());
    }

    /** Lee una línea y la convierte a entero; si no es un número devuelve -1 (opción inválida). */
    private static int leerEntero(Scanner scanner) {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        } catch (NoSuchElementException e) {
            System.out.printf("%nEntrada finalizada. Saliendo del juego.%n");
            System.exit(0);
            return -1;
        }
    }
}
