package com.billetin.model;

public class Ingreso {
    private double monto;
    private String fecha;
    private String categoria;

    public Ingreso(double monto, String fecha, String categoria) {
        this.monto = monto;
        this.fecha = fecha;
        this.categoria = categoria;
    }

    public double getMonto() {
        return monto;
    }

    public String getFecha() {
        return fecha;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
}
