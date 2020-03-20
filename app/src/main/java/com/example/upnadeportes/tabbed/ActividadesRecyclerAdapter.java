package com.example.upnadeportes.tabbed;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.upnadeportes.R;
import com.example.upnadeportes.data.Actividad;

import java.util.ArrayList;

public class ActividadesRecyclerAdapter extends RecyclerView.Adapter<ActividadesRecyclerAdapter.ActividadesViewHolder> {

    protected View.OnClickListener onClickListener;
    private ArrayList<Actividad> actividades;

    public static class ActividadesViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewNombreActividad;
        public TextView textViewHora;

        public ActividadesViewHolder(View v) {
            super(v);
            this.textViewNombreActividad = v.findViewById(R.id.nombre_actividad);
            this.textViewHora = v.findViewById(R.id.hora_actividad);
        }

    }

    public ActividadesRecyclerAdapter(ArrayList<Actividad> actividades) {
        this.actividades = actividades;
    }

    // Crea nuevas Views (lo invoca el layout manager)
    public ActividadesViewHolder onCreateViewHolder(ViewGroup parent, int type) {
        // Crea una nueva view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_actividad_recycler, parent, false);
        v.setOnClickListener(onClickListener);
        ActividadesViewHolder vh = new ActividadesViewHolder(v);
        return vh;
    }

    // Reemplaza el contenido de una View (lo invoca el layout manager)
    public void onBindViewHolder(ActividadesRecyclerAdapter.ActividadesViewHolder holder, int position) {
        TextView textView_nombreActividad = holder.textViewNombreActividad;
        TextView textView_hora = holder.textViewHora;

        textView_nombreActividad.setText(actividades.get(position).getNombre());
        String hora = actividades.get(position).getHoraInicio().substring(0, 5) + " - " + actividades.get(position).getHoraFin().substring(0, 5);
        textView_hora.setText(hora);
    }

    @Override
    public int getItemCount() {
        return actividades.size();
    }
}
