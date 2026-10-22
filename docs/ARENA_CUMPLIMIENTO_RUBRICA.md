# Arena RPG con POO Avanzada, Colecciones y Genéricos – Cumplimiento de rúbrica

Paquete: `co.edu.puj.rpg.arena` (subpaquetes `modelo`, `inventario`, `tablero`, `principal`).
Se creó un paquete nuevo porque el `Personaje` de este taller es **abstracto**; así no pisa el `Personaje` concreto del taller 2.2.
Compilado con `javac -Xlint:all --release 17` → 0 errores, 0 warnings.

## Diagrama UML (sección 1.1)
```
<<interface>> ICombatiente
      ▲  (implementa)
<<abstract>> Personaje
   ├── Guerrero
   ├── Mago
   └── Enemigo   (extensión: es la 'E' del tablero)
```

## Sección 1 – Arquitectura
| Requisito | Dónde |
|---|---|
| Interfaz `ICombatiente` con `void defender(int danoRecibido)` | `modelo/ICombatiente.java` |
| `Personaje` abstracta, implementa `ICombatiente` | `modelo/Personaje.java` |
| Atributos `protected`: nombre, vida, ataqueBase, posicionX, posicionY | `Personaje` |
| `public abstract void realizarAtaqueEspecial(Personaje objetivo)` | `Personaje` |
| `estaVivo()` (vida > 0), `recibirDano(int)` sin bajar de cero, `getNombre()`, `getVida()` | `Personaje` |
| `extends` + `super(nombre, vida, ataqueBase, posX, posY)` en la primera línea | `Guerrero`, `Mago`, `Enemigo` |
| Atributos propios `protected int fuerzaEscudo` / `protected int puntosMana` | `Guerrero`, `Mago` |
| `@Override realizarAtaqueEspecial` (Golpe de escudo / Bola de fuego con maná) | `Guerrero`, `Mago` |
| Sobrecarga `atacar(Personaje)` y `atacar(Personaje, boolean modoFuria)` | `Personaje` |
| ¿Clase abstracta o interfaz? | Abstracta porque hay estado común y lógica reutilizable; la interfaz solo define el contrato `defender()` |

## Sección 2 – ArrayList
`List<Personaje> heroes = new ArrayList<>()` y `List<Enemigo> enemigos` en `principal/Batalla.java`:
`add` (inicializar), `get`/`size` (menús y validación), `removeIf(p -> !p.estaVivo())` (limpieza de bajas) y `for-each` polimórfico en los turnos.

## Sección 3 – Genéricos y tablero
| Requisito | Dónde |
|---|---|
| `Inventario<T>` con `agregar`, `obtener`, `getItems` | `inventario/Inventario.java` (más `quitar`, `size`, `estaVacio`) |
| Uso `Inventario<Pocion>` sin casteo | `Batalla.usarInventario` |
| Matriz `char[5][5]` y `mostrarTablero(char[][])` con bucles anidados | `tablero/Tablero.java` |
| Leyenda G, M, E, C, `.` | `Personaje.getSimbolo()` + `Tablero` |

## Sección 4 – Reto integrador
| Requisito | Dónde |
|---|---|
| Inicialización: mapa, lista de héroes, lista de enemigos, `Inventario<Pocion>` | `Batalla.inicializar` |
| Ciclo `do-while` mientras haya héroes vivos y enemigos activos | `Batalla.iniciar` |
| Render del tablero y vida/maná de cada personaje | `Batalla.mostrarEstado` + `resumenEstado()` |
| Turno del jugador con `for-each`, `Scanner` y `switch`: 1 Atacar, 2 Usar Inventario, 3 Mover | `Batalla.turnoJugador/ejecutarAccion` (opción 4 extra: pasar turno) |
| Turno enemigo automático | `Batalla.turnoEnemigo` |
| Limpieza con `removeIf()` al cierre del turno | `Batalla.iniciar` |
| Victoria: `enemigos.isEmpty()` o el héroe principal alcanza el cofre | `iniciar` / `principalEnCofre` |
| Derrota: vida total <= 0 o `heroes.isEmpty()` | `mostrarResultado` |
| Sin `NullPointerException`: la matriz se rellena con `.` y el movimiento se valida | `Tablero.limpiar/estaDentro/estaLibre` |
| Todo daño se imprime y el tablero se redibuja | `Personaje.defender`, `Tablero.actualizar` |

## Sección 5 – Depuración
`principal/PruebaDepuracion.java` reproduce `IndexOutOfBoundsException`, `ClassCastException` y la instanciación abstracta, y muestra la corrección de cada una.

### Guía en IntelliJ
1. Breakpoint (clic en el margen) en `Personaje.defender`, en `Guerrero/Mago.realizarAtaqueEspecial` y en la línea `do {` de `Batalla.iniciar`.
2. Debug con **Shift + F9**.
3. **F8** (Step Over) y **F7** (Step Into, por ejemplo para entrar en `calcularMitigacion()`).
4. Panel **Variables**: despliega `heroes` y revisa `vida`, `nombre`, `posicionX`, `posicionY`.
5. **Alt + F8** (Evaluate): `heroes.get(0).estaVivo()` o `tablero.getMatriz()[0][1]`.

## Decisiones de interpretación
- **Indexación:** `posicionX` es la fila y `posicionY` la columna (`matriz[posicionX][posicionY]`), igual que en la guía 5.2.
- **Victoria:** se cumple si ocurre cualquiera de las dos condiciones (enemigos eliminados **o** cofre alcanzado por el héroe principal, Thorin). Si tu profesor exige ambas, cambia en `Batalla.iniciar` la condición del `while` y en `mostrarResultado`.
- `Enemigo` es una subclase concreta adicional de `Personaje`; no altera el diagrama de la guía.
