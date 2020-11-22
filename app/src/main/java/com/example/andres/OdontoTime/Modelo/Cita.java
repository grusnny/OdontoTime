package com.example.andres.OdontoTime.Modelo;

/**
 * Created by Juan David on 11/03/2018.
 */
//Agrupacion de tabla de la base de datos
public class Cita {

    private String nombre;
    private String serie;
    private String tipologia;

    public Cita(String nombre, String serie, String tipologia) {
        this.nombre = nombre;
        this.serie = serie;
        this.tipologia = tipologia;
    }

    public Cita() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public String getTipologia() {
        return tipologia;
    }

    public void setTipologia(String tipologia) {
        this.tipologia = tipologia;
    }
}
