package com.libre.escuadronpromotor.ui;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.libre.escuadronpromotor.R;
import com.libre.escuadronpromotor.ui.adapters.NewClientAdapter;
import com.libre.escuadronpromotor.ui.fragments.OrderFragment;
import com.libre.escuadronpromotor.ui.pojos.CartOrder;
import com.libre.escuadronpromotor.ui.pojos.Delivery;
import com.libre.escuadronpromotor.ui.pojos.Member;
import com.libre.escuadronpromotor.ui.pojos.Order;
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
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

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
    private List<Member> newMembers=new ArrayList<>();
    private NewClientAdapter newClientAdapter;
    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private String userGuid;
    private FirebaseAuth mAuth;
    public Dialog  dialogUpload,dialogError;
    private FragmentTransaction ft;
    private Fragment prev;
    private OrderFragment orderFragment = new OrderFragment();
    private TapBarMenu tapBarMenu;
    private ImageView imageViewAdd,imageViewUp,imageViewScan,imageViewDeli;
    private TextView textCounter;
    private int pendings=0;
    private DBHelper db;
    private FragmentManager fragmentManager;
    private DatabaseReference ref;
    private DatabaseReference pedRef ;
    private DatabaseReference cliRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();
        setContentView(R.layout.list_users);

        calendar = Calendar.getInstance();
        context=this;
        db=new DBHelper(context);
        PreferencesStorage prefs=new PreferencesStorage(context);
        userGuid=prefs.loadData("REGISTER_USER_KEY");
        ref = database.getReference("registro");
        pedRef = ref.child("pedidos");
        cliRef = ref.child("clientes");

        productList=new ArrayList<>();
        recyclerView =findViewById(R.id.recycler_view);
        tapBarMenu=findViewById(R.id.tapBarMenu);
        textCounter=findViewById(R.id.txtCountPendings);
        dialogError = new Dialog(context);
        dialogUpload= new Dialog(context);
        dialogError.setContentView(R.layout.dialog_error);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        ft = getFragmentManager().beginTransaction();

        fragmentManager=getFragmentManager();
        prev = getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }

        ft.addToBackStack(null);
        tapBarMenu.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                tapBarMenu.toggle();
                textCounter.setVisibility(View.VISIBLE);
            }
        });
        imageViewAdd= findViewById(R.id.imgAdd);
        imageViewUp= findViewById(R.id.imgUpload);
        imageViewScan= findViewById(R.id.imgScan);
        imageViewDeli= findViewById(R.id.imgDelivery);
        imageViewAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ListUsersActivity.this,RegisterActivity.class);
                startActivityForResult(intent,500);
            }
        });
        imageViewDeli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ListUsersActivity.this,ListDeliveryActivity.class);
                startActivityForResult(intent,400);
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
        counterPendings();
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
        if (requestCode == 400) {
            if (resultCode == RESULT_OK) {
                newMembers= db.getAllMembers();
                newClientAdapter = new NewClientAdapter(this, newMembers);
                recyclerView.setAdapter(newClientAdapter);
            }
        }
        if (requestCode == 500) {
            if (resultCode == RESULT_OK) {
                newMembers= db.getAllMembers();
                newClientAdapter = new NewClientAdapter(this, newMembers);
                recyclerView.setAdapter(newClientAdapter);
            }
        }
        if (requestCode == 600) {
            if (resultCode == RESULT_OK) {
                String id_user= data.getStringExtra("id");
                Intent intent=new Intent(this,OrderFragment.class);
                intent.putExtra("id",id_user);
                startActivity(intent);
            }
        }
    }


    public void registerUser(Member newMember){
        String user=newMember.mail;
        String pass= newMember.phone;
        final Member memberi=newMember;
        mAuth.createUserWithEmailAndPassword(user,pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            String guid=task.getResult().getUser().getUid();
                            saveDataUser(guid,memberi);

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
        final String emailToDelete=newMember.mail;
        usersRef.child(userGuid).setValue(newMember,new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError == null) {

                    db.deleteMemberRegister(emailToDelete);
                    newMembers= db.getAllMembers();
                    newClientAdapter = new NewClientAdapter(context, newMembers);
                    recyclerView.setAdapter(newClientAdapter);
                }
            }
        });
    }

    private class RegisterMemberTask extends AsyncTask<Void, Void, Void> {
        private ProgressDialog dialog;
        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(context, R.style.MyDialogTheme);
            dialog.setMessage("Guardando pedido.");
            dialog.show();
        }
        @Override
        protected Void doInBackground(Void... params) {
            try {
                Thread.sleep(5000);
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

            dialog.dismiss();
        }
    }

    public void scanCode(){

        Intent intent = new Intent(getApplicationContext(),ScannerActivity.class);
        startActivityForResult(intent, 600);
    }

    public void  counterPendings(){
          ref = database.getReference("registro");
          pedRef = ref.child("pedidos");

        Query query = pedRef.orderByChild("pay").equalTo(false);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot pedido: snapshot.getChildren()) {
                    final Order order=pedido.getValue(Order.class);
                    Query query = ref.child("clientes").child(order.userGuid);
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                Member member=dataSnapshot.getValue(Member.class);
                                Log.e("################",""+order.userGuid);
                                Log.e("################",""+member.name);
                                Log.e("################",""+order.dateOrder);
                                Log.e("################",""+order.productList);

                                Delivery delivery=new Delivery();
                                delivery.user_key=order.userGuid;
                                delivery.user_name=member.name;
                                //delivery.user_idb64=member.b64FrontId;
                                List<CartOrder> orderCart= order.productList;
                                String stProduct="";
                                if(orderCart!=null) {
                                    for (CartOrder cart : orderCart) {


                                        stProduct += cart.id + " x " + cart.count + "|";
                                    }
                                }

                                delivery.products=stProduct;
                                delivery.delivery_date=order.dateOrder;
                                delivery.total=order.total+"";
                                db.insertOrder(delivery);

                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            databaseError.getMessage().toString();
                        }
                    });


                    pendings++;
                }
                if(pendings>0) {
                    textCounter.setText("" + pendings);
                }

}

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "getUser:onCancelled", databaseError.toException());
            }
        });
    }
}
