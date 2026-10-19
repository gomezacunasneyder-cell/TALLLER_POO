package com.taller.poo;

public class Principal {

    public static void main(String[] args) {

        // ---------- Vehiculo ----------
        Vehiculo vehiculo1 = new Vehiculo("Toyota", "Corolla", 15000.50, 180, true);
        Vehiculo vehiculo2 = new Vehiculo();
        Vehiculo vehiculo3 = new Vehiculo("Renault", "Duster", 48000.0, 180, false);

        // ---------- Estudiante ----------
        Estudiante estudiante1 = new Estudiante("1098765432", "Laura Martínez", 4.35, true);
        Estudiante estudiante2 = new Estudiante("1005432198", "Carlos Rojas", 3.80, true);
        Estudiante estudiante3 = new Estudiante();

        // ---------- CuentaBancaria ----------
        CuentaBancaria cuenta1 = new CuentaBancaria("1001", "Carlos Pérez", 1000.0, 500.0);
        CuentaBancaria cuenta2 = new CuentaBancaria("1002", "Ana Gómez");
        CuentaBancaria cuenta3 = new CuentaBancaria();

        // ---------- Alteración del estado ----------
        cuenta1.depositar(250.75);
        cuenta2.depositar(50.0);
        cuenta3.depositar(500.0);
        cuenta3.retirar(200.0);

        // ---------- Reporte de vehículos ----------
        System.out.printf("=== REPORTE DE VEHÍCULOS ===%n");
        System.out.printf("[1] Marca: %s | Modelo: %s | KM: %.2f | VelMax: %d km/h | Encendido: %b%n",
                vehiculo1.getMarca(), vehiculo1.getModelo(), vehiculo1.getKilometraje(),
                vehiculo1.getVelocidadMaxima(), vehiculo1.isMotorEncendido());
        System.out.printf("[2] Marca: %s | Modelo: %s | KM: %.2f | VelMax: %d km/h | Encendido: %b%n",
                vehiculo2.getMarca(), vehiculo2.getModelo(), vehiculo2.getKilometraje(),
                vehiculo2.getVelocidadMaxima(), vehiculo2.isMotorEncendido());
        System.out.printf("[3] Marca: %s | Modelo: %s | KM: %.2f | VelMax: %d km/h | Encendido: %b%n",
                vehiculo3.getMarca(), vehiculo3.getModelo(), vehiculo3.getKilometraje(),
                vehiculo3.getVelocidadMaxima(), vehiculo3.isMotorEncendido());

        // ---------- Reporte de estudiantes ----------
        System.out.printf("%n=== REPORTE DE ESTUDIANTES ===%n");
        System.out.printf("%s%n%n", estudiante1.obtenerReporteAcademicos());
        System.out.printf("%s%n%n", estudiante2.obtenerReporteAcademicos());
        System.out.printf("%s%n", estudiante3.obtenerReporteAcademicos());

        // ---------- Reporte de cuentas ----------
        System.out.printf("%n=== REPORTE DE CUENTAS BANCARIAS ===%n");
        System.out.printf("Cuenta: %s | Titular: %s | Saldo Final: $%.2f | Crédito: $%.2f%n",
                cuenta1.getNumeroCuenta(), cuenta1.getTitular(),
                cuenta1.getSaldo(), cuenta1.getLimiteCredito());
        System.out.printf("Cuenta: %s | Titular: %s | Saldo Final: $%.2f | Crédito: $%.2f%n",
                cuenta2.getNumeroCuenta(), cuenta2.getTitular(),
                cuenta2.getSaldo(), cuenta2.getLimiteCredito());
        System.out.printf("Cuenta: %s | Titular: %s | Saldo Final: $%.2f | Crédito: $%.2f%n",
                cuenta3.getNumeroCuenta(), cuenta3.getTitular(),
                cuenta3.getSaldo(), cuenta3.getLimiteCredito());

        // ---------- ConversorMoneda ----------
        System.out.printf("%n=== CONVERSOR DE MONEDA ===%n");
        double euros = ConversorMoneda.convertirDolaresAEuros(100.0, 0.92);
        System.out.printf("100 USD en euros: %s%n", ConversorMoneda.formatearMoneda(euros));
        System.out.printf("Monto inválido: %.2f%n", ConversorMoneda.convertirDolaresAEuros(-5.0, 0.92));
    }
}