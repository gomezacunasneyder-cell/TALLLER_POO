# Taller Integrador "Ahorcado de Consola": cumplimiento y evidencias

Paquete: `com.educacion.ahorcado`. Cada fase del taller está implementada en su propia clase. El
código compila sin errores ni advertencias con `javac -Xlint:all`.

## 1. Una clase por fase

| Fase | Clase | Responsabilidad |
|---|---|---|
| Entrada | `Main` | Punto de entrada (`public static void main(String[] args)`) |
| Fase 1: Arquitectura del menú principal y control de flujo | `Fase1MenuPrincipal` | `do-while` + `switch`, validación con `while` y `Integer.parseInt()`, lectura con `nextLine()` |
| Fase 2: Registro del jugador e inicialización de datos | `Fase2RegistroJugador` y `EstadoPartida` | Normaliza el nombre (`trim`, `toUpperCase`, `length`), elige la palabra y crea las variables de estado |
| Fase 3: Bucle del juego con `StringBuilder` | `Fase3BucleJuego` | Máscara e historial, `charAt`, `equalsIgnoreCase`, `break`, `continue`, ternario |
| Fase 4: Resumen de partida y formateo | `Fase4Resumen` | `printf` con `%s`, `%d`, `%.2f`, casting a `double` y estadísticas de sesión |
| Fase 5: Retos de expansión | `Fase5Retos` | Pista con `substring`, dificultad dinámica y multiplicador de puntaje |

`EstadoPartida` agrupa las variables de la tabla de la Fase 2 (`MAX_INTENTOS`, `intentosRestantes`,
`puntaje`, `juegoGanado`, `ultimaLetra`, `nombreJugador`) y los dos `StringBuilder`. Se crea en la Fase 2,
se modifica en la Fase 3 y se lee en la Fase 4.

## 2. Cumplimiento de los requisitos del PDF

| Requisito | Dónde está |
|---|---|
| Paquete `com.educacion.ahorcado` y `Main` con `main` | `Main` |
| Menú `1. Jugar / 2. Ver Estadísticas / 3. Salir` con `do-while` y `switch` | `Fase1MenuPrincipal.ejecutar()` |
| Validación repetitiva ante texto o rango inválido, con aviso pedagógico | `Fase1MenuPrincipal.leerOpcionMenu()` |
| Nombre limpio, en mayúsculas y no vacío | `Fase2RegistroJugador.leerNombre()` |
| Variables de la tabla (`final int`, `int`, `double`, `boolean`, `char`, `String`) | `EstadoPartida` |
| Máscara oculta (`_ _ _ _`) e historial con `StringBuilder` | `EstadoPartida` y `Fase3BucleJuego` |
| `charAt()` y `equalsIgnoreCase()` | `Fase3BucleJuego.ejecutar()` |
| `break` por victoria, intentos agotados y rendición | `Fase3BucleJuego.ejecutar()` |
| `continue` por entrada vacía, inválida o letra repetida | `Fase3BucleJuego.ejecutar()` |
| Operador ternario de retroalimentación | Variable `mensaje` en `Fase3BucleJuego` |
| Descontar intentos solo cuando la letra falla | Rama `else` del acierto en `Fase3BucleJuego` |
| Resumen con `printf` y casting a `double` | `Fase4Resumen.mostrarResumen()` |
| Estadísticas de la sesión | `Fase4Resumen.mostrarEstadisticas()` |
| Reto 1: pista con `substring()` con 1 intento restante | `Fase5Retos.generarPista()` |
| Reto 2: dificultad según `length()` | `Fase5Retos.calcularMaxIntentos()` |
| Reto 3: multiplicador por velocidad y por fallos | `Fase5Retos.calcularBonoVictoria()` |

## 3. Decisiones de interpretación

- **Aciertos y porcentaje:** `aciertos` cuenta las letras acertadas y `turnos = aciertos + fallos`. El
  porcentaje es `aciertos / turnos * 100`. El PDF no define la fórmula y su ejemplo (83.33%) es ilustrativo.
- **Puntaje:** empieza en 100.0, suma 10.0 por acierto y resta 5.0 por fallo (nunca baja de 0). Al ganar, se
  suma un bono de 15.0 por intento sobrante más `max(0, 30 - 2 * turnos)` por velocidad.
