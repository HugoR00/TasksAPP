package com.example.tasksapp;

import android.content.Intent;
import android.os.Bundle;
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
import com.example.tasksapp.model.Task;
import com.example.tasksapp.util.SecurityPreferences;
import com.example.tasksapp.viewmodel.TaskViewModel;

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

        setupListeners();

        syncTasks();

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
        viewModel.getAllTasks().observe(this, tasks -> {
            adapter.setTasks(tasks);
        });
    }

    private void setupListeners(){
        btnSync.setOnClickListener(v -> syncTasks());
    }

    private void syncTasks(){
        viewModel.syncTasks();
    }

    private void goToLoginActivity(){
        Intent intent = new Intent(this,LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

}