package com.nutrevida.app;

public class Rutina {
    private final int id;
    private final String fecha;
    private final String tipo;
    private final String descripcion;

    public Rutina(int id, String fecha, String tipo, String descripcion) {
        this.id = id;
        this.fecha = fecha;
        this.tipo = tipo;
        this.descripcion = descripcion;
    }

    public int getId() {
        return id;
    }

    public String getFecha() {
        return fecha;
    }

    public String getTipo() {
        return tipo;
    }

    public String getDescripcion() {
        return descripcion;
    }
}