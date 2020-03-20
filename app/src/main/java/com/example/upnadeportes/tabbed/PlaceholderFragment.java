package com.example.upnadeportes.tabbed;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.upnadeportes.R;
import com.example.upnadeportes.data.Actividad;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 * Se encarga de guardar la vista de lo que se va a mostrar
 */
public class PlaceholderFragment extends Fragment {

    public static String TAG = "Fragment";

    private static final String ARG_SECTION_NUMBER = "section_number";
    // Guarda el modelo con la información de la vista
    private PageViewModel pageViewModel;

    private ArrayList<Actividad> actividadesDia;
    private ActividadesRecyclerAdapter adapter;

    /**
     * Método estático al que se le pide una nueva instancia y la crea
     * Según el número al final lo que está devolviendo lo que queremos
     */
    public static PlaceholderFragment newInstance(int index) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        // Le pasa el índice de la ventana con un index
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Iniciamos la lista con las actividades de lunes
        this.actividadesDia = new ArrayList<>();

        pageViewModel = ViewModelProviders.of(this).get(PageViewModel.class);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }

        // Pensaba que iba a guardar la información de las ventanas
        this.setRetainInstance(true);

        // Aquí es donde se hace el cambio del contenido de la pestaña. Si clicamos en
        // en otra que tenga un índice distinto devuelve otro día de actividades
        pageViewModel.setIndex(index);

        // Ponemos aquí un switch con lo índices y la carga de las actividades
        switch (index) {
            case 1:
                Log.d(TAG, "Pedimos actividades lunes");
                pageViewModel.getLunes().observe(this, new Observer<List<Actividad>>() {
                    @Override
                    public void onChanged(List<Actividad> actividades) {
                        actualizarAdapter(actividades);
                    }
                });
                break;
            case 2:
                Log.d(TAG,"Pedimos actividades martes");
                pageViewModel.getMartes().observe(this, new Observer<List<Actividad>>() {
                    @Override
                    public void onChanged(List<Actividad> actividades) {
                        actualizarAdapter(actividades);
                    }
                });
                break;
            case 3:
                Log.d(TAG, "Pedimos actividades miércoles");
                pageViewModel.getMiercoles().observe(this, new Observer<List<Actividad>>() {
                    @Override
                    public void onChanged(List<Actividad> actividades) {
                        actualizarAdapter(actividades);
                    }
                });
                break;
            case 4:
                Log.d(TAG, "Pedimos actividades jueves");
                pageViewModel.getJueves().observe(this, new Observer<List<Actividad>>() {
                    @Override
                    public void onChanged(List<Actividad> actividades) {
                        actualizarAdapter(actividades);
                    }
                });
                break;
            case 5:
                Log.d(TAG, "Pedimos actividades viernes");
                pageViewModel.getViernes().observe(this, new Observer<List<Actividad>>() {
                    @Override
                    public void onChanged(List<Actividad> actividades) {
                        actualizarAdapter(actividades);
                    }
                });
                break;
        }

    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_main, container, false);


        final TextView textView = root.findViewById(R.id.section_label);
        pageViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });


        // Cargamos las actividades
        RecyclerView recyclerView = root.findViewById(R.id.lista_actividades);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ActividadesRecyclerAdapter(this.actividadesDia);
        recyclerView.setAdapter(adapter);


        return root;
    }


    public void actualizarAdapter(List<Actividad> actividades) {
        this.actividadesDia.clear();
        this.actividadesDia.addAll(actividades);
        this.adapter.notifyDataSetChanged();
    }


}