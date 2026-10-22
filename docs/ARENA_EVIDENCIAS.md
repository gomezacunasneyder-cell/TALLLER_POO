# Evidencias – Arena RPG (salidas reales, JDK 21 con `--release 17`)

## Compilación
`javac -Xlint:all --release 17` → sin errores ni warnings.

## Error de compilación: instanciar la clase abstracta
```
CasoAbstracta.java:5: error: Personaje is abstract; cannot be instantiated
```

## PruebaDepuracion
```
Capturado: Index 5 out of bounds for length 5
Capturado: Index 3 out of bounds for length 1
Capturado: ClassCastException al castear Guerrero a Mago
[OK] Inventario<Pocion> devuelve Pocion sin casteo
Thorin recibe 25 de daño (mitigado: 5). Vida: 95/120
Gandalf lanza BOLA DE FUEGO sobre Troll (50 de daño, maná restante: 35)
Todas las comprobaciones pasaron.
```

## Tablero inicial
```
       0  1  2  3  4
  0   [G][M][.][.][E]
  1   [.][.][.][.][.]
  2   [.][.][C][.][.]
  3   [.][.][.][.][.]
  4   [E][.][.][.][E]
```

## Partidas simuladas
- **Victoria por exterminio** (4 rondas): Golpe de escudo, Bola de fuego, embestidas y un Troll en furia; `VICTORIA! Todos los enemigos han sido eliminados.`
- **Victoria por cofre**: Thorin se mueve (1,0) → (2,0) → (2,1) → (2,2); `¡Thorin ha alcanzado el cofre!`
- **Derrota** (7 rondas pasando turno): `DERROTA: el escuadrón héroe ha caído.`
- **Validaciones**: `Movimiento imposible: (-1,0) está fuera del tablero.`, `La celda (0,1) está ocupada.`, `Thorin ya tiene la vida al máximo.`, opciones no numéricas o fuera de rango.
- **Inventario**: `Gandalf usa Poción menor y recupera 30 de vida. Vida: 75/80` y las pociones bajan de 3 a 2.
