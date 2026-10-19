package com.taller.poo;

/*
 * Impacto en memoria y valores nulos: Wrappers vs. primitivos.
 * Los tipos primitivos (double, int, boolean) guardan el valor directamente
 * dentro del objeto, con un tamaño fijo y pequeño, por eso consumen menos memoria.
 * Los Wrappers (Double, Integer, Boolean) son objetos independientes en el heap:
 * tienen cabecera propia y el atributo solo guarda una referencia hacia ellos,
 * así que consumen más memoria.
 * Además, los Wrappers admiten null (dato no definido), mientras que los primitivos
 * siempre tienen un valor por defecto (0, 0.0 o false). Si un Wrapper es null y se
 * usa como número, ocurre un NullPointerException.
 * En esta clase se usaron primitivos porque todos los datos siempre existen.
 */
public class Vehiculo {

    private String marca;
    private String modelo;

    // double: el kilometraje siempre existe (mínimo 0.0) y admite decimales; no necesita null.
    private double kilometraje;

    // int: la velocidad máxima es un número entero que siempre se conoce; ahorra memoria.
    private int velocidadMaxima;

    // boolean: el motor solo está encendido o apagado; null no tendría sentido físico.
    private boolean motorEncendido;

    public Vehiculo() {
        this.marca = "Genérico";
        this.modelo = "Sin Modelo";
        this.kilometraje = 0.0;
        this.velocidadMaxima = 120;
        this.motorEncendido = false;
    }

    public Vehiculo(String marca, String modelo, double kilometraje,
                    int velocidadMaxima, boolean motorEncendido) {
        this.marca = marca;
        this.modelo = modelo;
        this.kilometraje = kilometraje;
        this.velocidadMaxima = velocidadMaxima;
        this.motorEncendido = motorEncendido;
    }


    public String getMarca() {
        return marca;
    }

    public String getModelo() {
        return modelo;
    }

    public double getKilometraje() {
        return kilometraje;
    }

    public int getVelocidadMaxima() {
        return velocidadMaxima;
    }

    public boolean isMotorEncendido() {
        return motorEncendido;
    }
}

