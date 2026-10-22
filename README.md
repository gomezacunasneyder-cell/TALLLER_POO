# TALLLER_POO – Talleres de Programación Orientada a Objetos en Java

Repositorio de entrega de los talleres de Programación Orientada a Objetos (POO) de **Sneyder Steven Gómez Acuña**, desarrollados en Java con IntelliJ IDEA y Maven. Cada taller vive en su propio paquete dentro de `src/main/java` y se entregó con un commit por ejercicio o por sección.

## Talleres resueltos

| N.º | Taller | Paquete / carpeta | Clase que se ejecuta |
|---|---|---|---|
| 5 | **Programación Orientada a Objetos, Clases y Objetos en Java** | `src/main/java/com/taller/poo` | Clases de prueba y principal del paquete `com.taller.poo` |
| 6 | **Encapsulamiento y Modificadores de Acceso en Java** (Simulador de Batalla RPG, Unidad 2.2) | `src/main/java/co/edu/puj/rpg/modelo` y `.../principal` | `co.edu.puj.rpg.principal.Main` y `PruebaLimites` |
| 7 | **Arena RPG con POO Avanzada, Colecciones y Genéricos** (Unidad 2.4) | `src/main/java/co/edu/puj/rpg/arena` | `co.edu.puj.rpg.arena.principal.Main` y `PruebaDepuracion` |
| 8 | **Relaciones entre Clases en Java** (Unidad 2.5) | `src/main/java/co/edu/puj/rpg/relaciones` | `co.edu.puj.rpg.relaciones.principal.AplicacionArenaRPG` y `PruebaDepuracion` |

## Contenido de cada taller

### 5. Programación Orientada a Objetos, Clases y Objetos (`com.taller.poo`)
- **Abstracción y atributos:** `Vehiculo`.
- **Modelado de entidades:** `Estudiante`.
- **Dominio financiero:** `CuentaBancaria`.
- **Métodos de instancia y estáticos:** utilidades como `ConversorMoneda`.
- **Constructores y uso de `this`:** sobrecarga y desambiguación de parámetros.
- **Instanciación y reportes formateados** con `printf`.
- **Depuración** en IntelliJ (paquete de depuración).

### 6. Encapsulamiento y Modificadores de Acceso (`co.edu.puj.rpg`)
- Atributos `private` y setters con validación (`setPuntosVida`, `setOro`); `setNivel` es `protected`.
- `Enemigo` en un subpaquete distinto para comprobar el aislamiento de acceso; método package-private `esDanoValido`.
- `Main` con `do-while`, `switch`, historial con `StringBuilder`, golpes críticos y tienda.
- `PruebaLimites` con valores límite (-10, 0, 100000).

### 7. Arena RPG con POO Avanzada, Colecciones y Genéricos (`co.edu.puj.rpg.arena`)
- Interfaz `ICombatiente`, clase abstracta `Personaje` y subclases `Guerrero`, `Mago` y `Enemigo`.
- Sobreescritura, sobrecarga (`atacar`) y polimorfismo.
- Escuadrón con `List<Personaje>` / `ArrayList`, `removeIf` y recorrido polimórfico.
- Contenedor genérico `Inventario<T>` y tablero 5x5 (`char[][]`).
- Batalla por turnos en consola con condiciones de victoria y derrota.
- `PruebaDepuracion`: `IndexOutOfBoundsException`, `ClassCastException` e instanciación de una clase abstracta.

### 8. Relaciones entre Clases (`co.edu.puj.rpg.relaciones`)
- **Asociación unidireccional:** `Personaje` → `Arma`.
- **Asociación bidireccional:** `Personaje` ↔ `Gremio`, con sincronización en ambos sentidos.
- **Agregación:** `Escuadron` agrupa personajes que existen por sí solos.
- **Composición:** `Personaje` crea y protege su `CorazonRPG`.
- Menú interactivo `AplicacionArenaRPG` con `do-while`, `switch`, `printf` y `StringBuilder`.
- `PruebaDepuracion`: `StackOverflowError`, `NullPointerException`, desincronización y `ConcurrentModificationException`.

## Estructura del repositorio

```
TALLLER_POO/
├── docs/                         Evidencias y cumplimiento de rúbrica de los talleres
├── src/main/java/
│   ├── com/taller/poo/           Taller 5
│   └── co/edu/puj/rpg/
│       ├── modelo/               Taller 6 (Personaje y enemigo/Enemigo)
│       ├── principal/            Taller 6 (Main, PruebaLimites)
│       ├── arena/                Taller 7 (modelo, inventario, tablero, principal)
│       └── relaciones/           Taller 8 (modelo, principal)
├── pom.xml
└── README.md
```

## Documentación

En la carpeta [`docs`](docs) hay, entre otros:
- `CUMPLIMIENTO_RUBRICA.md` y `EVIDENCIAS.md` – taller 6.
- `ARENA_CUMPLIMIENTO_RUBRICA.md` y `ARENA_EVIDENCIAS.md` – taller 7.
- `RELACIONES_CUMPLIMIENTO_RUBRICA.md` y `RELACIONES_EVIDENCIAS.md` – taller 8.

## Cómo ejecutarlos

**Requisitos:** JDK 17 o superior y Maven (IntelliJ IDEA los gestiona al abrir el proyecto).

1. Clona el repositorio:
   ```bash
   git clone https://github.com/gomezacunasneyder-cell/TALLLER_POO.git
   ```
2. Ábrelo en IntelliJ IDEA (**File → Open** sobre la carpeta) y espera a que Maven cargue.
3. Abre la clase indicada en la tabla de arriba, haz clic en el triángulo verde junto a `public static void main` y elige **Run**. Los menús se manejan escribiendo el número de la opción en la pestaña **Run**.

Desde la terminal, con Maven:
```bash
mvn compile
java -cp target/classes co.edu.puj.rpg.relaciones.principal.AplicacionArenaRPG
```

## Otros talleres

El taller **Ahorcado de Consola** (`com.educacion.ahorcado`) está en el repositorio [taller2_java](https://github.com/gomezacunasneyder-cell/taller2_java).

## Autor

**Sneyder Steven Gómez Acuña** – estudiante de Ingeniería de Software.
