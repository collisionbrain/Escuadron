package com.libre.escuadroncliente.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.libre.escuadroncliente.R;
import com.libre.escuadroncliente.ui.pojos.Quiz;
import com.libre.escuadroncliente.ui.storage.PreferencesStorage;
import com.libre.escuadroncliente.ui.util.Data;
import com.unstoppable.submitbuttonview.SubmitButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.libre.escuadroncliente.ui.util.Constants.JSON_FILE_CONFIG;
import static com.libre.escuadroncliente.ui.util.Constants.URL_REMOTE;
import static com.libre.escuadroncliente.ui.util.Data.saveJSONFile;

public class QuizActivity extends Activity implements  View.OnClickListener  {

    private PreferencesStorage prefs;
    private RatingBar response1,response2,response3,response4;
    private TextView question1,question2,question3,question4;
    private SubmitButton btnEnviarQuiz;
    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseStorage storage;
    final long ONE_MEGABYTE = 1024 * 1024;
    private String ORDER_PENDING="ORDER_PENDING";
    private JSONObject data;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.quiz_screen);
        // requestWindowFeature(Window.FEATURE_NO_TITLE);
        question1=findViewById(R.id.q1);
        question2=findViewById(R.id.q1);
        question3=findViewById(R.id.q1);
        question4=findViewById(R.id.q1);



        btnEnviarQuiz= findViewById(R.id.btnSiguienteQuiz);
        btnEnviarQuiz.setOnClickListener(this);
        this.prefs=new PreferencesStorage(this);
        prefs.saveData(ORDER_PENDING,"0");
        final ArrayList<Quiz> questionList = new ArrayList<Quiz>(4);
        try{
            JSONObject productObject=Data.loadJSONFileObjet("quiz","quiz");
            JSONArray items= productObject.getJSONArray("items");

            for (int a=0;a<=items.length()-1;a++) {
                JSONObject jsonObject = items.getJSONObject(a);
                Quiz question=new Quiz();
                question.question=jsonObject.getString("question");
                question.result=0;
                questionList.add(question);
            }



        }catch (JSONException ex){
            ex.getStackTrace();
        }

        question1.setText(questionList.get(0).question);
        question2.setText(questionList.get(1).question);
        question3.setText(questionList.get(2).question);
        question4.setText(questionList.get(3).question);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSiguienteQuiz:
                response1.getRating();

                break;

        }
    }


}