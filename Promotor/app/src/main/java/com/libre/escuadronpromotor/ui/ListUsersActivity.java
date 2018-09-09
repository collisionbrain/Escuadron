package com.libre.escuadronpromotor.ui;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;

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
import com.libre.escuadronpromotor.R;
import com.libre.escuadronpromotor.ui.adapters.NewClientAdapter;
import com.libre.escuadronpromotor.ui.fragments.DialogUploadFragment;
import com.libre.escuadronpromotor.ui.pojos.Member;
import com.libre.escuadronpromotor.ui.pojos.Product;
import com.libre.escuadronpromotor.ui.storage.PreferencesStorage;
import com.libre.escuadronpromotor.ui.storage.db.DBHelper;
import com.michaldrabik.tapbarmenulib.TapBarMenu;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.content.ContentValues.TAG;

public class ListUsersActivity extends AppCompatActivity {



    private Context context;
    public List<Product> productList;
    private RecyclerView recyclerView;
    private int count = 0;
    private DatabaseReference mDatabase;
    private Calendar calendar ;
    private Date now ;
    private Button floatingActionButton,floatingSyncButton;
    private DBHelper db;
    private List<Member> newMembers=new ArrayList<>();
    private NewClientAdapter newClientAdapter;
    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private String userGuid;
    private FirebaseAuth mAuth;
    public Dialog  dialogUpload,dialogError;
    private FragmentTransaction ft;
    private Fragment prev;
    private DialogFragment dialogFragment = new DialogUploadFragment();
    private TapBarMenu tapBarMenu;
    private ImageView imageViewAdd,imageViewUp,imageViewScan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.list_users);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#fff5f5f5")));
        calendar = Calendar.getInstance();
        context=this;
        db=new DBHelper(context);
        PreferencesStorage prefs=new PreferencesStorage(context);
        userGuid=prefs.loadData("REGISTER_USER_KEY");
        productList=new ArrayList<>();
        recyclerView =findViewById(R.id.recycler_view);
        tapBarMenu=findViewById(R.id.tapBarMenu);
        dialogError = new Dialog(context);
        dialogUpload= new Dialog(context);
        dialogError.setContentView(R.layout.dialog_error);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        ft = getFragmentManager().beginTransaction();
        prev = getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }

        ft.addToBackStack(null);
        tapBarMenu.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                tapBarMenu.toggle();
            }
        });
        imageViewAdd= findViewById(R.id.imgAdd);
        imageViewUp= findViewById(R.id.imgUpload);
        imageViewScan= findViewById(R.id.imgScan);

        imageViewAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ListUsersActivity.this,RegisterActivity.class);
                startActivityForResult(intent,500);
            }
        });
        imageViewUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new RegisterMemberTask().execute();

            }
        });
        imageViewScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               scanCode();

            }
        });
        newMembers= db.getAllMembers();

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        newClientAdapter = new NewClientAdapter(this, newMembers);
        recyclerView.setAdapter(newClientAdapter);
    }


    @Override
    public void onBackPressed(){


        finish();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == 500) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {

                newMembers= db.getAllMembers();
                newClientAdapter = new NewClientAdapter(this, newMembers);
                recyclerView.setAdapter(newClientAdapter);
            }
        }
    }


    public void registerUser(Member newMember){
        String user=newMember.mail;
        String pass= newMember.phone;
        Member member=newMember;
        mAuth.createUserWithEmailAndPassword(user,pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            String guid=task.getResult().getUser().getUid();
                            saveDataUser(guid,member);

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

    public void saveDataUser(String userGuid,Member newMember){
        DatabaseReference ref = database.getReference("registro");
        DatabaseReference usersRef = ref.child("clientes");
        usersRef.child(userGuid).setValue(newMember,new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError != null) {
                    System.out.println("Data could not be saved " + databaseError.getMessage());

                } else {

                }
            }
        });
    }

    private class RegisterMemberTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {

            dialogFragment.show(ft, "dialog");
        }
        @Override
        protected Void doInBackground(Void... params) {
            try {
                Thread.sleep(500);
                newMembers= db.getAllMembers();
                for (Member mem:newMembers) {
                    registerUser(mem);

                }
            }catch (InterruptedException ex){
                ex.getStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void param) {
            dialogFragment.dismiss();
        }
    }

    public void scanCode(){
        Intent intent = new Intent(getApplicationContext(),ScannerActivity.class);
        intent.setAction("com.google.zxing.client.android.SCAN");
        intent.putExtra("SAVE_HISTORY", false);
        startActivityForResult(intent, 0);
    }
}
