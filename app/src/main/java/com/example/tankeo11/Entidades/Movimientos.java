package com.example.tankeo11.Entidades;

public class Movimientos {
    private String fecha;
    private String tipo;

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    private String valor;



    public Movimientos() {
    }


    public Movimientos(String fecha, String tipo, String valor) {
        this.fecha = fecha;
        this.tipo = tipo;
        this.valor = valor;

    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }



}
