package com.example.upnadeportes.data;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Actividad implements Serializable {
    private int idActividad;
    private Date fecha;
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
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            this.fecha = sdf.parse(fecha);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.nombre = nombreActividad;
        this.centro = centro;
        this.nombreMonitor = nombreMonitor;
        this.recursoActividad = recursoActividad;
        this.plazasLibres = plazasLibres;
    }

    public int getIdActividad() { return this.idActividad; }

    public Date getFecha() { return this.fecha; }

    public String getHoraInicio() { return this.horaInicio; }

    public String getHoraFin() { return this.horaFin; }

    public String getNombre() { return this.nombre; }

    public String getCentro() { return this.centro; }

    public String getNombreMonitor() { return this.nombreMonitor; }

    public String getRecursoActividad() { return this.recursoActividad; }

    public int getPlazasLibres() { return this.plazasLibres; }

    public String getFechaTexto() {
        Locale espanol = new Locale("es","ES");
        SimpleDateFormat dia = new SimpleDateFormat("EEEE d MMMM", espanol);
        String diaActividad = dia.format(this.fecha);
        return diaActividad;
    }

    public String toString() {
        String s = "Id Actividad: " + getIdActividad() + ", nombre: " + getNombre();
        s  = s + ", fecha: " + getFecha() + ", hora: " + getHoraInicio() + "-" + getHoraFin();
        return s;
    }

}
