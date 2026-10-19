package com.taller.poo.depuracion;

public class CuentaBancaria {

    private String numeroCuenta;
    private double saldo;

    // Declaración errónea del constructor
    public CuentaBancaria(String numeroCuenta, double saldoInicial)  {
        this.numeroCuenta = numeroCuenta;
        this.saldo = saldoInicial;
    }
}