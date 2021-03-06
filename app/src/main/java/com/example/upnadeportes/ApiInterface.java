package com.example.upnadeportes;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiInterface {

    @FormUrlEncoded
    //@POST("actividades/ajax/TInnova_v2/ReservaClasesPuntuales_Selector/llamadaAjax/solicitaDisponibilidad")
    @POST("web-aro/api/actividades")
    Call<ResponseBody> getActividades(
            @Field("fechaInicio") String fecha
    );

    @GET("api/carreras")
    Call<ResponseBody> getCarreras();


    @FormUrlEncoded
    @POST("web-aro/api/login")
    Call<ResponseBody> postLogin(
            @Field("email") String email,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("web-aro/api/nuevousuario")
    Call<ResponseBody> postNuevoUsuario(
            @Field("nombre") String nombre,
            @Field("email") String email,
            @Field("idCarrera") String idCarrera,
            @Field("password") String password,
            @Field("fNacimiento") String fNacimiento,
            @Field("sexo") String sexo
    );

    @FormUrlEncoded
    @POST("web-aro/api/reservar")
    Call<ResponseBody> postReservar(
            @Field("IdActividad") int IdActividad,
            @Field("fecha") String fecha, //(dd-mm-yyyy)
            @Field("hora") String hora, // (hh:mm:ss)
            @Field("idUsuario") int idUsuario
    );

}
