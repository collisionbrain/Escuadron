package com.libre.escuadroncliente.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import com.google.zxing.WriterException;
import com.libre.escuadroncliente.R;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.libre.escuadroncliente.ui.storage.PreferencesStorage;
import com.libre.escuadroncliente.ui.util.Data;
import com.libre.escuadroncliente.ui.util.Network;
import com.unstoppable.submitbuttonview.SubmitButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;

import static com.libre.escuadroncliente.ui.util.Constants.JSON_FILE;
import static com.libre.escuadroncliente.ui.util.Constants.JSON_FILE_CONFIG;
import static com.libre.escuadroncliente.ui.util.Constants.URL_REMOTE;
import static com.libre.escuadroncliente.ui.util.Data.saveJSONFile;

/**
 * Created by hugo on 26/05/18.
 */

public class LoginActivity  extends Activity implements  View.OnClickListener  {
    private static final String TAG = "EmailPasswordActivity";
    private EditText edtMail, edtPassword;

    private SubmitButton btnEntrar;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private Context context;
    private PreferencesStorage prefs;
    private Dialog dialogError;
    private  SubmitButton dialogButton;
    private boolean loginSuccess;
    private int netStatus;
    private   TextView messageError;
    private String userGuid;
    private FirebaseStorage storage;
    final long ONE_MEGABYTE = 1024 * 1024;
    private int smallerDimension;
    private PrettyDialog prettyDialog=null;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        context=this;
        prefs=new PreferencesStorage(this);
        edtMail = findViewById(R.id.edtCorreo);
        edtPassword = findViewById(R.id.edtTelefono);
        btnEntrar= findViewById(R.id.btnSiguienteLogin);
        btnEntrar.setOnClickListener(this);
        btnEntrar.setOnResultEndListener(finishListener);
        dialogError = new Dialog(context);
        messageError = dialogError .findViewById(R.id.txtMensaje);
        netStatus= Network.getConnectivityStatus(context);
        mAuth = FirebaseAuth.getInstance();
        storage=FirebaseStorage.getInstance();
        WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        int width = point.x;
        int height = point.y;
        smallerDimension = width < height ? width : height;
        smallerDimension = smallerDimension * 3 / 4;
        prettyDialog= new PrettyDialog(context);
        prettyDialog.setIcon(
                R.drawable.pdlg_icon_info,     // icon resource
                R.color.pdlg_color_red,      // icon tint
                new PrettyDialogCallback() {   // icon OnClick listener
                    @Override
                    public void onClick() {
                        // Do what you gotta do
                    }
                })
                .addButton(
                        "OK",					// button text
                        R.color.pdlg_color_white,		// button text color
                        R.color.pdlg_color_green,		// button background color
                        new PrettyDialogCallback() {		// button OnClick listener
                            @Override
                            public void onClick() {

                                btnEntrar.reset();
                                prettyDialog.dismiss();
                            }
                        }
                );
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSiguienteLogin:

