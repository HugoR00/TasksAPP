package com.example.tasksapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.tasksapp.model.Task;
import com.example.tasksapp.repository.TaskRepository;

import java.util.List;

//ViewModel é o intermediário entre UI e dados (repo)
public class TaskViewModel extends AndroidViewModel {

    //Variável de classe do repo
    private TaskRepository repository;

    //Variável de classe para gerar cache das tarefas
    private LiveData<List<Task>> allTasks;

    //Construtor da viewmodel, instanciando o repo e vinculando ao cache as tasks vindas do repo
    public TaskViewModel(@NonNull Application application) {
        super(application);
        repository = new TaskRepository(application);
        allTasks = repository.getAllTasks();
    }

    //Metodo que retorna as tarefas com os dados vindos do banco
    //Banco -> Repo -> ViewModel -> UI
    //É importante para que a UI não chame direto do repo, fazendo separação das tarefas
    public LiveData<List<Task>> getAllTasks(){
        return allTasks;
    }

    //Delega para o repo syncar as tarefas, buscando na API e salvando no banco
    public void syncTasks(){
        repository.syncTasks();
    }
}
