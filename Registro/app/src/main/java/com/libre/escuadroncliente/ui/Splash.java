package com.libre.escuadroncliente.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;

import com.google.firebase.messaging.RemoteMessage;
import com.libre.escuadroncliente.R;
import com.libre.escuadroncliente.ui.storage.DBHelper;
import com.libre.escuadroncliente.ui.storage.PreferencesStorage;

public class Splash extends Activity {

    private PreferencesStorage prefs;
    private String REGISTER_USER_KEY="REGISTER_USER_KEY";
    private String ORDER_PENDING="ORDER_PENDING";
    private String ID_CAMERA_PREFERENCE="ORDER_PENDING";
    public  boolean needUpdate;
    public  boolean addresDBloaded;
    public Context context;
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.splash_screen);
        Intent intent = getIntent();
        context=this;
        needUpdate=intent.getBooleanExtra("UPDATE",false);
        addresDBloaded=intent.getBooleanExtra("LOADED",false);
        DBHelper dbHelper=new DBHelper(this);

        this.prefs=new PreferencesStorage(this);
        prefs.saveData(ID_CAMERA_PREFERENCE,"0");
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {

                String key=prefs.loadData(REGISTER_USER_KEY);
                String feed=prefs.loadData(ORDER_PENDING);

                if(key==null){
                    new insertDataForAddressTask().execute();

                }else{
                    Intent registerIntent = new Intent(Splash.this,MarketActivity.class);
                    registerIntent.putExtra("UPDATE",needUpdate);
                    Splash.this.startActivity(registerIntent);
                    Splash.this.finish();

    /*
                    if(feed==null){

                        Intent registerIntent = new Intent(Splash.this,MarketActivity.class);
                        Splash.this.startActivity(registerIntent);
                        Splash.this.finish();
                    }else{
                        Intent registerIntent = new Intent(Splash.this,QuizActivity.class);
                        Splash.this.startActivity(registerIntent);
                        Splash.this.finish();
                    }

*/

                }
            }
        }, 2000);
    }

    private class insertDataForAddressTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {

        }
        @Override
        protected Void doInBackground(Void... params) {
            loadAddressData(context);
            return null;
        }

        @Override
        protected void onPostExecute(Void param) {
            Intent registerIntent = new Intent(Splash.this,RegisterActivity.class);
            Splash.this.startActivity(registerIntent);
            Splash.this.finish();
        }
    }


    public void loadAddressData(Context context){
        DBHelper dbHelper=new DBHelper(context);

    }



}