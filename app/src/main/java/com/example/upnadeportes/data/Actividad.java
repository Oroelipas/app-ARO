package com.example.upnadeportes.data;

import java.io.Serializable;

public class Actividad implements Serializable {
    private int idActividad;
    private String fecha;
    private String horaInicio;
    private String horaFin;
    private String nombre;
    private String centro;
    private String nombreMonitor;
    private String recursoActividad;
    private int plazasLibres;

    public Actividad(int idActividad, String fecha, String horaInicio, String horaFin,
                     String nombreActividad, String centro, String nombreMonitor,
                     String recursoActividad, int plazasLibres) {
        this.idActividad = idActividad;
        this.fecha = fecha;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.nombre = nombreActividad;
        this.centro = centro;
        this.nombreMonitor = nombreMonitor;
        this.recursoActividad = recursoActividad;
        this.plazasLibres = plazasLibres;
    }

    public int getIdActividad() { return this.idActividad; }

    public String getFecha() { return this.fecha; }

    public String getHoraInicio() { return this.horaInicio; }

    public String getHoraFin() { return this.horaFin; }

    public String getNombre() { return this.nombre; }

    public String getCentro() { return this.centro; }

    public String getNombreMonitor() { return this.nombreMonitor; }

    public String getRecursoActividad() { return this.recursoActividad; }

    public int getPlazasLibres() { return this.plazasLibres; }

}
