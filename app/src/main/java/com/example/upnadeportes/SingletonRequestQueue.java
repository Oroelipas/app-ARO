package com.example.upnadeportes;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Clase singleton para que solo haya un RequestQueue en toda la aplicacion
 * La instancia singleton tendra el context de la aplicacion, y no de la actividad,
 * por lo tanto durara todo el ciclo de vida de la aplicación
 * Copiada de: https://developer.android.com/training/volley/requestqueue.html
 */
public class SingletonRequestQueue {
        private static SingletonRequestQueue instance;
        private RequestQueue requestQueue;
        private static Context ctx;

        private SingletonRequestQueue(Context context) {
            ctx = context;
            requestQueue = getRequestQueue();

        }

        public static synchronized SingletonRequestQueue getInstance(Context context) {
            if (instance == null) {
                instance = new SingletonRequestQueue(context);
            }
            return instance;
        }

        public RequestQueue getRequestQueue() {
            if (requestQueue == null) {
                // getApplicationContext() coge el context de LA APLICACION,
                //  no de la actividad en la que se llame
                requestQueue = Volley.newRequestQueue(ctx.getApplicationContext());
            }
            return requestQueue;
        }

        public <T> void addToRequestQueue(Request<T> req) {
            getRequestQueue().add(req);
        }

    }