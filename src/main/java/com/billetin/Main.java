package com.billetin;

import com.billetin.model.Ingreso;
import com.billetin.model.Gasto;

public class Main {
    public static void main(String[] args) {
        // Crear un ingreso
        Ingreso ingreso1 = new Ingreso(2000000, "2025-08-31", "Sueldo");

        // Crear un gasto
        Gasto gasto1 = new Gasto(550000, "2025-08-31", "Alquiler", true);

        // Mostrar info en consola
        System.out.println("=== INGRESO ===");
        System.out.println("Monto: " + ingreso1.getMonto());
        System.out.println("Fecha: " + ingreso1.getFecha());
        System.out.println("Categoria: " + ingreso1.getCategoria());

        System.out.println("\n=== GASTO ===");
        System.out.println("Monto: " + gasto1.getMonto());
        System.out.println("Fecha: " + gasto1.getFecha());
        System.out.println("Categoria: " + gasto1.getCategoria());
        System.out.println("Es fijo? " + gasto1.isFijo());
    }
}
