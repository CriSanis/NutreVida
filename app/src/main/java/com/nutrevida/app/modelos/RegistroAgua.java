package com.nutrevida.app.modelos;

public class RegistroAgua {
    private int id;
    private double cantidad; // Cantidad en mililitros
    private String fecha;
    private String hora;

    // Constructor vacío
    public RegistroAgua() {}

    // Constructor completo
    public RegistroAgua(int id, double cantidad, String fecha, String hora) {
        this.id = id;
        this.cantidad = cantidad;
        this.fecha = fecha;
        this.hora = hora;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public double getCantidad() { return cantidad; }
    public void setCantidad(double cantidad) { this.cantidad = cantidad; }
    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }
    public String getHora() { return hora; }
    public void setHora(String hora) { this.hora = hora; }
}