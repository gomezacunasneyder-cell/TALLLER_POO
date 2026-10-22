# Relaciones entre Clases en Java (Unidad 2.5) – Cumplimiento de rúbrica

Paquete: `co.edu.puj.rpg.relaciones` (subpaquetes `modelo` y `principal`), dentro del mismo proyecto RPG.
Se agrega en una carpeta propia para que este `Personaje` (nombre, nivel, arma, gremio, corazón) no choque con los `Personaje` de los talleres 2.2 y de la Arena.
Compilado con `javac -Xlint:all --release 17` → 0 errores, 0 warnings.

## Sección 2 – Asociaciones
| Requisito | Dónde |
|---|---|
| `Personaje` con `nombre`, `nivel`, `armaEquipada` (inicia en null) | `modelo/Personaje.java` |
| `equiparArma`: asigna y confirma con mensaje | `Personaje.equiparArma` |
| `desequiparArma`: valida null, la retira e informa; si no hay arma, lo indica | `Personaje.desequiparArma` |
| `calcularAtaqueTotal`: nivel × 5 + daño del arma solo si no es null | `Personaje.calcularAtaqueTotal` |
| `Arma` no conoce a su dueño (unidireccional) | `modelo/Arma.java` (sin atributo de tipo Personaje) |
| `unirseAGremio`: valida null/mismo gremio, abandona el anterior, asigna y sincroniza | `Personaje.unirseAGremio` |
| `agregarMiembro`: valida null, evita duplicados, añade y sincroniza si hace falta | `Gremio.agregarMiembro` |
| Sin recursión infinita en la sincronización | `contains` en `Gremio` + comparación `getGremio() != this` |

## Sección 3 – Agregación y composición
| Requisito | Dónde |
|---|---|
| `Escuadron` con `List<Personaje>` inicializada en el constructor | `modelo/Escuadron.java` |
| `reclutarPersonaje`: valida null y duplicados, agrega sin alterar al personaje | `Escuadron.reclutarPersonaje` |
| `expulsarPersonaje`: remueve si existe; el personaje conserva su estado | `Escuadron.expulsarPersonaje` |
| Los personajes se crean fuera del escuadrón (agregación) y sobreviven a `disolver()` | `Escuadron`, `PruebaDepuracion` caso 5 |
| `CorazonRPG` con `saludBase` y `manaBase` | `modelo/CorazonRPG.java` |
| Composición: el corazón se instancia en el constructor de `Personaje` | `Personaje(String, int)` |
| Sin `setCorazonRPG()` ni getter que entregue el objeto | `Personaje` (atributo `private final`) |
| Mutadores del corazón con acceso de paquete (solo `Personaje` puede usarlos) | `CorazonRPG.recibirDano/curar/gastarMana/recuperarMana` |
| Multiplicidad 0..1 y 1..1 con atributo directo; 0..* con `List<T>` | `armaEquipada`, `corazon`, `miembros` |

## Sección 4 – Reto integrador
| Requisito | Dónde |
|---|---|
| Ciclo `do-while` respaldado por `switch` | `AplicacionArenaRPG.ejecutar` |
| Menú de 6 opciones de la plantilla (6 = Salir) | `AplicacionArenaRPG.mostrarMenu` |
| Opción 1 crear personaje / 2 crear gremio / 3 reclutar a escuadrón / 4 equipar arma / 5 estado detallado | métodos `crearPersonaje`, `gestionarGremios`, `gestionarEscuadrones`, `gestionarArmas`, `mostrarEstadoDetallado` |
| Colecciones globales de personajes, gremios y escuadrones | atributos de `AplicacionArenaRPG` |
| `System.out.printf` para tablas de estadísticas | `mostrarEstadoDetallado` |
| `StringBuilder` en `toString()` y reportes (CorazonRPG, Arma, Gremio, Escuadron) | `toString()` de cada clase, `Personaje.generarReporteDetallado`, `Escuadron.generarReporte` |
| Lectura segura de entrada (sin los saltos pendientes de `nextInt`) | `leerEntero` con `nextLine` + `parseInt` |

La opción 2 incluye un submenú para inscribir personajes en un gremio, porque la sección 2.2 describe esa relación y la plantilla no tenía una opción propia para ella; así se conservan exactamente las 6 opciones del enunciado.

## Sección 5 – Depuración
`principal/PruebaDepuracion.java` reproduce y corrige: `StackOverflowError` (toString circular; `Gremio.toString` solo imprime nombres), `NullPointerException` (arma null), desincronización bidireccional y `ConcurrentModificationException` (recorrer una copia o `removeIf`).

### Guía en IntelliJ
1. Breakpoint en la primera línea de `Personaje.toString()`.
2. Debug con **Shift + F9**.
3. Ventana Frames / Call Stack: si `Personaje.toString` y `Gremio.toString` se alternan cientos de veces, es recursión circular.
4. Watch con **Alt + F8**: `personaje.getGremio()` y `gremio.getMiembros()` para comprobar la coherencia mutua.
5. Para `NullPointerException`: panel Variables, revisar si la referencia muestra `null` antes de la línea que falla.

## Decisiones de diseño
- `Personaje.nivel` y `nombre` son `final`: no hay setters que puedan romper la relación.
- Al subir de nivel la salud y el maná base del corazón aumentan (100 + 10 × (nivel − 1) y 50 + 5 × (nivel − 1)).
- El taller no pide diagramas; esta entrega es solo código y documentación de texto.
