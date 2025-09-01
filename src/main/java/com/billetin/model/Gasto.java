package com.billetin.model;

public class Gasto {
    private double monto;
    private String fecha;
    private String categoria;
    private boolean fijo;

    public Gasto(double monto, String fecha, String categoria, boolean fijo) {
        this.monto = monto;
        this.fecha = fecha;
        this.categoria = categoria;
        this.fijo = fijo;
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

    public boolean isFijo() {
        return fijo;
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

    public void setFijo(boolean fijo) {
        this.fijo = fijo;
    }
}
