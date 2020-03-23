package com.example.upnadeportes.tabbed;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

        // Creamos un clickListener e implementamos su acción
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Actividad actividad = actividades.get(position);
                System.out.println(actividad);
                reservaDialog(view, actividad);


            }
        });

    }

    @Override
    public int getItemCount() {
        return actividades.size();
    }

    public void setOnItemClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void reservaDialog(View view, Actividad actividad) {
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getRootView().getContext());
        builder.setTitle(R.string.tituloDialog);

        String datosActividad = "Actividad: " + actividad.getNombre() + "\n";
        datosActividad += "Fecha: " + actividad.getFecha() + "\n";
        datosActividad += "Hora: " + actividad.getHoraInicio().substring(0, 5) + " - " + actividad.getHoraFin().substring(0, 5) + "\n\n";
        datosActividad += "¿Seguro que quieres reservar una plaza para esta actividad?";

        builder.setMessage(datosActividad);

        builder.setPositiveButton(R.string.siDialog, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                System.out.println("Actividad reservada");
            }
        });

        builder.setNegativeButton(R.string.noDialog, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                System.out.println("Actividad rechazada");
            }
        });

        builder.create().show();
    }


}
