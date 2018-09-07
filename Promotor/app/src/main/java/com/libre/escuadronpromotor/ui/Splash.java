package com.libre.escuadronpromotor.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

import com.libre.escuadronpromotor.R;
import com.libre.escuadronpromotor.ui.storage.PreferencesStorage;

public class Splash extends Activity {

    private PreferencesStorage prefs;
    private String REGISTER_USER_KEY="REGISTER_USER_KEY";
    private String ID_CAMERA_PREFERENCE="ID_CAMERA_PREFERENCE";
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.splash_screen);
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {

                Intent registerIntent = new Intent(Splash.this,ListUsersActivity.class);
                Splash.this.startActivity(registerIntent);
                Splash.this.finish();
            }
        }, 2000);

    }


}