package com.example.upnadeportes;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    JsonDisponibilidades jsonDisponibilidades;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getJsonActividades();
    }

    public void getJsonActividades(){
        Call<ResponseBody> call = ApiClient.getInstance().getUpnaApi().getActividades("2020-03-09");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String json = response.body().string();
                    System.out.println("JSON pedido");
                    System.out.println(json);
                    jsonDisponibilidades = new JsonDisponibilidades(json);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


    public void irWebView(View view) throws JSONException {
        Intent intent = new Intent(getApplicationContext(), InicioSesionWebActivity.class);

        int codigoActividad = 1234;
        String fechaActividad = "2020-02-03";
        String horaActividad = "18:00:00";

        JSONObject disponibilidad = jsonDisponibilidades.getDisponibilidad(codigoActividad, fechaActividad, horaActividad);
        JSONObject actividad = jsonDisponibilidades.getActividad(codigoActividad);
        JSONObject monitor = jsonDisponibilidades.getMonitor(disponibilidad.getInt("IdMonitor"));
        JSONObject centro = jsonDisponibilidades.getCentro(actividad.getInt("IdCentro"));
        JSONObject recurso = jsonDisponibilidades.getRecurso(disponibilidad.getInt("IdRecurso"));

        String nombreActividad = actividad.getString("Nombre");
        String centroActividad = centro.getString("Descripcion");
        String nomProfActividad = monitor.getString("Nombre");
        String recursoActividad = recurso.getString("Nombre");


        intent.putExtra("codigoActividad", codigoActividad);
        intent.putExtra("fechaActividad", fechaActividad);
        intent.putExtra("horaActividad", horaActividad);
        intent.putExtra("nombreActividad", nombreActividad);
        intent.putExtra("centroActividad", centroActividad);
        intent.putExtra("nomProfActividad", nomProfActividad);
        intent.putExtra("recursoActividad", recursoActividad);

        startActivity(intent);
    }


}
