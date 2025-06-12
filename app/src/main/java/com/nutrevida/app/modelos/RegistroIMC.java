package com.nutrevida.app.modelos;

public class RegistroIMC {
    private int id;
    private double peso;
    private double altura;
    private double imc;
    private String resultado;
    private String fecha;

    // Constructor vacío
    public RegistroIMC() {}

    // Constructor completo
    public RegistroIMC(int id, double peso, double altura, double imc, String resultado, String fecha) {
        this.id = id;
        this.peso = peso;
        this.altura = altura;
        this.imc = imc;
        this.resultado = resultado;
        this.fecha = fecha;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public double getAltura() {
        return altura;
    }

    public void setAltura(double altura) {
        this.altura = altura;
    }

    public double getImc() {
        return imc;
    }

    public void setImc(double imc) {
        this.imc = imc;
    }

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}