- **Dificultad:** 4 letras o menos dan 6 intentos, de 5 a 7 dan 7, y 8 o más dan 8.
- **Rendición:** escribir `RENDIRSE` abandona la partida y cuenta como derrota.
- **Punto decimal:** el resumen usa `Locale.US` para mostrar `83.33%` como en el PDF.
- **Campos de `EstadoPartida`:** son de paquete (sin `private`) para que las fases 3 y 4 los lean y modifiquen
  directamente, ya que la lógica pedida por la guía debe verse dentro del bucle.

## 4. Prevención de errores de la matriz de depuración (Fase 6)

- **Comparar con `==`:** nunca se compara texto con `==`; se usa `equalsIgnoreCase()`.
- **Bucles infinitos en el menú:** cada lectura usa `nextLine()` seguido de `Integer.parseInt()`, y el `while` solo termina con 1, 2 o 3.
- **`StringIndexOutOfBoundsException`:** `charAt()` y `substring()` solo se invocan después de comprobar `length()`.

## 5. Evidencias de ejecución (salidas reales)

### 5.1 Tres partidas con la palabra fijada, más las estadísticas

Victoria (con entradas vacía, `ab`, `7`, una letra repetida y una minúscula), derrota con pista y rendición.

```
Escribe una letra por turno, o RENDIRSE para abandonar la partida.

Palabra: _ _ _ _
Letras usadas: -
Intentos restantes: 6 / 6
Ingrese una letra: Fallaste. La letra X no está en la palabra.

Palabra: _ _ _ _
Letras usadas: X 
Intentos restantes: 5 / 6
Ingrese una letra: Error: no ingresó nada. Escriba una letra.

Palabra: _ _ _ _
Letras usadas: X 
Intentos restantes: 5 / 6
Ingrese una letra: Error: "ab" no es válido. Ingrese una sola letra.

Palabra: _ _ _ _
Letras usadas: X 
Intentos restantes: 5 / 6
Ingrese una letra: Error: "7" no es válido. Ingrese una sola letra.

Palabra: _ _ _ _
Letras usadas: X 
Intentos restantes: 5 / 6
Ingrese una letra: Fallaste. La letra Z no está en la palabra.

Palabra: _ _ _ _
Letras usadas: X Z 
Intentos restantes: 4 / 6
Ingrese una letra: ¡Bien hecho! La letra J está en la palabra.

Palabra: J _ _ _
Letras usadas: X Z J 
Intentos restantes: 4 / 6
Ingrese una letra: La letra J ya fue utilizada. Intente con otra.

Palabra: J _ _ _
Letras usadas: X Z J 
Intentos restantes: 4 / 6
Ingrese una letra: ¡Bien hecho! La letra A está en la palabra.

Palabra: J A _ A
Letras usadas: X Z J A 
Intentos restantes: 4 / 6
Ingrese una letra: ¡Bien hecho! La letra V está en la palabra.

Palabra: J A V A

====================================================
RESUMEN DE LA PARTIDA
====================================================
Jugador: CARLOS
Estado Final: ¡VICTORIA!
Palabra Secreta: JAVA
Intentos Restantes: 4 / 6
Porcentaje Aciertos: 60.00%
Puntaje Final: 200.00 pts
====================================================
Escribe una letra por turno, o RENDIRSE para abandonar la partida.

Palabra: _ _ _ _
Letras usadas: -
Intentos restantes: 6 / 6
Ingrese una letra: Fallaste. La letra X no está en la palabra.

Palabra: _ _ _ _
Letras usadas: X 
Intentos restantes: 5 / 6
Ingrese una letra: Fallaste. La letra Z no está en la palabra.

Palabra: _ _ _ _
Letras usadas: X Z 
Intentos restantes: 4 / 6
Ingrese una letra: Fallaste. La letra Q no está en la palabra.

Palabra: _ _ _ _
Letras usadas: X Z Q 
Intentos restantes: 3 / 6
Ingrese una letra: Fallaste. La letra W no está en la palabra.

Palabra: _ _ _ _
Letras usadas: X Z Q W 
Intentos restantes: 2 / 6
Ingrese una letra: Fallaste. La letra K no está en la palabra.

Palabra: _ _ _ _
Letras usadas: X Z Q W K 
Intentos restantes: 1 / 6
PISTA: la palabra contiene la letra J.
Ingrese una letra: Fallaste. La letra B no está en la palabra.

Palabra: _ _ _ _

====================================================
RESUMEN DE LA PARTIDA
====================================================
Jugador: ANA
Estado Final: DERROTA
Palabra Secreta: JAVA
Intentos Restantes: 0 / 6
Porcentaje Aciertos: 0.00%
Puntaje Final: 70.00 pts
====================================================
Escribe una letra por turno, o RENDIRSE para abandonar la partida.

Palabra: _ _ _ _ _
Letras usadas: -
Intentos restantes: 6 / 6
Ingrese una letra: ¡Bien hecho! La letra C está en la palabra.

Palabra: C _ _ _ _
Letras usadas: C 
Intentos restantes: 6 / 6
Ingrese una letra: 
Palabra: C _ _ _ _

====================================================
RESUMEN DE LA PARTIDA
====================================================
Jugador: LUIS
Estado Final: RENDICIÓN
Palabra Secreta: CLASE
Intentos Restantes: 6 / 6
Porcentaje Aciertos: 100.00%
Puntaje Final: 110.00 pts
====================================================

====================================================
ESTADÍSTICAS DE LA SESIÓN
====================================================
Partidas Jugadas: 3
Victorias: 1
Derrotas: 2
Porcentaje de Victorias: 33.33%
Mejor Puntaje: 200.00 pts
Puntaje Promedio: 126.67 pts
====================================================
```

