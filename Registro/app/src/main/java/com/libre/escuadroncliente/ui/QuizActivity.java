package com.libre.escuadroncliente.ui;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RatingBar;

import com.libre.escuadroncliente.R;
import com.libre.escuadroncliente.ui.storage.PreferencesStorage;

public class QuizActivity extends Activity {

    private PreferencesStorage prefs;
    private String REGISTER_USER_KEY="REGISTER_USER_KEY";
    private String ID_CAMERA_PREFERENCE="ID_CAMERA_PREFERENCE";
    private RatingBar ratingBar;
    private EditText edtCommnet;
    private RadioButton apply,vape,drop;
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.quiz_screen);
        // requestWindowFeature(Window.FEATURE_NO_TITLE);
        ratingBar=findViewById(R.id.valoracion);
        apply=findViewById(R.id.apply);
        vape=findViewById(R.id.vape);
        drop=findViewById(R.id.drop);

        this.prefs=new PreferencesStorage(this);
        prefs.saveData(ID_CAMERA_PREFERENCE,"0");


    }


}