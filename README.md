# TALLLER_POO: Talleres de Programación en Java

Repositorio de entrega con **tres talleres** desarrollados en Java dentro de un mismo proyecto Maven.
Cada taller vive en su propia carpeta (paquete) dentro de `src/main/java`.

**Autor:** Sneyder Gómez

---

## Resumen: qué carpeta resuelve qué taller

| # | Carpeta (paquete) | Taller que resuelve | Clase para ejecutarlo |
|---|---|---|---|
| 1 | `src/main/java/com/taller/poo/` | **Taller de Programación Orientada a Objetos (POO)**: clases, atributos, métodos, constructores, objetos y depuración | `Principal` y `SistemaGestionCuentas` |
| 2 | `src/main/java/co/edu/puj/rpg/` | **Unidad 2.2: Encapsulamiento y Modificadores de Acceso en Java** (Simulador de Batalla RPG) | `principal/Main` |
| 3 | `src/main/java/com/educacion/ahorcado/` | **Taller Práctico Integrador (Unidades 1.1 a 1.4)**: Adivina la Palabra Secreta / Ahorcado de Consola | `Main` |

---

## Taller 1: Programación Orientada a Objetos

**Carpeta:** `src/main/java/com/taller/poo/`

| Sección del taller | Clases que lo resuelven |
|---|---|
| 1.1 Abstracción y atributos | `Vehiculo` |
| 1.2 Modelado de entidades | `Estudiante` |
| 1.3 Dominio financiero | `CuentaBancaria` |
| 2.1 Métodos de instancia y manipulación de cadenas | `Estudiante.obtenerReporteAcademicos()`, `CuentaBancaria.depositar()` y `retirar()` |
| 2.2 Métodos estáticos utilitarios | `ConversorMoneda` |
| 3.1 y 3.2 Sobrecarga de constructores y uso de `this` | Constructores de `Vehiculo`, `Estudiante` y `CuentaBancaria` |
| 4.1 y 4.2 Instanciación de objetos y reportes con `printf` | `Principal` |
| 5 Reto integrador: sistema de gestión de cuentas en consola | `SistemaGestionCuentas` |
| 6 Análisis y depuración de código | `PruebaEstudiante`, `GestorVehiculos` y la subcarpeta `depuracion/` |

La subcarpeta `depuracion/` contiene los casos de estudio 3 y 4 (constructor con `void` y sombreado de
variables sin `this`), con su código ya corregido.

---

## Taller 2: Encapsulamiento y Modificadores de Acceso (Simulador de Batalla RPG)

**Carpeta:** `src/main/java/co/edu/puj/rpg/`

| Archivo | Qué contiene |
|---|---|
| `modelo/Personaje.java` | Héroe con atributos `private`, setters validados, getters, golpes críticos y experiencia |
| `modelo/enemigo/Enemigo.java` | Enemigo con atributos privados, en un subpaquete distinto para comprobar el aislamiento de acceso |
| `principal/Main.java` | Batalla por consola con `do-while`, `switch`, `printf`, `StringBuilder` y tienda |
| `principal/PruebaLimites.java` | Prueba manual de los setters con valores límite (-10, 0, 100000) |

**Retos incluidos:** tienda de Pociones Grandes (con `setOro()` validado), sistema de experiencia con
`setNivel()` protegido y golpes críticos del 15%.

**Documentación:** `docs/RESPUESTAS_SECCION5.md` (casos de depuración A, B y C) y `docs/EVIDENCIAS.md`
(salidas reales de compilación y ejecución).

---

## Taller 3: Ahorcado de Consola

**Carpeta:** `src/main/java/com/educacion/ahorcado/`

Una clase por cada fase del taller:

| Fase del taller | Clase |
|---|---|
| Punto de entrada | `Main` |
| Fase 1: Arquitectura del menú principal y control de flujo | `Fase1MenuPrincipal` |
| Fase 2: Registro del jugador e inicialización de datos | `Fase2RegistroJugador` y `EstadoPartida` |
| Fase 3: Bucle del juego con `StringBuilder` | `Fase3BucleJuego` |
| Fase 4: Resumen de partida y formateo con `printf` | `Fase4Resumen` |
| Fase 5: Retos de expansión (pistas, dificultad dinámica y multiplicador de puntaje) | `Fase5Retos` |

**Documentación:** `docs/AHORCADO_CUMPLIMIENTO_Y_EVIDENCIAS.md` (cumplimiento de la guía y salidas de ejecución).

---

## Estructura del repositorio

```
TALLLER_POO/
├── pom.xml
├── README.md
├── docs/
│   ├── RESPUESTAS_SECCION5.md                  (Taller 2)
│   ├── EVIDENCIAS.md                           (Taller 2)
│   └── AHORCADO_CUMPLIMIENTO_Y_EVIDENCIAS.md   (Taller 3)
└── src/main/java/
    ├── com/taller/poo/                         (Taller 1: POO)
    │   └── depuracion/
    ├── co/edu/puj/rpg/                         (Taller 2: Encapsulamiento RPG)
    │   ├── modelo/
    │   │   └── enemigo/
    │   └── principal/
    └── com/educacion/ahorcado/                 (Taller 3: Ahorcado)
```

---

## Cómo ejecutar

**En IntelliJ IDEA:** abre el proyecto, ubica la clase indicada en la tabla de resumen y pulsa el triángulo
verde junto a su método `main`. Como hay varias clases `Main`, elige la del paquete del taller que quieres probar.

| Taller | Clase con `main` |
|---|---|
| 1 | `com.taller.poo.Principal` y `com.taller.poo.SistemaGestionCuentas` |
| 2 | `co.edu.puj.rpg.principal.Main` y `co.edu.puj.rpg.principal.PruebaLimites` |
| 3 | `com.educacion.ahorcado.Main` |

**Por línea de comandos** (requiere Maven y el JDK configurado en `pom.xml`):

```
mvn compile
java -cp target/classes com.educacion.ahorcado.Main
```

Cambia `com.educacion.ahorcado.Main` por la clase del taller que quieras ejecutar.

**Requisitos:** JDK compatible con la versión definida en `pom.xml` (propiedades `maven.compiler.source` y
`maven.compiler.target`; ajústalas si usas otro JDK) y, opcionalmente, Maven.

---

## Convención de commits

- Talleres 1 y 2: un commit por ejercicio o clase (`feat(1.1): ...`, `fix(6.2): ...`).
- Taller 3: un commit por fase (`feat(ahorcado): Fase 1 - ...`).
- 
