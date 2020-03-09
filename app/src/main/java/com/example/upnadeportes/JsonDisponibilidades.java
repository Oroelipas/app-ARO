package com.example.upnadeportes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


// https://stackoverflow.com/questions/9605913/how-do-i-parse-json-in-android
public class JsonDisponibilidades {

    JSONObject myJSON;

    public JsonDisponibilidades(String json){
        try {
            myJSON = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    public JSONObject getDisponibilidad(int id, String fecha, String hora) {
        try {
            JSONArray disponibilidades = myJSON.getJSONArray("Disponibilidad");
            for (int i = 0; i < disponibilidades.length(); i++){
                JSONObject item = disponibilidades.getJSONObject(i);
                if (item.getInt("IdActividad") == id &&
                        item.getString("Fecha").equals(fecha) &&
                        item.getString("HoraInicio").equals(hora)){
                    return item;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public JSONObject getActividad(int id) {
        try {
            JSONArray actividades = myJSON.getJSONArray("Actividades");
            for (int i = 0; i < actividades.length(); i++){
                JSONObject item = actividades.getJSONObject(i);
                if (item.getInt("Id") == id){
                    return item;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public JSONObject getMonitor(int id) {
        try {
            JSONArray monitores = myJSON.getJSONArray("Monitores");
            for (int i = 0; i < monitores.length(); i++){
                JSONObject item = monitores.getJSONObject(i);
                if (item.getInt("Id") == id){
                    return item;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public JSONObject getCentro(int id) {
        try {
            JSONArray centros = myJSON.getJSONArray("Centros");
            for (int i = 0; i < centros.length(); i++){
                JSONObject item = centros.getJSONObject(i);
                if (item.getInt("Id") == id){
                    return item;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public JSONObject getRecurso(int id) {
        try {
            JSONArray recursos = myJSON.getJSONArray("Recursos");
            for (int i = 0; i < recursos.length(); i++){
                JSONObject item = recursos.getJSONObject(i);
                if (item.getInt("Id") == id){
                    return item;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }





}




