package com.libre.escuadroncliente.ui.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.libre.escuadroncliente.ui.util.Data;

import java.io.Serializable;

public class PreferencesStorage {

    private SharedPreferences preferences;

    public PreferencesStorage(Context context ){
        this.preferences  = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void saveData (String key,  String data){
        SharedPreferences.Editor editor = this.preferences.edit();
        editor.putString(key,data);
        editor.commit();
    }

    public String loadData(String key){
        return this.preferences.getString(key, null);
    }



    public void saveDataObjet(String key,  Serializable data){
        SharedPreferences.Editor editor = this.preferences.edit();
        editor.putString(key, Data.objectToString(data));
        editor.commit();
    }

    public Serializable loadDatObjet(String key){
        return Data.stringToObject(this.preferences.getString(key, null));
    }



}
