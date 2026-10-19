package com.taller.poo;

import java.util.Scanner;

public class SistemaGestionCuentas {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        CuentaBancaria[] cuentas = new CuentaBancaria[3];
        cuentas[0] = new CuentaBancaria("1001", "Carlos Pérez", 1000.0, 500.0);
        cuentas[1] = new CuentaBancaria("1002", "Ana Gómez");
        cuentas[2] = new CuentaBancaria("1003", "Laura Martínez", 2500.0, 0.0);

        int opcion;
        do {
            System.out.printf("%n=== SISTEMA DE GESTIÓN DE CUENTAS ===%n");
            System.out.printf("1. Depositar%n");
            System.out.printf("2. Retirar%n");
            System.out.printf("3. Ver reporte consolidado%n");
            System.out.printf("4. Salir%n");
            System.out.printf("Seleccione una opción: ");
            opcion = leerEntero(scanner);

            switch (opcion) {
                case 1:
                    opcionDepositar(scanner, cuentas);
                    break;
                case 2:
                    opcionRetirar(scanner, cuentas);
                    break;
                case 3:
                    opcionReporte(cuentas);
                    break;
                case 4:
                    System.out.printf("Saliendo del sistema...%n");
                    break;
                default:
                    System.out.printf("Opción inválida. Elija un número del 1 al 4.%n");
            }
        } while (opcion != 4);

        scanner.close();
    }

    private static void opcionDepositar(Scanner scanner, CuentaBancaria[] cuentas) {
        System.out.printf("Número de cuenta: ");
        String numero = scanner.nextLine().trim();
        System.out.printf("Monto a depositar: ");
        double monto = leerMonto(scanner);

        CuentaBancaria cuenta = buscarCuenta(cuentas, numero);

        if (monto <= 0) {
            System.out.printf("Error: el monto debe ser mayor a 0.%n");
        } else if (cuenta == null) {
            System.out.printf("Error: no existe una cuenta con el número %s.%n", numero);
        } else {
            cuenta.depositar(monto);
            System.out.printf("Depósito exitoso. Nuevo saldo: $%.2f%n", cuenta.getSaldo());
        }
    }

    private static void opcionRetirar(Scanner scanner, CuentaBancaria[] cuentas) {
        System.out.printf("Número de cuenta: ");
        String numero = scanner.nextLine().trim();
        System.out.printf("Monto a retirar: ");
        double monto = leerMonto(scanner);

        CuentaBancaria cuenta = buscarCuenta(cuentas, numero);

        if (monto <= 0) {
            System.out.printf("Error: el monto debe ser mayor a 0.%n");
        } else if (cuenta == null) {
            System.out.printf("Error: no existe una cuenta con el número %s.%n", numero);
        } else if (cuenta.retirar(monto)) {
            System.out.printf("Retiro exitoso. Nuevo saldo: $%.2f%n", cuenta.getSaldo());
        } else {
            System.out.printf("Error: fondos insuficientes.%n");
        }
    }

    private static void opcionReporte(CuentaBancaria[] cuentas) {
        StringBuilder detalle = new StringBuilder();
        double saldoTotal = 0.0;

        for (int i = 0; i < cuentas.length; i++) {
            CuentaBancaria cuenta = cuentas[i];
            detalle.append(String.format("Cuenta: %s | Titular: %s | Saldo: $%.2f%n",
                    cuenta.getNumeroCuenta(), cuenta.getTitular(), cuenta.getSaldo()));
            saldoTotal += cuenta.getSaldo();
        }

        System.out.printf("%n=== REPORTE CONSOLIDADO ===%n");
        System.out.printf("%s", detalle.toString());
        System.out.printf("Saldo total acumulado: $%.2f%n", saldoTotal);
    }

    private static CuentaBancaria buscarCuenta(CuentaBancaria[] cuentas, String numero) {
        for (int i = 0; i < cuentas.length; i++) {
            if (cuentas[i].getNumeroCuenta().equals(numero)) {
                return cuentas[i];
            }
        }
        return null;
    }

    private static int leerEntero(Scanner scanner) {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static double leerMonto(Scanner scanner) {
        try {
            return Double.parseDouble(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}