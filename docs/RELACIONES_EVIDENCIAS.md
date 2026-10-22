# Evidencias – Relaciones entre Clases (salidas reales, JDK 21 con `--release 17`)

## Compilación
`javac -Xlint:all --release 17` → sin errores ni warnings. Cada etapa (secciones 1 a 5) compila por separado.

## PruebaDepuracion
```
Capturado: StackOverflowError (Personaje.toString -> Gremio.toString -> Personaje.toString ...)
[OK] La versión corregida imprime sin recursión: Aragorn (nivel 3) | Corazón[salud: 120/120, maná: 60/60] | Arma: Desarmado | Gremio: Dragones // Gremio[Dragones, miembros (1): Aragorn]
Capturado: NullPointerException al leer el daño de un arma null
[OK] Desarmado: ataque total = nivel x 5 = 10
[OK] Con arma: 10 + 12 = 22
[OK] Personaje.unirseAGremio sincroniza ambos lados
[OK] Gremio.agregarMiembro sincroniza ambos lados
[OK] Unirse dos veces al mismo gremio no duplica
[OK] Cambiar de gremio lo retira del anterior
[OK] disolver() deja a todos sin gremio
Capturado: ConcurrentModificationException
[OK] Personaje no tiene getter ni setter de CorazonRPG
[OK] El atributo corazon es private final
[OK] Los métodos que mutan el corazón no son públicos
[OK] Arma no guarda ninguna referencia a Personaje
[OK] Tras disolver el escuadrón, el personaje conserva vida y arma
Todas las comprobaciones pasaron.
```

## Sesión del menú (opciones 1 a 6)
```
Personaje creado: Aragorn (nivel 5) | Corazón[salud: 140/140, maná: 70/70] | Arma: Desarmado | Gremio: Sin gremio
Ya existe un personaje llamado aragorn.
Nivel inválido.
Opción inválida.
Gremio creado: Gremio[Dragones, miembros (0): ninguno]
Aragorn se une al gremio Dragones.
Gandalf se une al gremio Dragones.
Aragorn se une al escuadrón Vanguardia.
Aragorn sale del escuadrón Vanguardia (conserva su estado).
Aragorn equipa Espada (+25 de daño).
Gandalf no tiene un arma para retirar.

Personaje    Nivel Arma           Gremio           Salud      Maná  Ataque
Aragorn          5 Espada         Dragones      140/140    70/70        50
Gandalf         10 Desarmado      Dragones      190/190    95/95        50

=== Aragorn ===
Ataque total:   50
Corazón RPG:    salud 140/140 | maná 70/70
Arma:           Espada (+25 de daño)
Gremio:         Dragones (2 miembros)
Cerrando la Arena de Héroes...
```
