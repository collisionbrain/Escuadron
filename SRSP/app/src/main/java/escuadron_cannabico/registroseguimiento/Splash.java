package escuadron_cannabico.registroseguimiento;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import escuadron_cannabico.registroseguimiento.storage.PreferencesStorage;

public class Splash extends Activity {

    private PreferencesStorage prefs;
    private String REGISTER_USER_KEY="REGISTER_USER_KEY";
    private String ID_CAMERA_PREFERENCE="ORDER_PENDING";
    public Context context;
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.splash);
        context=this;
         this.prefs=new PreferencesStorage(this);
        prefs.saveData(ID_CAMERA_PREFERENCE,"0");
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {

                String key=prefs.loadData(REGISTER_USER_KEY);

                if(key==null){

                    Intent registerIntent = new Intent(Splash.this,Register.class);

                    Splash.this.startActivity(registerIntent);
                    Splash.this.finish();

                }else{
                    Intent registerIntent = new Intent(Splash.this,Principal.class);

                    Splash.this.startActivity(registerIntent);
                    Splash.this.finish();


                }
            }
        }, 2000);
    }


}