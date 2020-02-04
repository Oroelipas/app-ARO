package com.example.upnadeportes;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class ActividadesActivity extends AppCompatActivity {

    private static final String TAG = ActividadesActivity.class.getName();
    private Button btnActividades;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividades);

        btnActividades =  findViewById(R.id.btnActividades);

        // Añadir el OnClickListener
        btnActividades.setOnClickListener(new View.OnClickListener() {
                                              @Override
                                              public void onClick(View v){
                                                  actividades();
                                              }
                                          }
        );

    }


    private void actividades() {
        String url = "https://extuniv.unavarra.es/actividades/reservas/actividades";
        StringRequest req = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Este bucle es solo para poder imprimir una String muy larga en varias veces
                        // Porque el problema esta en que la funcion Log() tiene una logitud maxima
                        int maxLogSize = 1000;
                        for(int i = 0; i <= response.length() / maxLogSize; i++) {
                            int start = i * maxLogSize;
                            int end = (i+1) * maxLogSize;
                            end = end > response.length() ? response.length() : end;
                            Log.v(TAG, response.substring(start, end));
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("status code;",  String.valueOf(error.networkResponse.statusCode));
                        Log.i("error", "de OnErrorResponse");
                    }
                }
        );

        // Añadir al request al singleton de RequestQueue
        SingletonRequestQueue.getInstance(this).addToRequestQueue(req);
    }
}


