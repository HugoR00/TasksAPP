package com.example.tasksapp.api;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static final String BASE_URL = "https://www.devmasterteam.com/CursoAndroidAPI/";

    private static Retrofit instance = null;

    private RetrofitClient(){

    }

    public static synchronized Retrofit getInstance(){
        if (instance == null){
            //Cria o intercepter que registra as requisiçoes no logcat
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor()
                    .setLevel(HttpLoggingInterceptor.Level.BODY);//Todas as requisiçoes virão completas (LEVEL.BODY)

            //Cria o client que realmente vai realizar as requisições
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .build();

            //Cria o objeto Retrofit que será usado no APP
            instance = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

        }
        return instance;
    }

    //func que ja implementa o service, para não precisar implantar novamente na activity que for usar
    public static ApiService getApiService(){
        return getInstance().create(ApiService.class);
    }
}
