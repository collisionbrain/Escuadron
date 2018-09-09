package com.libre.escuadronpromotor.ui;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
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
import com.libre.escuadronpromotor.R;
import com.libre.escuadronpromotor.ui.adapters.PageAdapter;
import com.libre.escuadronpromotor.ui.fragments.CredentialFragment;
import com.libre.escuadronpromotor.ui.pojos.Member;
import com.libre.escuadronpromotor.ui.storage.PreferencesStorage;
import com.libre.escuadronpromotor.ui.storage.db.DBHelper;
import com.libre.escuadronpromotor.ui.util.Data;
import com.libre.escuadronpromotor.ui.util.NonSwipeableViewPager;

import java.io.File;


import static android.content.ContentValues.TAG;

public class RegisterActivity extends FragmentActivity {

    private NonSwipeableViewPager vwPaginas;
    private PageAdapter adPaginador;
    private Context context;
    private TextView txtTitulo;
    public Resources resources;
    public Dialog dialogError,dialogPrivacy;
    public   TextView messageError;
    final long ONE_MEGABYTE = 1024 * 1024;
    private FirebaseStorage storage;
    public Member newMember;
    private int smallerDimension;
    private PreferencesStorage preferencesStorage;
    private Uri imageUri;
    private DBHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_activity);
        context = this;
        db=new DBHelper(context);
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
        storage=FirebaseStorage.getInstance();
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
                        txtTitulo.setText(resources.getString(R.string.stPersonal));
                        break;
                    case 2:
                        txtTitulo.setText(resources.getString(R.string.stBiomedica));

                        break;
                    case 3:
                        txtTitulo.setText(resources.getString(R.string.strPadecimiento));
                        break;

                    case 4:
                        txtTitulo.setText(resources.getString(R.string.strIdentidad));
                        break;
                    case 5:
                        txtTitulo.setText(resources.getString(R.string.strFirma));
                        break;
                }
            }

        });
    }
    public void onActivityResult(int requestCode, int resultCode,   Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        CredentialFragment credentialFragment=(CredentialFragment)adPaginador.getCredentialFragment();

        switch (requestCode) {
            case 200:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = imageUri;
                    getContentResolver().notifyChange(selectedImage, null);
                    ContentResolver cr = getContentResolver();
                    Bitmap bitmap;
                    try {
                        bitmap = android.provider.MediaStore.Images.Media.getBitmap(cr, selectedImage);
                        newMember.b64FrontId= Data.bitmapToBase64(bitmap);
                        credentialFragment.setFrontImage(bitmap);

                    } catch (Exception e) {

                        Log.e("Camera", e.toString());
                    }
                }
                break;
            case 300:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = imageUri;
                    getContentResolver().notifyChange(selectedImage, null);
                    ContentResolver cr = getContentResolver();
                    Bitmap bitmap;
                    try {
                        bitmap = android.provider.MediaStore.Images.Media.getBitmap(cr, selectedImage);
                        newMember.b64BackId= Data.bitmapToBase64(bitmap);
                        credentialFragment.setBackImage(bitmap);

                    } catch (Exception e) {

                        Log.e("Camera", e.toString());
                    }
                }
                break;
        }
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


    public void showError(String message){
        ViewDialog alert = new ViewDialog();
        alert.showDialog(RegisterActivity.this, message);

    }
    public void showPrivacy(){
        ViewDialog alert = new ViewDialog();
        alert.showDialogPrivacy(RegisterActivity.this);

    }

    public void takePicture(int type){
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        File photo;
        switch (type){
            case 0:
                photo = new File(Environment.getExternalStorageDirectory(),  "/Escuadron/CredentialFront.jpg");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photo));
                imageUri = Uri.fromFile(photo);
                this.startActivityForResult(intent, 200);
                break;
            case 1:
                photo = new File(Environment.getExternalStorageDirectory(),  "/Escuadron/CredentialBack.jpg");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photo));
                imageUri = Uri.fromFile(photo);
                this.startActivityForResult(intent, 300);
                break;
        }

    }

    public void saveRegiter(){
        Intent intent = getIntent();

        long id=db.insertMember(newMember);
        intent.putExtra("Member", id);
        setResult(RESULT_OK, intent);
        finish();
    }
}
