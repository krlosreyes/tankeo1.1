package com.example.tankeo11.models;

import java.util.Date;

public class recarga {
    private String estado;
    private Date fecha;
    private String valor;

    public recarga(){}

    public recarga(String estado, Date fecha, String valor) {
        this.estado = estado;
        this.fecha = fecha;
        this.valor = valor;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }
}
