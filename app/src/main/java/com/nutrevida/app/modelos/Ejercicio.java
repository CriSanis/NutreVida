package com.nutrevida.app.modelos;

public class Ejercicio {
    private int id;
    private String nombre;
    private int duracion; // en minutos
    private double caloriasQuemadas;
    private String fecha;

    // Constructor vacío
    public Ejercicio() {}

    // Constructor completo
    public Ejercicio(int id, String nombre, int duracion, double caloriasQuemadas, String fecha) {
        this.id = id;
        this.nombre = nombre;
        this.duracion = duracion;
        this.caloriasQuemadas = caloriasQuemadas;
        this.fecha = fecha;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getDuracion() {
        return duracion;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    public double getCaloriasQuemadas() {
        return caloriasQuemadas;
    }

    public void setCaloriasQuemadas(double caloriasQuemadas) {
        this.caloriasQuemadas = caloriasQuemadas;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}