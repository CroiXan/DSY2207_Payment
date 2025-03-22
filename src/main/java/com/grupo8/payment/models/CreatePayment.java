package com.grupo8.payment.models;

public class CreatePayment {

    private double monto;
    private String descripcion;
    private String token;
    private String metodo;

    public CreatePayment(double monto, String descripcion, String token, String metodo) {
        this.monto = monto;
        this.descripcion = descripcion;
        this.token = token;
        this.metodo = metodo;
    }

    public CreatePayment() {
    }

    public double getMonto() {
        return monto;
    }
    public void setMonto(double monto) {
        this.monto = monto;
    }
    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public String getMetodo() {
        return metodo;
    }
    public void setMetodo(String metodo) {
        this.metodo = metodo;
    }

}