### 5.2 Dificultad dinámica (Reto 2)

```
JAVA (4 letras) -> 6 intentos
CLASE (5 letras) -> 7 intentos
VARIABLE (8 letras) -> 8 intentos
PROGRAMACION (12 letras) -> 8 intentos
```

### 5.3 Menú real con entradas simuladas

Entradas: `abc`, `9`, `0`, línea vacía, `2`, `1`, línea de espacios, `  carlos  `, `rendirse`, `2` y `3`.

```

=== AHORCADO DE CONSOLA ===
1. Jugar
2. Ver Estadísticas
3. Salir
Seleccione una opción (1-3): Error: "abc" no es un número válido. Ingrese 1, 2 o 3.
Seleccione una opción (1-3): Error: la opción 9 está fuera de rango. Elija 1, 2 o 3.
Seleccione una opción (1-3): Error: la opción 0 está fuera de rango. Elija 1, 2 o 3.
Seleccione una opción (1-3): Error: "" no es un número válido. Ingrese 1, 2 o 3.
Seleccione una opción (1-3): 
Aún no hay partidas registradas en esta sesión. Elija 1 para jugar.

=== AHORCADO DE CONSOLA ===
1. Jugar
2. Ver Estadísticas
3. Salir
Seleccione una opción (1-3): 
Ingrese su nombre: Error: el nombre no puede estar vacío. Intente de nuevo.

Ingrese su nombre: 
¡Bienvenido, CARLOS! La palabra secreta tiene 12 letras y tienes 8 intentos.
Escribe una letra por turno, o RENDIRSE para abandonar la partida.

Palabra: _ _ _ _ _ _ _ _ _ _ _ _
Letras usadas: -
Intentos restantes: 8 / 8
Ingrese una letra: 
Palabra: _ _ _ _ _ _ _ _ _ _ _ _

====================================================
RESUMEN DE LA PARTIDA
====================================================
Jugador: CARLOS
Estado Final: RENDICIÓN
Palabra Secreta: PROGRAMACION
Intentos Restantes: 8 / 8
Porcentaje Aciertos: 0.00%
Puntaje Final: 100.00 pts
====================================================

=== AHORCADO DE CONSOLA ===
1. Jugar
2. Ver Estadísticas
3. Salir
Seleccione una opción (1-3): 
====================================================
ESTADÍSTICAS DE LA SESIÓN
====================================================
Partidas Jugadas: 1
Victorias: 0
Derrotas: 1
Porcentaje de Victorias: 0.00%
Mejor Puntaje: 100.00 pts
Puntaje Promedio: 100.00 pts
====================================================

=== AHORCADO DE CONSOLA ===
1. Jugar
2. Ver Estadísticas
3. Salir
Seleccione una opción (1-3): 
¡Gracias por jugar! Hasta pronto.
```
