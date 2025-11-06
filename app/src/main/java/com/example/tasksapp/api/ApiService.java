package com.example.tasksapp.api;

import com.example.tasksapp.model.Task;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

//Interface que define quais requisições HTTP o app pode realizar para API
public interface ApiService {

    @GET("Task")
    Call<List<Task>> getAllTasks();
}
