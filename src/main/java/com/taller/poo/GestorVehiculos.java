package com.taller.poo;

public class GestorVehiculos {

    public void encenderMotor() {
        System.out.println("El motor del vehículo ha sido encendido.");
    }

    public static void main(String[] args) {
        GestorVehiculos gestor = new GestorVehiculos();
        gestor.encenderMotor();
    }
}