package com.example.upnadeportes.tabbed;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.upnadeportes.R;
import com.google.android.material.tabs.TabLayout;

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
        // Se crea un adaptador de secciones.
        // Cada sección guardará la información de esa sección.
        // Ese adaptador está en una clase dentro del directorio ui.main
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        // Esta parte carga el visor que está dentro de la sección de la activity
        ViewPager viewPager = findViewById(R.id.view_pager);
        // Le ponemos a ese visor el adaptador que hemos creado.
        viewPager.setAdapter(sectionsPagerAdapter);
        // Cargamos las pestañas que dividen el contenido en secciones
        TabLayout tabs = findViewById(R.id.tabs);
        // Viculamos las pestañas con el visor de página
        tabs.setupWithViewPager(viewPager);


    }
}