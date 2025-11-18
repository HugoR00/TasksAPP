package com.example.tasksapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tasksapp.adapter.TaskAdapter;
import com.example.tasksapp.api.RetrofitClient;
import com.example.tasksapp.database.AppDatabase;
import com.example.tasksapp.model.Task;
import com.example.tasksapp.repository.TaskRepository;
import com.example.tasksapp.util.SecurityPreferences;
import com.example.tasksapp.viewmodel.TaskViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements TaskAdapter.OnTaskListener {

    private TaskViewModel viewModel;
    private TaskAdapter adapter;
    private SecurityPreferences preferences;


    private EditText etTaskDescription;
    private Button btnAdd;
    private Button btnSync;
    private RecyclerView rvTasks;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        preferences = new SecurityPreferences(this);
        if(!preferences.isLoggedIn()){
            goToLoginActivity();
            return;
        }
        setContentView(R.layout.activity_main);

        initViewModel();

        initViews();

        setupRecyclerView();

        observeData();

        syncTasks();

        setupListeners();

    }


    @Override
    public void onTaskComplete(Task task) {
    }

    @Override
    public void onTaskDelete(Task task) {

    }

    private void initViewModel(){
        viewModel = new ViewModelProvider(this).get(TaskViewModel.class);
    }

    private void initViews(){
        etTaskDescription = findViewById(R.id.edit_text_description);
        btnAdd = findViewById(R.id.button_add);
        btnSync = findViewById(R.id.button_sync);
        rvTasks = findViewById(R.id.recycler_view_tasks);
    }

    private void setupRecyclerView(){
        adapter = new TaskAdapter(this);

        rvTasks.setLayoutManager(new LinearLayoutManager(this));
        rvTasks.setAdapter(adapter);
    }

    private void observeData(){
        Log.d("MAIN", "observeData() configurado");
        viewModel.getAllTasks().observe(this, tasks -> {
            Log.d("MAIN", "Observer notificado! Tarefas: " + (tasks != null ? tasks.size() : "null"));
            adapter.setTasks(tasks);
        });
    }

    private void setupListeners(){

        btnSync.setOnClickListener(v -> {
            syncTasks();
        });

        btnAdd.setOnClickListener(v -> {
            addTask();
        });
    }

    private void syncTasks(){
        viewModel.syncTasks();
        Toast.makeText(this, "Sincronizando...", Toast.LENGTH_SHORT).show();
    }

    private void addTask(){
        Toast.makeText(this, "Botão clicado!", Toast.LENGTH_SHORT).show();

        String description = etTaskDescription.getText().toString().trim();
        Log.d("MAIN", "Descrição: '" + description + "'");

        if(description.isEmpty()){
            Log.d("MAIN", "Descrição vazia!");
            Toast.makeText(this,"Digite uma descrição!",Toast.LENGTH_SHORT).show();
            return;
        }


        Task task = new Task();
        task.setPriorityId(1);
        task.setDescription(description);
        task.setDueDate("2025-12-31");
        task.setComplete(false);

        viewModel.createTask(task, new TaskRepository.OnTaskCreatedListener() {
            @Override
            public void onSuccess() {
                runOnUiThread(() ->{
                    Toast.makeText(MainActivity.this,"Tarefa criada com sucesso!",Toast.LENGTH_SHORT).show();
                    etTaskDescription.setText("");
                    etTaskDescription.clearFocus();
                });
            }

            @Override
            public void onFailure(String errorMessage) {

            }
        });
    }


    private void goToLoginActivity(){
        Intent intent = new Intent(this,LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

}