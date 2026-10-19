package com.taller.poo;

public class CuentaBancaria {

    private String numeroCuenta;
    private String titular;
    private double saldo;
    private Double limiteCredito;
    public CuentaBancaria() {
        this.numeroCuenta = "000000";
        this.titular = "Sin Nombre";
        this.saldo = 0.0;
        this.limiteCredito = 0.0;
    }

    public CuentaBancaria(String numeroCuenta, String titular) {
        this.numeroCuenta = numeroCuenta;
        this.titular = titular;
        this.saldo = 0.0;
        this.limiteCredito = 100.0;
    }

    public CuentaBancaria(String numeroCuenta, String titular, double saldo, Double limiteCredito) {
        this.numeroCuenta = numeroCuenta;
        this.titular = titular;
        this.saldo = saldo;
        this.limiteCredito = limiteCredito;
    }

    public boolean depositar(double monto) {
        if (monto > 0) {
            saldo += monto;
            return true;
        } else {
            return false;
        }
    }

    public boolean retirar(double monto) {
        if (monto > 0) {
            double disponible = saldo;
            if (limiteCredito != null) {
                disponible += limiteCredito;
            }
            if (disponible >= monto) {
                saldo -= monto;
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
    public String getNumeroCuenta() {
        return numeroCuenta;
    }

    public String getTitular() {
        return titular;
    }

    public double getSaldo() {
        return saldo;
    }

    public Double getLimiteCredito() {
        return limiteCredito;
    }
    /*
     * PREGUNTAS DE ANÁLISIS:
     *
     * 1. ¿Qué diferencia ocurre a nivel de memoria y asignación por defecto al declarar
     *    'saldo' como double primitivo frente a 'limiteCredito' como Wrapper Double?
     *
     * R: 'saldo' (double) guarda el valor directamente dentro del objeto, ocupa un
     *    tamaño fijo de 8 bytes y Java le asigna automáticamente el valor por defecto 0.0.
     *    'limiteCredito' (Double) es una referencia a un objeto en el heap. Mientras no se
     *    le asigne un valor, su valor por defecto es null, o sea, no apunta a ningún objeto
     *    y no existe todavía el objeto Double. Cuando se le asigna un valor, se crea un
     *    objeto con cabecera propia más el número, por lo que consume más memoria que el
     *    primitivo.
     *
     * 2. ¿Por qué una operación aritmética sobre un atributo no inicializado de tipo Double
     *    provoca una NullPointerException, mientras que sobre un double primitivo opera
     *    con 0.0?
     *
     * R: Para operar aritméticamente, Java necesita el valor numérico, así que convierte el
     *    Double en double automáticamente (unboxing), lo que internamente equivale a llamar
     *    a un método sobre el objeto. Si el atributo es null no hay objeto sobre el cual
     *    llamarlo, y se lanza NullPointerException. En cambio, el double primitivo nunca es
     *    null: ya trae 0.0 por defecto, por eso la operación se realiza sin errores.
     */
}