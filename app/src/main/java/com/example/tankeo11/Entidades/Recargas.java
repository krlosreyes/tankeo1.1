package com.example.tankeo11.Entidades;

public class Recargas {
    private String fecha;
    private String estado;
    private String valor;

public Recargas(){

}

    public Recargas(String fecha, String estado, String valor) {
        this.fecha = fecha;
        this.estado = estado;
        this.valor = valor;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }
}