                if (netStatus != 0) {
                    if(validateForm()) {

                        try {
                            String email = edtMail.getText().toString();
                            String password = edtPassword.getText().toString();

                            mAuth.signInWithEmailAndPassword(email, password)
                                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {
                                                // Sign in success, update UI with the signed-in user's information
                                                FirebaseUser user = mAuth.getCurrentUser();
                                                userGuid=task.getResult().getUser().getUid();
                                                userGuid=user.getUid();
                                                StorageReference fileRef = storage.getReferenceFromUrl(URL_REMOTE).child(JSON_FILE);
                                                if (fileRef != null) {
                                                    fileRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                                        @Override
                                                        public void onSuccess(byte[] bytes) {


                                                            saveJSONFile(bytes,"db");
                                                            downloadConfig();
                                                            btnEntrar.doResult(true);
                                                            loginSuccess=true;


                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception exception) {
                                                            btnEntrar.doResult(false);
                                                            loginSuccess=false;
                                                        }
                                                    });
                                                }else{

                                                    btnEntrar.doResult(false);
                                                    String messg=task.getException().getMessage();
                                                    showError(messg);

                                                }
                                            } else {

                                                btnEntrar.doResult(false);
                                                loginSuccess=false;
                                                String messg=task.getException().getMessage();
                                                showError(messg);
                                            }

                                            // ...
                                        }
                                    });
                        } catch (Exception ex) {
                            Log.e(TAG, ex.getMessage());
                        }

                    }
                }else
                {
                    messageError.setText("Verifica tu conexion");
                    dialogError.show();
                    btnEntrar.doResult(false);
                    loginSuccess=false;

                }

        }
    }
    private boolean validateForm() {
        if (TextUtils.isEmpty(edtMail.getText().toString())) {
            edtMail.requestFocus();
            btnEntrar.doResult(false);
            loginSuccess=false;
            setErrorMessage("Correo no puede ir Vacio");
            return false;
        } else if (TextUtils.isEmpty(edtPassword.getText().toString())) {
            edtPassword.requestFocus();
            btnEntrar.doResult(false);
            loginSuccess=false;
            setErrorMessage("Contraseña no puede ir Vacio");
            return false;
        }  else {

            return true;
        }
    }
    public void setErrorMessage(String message){
        messageError.setText(message);
        dialogError.show();
    }
    public void generateCode(){
        Bitmap bitmap ;

        QRGEncoder qrgEncoder = new QRGEncoder(userGuid, null, QRGContents.Type.TEXT, smallerDimension);
        try {

            bitmap = qrgEncoder.encodeAsBitmap();
            Data.saveImage(bitmap);

        } catch (WriterException e) {
            Log.v(TAG, e.toString());
        }


    }
    SubmitButton.OnResultEndListener finishListener=new SubmitButton.OnResultEndListener() {
        @Override
        public void onResultEnd() {
            if(loginSuccess) {
                generateCode();
                prefs.saveData("REGISTER_USER_KEY", userGuid);
                Intent registerIntent = new Intent(LoginActivity.this, MarketActivity.class);
                LoginActivity.this.startActivity(registerIntent);
                LoginActivity.this.finish();
            } else {
                btnEntrar.doResult(false);
                loginSuccess=false;
            }



        }
    };

    public void showError(String message){
        prettyDialog.setTitle("Ocurrio un error")
                .setMessage(message)

                .show();

    }
    public void downloadConfig(){


        StorageReference fileRef = storage.getReferenceFromUrl(URL_REMOTE).child(JSON_FILE_CONFIG);
        if (fileRef != null) {

            fileRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    try{

                        saveJSONFile(bytes, "config");
                        JSONObject dataObject = Data.loadJSONFileObjet("configuracion", "config");
                        JSONArray items = dataObject.getJSONArray("items");
                        JSONObject jsonObject = items.getJSONObject(0);
                        boolean status=jsonObject.getBoolean("activo");
                        boolean delivery=jsonObject.getBoolean("delivery");
                        JSONArray pay = jsonObject.getJSONArray("pay");
                        List<String> account=new ArrayList<>();
                        for (int a=0;a<=pay.length()-1;a++) {
                            JSONObject jsonObjectAccount = pay.getJSONObject(a);
                            account.add(jsonObjectAccount.get("banco")+","+jsonObjectAccount.get("tarjeta"));
                        }

                        prefs.saveData("REGISTER_USER_ACTIVE", ""+status);
                        prefs.saveData("DELIVER_ACTIVE", ""+delivery);
                        prefs.saveData("PAY_ACCOUNT_ONE", account.get(0));
                        prefs.saveData("PAY_ACCOUNT_TWO", account.get(1));
                        prefs.saveData("PAY_ACCOUNT_THREE", account.get(2));
                        prefs.saveData("PAY_ACCOUNT_FOUR", account.get(3));

                    }catch (JSONException ex){
                        ex.getStackTrace();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {

                }
            });
        }

    }
}
