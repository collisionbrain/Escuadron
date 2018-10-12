package com.libre.escuadroncliente.ui;

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
import android.telephony.TelephonyManager;
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
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.zxing.WriterException;
import com.libre.escuadroncliente.R;
import com.libre.escuadroncliente.ui.adapters.PageAdapter;
import com.libre.escuadroncliente.ui.fragments.CredentialFragment;
import com.libre.escuadroncliente.ui.fragments.CronicSuffering;
import com.libre.escuadroncliente.ui.fragments.DigitalCodeRegister;
import com.libre.escuadroncliente.ui.pojos.Member;
import com.libre.escuadroncliente.ui.storage.PreferencesStorage;
import com.libre.escuadroncliente.ui.util.Data;
import com.libre.escuadroncliente.ui.util.NonSwipeableViewPager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

import static android.content.ContentValues.TAG;
import static com.libre.escuadroncliente.ui.util.Constants.JSON_FILE;
import static com.libre.escuadroncliente.ui.util.Constants.JSON_FILE_CONFIG;
import static com.libre.escuadroncliente.ui.util.Constants.URL_REMOTE;
import static com.libre.escuadroncliente.ui.util.Data.saveJSONFile;

public class RegisterActivity extends FragmentActivity {

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
    private CredentialFragment credentialFragment;
    private DigitalCodeRegister digitalCodeRegister;
    private CronicSuffering cronicSuffering;


    private Uri imageUri;


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
                        txtTitulo.setText(resources.getString(R.string.strPadecimiento));

                        break;

                    case 3:
                        txtTitulo.setText(resources.getString(R.string.strIdentidad));
                        break;
                    case 4:
                        txtTitulo.setText(resources.getString(R.string.strFirma));
                        break;
                    case 5:
                        txtTitulo.setText(resources.getString(R.string.stTerminar));
                        break;
                }
            }

        });
    }
    public void onActivityResult(int requestCode, int resultCode,   Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bitmap;
        Uri selectedImage = imageUri;
        getContentResolver().notifyChange(selectedImage, null);
        ContentResolver cr = getContentResolver();
        credentialFragment=(CredentialFragment)adPaginador.getCredentialFragment();
        switch (requestCode) {
            case 200:
                if (resultCode == RESULT_OK) {


                    try {
                        bitmap = android.provider.MediaStore.Images.Media.getBitmap(cr, selectedImage);
                        newMember.b64FrontId=Data.bitmapToBase64(bitmap);
                       credentialFragment.setFrontImage();

                    } catch (Exception e) {

                        Log.e("Camera", e.toString());
                    }
                }
                break;
            case 300:
                if (resultCode == RESULT_OK) {
                    try {
                        bitmap = android.provider.MediaStore.Images.Media.getBitmap(cr, selectedImage);
                        newMember.b64BackId=Data.bitmapToBase64(bitmap);
                        credentialFragment.setBackImage();

                    } catch (Exception e) {

                        Log.e("Camera", e.toString());
                    }
                }
                break;
            case 400:
                if (resultCode == RESULT_OK) {
                    try {
                        bitmap = android.provider.MediaStore.Images.Media.getBitmap(cr, selectedImage);
                        newMember.b64Recipe=Data.bitmapToBase64(bitmap);
                        credentialFragment.setRecipemage();

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

         if(paginaSiguiente==2) {
             cronicSuffering = (CronicSuffering) adPaginador.getCronicFragment();
             if (newMember.ludic) {


                 cronicSuffering.setHomeopaticView();
             }else if(newMember.ludic && newMember.suffering){
                 cronicSuffering.setAllView();
             }else{
                 cronicSuffering.setCronicView();
             }
         }
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
    public void downloadDB(){

        StorageReference fileRef = storage.getReferenceFromUrl(URL_REMOTE).child(JSON_FILE);
        if (fileRef != null) {
            fileRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    saveJSONFile(bytes,"db");
                    generateCode();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {

                }
            });
        }
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
                        preferencesStorage.saveData("REGISTER_USER_ACTIVE", ""+jsonObject.getBoolean("activo"));

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
                     downloadDB();
                    downloadConfig();
                }
            }
        });
    }

    public void registerUser(){
        String user=newMember.mail;
        String pass= newMember.phone;
        digitalCodeRegister=(DigitalCodeRegister)adPaginador.getRegisterFragment();
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
                                showError("Tu Contraseña es debil");
                                digitalCodeRegister.setErrorButton();


                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                showError("Existe un error con el correo electronico");
                                digitalCodeRegister.setErrorButton();

                            } catch (FirebaseAuthUserCollisionException e) {
                                showError("Correo electronico ya fue registrado");
                                digitalCodeRegister.setErrorButton();
                            } catch (Exception e) {
                                Log.e(TAG, e.getMessage());
                            }
                        }
                    }
                });
    }

    public void showError(String message){
        ViewDialog alert = new ViewDialog();
        alert.showDialog(RegisterActivity.this, message);

    }
    public void showPrivacy(){
        ViewDialog alert = new ViewDialog();
        newMember.privacy=true;
        alert.showDialogPrivacy(RegisterActivity.this);

    }
    public void startLogin(){
        Intent intent=new Intent(this,LoginActivity.class);
        this.startActivity(intent);
        finish();
    }
    public void takePicture(int type){
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        File photo;
        switch (type){
            case 0:
                photo = new File(Environment.getExternalStorageDirectory(),  "CredentialFront.jpg");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photo));
                imageUri = Uri.fromFile(photo);
                this.startActivityForResult(intent, 200);
                break;
            case 1:
                photo = new File(Environment.getExternalStorageDirectory(),  "CredentialBack.jpg");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photo));
                imageUri = Uri.fromFile(photo);
                this.startActivityForResult(intent, 300);
                break;
            case 2:
                photo = new File(Environment.getExternalStorageDirectory(),  "Receta.jpg");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photo));
                imageUri = Uri.fromFile(photo);
                this.startActivityForResult(intent, 400);
                break;
        }

    }
}
