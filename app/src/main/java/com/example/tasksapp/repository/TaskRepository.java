package com.example.tasksapp.repository;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.tasksapp.api.ApiService;
import com.example.tasksapp.api.RetrofitClient;
import com.example.tasksapp.database.AppDatabase;
import com.example.tasksapp.database.TaskDao;
import com.example.tasksapp.model.Task;
import com.example.tasksapp.util.SecurityPreferences;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//Busca os dados e transfere a UI (Activity ou vm)
public class TaskRepository {

    //Variável de classe que insere o taskDao que pode executar comandos no banco
    private TaskDao taskDao;

    //Insere a apiservice, que delimita os metodos que o repository pode executar
    private ApiService apiService;

    //Cache das tasks salvas no banco local
    private LiveData<List<Task>> allTasks;

    //Gerenciador de threads
    private ExecutorService executorService;

    //Insere as preferencias de segurança
    private SecurityPreferences securityPreferences;

    public TaskRepository(Application application){

        //Obtém a instância do DB
        AppDatabase database = AppDatabase.getInstance(application);

        //Pega o DAO para realizar operações no banco
        taskDao = database.taskDao();

        //Pega o apiservice pronto para realizar requisições HTTP
        apiService = RetrofitClient.getApiService();

        //Pega as tasks salvas em cache e analisa em caso de update
        allTasks = taskDao.getAllTasks();

        //Define as threads
        executorService = Executors.newFixedThreadPool(2);

        securityPreferences = new SecurityPreferences(application);

        //OBS: A ApiService e quem executa as requisições HTTP com a API, a DAO apenas executa
        //operações no banco de dados, então o service passa pro banco e a DAO pega ou executa
        //as operações no banco
    }

    //Retorna o livedata do banco local, apresentando a lista de tasks
    //O DAO executa a operação de puxar as tasks, passa para o repositório via construtor (allTasks = taskDao.getAllTasks();)
    //e o repositório retorna esses dados a partir desse method
    public LiveData<List<Task>> getAllTasks(){
        return allTasks;
    }

    public void syncTasks(){

        String token = securityPreferences.getToken();
        String personKey = securityPreferences.getPersonKey();

        //Requisicao GET la da ApiService que passa token e personkey pegos da securityprefs
        //FLUXO -> Faz requisicao de cadastro > Api retorna token > Token e passado ao api service > salva no sharedprefs >
        apiService.getAllTasks(token, personKey).enqueue(new Callback<List<Task>>() {
            @Override
            public void onResponse(Call<List<Task>> call, Response<List<Task>> response) {
                if(response.isSuccessful() && response.body() != null){
                    List<Task> taskList = response.body();

                    executorService.execute(() ->{
                        taskDao.deleteAll();
                        taskDao.insertAll(taskList);
                    });
                } else if(response.code() == 401){
                    securityPreferences.logout();
                }
            }

            @Override
            public void onFailure(Call<List<Task>> call, Throwable t) {
                Log.e("SYNC", "Erro ao syncar");
            }
        });
    }



}
