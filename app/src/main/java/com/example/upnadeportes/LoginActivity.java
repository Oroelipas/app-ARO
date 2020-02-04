package com.example.upnadeportes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getName();
    private Button btnLogin;
    private TextView txtLoginError;
    private EditText passwordText;
    private EditText niaText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Esta linea se encarga de mantener la sesion guardando las cookies
        // Se aplica a todas las activitades y no es necesario repetirlo
        CookieHandler.setDefault( new CookieManager());

        // Esto es para poder hacer conexion https
        // Tambien solo es necesario llamarlo una vez
        HttpsTrustManager.allowAllSSL();


        btnLogin      = findViewById(R.id.btnLogin);
        txtLoginError = findViewById(R.id.txtLoginError);
        passwordText  = findViewById(R.id.passwordText);
        niaText       = findViewById(R.id.niaText);

        // Añadir el OnClickListener
        btnLogin.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v){
                                            login();
                                        }
                                    }

        );

    }



    private void login() {

        String url = "https://extuniv.unavarra.es/actividades/login";
        StringRequest req = new StringRequest(Request.Method.POST, url,
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
                        Intent myIntent = new Intent(LoginActivity.this, ActividadesActivity.class);
                        LoginActivity.this.startActivity(myIntent);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Log.i("error",error.getMessage());
                        Log.i("error", "de OnErrorResponse");
                        txtLoginError.setText("Usuario o contraseña incorrectos");

                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String>  params = new HashMap<>();
                String nia = niaText.getText().toString();
                String password = passwordText.getText().toString();
                params.put("email", nia);
                params.put("password", password);
                return params;
            }
        };

        // Añadir al request al singleton de RequestQueue
        SingletonRequestQueue.getInstance(this).addToRequestQueue(req);
    }

}
