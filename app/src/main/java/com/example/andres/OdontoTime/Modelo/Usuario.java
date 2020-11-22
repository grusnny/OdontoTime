package com.example.andres.OdontoTime.Modelo;

import java.util.Date;

/**
 * Created by Andres on 10/03/2018.
 */

public class Usuario {

    String nombre;
    String email;
    int documento;
    String identificador;
    String QR;
    Date fecha;

    public Usuario(String nombre, String email, int documento, String identificador, String QR, Date lapso) {
        this.nombre = nombre;
        this.email = email;
        this.documento = documento;
        this.identificador = identificador;
        this.QR = QR;
        this.fecha = lapso;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getDocumento() {
        return documento;
    }

    public void setDocumento(int documento) {
        this.documento = documento;
    }

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    public String getQR() {
        return QR;
    }

    public void setQR(String QR) {
        this.QR = QR;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

}

