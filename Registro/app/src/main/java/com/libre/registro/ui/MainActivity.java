package com.libre.registro.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Display;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.google.zxing.WriterException;
import com.libre.registro.R;
import com.libre.registro.ui.adapters.PageAdapter;
import com.libre.registro.ui.pojos.Member;
import com.libre.registro.ui.util.Data;
import com.libre.registro.ui.util.NonSwipeableViewPager;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_activity);
        context = this;
        resources=getResources();
        vwPaginas=(NonSwipeableViewPager) findViewById(R.id.id_viewpager);
        txtTitulo=(TextView) findViewById(R.id.txtTitulo);
        adPaginador=new PageAdapter(getApplicationContext(),getSupportFragmentManager());
        vwPaginas.setAdapter(adPaginador);
        vwPaginas.setCurrentItem(0);
        dialogError = new Dialog(context);
        dialogPrivacy= new Dialog(context);
        dialogPrivacy.setContentView(R.layout.dialog_privacy);
        dialogError.setContentView(R.layout.dialog_error);
        messageError=(TextView)dialogError .findViewById(R.id.txtMensaje);
        newMember=new Member();
        WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        int width = point.x;
        int height = point.y;
        smallerDimension = width < height ? width : height;
        smallerDimension = smallerDimension * 3 / 4;

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

    public Bitmap generateCode(){

        Gson gson = new Gson();
        String inputValue = gson.toJson(userGuid);

        Bitmap bitmap ;

        QRGEncoder qrgEncoder = new QRGEncoder(inputValue, null, QRGContents.Type.TEXT, smallerDimension);
        try {
             bitmap = qrgEncoder.encodeAsBitmap();
            Data.saveImage(bitmap);
            return bitmap;
        } catch (WriterException e) {
            Log.v(TAG, e.toString());
        }
       return null;
    }

    public void saveToServer(){
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("server/register-data/escuadron");
        DatabaseReference usersRef = ref.child("clientes");
        String userID= UUID.randomUUID().toString();
        Map<String, Member> member = new HashMap<>();
        member.put(userGuid,newMember);
        usersRef.setValue(member);

    }

    public void registerUser(){
        mAuth.createUserWithEmailAndPassword(newMember.mail, newMember.phone)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            userGuid=task.getResult().getUser().getUid();


                        } else {
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthWeakPasswordException e) {


                            } catch (FirebaseAuthInvalidCredentialsException e) {


                            } catch (FirebaseAuthUserCollisionException e) {

                            } catch (Exception e) {
                                Log.e(TAG, e.getMessage());
                            }
                        }
                    }
                });
    }
}
