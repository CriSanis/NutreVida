package com.nutrevida.app;

public class RegistroIMC {
    private int id;
    private double peso;
    private double altura;
    private double imc;
    private String categoria;
    private String fecha;

    // Constructor vacío
    public RegistroIMC() {
    }

    // Constructor completo
    public RegistroIMC(int id, double peso, double altura, double imc, String categoria, String fecha) {
        this.id = id;
        this.peso = peso;
        this.altura = altura;
        this.imc = imc;
        this.categoria = categoria;
        this.fecha = fecha;
    }

    // Getters
    public int getId() {
        return id;
    }

    public double getPeso() {
        return peso;
    }

    public double getAltura() {
        return altura;
    }

    public double getImc() {
        return imc;
    }

    public String getCategoria() {
        return categoria;
    }

    public String getFecha() {
        return fecha;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public void setAltura(double altura) {
        this.altura = altura;
    }

    public void setImc(double imc) {
        this.imc = imc;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    @Override
    public String toString() {
        return "RegistroIMC{" +
                "id=" + id +
                ", peso=" + peso +
                ", altura=" + altura +
                ", imc=" + imc +
                ", categoria='" + categoria + '\'' +
                ", fecha='" + fecha + '\'' +
                '}';
    }
}