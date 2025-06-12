package com.nutrevida.app.modelos;

public class Alimento {
    private int id;
    private String nombre;
    private double calorias;
    private double proteinas;
    private double carbohidratos;
    private double grasas;

    // Constructor vacío
    public Alimento() {}

    // Constructor completo
    public Alimento(int id, String nombre, double calorias, double proteinas, double carbohidratos, double grasas) {
        this.id = id;
        this.nombre = nombre;
        this.calorias = calorias;
        this.proteinas = proteinas;
        this.carbohidratos = carbohidratos;
        this.grasas = grasas;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public double getCalorias() { return calorias; }
    public void setCalorias(double calorias) { this.calorias = calorias; }
    public double getProteinas() { return proteinas; }
    public void setProteinas(double proteinas) { this.proteinas = proteinas; }
    public double getCarbohidratos() { return carbohidratos; }
    public void setCarbohidratos(double carbohidratos) { this.carbohidratos = carbohidratos; }
    public double getGrasas() { return grasas; }
    public void setGrasas(double grasas) { this.grasas = grasas; }
}