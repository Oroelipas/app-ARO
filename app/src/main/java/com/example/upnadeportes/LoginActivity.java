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
    private RequestQueue mRequestQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //si quitamos estas lineas entonces podemos ver las cookies en parseNetworkResponse
        // al parecer estas lineas cojen a cookie antes que tu y la ocultan
        CookieManager manager = new CookieManager();
        CookieHandler.setDefault( manager  );


        // esto es para poder hacer conexion https
        HttpsTrustManager.allowAllSSL();

        this.mRequestQueue = Volley.newRequestQueue(this);


        btnLogin = findViewById(R.id.btnLogin);
        txtLoginError = findViewById(R.id.txtLoginError);
        passwordText   = (EditText)findViewById(R.id.passwordText);
        niaText   = (EditText)findViewById(R.id.niaText);


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
                        // response
                        //ESTO ES SOLO PARA IMPRIMIR STRINGS LARGAS
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
                        // error
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
        mRequestQueue.add(req);
    }

}
