package com.taller.poo;

public class Estudiante {

    private String identificacion;
    private String nombreCompleto;
    private double promedioCalificaciones;
    private boolean estaMatriculado;

    public Estudiante() {
        this.identificacion = "";
        this.nombreCompleto = "";
        this.promedioCalificaciones = 0.0;
        this.estaMatriculado = false;
    }

    public Estudiante(String identificacion, String nombreCompleto,
                      double promedioCalificaciones, boolean estaMatriculado) {
        this.identificacion = identificacion;
        this.nombreCompleto = nombreCompleto;
        this.promedioCalificaciones = promedioCalificaciones;
        this.estaMatriculado = estaMatriculado;
    }

    public String obtenerReporteAcademicos() {
        StringBuilder reporte = new StringBuilder();
        reporte.append(String.format("Estudiante: %s%n", nombreCompleto));
        reporte.append(String.format("Promedio: %.2f%n", promedioCalificaciones));
        reporte.append(String.format("Matriculado: %b", estaMatriculado));
        return reporte.toString();
    }
}