package com.example.upnadeportes;

import android.app.Application;
import android.content.Context;

/*
* Esta clase guardara datos estaticos para toda la aplicacion que todas las Activities puedan leer
* */
public class MyApplication extends Application {
    private int idUsuario;
    private String email;
    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext =   getApplicationContext();

    }

    public static Context getContext() {
        return sContext;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public String getEmailUsuario() {
        return email;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public void setEmailUsuario(String email) {
        this.email = email;
    }
}
