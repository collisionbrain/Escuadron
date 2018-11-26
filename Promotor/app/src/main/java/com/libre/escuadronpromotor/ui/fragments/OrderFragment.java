package com.libre.escuadronpromotor.ui.fragments;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.libre.escuadronpromotor.R;
import com.libre.escuadronpromotor.ui.ListUsersActivity;
import com.libre.escuadronpromotor.ui.adapters.SignaturePanelAdapter;
import com.libre.escuadronpromotor.ui.pojos.CartOrder;
import com.libre.escuadronpromotor.ui.pojos.Delivery;
import com.libre.escuadronpromotor.ui.pojos.Member;
import com.libre.escuadronpromotor.ui.pojos.Order;
import com.libre.escuadronpromotor.ui.pojos.Product;
import com.libre.escuadronpromotor.ui.storage.db.DBHelper;
import com.libre.escuadronpromotor.ui.util.Data;
import com.unstoppable.submitbuttonview.SubmitButton;

import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;

import com.elyeproj.loaderviewlibrary.LoaderTextView;

import java.util.List;

public class OrderFragment extends Activity implements  View.OnClickListener  {
    private View view;
    private Context context,activity;
    private TextView txtMember,txtFecha,txtEstatus,txtDetalle,txtProductos;
    private SubmitButton btnConfirmOrder;
    private SignaturePanelAdapter firmaPanel;
    private  Order order_local;
    private  Member member;
    private  String client_key;
    private  DatabaseReference reference;
    private PrettyDialog prettyDialog;
    private Delivery delivery;
    private DBHelper db;
    private int WAIT_DURATION = 5000;
    private DummyWait dummyWait;
    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=this;
        activity=getParent();
        db=new DBHelper(context);
        client_key = getIntent().getStringExtra("id");
        setContentView(R.layout.order_fragment);
        btnConfirmOrder=findViewById(R.id.btnFinalizar);
        btnConfirmOrder.setOnClickListener(this);
        btnConfirmOrder.setOnResultEndListener(finishOrder);
        txtMember=findViewById(R.id.txtMember);

        reference = database.getReference("registro");
        prettyDialog= new PrettyDialog(context)
                .setTitle("Entrega Completada")
                .setMessage("Entrega Completada").setIcon(
                R.drawable.pdlg_icon_info,     // icon resource
                R.color.pdlg_color_green,      // icon tint
                null)
                .addButton(
                        "OK",     // button text
                        R.color.pdlg_color_white,  // button text color
                        R.color.pdlg_color_green,  // button background color
                        new PrettyDialogCallback() {  // button OnClick listener
                            @Override
                            public void onClick() {
                                // Do what you gotta do
                            }
                        }
                ) ;

      /*  DatabaseReference reference = database.getReference("registro");
        Query query = reference.child("clientes").child(client_key);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    member = dataSnapshot.getValue(Member.class);
                    txtMember.setText(member.name);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                databaseError.getMessage().toString();
            }
        });*/

        loadData();

    }


    @Override
    public void onClick(View v) {
        btnConfirmOrder.doResult(true);
    }

    SubmitButton.OnResultEndListener finishOrder=new SubmitButton.OnResultEndListener() {
        @Override
        public void onResultEnd() {
            order_local=new Order();
            order_local.pay=true;
            reference.child("pedidos").child(client_key).setValue(order_local,new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    if (databaseError != null) {
                        System.out.println("Data could not be saved " + databaseError.getMessage());
                        prettyDialog.show();

                    } else {

                        ((ListUsersActivity)activity).onBackPressed();

                    }
                }
            });


        }
    };

    private void loadData() {
        if (dummyWait != null) {
            dummyWait.cancel(true);
        }
        dummyWait = new DummyWait();
        dummyWait.execute();
    }

    private void postLoadData(Delivery delivery) {
        txtMember.setText(delivery.user_name);
        ((TextView)findViewById(R.id.txt_date)).setText(delivery.delivery_date);
        ((TextView)findViewById(R.id.txt_title)).setText(delivery.total);
        if(delivery.products!=null){
            String formated=delivery.products.replace("|","\n");
            ((TextView)findViewById(R.id.txt_phone)).setText(formated);
        }

        if(delivery.user_idb64!=null){
            Bitmap imgbitmap=Data.base64ToBitmap(delivery.user_idb64);

            ((ImageView)findViewById(R.id.image_icon)).setImageBitmap(imgbitmap);
        }

        if(delivery.total!=null || delivery.total!=""){
            ((TextView)findViewById(R.id.txt_email)).setText(delivery.total);

        }else{
            ((TextView)findViewById(R.id.txt_email)).setText("Sin pedidos pendientes");
        }


    }

    public void resetLoader(View view) {
        ((LoaderTextView)findViewById(R.id.txt_date)).resetLoader();
        ((LoaderTextView)findViewById(R.id.txt_title)).resetLoader();
        ((LoaderTextView)findViewById(R.id.txt_phone)).resetLoader();
        ((LoaderTextView)findViewById(R.id.txt_email)).resetLoader();

        loadData();
    }

    class DummyWait extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Thread.sleep(WAIT_DURATION);

                //getDataClient();
                //getDataDelivery();
                  delivery=db.getOrder(client_key);


            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            postLoadData(delivery);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dummyWait != null) {
            dummyWait.cancel(true);
        }
    }
    public void getDataDelivery(){
        Query query = reference.child("pedidos").child(client_key);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    order_local = dataSnapshot.getValue(Order.class);
                    ((TextView)findViewById(R.id.txt_date)).setText(order_local.dateOrder);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                databaseError.getMessage().toString();
            }
        });
    }
    public void getDataClient(){

    }
}
