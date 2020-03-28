package com.example.upnadeportes.tabbed;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.upnadeportes.ApiClient;
import com.example.upnadeportes.MyApplication;
import com.example.upnadeportes.R;
import com.example.upnadeportes.data.Actividad;
import com.example.upnadeportes.login.LoggedInUserView;
import com.example.upnadeportes.registro.RegistroResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActividadesRecyclerAdapter extends RecyclerView.Adapter<ActividadesRecyclerAdapter.ActividadesViewHolder> {

    public final String TAG = this.getClass().getName();

    protected View.OnClickListener onClickListener;
    private ArrayList<Actividad> actividades;
    static String tag = "ActividadesRecyclerAdapter";


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

        // Creamos un clickListener para la vista del item e implementamos su acción
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Actividad actividad = actividades.get(position);
                Log.d(tag, actividad.toString());
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
        datosActividad += "Fecha: " + actividad.getFechaTexto() + "\n";
        datosActividad += "Hora: " + actividad.getHoraInicio().substring(0, 5) + " - " + actividad.getHoraFin().substring(0, 5) + "\n\n";
        datosActividad += "¿Seguro que quieres reservar una plaza para esta actividad?";

        builder.setMessage(datosActividad);

        builder.setPositiveButton(R.string.siDialog, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.d(tag, "Reservamos actividad");

                int idActividad = actividad.getIdActividad();
                String fecha =  actividad.getFechaTextoBD();
                String hora =  actividad.getHoraInicio();
                int idUsuario = ((MyApplication)MyApplication.getContext()).getIdUsuario();

                Call<ResponseBody> call = ApiClient.getInstance().getAwsApi().postReservar(idActividad, fecha, hora, idUsuario );
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {
                            String json = response.body().string();
                            JSONObject jsonRespuesta;
                            jsonRespuesta = new JSONObject(json);
                            if (jsonRespuesta.getString("error").equals("null")) {
                                String texto = "Reserva completada";
                                Toast toast = Toast.makeText(MyApplication.getContext(), texto, Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.BOTTOM, 0, 0);
                                toast.show();
                                Log.v(TAG,"Reserva correcta");
                                String idReserva = (String) jsonRespuesta.get("idReserva");
                            } else {
                                if (jsonRespuesta.getInt("error") == 409) {
                                    String texto = "Reserva ya realizada";
                                    Toast toast = Toast.makeText(MyApplication.getContext(), texto, Toast.LENGTH_LONG);
                                    toast.setGravity(Gravity.BOTTOM, 0, 0);
                                    toast.show();
                                    Log.v(TAG,"Reserva ya existente: 409");
                                }
                            }
                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                            Log.v(TAG,"Error procesando la respuesta a la petición");
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.v(TAG,"Error Reservando");
                    }
                });
            }
        });

        builder.setNegativeButton(R.string.noDialog, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.d(tag, "No reservamos actividad");
            }
        });

        builder.create().show();
    }


}
