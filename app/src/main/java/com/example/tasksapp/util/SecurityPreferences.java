package com.example.tasksapp.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SecurityPreferences {

    //Nome do arquivo que será criado com os dados salvos
    private static final String PREFS_NAME = "TaskAppPrefs";
    private static final String TOKEN = "token";
    private static final String PERSON_KEY = "personKey";
    private static final String NAME = "name";

    private SharedPreferences preferences;

    //Construtor com a criaçao das preferences a partir do method getSharedPreferences
    //Method que tem como parametros o nome do arquivo a ser criado e o modo de acesso privado
    //para que somente o APP acesse esses dados
    public SecurityPreferences(Context context){
        preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void saveAuthData(String token, String personKey, String name){
        SharedPreferences.Editor editor = preferences.edit(); //Cria o editor e faz ele ser editavel
        //Adiciona os key/value pair ao editor
        editor.putString(TOKEN,token);
        editor.putString(PERSON_KEY,personKey);
        editor.putString(NAME,name);
        editor.apply();
    }

    public String getToken(){
        return preferences.getString(TOKEN, "");
    }

    public String getPersonKey(){
        return preferences.getString(PERSON_KEY,"");
    }

    public String getName(){
        return preferences.getString(NAME, "");
    }

    public boolean isLoggedIn(){
        String token = getToken();
        String personKey = getPersonKey();
        return !token.isEmpty() && !personKey.isEmpty();
    }

    public void logout(){
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }
}
