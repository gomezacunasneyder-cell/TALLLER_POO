# Evidencias (salidas reales, JDK 21 con `--release 17`)

## Compilación
`javac -Xlint:all --release 17` → sin errores ni warnings.

## Caso A – Violación de acceso (error de compilación esperado)
```
CasoA.java:7: error: puntosVida has private access in Personaje
CasoA.java:8: error: setNivel(int) has protected access in Personaje
CasoA.java:10: error: esDanoValido(int) is not public in Enemigo; cannot be accessed from outside package
```

## API pública visible (`javap -public Personaje`)
Constructores, `atacar`, `usarPocion`, `recibirDano`, `consumirPocion`, `agregarPocionGrande`, `experienciaParaSiguienteNivel`,
`setPuntosVida`, `setOro`, y getters. No aparece ningún atributo, `setNivel`, `setVidaMaxima`, `setPuntosAtaque` ni `ganarExperiencia`.

## Caso B/C – PruebaLimites
```
[OK] setPuntosVida(-10) -> vida: 0 | estaVivo: false
[OK] setPuntosVida(0) -> vida: 0 | estaVivo: false
[OK] setPuntosVida(50) -> vida: 50 | estaVivo: true
[OK] setPuntosVida(100000) -> vida: 100 | estaVivo: true
Saldo insuficiente: la operación dejaría el oro en -10.00. Operación rechazada.
[OK] setOro(-10.00) -> aplicado: false | oro antes: 50.00 | oro ahora: 50.00
[OK] setOro(0.00) -> aplicado: true
[OK] setOro(100000.00) -> aplicado: true
[OK] Tras derrotar al enemigo -> nivel: 2 | experiencia: 10 | oro: 60.00
Todas las comprobaciones pasaron: el objeto conservó su coherencia.
```

## Partida simulada (tienda, entrada inválida, huir)
```
Opción inválida. Elige un número del 1 al 5.
Compra exitosa. Saldo restante: 10.00
Saldo insuficiente: la operación dejaría el oro en -30.00. Operación rechazada.
Compra rechazada: la poción no se entrega. Oro disponible: 10.00
Pociones: 2 normales, 1 grandes
Aragorn huye del combate.
=== FIN DEL JUEGO === Huiste del combate.
```
