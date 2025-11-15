package com.example.tasksapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tasksapp.api.RetrofitClient;
import com.example.tasksapp.model.AuthResponse;
import com.example.tasksapp.model.LoginRequest;
import com.example.tasksapp.util.SecurityPreferences;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail;
    private EditText etPassword;
    private Button btnLogin;
    private TextView tvRegister;

    private SecurityPreferences preferences;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferences = new SecurityPreferences(this);
        if(preferences.isLoggedIn()){
            goToMainActivity();
            return;
        }

        setContentView(R.layout.activity_login);

        initViews();
        setupListeners();
    }

    private void initViews(){
        etEmail = findViewById(R.id.edit_text_email);
        etPassword = findViewById(R.id.edit_text_password);
        btnLogin = findViewById(R.id.button_login);
        tvRegister = findViewById(R.id.text_view_register);
    }

    private void setupListeners(){
        btnLogin.setOnClickListener(v -> login());
        tvRegister.setOnClickListener(v -> goToRegisterActivity());
    }

    private void login(){
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (email.isEmpty()){
            Toast.makeText(this, "Digite o email!", Toast.LENGTH_SHORT).show();
        }

        if (password.isEmpty()){
            Toast.makeText(this,"Digite a senha",Toast.LENGTH_SHORT).show();
        }

        LoginRequest request = new LoginRequest(email,password);

        RetrofitClient.getApiService().login(request).enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful() && response.body() != null){
                    AuthResponse auth = response.body();

                    Log.d("AUTH", "Token recebido: " + auth.getToken());
                    Log.d("AUTH", "PersonKey recebido: " + auth.getPersonKey());
                    Log.d("AUTH", "Nome recebido: " + auth.getName());

                    preferences.saveAuthData(
                            auth.getToken(),
                            auth.getPersonKey(),
                            auth.getName()
                    );
                    Log.d("AUTH", "Token salvo: " + preferences.getToken());
                    Log.d("AUTH", "PersonKey salvo: " + preferences.getPersonKey());


                    goToMainActivity();
                }else{
                    Toast.makeText(LoginActivity.this, "Email ou senha incorretos!",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this,"Erro ao logar!" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void goToMainActivity(){
        Intent intent = new Intent(this,MainActivity.class);
        //Impede que o usuário ao clicar para voltar volte para a tela de login, fazendo na verdade
        //com que o app seja fechado
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        //Mata a login activity
        finish();
    }

    private void goToRegisterActivity(){
        Intent intent = new Intent(this,RegisterActivity.class);
        startActivity(intent);
    }
}
