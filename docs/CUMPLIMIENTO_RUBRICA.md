# Taller Unidad 2.2 – Encapsulamiento y Modificadores de Acceso (RPG) – Cumplimiento de rúbrica

Paquete raíz: `co.edu.puj.rpg` · Compilado con `javac -Xlint:all --release 17` → **0 errores, 0 warnings**.

| Requisito de la guía | Dónde se cumple |
|---|---|
| 7 atributos `private` (nombre, puntosVida, vidaMaxima, puntosAtaque, nivel, oro, estaVivo) | `Personaje` líneas 25-31 |
| Constructor completo con `this` + constructor por defecto con `this(...)` | `Personaje` |
| Constructor NO asigna directo: delega en setters (evita el Caso B) | `Personaje(String,int,int,double)` |
| `setPuntosVida`: <=0 → 0 y estaVivo=false; > vidaMaxima → vidaMaxima; intermedio → valor | `Personaje.setPuntosVida` |
| `setOro`: rechaza saldo < 0.0, **indica "Saldo insuficiente"** y no modifica el atributo | `Personaje.setOro` (devuelve boolean) |
| `setNivel` **protected** | `Personaje.setNivel` |
| Getters públicos para los 7 atributos | `Personaje` |
| `atacar(Enemigo)`, `usarPocion(int)`, `recibirDano(int)` | `Personaje` |
| Crítico 15 % con `Math.random()` | `Personaje.atacar` |
| Subida de nivel al SUPERAR nivel×50 de experiencia; sube vida máx. y ataque | `Personaje.ganarExperiencia` (privado, estrictamente `>`) |
| `Enemigo` en otro paquete + miembro package-private | `modelo.enemigo.Enemigo.esDanoValido` |
| `Main` en `principal`, solo usa API pública | `Main` |
| Bucle `do-while` mientras ambos `estaVivo` | `Main` (condición pura) |
| Menú `switch` 1-5 y entrada inválida con `if-else` | `Main` |
| Huir rompe el combate | `break combate;` (opción 4) |
| Tienda: compra llama a `setOro(oro - 40)`; si falla NO se entrega la poción | `Main.abrirTienda` |
| Historial con `StringBuilder` impreso al final de cada ronda | `Main.imprimirHistorial` |
| Salida con `printf` (`%s %d %.2f %b %n`) | todo `Main`, cero `println` |
| Prueba de límites (-10, 0, 100000) | `PruebaLimites` |

## Cambios respecto a la versión anterior
1. Constructor de `Personaje` y `ganarExperiencia` ya no escriben atributos directamente: todo pasa por setters.
2. `setOro` ahora informa "saldo insuficiente" y devuelve `boolean`; la tienda solo entrega la poción si el pago se aprobó.
3. `ganarExperiencia` pasa a `private` (el nivel solo se gana derrotando enemigos) y usa `>` (superar el umbral).
4. `Enemigo`: constructor sin asignación directa de `estaVivo`; nuevo método package-private `esDanoValido`.
5. `Main`: condición del `do-while` pura (`heroe.isEstaVivo() && enemigo.isEstaVivo()`) y salida con `break combate;`.
6. `setPuntosVida`/`setOro`/`setNivel` son `final` → desaparecen los 2 warnings `[this-escape]`.
7. `PruebaLimites` verifica invariantes con valores -10, 0, 100000 (y extremos de `int`).
