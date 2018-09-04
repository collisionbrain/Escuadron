package com.libre.registro.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.google.zxing.WriterException;
import com.libre.registro.R;
import com.libre.registro.ui.adapters.PageAdapter;
import com.libre.registro.ui.fragments.DigitalCodeRegister;
import com.libre.registro.ui.pojos.Member;
import com.libre.registro.ui.storage.PreferencesStorage;
import com.libre.registro.ui.util.Data;
import com.libre.registro.ui.util.NonSwipeableViewPager;
import com.unstoppable.submitbuttonview.SubmitButton;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

import static android.content.ContentValues.TAG;

public class MainActivity extends FragmentActivity {

    private NonSwipeableViewPager vwPaginas;
    private PageAdapter adPaginador;
    private Context context;
    private TextView txtTitulo;
    public Resources resources;
    public Dialog dialogError,dialogPrivacy;
    public   TextView messageError;
    private FirebaseStorage storage;
    final long ONE_MEGABYTE = 1024 * 1024;
    public Member newMember;
    private int smallerDimension;
    private String userGuid;
    private FirebaseAuth mAuth;
    private PreferencesStorage preferencesStorage;
    private final FirebaseDatabase database = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_activity);
        context = this;
        resources=getResources();
        preferencesStorage=new PreferencesStorage(context);
        vwPaginas=findViewById(R.id.id_viewpager);
        txtTitulo=findViewById(R.id.txtTitulo);
        adPaginador=new PageAdapter(getApplicationContext(),getSupportFragmentManager());
        vwPaginas.setAdapter(adPaginador);
        vwPaginas.setCurrentItem(0);
        dialogError = new Dialog(context);
        dialogPrivacy= new Dialog(context);
        dialogPrivacy.setContentView(R.layout.dialog_privacy);
        dialogError.setContentView(R.layout.dialog_error);

        messageError=dialogError .findViewById(R.id.txtMensaje);
        newMember=new Member();
        WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        int width = point.x;
        int height = point.y;
        smallerDimension = width < height ? width : height;
        smallerDimension = smallerDimension * 3 / 4;
        mAuth = FirebaseAuth.getInstance();
        vwPaginas.setOnPageChangeListener(new ViewPager.OnPageChangeListener(){

            @Override
            public void onPageScrollStateChanged(int position) {}
            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {}
            @Override
            public void onPageSelected(int position) {
                // TODO Auto-generated method stub
                switch(position){
                    case 0:
                        txtTitulo.setText(resources.getString(R.string.stPersonal));
                        break;
                    case 1:
                        txtTitulo.setText(resources.getString(R.string.stBiomedica));
                        break;
                    case 2:
                        txtTitulo.setText(resources.getString(R.string.stFirma));

                        break;
                    case 3:
                        txtTitulo.setText(resources.getString(R.string.strPadecimiento));
                        break;

                    case 4:
                        txtTitulo.setText(resources.getString(R.string.strCode));
                        break;
                }
            }

        });
    }
    public void onActivityResult(int requestCode, int resultCode,   Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
         }

    @Override
    public void onBackPressed(){

        int current=vwPaginas.getCurrentItem();
        if(current>0) {
            vwPaginas.setCurrentItem(current - 1);
        }else{
            super.onBackPressed();
        }
    }
    public void paginaSiguiente(int paginaSiguiente){
        vwPaginas.setCurrentItem(paginaSiguiente, true);

    }
    @Override
    public void onResume() {
        super.onResume();

    }

    public void generateCode(){
        Bitmap bitmap ;
        QRGEncoder qrgEncoder = new QRGEncoder(userGuid, null, QRGContents.Type.TEXT, smallerDimension);
        try {

             bitmap = qrgEncoder.encodeAsBitmap();
             Data.saveImage(bitmap);
             DigitalCodeRegister activeFragment =(DigitalCodeRegister) adPaginador.getItemCode();
             activeFragment.setCode(bitmap);

        } catch (WriterException e) {
            Log.v(TAG, e.toString());
        }


    }
    public void launchMarket(){
        Intent intent = new Intent(this, MarketActivity.class);
        startActivityForResult(intent, 100);
        finish();

    }
    public void saveDataUser(String userGuid){
        DatabaseReference ref = database.getReference("registro");
        DatabaseReference usersRef = ref.child("clientes");
        usersRef.child(userGuid).setValue(newMember,new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError != null) {
                    System.out.println("Data could not be saved " + databaseError.getMessage());
                    preferencesStorage.saveDataObjet("OBJET_TO_REGITER",newMember);
                } else {
                    System.out.println("Data saved successfully.");
                    generateCode();
                }
            }
        });
    }

    public void registerUser(){
        String user=newMember.mail;
        String pass= newMember.phone;
        mAuth.createUserWithEmailAndPassword(user,pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            userGuid=task.getResult().getUser().getUid();
                            preferencesStorage.saveData("REGISTER_USER_KEY",userGuid);
                            saveDataUser(userGuid);

                        } else {
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthWeakPasswordException e) {
                                showError("Tu Contraseña debil");

                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                showError("Existe un error con el correo electronico");

                            } catch (FirebaseAuthUserCollisionException e) {
                                showError("Correo electronico ya fue registrado");
                            } catch (Exception e) {
                                Log.e(TAG, e.getMessage());
                            }
                        }
                    }
                });
    }

    public void showError(String message){
        ViewDialog alert = new ViewDialog();
        alert.showDialog(MainActivity.this, message);

    }
}
