package com.example.tasksapp.api;

import com.example.tasksapp.model.AuthResponse;
import com.example.tasksapp.model.LoginRequest;
import com.example.tasksapp.model.RegisterRequest;
import com.example.tasksapp.model.Task;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

//Interface que define quais requisições HTTP o app pode realizar para API
public interface ApiService {

    //Cria um objeto do tipo RegisterRequest ex("Joao","joao@gmail.com","senha123")
    //O register da apiService será chamado depois passando esse objeto como argumento
    //O retrofit envia esse objeto como body da requisiçao, a API vai retornar um token e uma personKey
    //O GSON converte em JSON e retorna um AUTHRESPONSE
    @POST("Authentication/Create")
    Call<AuthResponse> register(@Body RegisterRequest request);

    //Igual o register, mas agora o objeto contem apenas login e password cadastrados e retorna
    // AUTHRESPONSE com token, personkey e name caso o login seja efetuado com sucesso
    @POST("Login")
    Call<AuthResponse> login(@Body LoginRequest request);

    //Metodo para retornar as tasks repassando no header um token e uma personkey para autenticacao
    @GET("Task")
    Call<List<Task>> getAllTasks(@Header("token") String token, @Header("personKey") String personKey);
}
