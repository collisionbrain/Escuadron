package com.libre.escuadroncliente.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

import com.libre.escuadroncliente.R;
import com.libre.escuadroncliente.ui.storage.PreferencesStorage;

public class Splash extends Activity {

    private PreferencesStorage prefs;
    private String REGISTER_USER_KEY="REGISTER_USER_KEY";
    private String ID_CAMERA_PREFERENCE="ID_CAMERA_PREFERENCE";
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.splash_screen);
       // requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.prefs=new PreferencesStorage(this);
        prefs.saveData(ID_CAMERA_PREFERENCE,"0");
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {

                String key=prefs.loadData(REGISTER_USER_KEY);
                if(key==null){
                    Intent registerIntent = new Intent(Splash.this,RegisterActivity.class);
                    Splash.this.startActivity(registerIntent);
                    Splash.this.finish();
                }else{
                    Intent registerIntent = new Intent(Splash.this,MarketActivity.class);
                    Splash.this.startActivity(registerIntent);
                    Splash.this.finish();
                }
            }
        }, 2000);

    }


}