package com.libre.registro.ui.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
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









}
