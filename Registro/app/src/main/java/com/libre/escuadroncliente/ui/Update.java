package com.libre.escuadroncliente.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.libre.escuadroncliente.R;
import com.libre.escuadroncliente.ui.storage.PreferencesStorage;

public class Update extends Activity {
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.update);
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                Log.e("INIT UPDATE"," ");
                Intent registerIntent = new Intent(Update.this,MarketActivity.class);
                registerIntent.putExtra("UPDATE",true);
                startActivity(registerIntent);
                finish();

            }
        }, 2000);

    }


}