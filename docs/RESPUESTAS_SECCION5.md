# Taller Unidad 2.2: Simulador de Batalla RPG

## Sección 5: Depuración de Encapsulamiento en IntelliJ IDEA

> Los fragmentos con errores **no** están en el proyecto, porque impedirían compilar el resto.
> Para reproducirlos, créalos en un archivo temporal dentro de `co.edu.puj.rpg.principal`,
> toma la captura del error y bórralos.

---

### Caso A: Acceso directo no autorizado (error de compilador)

```java
Personaje heroe = new Personaje("Aragorn", 100, 15, 50.0);
heroe.puntosVida = 200;                       // error
System.out.println("Vida: " + heroe.puntosVida); // error
```

**Mensaje del compilador** (ejecutado con las clases de este proyecto):

```
error: puntosVida has private access in Personaje
```

En IntelliJ aparece como `'puntosVida' has private access in 'co.edu.puj.rpg.modelo.Personaje'`.

**Causa raíz:** `puntosVida` está declarado `private`, así que solo el código dentro de la clase
`Personaje` puede leerlo o escribirlo. `Main` está en otra clase y en otro paquete, por lo que el
compilador bloquea el acceso directo. Esto protege el estado interno: nadie puede dejar al héroe
con 200 de vida cuando su máximo es 100.

**Corrección:** usar la API pública.

```java
heroe.setPuntosVida(200);                          // el setter limita el valor a vidaMaxima
System.out.println("Vida: " + heroe.getPuntosVida());
```

**Extra, `setNivel`:** al llamar `heroe.setNivel(5)` desde `Main` el compilador dice
`setNivel(int) has protected access in Personaje`. Un método `protected` solo lo ven la propia
clase, su paquete y sus subclases; `Main` no es ninguna de las tres. Por eso el nivel solo cambia
a través de `ganarExperiencia()`.

---

### Caso B: Bypassing de validaciones mediante constructor defectuoso

```java
public Personaje(int puntosVida) {
    this.puntosVida = -50;
    this.estaVivo = true;
}
```

**Qué falla:** este código compila, pero deja al objeto en un **estado inconsistente**: tiene vida
negativa (-50) y a la vez `estaVivo = true`. El constructor asigna directamente a los atributos y
se salta la lógica del setter (que habría puesto la vida en 0 y `estaVivo` en `false`).

**Causa raíz:** el encapsulamiento no se logra solo con `private`. Si el constructor escribe en los
atributos sin pasar por la validación, la regla de negocio queda sin proteger.

**Corrección:** que el constructor delegue en el setter validado.

```java
public Personaje(int puntosVida) {
    this.vidaMaxima = 100;
    this.estaVivo = true;
    setPuntosVida(puntosVida);   // con -50 queda en 0 y estaVivo pasa a false
}
```

Así es como están escritos los constructores de `Personaje` y `Enemigo` en este proyecto.

---

### Caso C: Fuga de estado y asignación fuera de límites

```java
heroe.setPuntosVida(999999);  // excede vidaMaxima
heroe.setPuntosVida(-100);    // vida negativa sin actualizar estaVivo
```

**Qué pasaría con un setter sin validar:** `puntosVida` valdría 999999 (más que la vida máxima) y
luego -100, mientras `estaVivo` seguiría en `true`. El objeto quedaría incoherente.

**Qué pasa con el setter validado de este proyecto** (resultado real de `PruebaLimites`):

| Llamada | `puntosVida` resultante | `estaVivo` |
|---|---|---|
| `setPuntosVida(-10)` | 0 | false |
| `setPuntosVida(0)` | 0 | false |
| `setPuntosVida(50)` | 50 | true |
| `setPuntosVida(100000)` | 100 (vidaMaxima) | true |

**Causa raíz:** un setter público sin reglas permite que cualquier clase externa rompa los
invariantes del objeto. La validación debe vivir **dentro** del setter, no en quien lo llama.

---

## Guía de verificación en IntelliJ

1. **Autocompletado:** en `Main`, escribe `heroe.` y presiona `Ctrl + Espacio`. Solo deben aparecer
   métodos `public` (`atacar`, `usarPocion`, `recibirDano`, getters y setters públicos). Los
   atributos `puntosVida`, `oro`, etc. no aparecen, y `setNivel` tampoco por ser `protected`.
2. **Acceso directo:** escribir `heroe.puntosVida = 200;` reporta
   `'puntosVida' has private access in 'co.edu.puj.rpg.modelo.Personaje'` (Caso A).
3. **Valores límite:** ejecuta `PruebaLimites` (clic en el triángulo verde de su `main`). Verifica
   que con `-10`, `0` y `100000` el objeto conserve la coherencia: la vida nunca baja de 0 ni
   supera `vidaMaxima`, `estaVivo` siempre coincide con la vida, y `setOro` rechaza saldos
   negativos sin modificar el atributo.
