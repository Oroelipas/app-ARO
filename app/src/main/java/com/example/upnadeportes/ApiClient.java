package com.example.upnadeportes;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    //public static final String UPNA_URL="https://extuniv.unavarra.es";
    public static final String UPNA_URL="https://upnadepor.me";
    public static final String AWS_URL="https://upnadepor.me";
    private static ApiClient myInstance;
    private Retrofit retrofitUpna;
    private Retrofit retrofitAws;


    private ApiClient() {
        retrofitUpna = new Retrofit.Builder()
                .baseUrl(UPNA_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofitAws = new Retrofit.Builder()
                .baseUrl(AWS_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static synchronized ApiClient getInstance() {
        if (myInstance == null){
            myInstance = new ApiClient();
        }
        return  myInstance;
    }


    public  ApiInterface getUpnaApi(){
        return  retrofitUpna.create(ApiInterface.class);
    }
    public  ApiInterface getAwsApi(){
        return  retrofitAws.create(ApiInterface.class);
    }

}