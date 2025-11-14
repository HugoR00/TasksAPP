package com.example.tasksapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tasksapp.api.RetrofitClient;
import com.example.tasksapp.model.AuthResponse;
import com.example.tasksapp.model.RegisterRequest;
import com.example.tasksapp.util.SecurityPreferences;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private EditText etName;
    private EditText etEmail;
    private EditText etPassword;
    private Button btnRegister;
    private TextView tvLogin;
    private ImageView btnBack;

    private SecurityPreferences preferences;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        preferences = new SecurityPreferences(this);

        initViews();
        setupListeners();
    }

    private void initViews(){
        etName = findViewById(R.id.edit_text_name);
        etEmail = findViewById(R.id.edit_text_email);
        etPassword = findViewById(R.id.edit_text_password);
        tvLogin = findViewById(R.id.text_view_login);
        btnRegister = findViewById(R.id.button_register);
        btnBack = findViewById(R.id.button_back);


    }

    private void setupListeners(){
        btnRegister.setOnClickListener(v-> register());
        tvLogin.setOnClickListener(v->finish());
        btnBack.setOnClickListener(v->finish());
    }

    private void register(){

        String name = etName.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String email = etEmail.getText().toString().trim();

        if (name.isEmpty()){
            Toast.makeText(this, "Digite o seu nome!", Toast.LENGTH_SHORT).show();
            etName.requestFocus();
            return;
        }

        if (password.isEmpty() || password.length() < 6){
            Toast.makeText(this, "Digite uma senha com no mínimo 6 caracteres", Toast.LENGTH_SHORT).show();
            etPassword.requestFocus();
            return;
        }

        if (email.isEmpty() || !isValidEmail(email)){
            Toast.makeText(this, "Digite um e-mail válido!", Toast.LENGTH_SHORT).show();
            etEmail.requestFocus();
            return;
        }

        RegisterRequest request = new RegisterRequest(name, email, password);

        RetrofitClient.getApiService().register(request).enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if(response.isSuccessful() && response.body() != null){
                    AuthResponse auth = response.body();

                    preferences.saveAuthData(
                            auth.getToken(),
                            auth.getPersonKey(),
                            auth.getName()
                    );

                    Toast.makeText(RegisterActivity.this, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show();
                    goToMainActivity();
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "Erro de conexão", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isValidEmail(String email){
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void goToMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
