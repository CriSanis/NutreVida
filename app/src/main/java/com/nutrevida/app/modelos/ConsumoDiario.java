package com.nutrevida.app.modelos;

public class ConsumoDiario {
    private int id;
    private int alimentoId;
    private double cantidad; // Cantidad consumida en gramos
    private String fecha;

    // Constructor vacío
    public ConsumoDiario() {}

    // Constructor completo
    public ConsumoDiario(int id, int alimentoId, double cantidad, String fecha) {
        this.id = id;
        this.alimentoId = alimentoId;
        this.cantidad = cantidad;
        this.fecha = fecha;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getAlimentoId() { return alimentoId; }
    public void setAlimentoId(int alimentoId) { this.alimentoId = alimentoId; }
    public double getCantidad() { return cantidad; }
    public void setCantidad(double cantidad) { this.cantidad = cantidad; }
    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }
}