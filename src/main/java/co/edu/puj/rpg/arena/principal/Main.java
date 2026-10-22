package co.edu.puj.rpg.arena.principal;

import java.util.Scanner;

/** Punto de entrada de la Arena RPG con POO avanzada, colecciones y genéricos. */
public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        new Batalla(scanner).iniciar();
        scanner.close();
    }
}
