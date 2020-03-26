package com.example.upnadeportes.tabbed;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.upnadeportes.ApiClient;
import com.example.upnadeportes.R;
import com.example.upnadeportes.data.ActividadesSemana;
import com.google.android.material.tabs.TabLayout;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActividadesActivity extends AppCompatActivity {

    /**
     * Lo único en este controlador es poner a funcionar la pantalla de las pestañas.
     * */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Llamamos a crear como siempre la actividad
        super.onCreate(savedInstanceState);
        // Seleccionamos la vista principal para que se muestre en la aplicación
        setContentView(R.layout.activity_actividades);

        // Cargamos la fecha
        Date d = new Date();
        SimpleDateFormat diaSemanaDate = new SimpleDateFormat("u");
        // Obtengo el día de la semana y le resto uno porque el índice de la tabbed
        // va de 0 a 4
        int diaSemana = Integer.valueOf(diaSemanaDate.format(d)) - 1;
        System.out.println(diaSemana);
        if (diaSemana > 4)
            diaSemana = 1;

        // Se crea un adaptador de secciones.
        // Cada sección guardará la información de esa sección.
        // Ese adaptador está en una clase dentro del directorio ui.main
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());

        // Pedimos el JSON con las actividades de la semana
        Call<ResponseBody> call = ApiClient.getInstance().getUpnaApi().getActividades("2020-03-09");
        int finalDiaSemana = diaSemana;
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        String jsonRecibido = response.body().string();
                        ActividadesSemana.getInstance().init(jsonRecibido);
                        // Esta parte carga el visor que está dentro de la sección de la activity
                        ViewPager viewPager = findViewById(R.id.view_pager);
                        // Le ponemos a ese visor el adaptador que hemos creado.
                        viewPager.setAdapter(sectionsPagerAdapter);
                        // Cargamos las pestañas que dividen el contenido en secciones
                        TabLayout tabs = findViewById(R.id.tabs);
                        // Viculamos las pestañas con el visor de página
                        tabs.setupWithViewPager(viewPager);
                        // Le comunicamos a la vista la pestaña que queremos ver

                        viewPager.setCurrentItem(finalDiaSemana);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }


            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //Toast.makeText(ActividadesSemana.this, t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });



    }



}