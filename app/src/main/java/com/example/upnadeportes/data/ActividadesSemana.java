package com.example.upnadeportes.data;

import android.annotation.SuppressLint;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ActividadesSemana {

    private JSONObject json;
    private ArrayList<Actividad>[] actividadesDias;
    private HashMap<Integer, String> actividades;
    private HashMap<Integer, String> monitores;
    private HashMap<Integer, String> recursos;
    private HashMap<Integer, String> centros;

    String tag = "Modelo / ActividadesSemana";

    private static ActividadesSemana miInstancia = null;

    public static ActividadesSemana getInstance() {
        if (miInstancia == null)
            miInstancia = new ActividadesSemana();
        return miInstancia;
    }

    public void init(String jsonRecibido) {
        // Nosostros por ahora la fecha no la vamos a comprobar, ya que solo queremos que nos devuelva esa fecha
        try {
            this.json = new JSONObject(jsonRecibido);
            cargarActividades();
            cargarMonitores();
            cargarRecursos();
            cargarCentros();
            cargarActividadesSemana();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("WrongViewCast")
    private ActividadesSemana() {
        actividadesDias = new ArrayList[5];
        actividades = new HashMap<>();
        monitores = new HashMap<>();
        recursos = new HashMap<>();
        centros = new HashMap<>();
        // Inicializamos el array de días
        for (int i = 0; i < actividadesDias.length; i++) {
            actividadesDias[i] = new ArrayList<>();
        }

    }


    private void cargarActividades() {
        try {
            JSONArray actividadesJSON = json.getJSONArray("Actividades");
            JSONObject item;
            int idActividad;
            String nombreActividad;
            for (int i = 0; i < actividadesJSON.length(); i++) {
                item = actividadesJSON.getJSONObject(i);
                idActividad = item.getInt("Id");
                nombreActividad = item.getString("Nombre");
                actividades.put(idActividad, nombreActividad);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void cargarMonitores() {
        try {
            JSONArray profesoresJSON = json.getJSONArray("Monitores");
            JSONObject item;
            int idMonitor;
            String nombreMonitor;
            for (int i = 0; i < profesoresJSON.length(); i++) {
                item = profesoresJSON.getJSONObject(i);
                idMonitor = Integer.parseInt(item.getString("Id"));
                nombreMonitor = item.getString("Nombre");
                monitores.put(idMonitor, nombreMonitor);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void cargarRecursos() {
        try {
            JSONArray recursosJSON = json.getJSONArray("Recursos");
            JSONObject item;
            int idRecurso;
            String nombreRecurso;
            for (int i = 0; i < recursosJSON.length(); i++) {
                item = recursosJSON.getJSONObject(i);
                idRecurso = Integer.parseInt(item.getString("Id"));
                nombreRecurso = item.getString("Nombre");
                recursos.put(idRecurso, nombreRecurso);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void cargarCentros() {
        try {
            JSONArray centrosJSON = json.getJSONArray("Centros");
            JSONObject item;
            int idCentro;
            String nombreCentro;
            for (int i = 0; i < centrosJSON.length(); i++) {
                item = centrosJSON.getJSONObject(i);
                idCentro = Integer.parseInt(item.getString("Id"));
                nombreCentro = item.getString("Descripcion");
                centros.put(idCentro, nombreCentro);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Método que carga las actividades que están en la Web de la UPNA poniéndolas en el array
     * correspondiente
     * */
    public void cargarActividadesSemana() {

        // Borramos la información de las actividades anteriores
        if (actividadesDias[0].size() > 0) {
            for (int i = 0; i < actividadesDias.length; i++) {
                actividadesDias[i].clear();
            }
        }

        try {
            JSONObject item;
            String fechaAnterior = "";
            Actividad actividad;
            // Nos servirá para meter en el día correspondiente la actividad
            int j = 0;
            // Cogemos el array del JSON con las disponibilidades
            JSONArray disponibilidades = json.getJSONArray("Disponibilidad");

            // La primera fecha será del lunes
            if (disponibilidades.length() > 0) {
                item = disponibilidades.getJSONObject(0);
                fechaAnterior = fecha(item);
            }

            for (int i = 0; i < disponibilidades.length(); i++) {
                item = disponibilidades.getJSONObject(i);
                // String codigo, String fecha, String horaInicio, String horaFin, String nombre, String centro, String nombreProfesor, String recursoActividad
                actividad = new Actividad(idActividad(item), fecha(item), horaInicio(item),
                        horaFin(item), nombreActividad(item), centro(item), nombreMonitor(item),
                        recurso(item), plazasLibres(item));
                if (!fechaIgual(fechaAnterior, item)) {
                    fechaAnterior = fecha(item);
                    j++;
                }
                actividadesDias[j].add(actividad);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private boolean fechaIgual(String fechaAnt, JSONObject item) {
        try {
            String fecha = item.getString("Fecha");
            return fechaAnt.equals(fecha);
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    private String fecha(JSONObject item) {
        String fecha = null;
        try {
            fecha = item.getString("Fecha");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return fecha;
    }

    private int idActividad(JSONObject item) {
        try {
             return item.getInt("IdActividad");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private String horaInicio(JSONObject item) {
        String horaInicio = null;
        try {
            horaInicio = item.getString("HoraInicio");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return horaInicio;
    }

    private String horaFin(JSONObject item) {
        String horaFin = null;
        try {
            horaFin = item.getString("HoraFin");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return horaFin;
    }

    private String nombreActividad(JSONObject item) {
        int idActividad;
        try {
            idActividad = item.getInt("IdActividad");
            return actividades.get(idActividad);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    private String nombreMonitor(JSONObject item) {
        String idMonitor = null;
        try {
            idMonitor = item.getString("IdMonitor");
            return monitores.get(idMonitor);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return idMonitor;
    }

    public int plazasLibres(JSONObject item) {
        try {
            return item.getInt("PlazasLibres");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private String recurso(JSONObject item) {
        String idRecurso = null;
        try {
            idRecurso = item.getString("IdRecurso");
            return recursos.get(idRecurso);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return idRecurso;
    }

    private String centro(JSONObject item) {
        if (centros.size() == 1)
            return (String) ((centros.values()).toArray())[0];
        else {
            String idActividad = String.valueOf(idActividad(item));
            JSONArray actividades;
            String idCentro = "";
            try {
                actividades = json.getJSONArray("Actividades");
                for (int i = 0; i < actividades.length(); i++) {
                    JSONObject itemActividad = actividades.getJSONObject(i);
                    if (idActividad.equals(itemActividad.get("Id"))) {
                        idCentro = itemActividad.getString("IdCentro");
                        break;
                    }
                }
                return centros.get(idCentro);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    public ArrayList<Actividad>[] getActividadesDiasSemana() {
        return this.actividadesDias;
    }

    public ArrayList<Actividad> getLunes() {
        return this.actividadesDias[0];
    }

    public ArrayList<Actividad> getMartes() {
        return this.actividadesDias[1];
    }

    public ArrayList<Actividad> getMiercoles() {
        return this.actividadesDias[2];
    }

    public ArrayList<Actividad> getJueves() {
        return this.actividadesDias[3];
    }

    public ArrayList<Actividad> getViernes() {
        return this.actividadesDias[4];
    }

}
