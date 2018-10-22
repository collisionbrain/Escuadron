package com.libre.escuadronpromotor.ui.fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.libre.escuadronpromotor.ui.animations.DotProgressBar;
import com.libre.escuadronpromotor.ui.pojos.Order;
import com.unstoppable.submitbuttonview.SubmitButton;

import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;

import com.elyeproj.loaderviewlibrary.LoaderTextView;
public class OrderFragment extends Fragment implements  View.OnClickListener  {
    private View view;
    private Context context;
    private TextView txtFecha,txtEstatus,txtDetalle,txtProductos;
    private SubmitButton btnConfirmOrder;
    private SignaturePanelAdapter firmaPanel;
    private  Order order_local;
    private  String client_key;
    private  DatabaseReference reference;
    private PrettyDialog prettyDialog;

    private int WAIT_DURATION = 5000;
    private DummyWait dummyWait;
    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        super.onSaveInstanceState(savedInstance);
        setRetainInstance(true);
        context=getActivity();
        client_key = getArguments().getString("id");
        this.view = inflater.inflate(R.layout.order_fragment, container, false);

        txtFecha=this.view.findViewById(R.id.txtFecha);
        txtEstatus=this.view.findViewById(R.id.txtEstatus);
        txtDetalle=this.view.findViewById(R.id.txtDetalle);
        txtProductos=this.view.findViewById(R.id.txtProductos);
        btnConfirmOrder=this.view.findViewById(R.id.btnFinalizar);
        btnConfirmOrder.setOnClickListener(this);
        btnConfirmOrder.setOnResultEndListener(finishOrder);

         reference = database.getReference("registro");
        Query query = reference.child("pedidos").child(client_key);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                     order_local = dataSnapshot.getValue(Order.class);
                     //start loader
                    //                    viewLoader.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                databaseError.getMessage().toString();
            }
        });
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
        return  this.view;
    }


    @Override
    public void onClick(View v) {
        btnConfirmOrder.doResult(true);
    }
    SubmitButton.OnResultEndListener finishOrder=new SubmitButton.OnResultEndListener() {
        @Override
        public void onResultEnd() {

            order_local.pay=true;
            reference.child("pedidos").child(client_key).setValue(order_local,new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    if (databaseError != null) {
                        System.out.println("Data could not be saved " + databaseError.getMessage());
                        prettyDialog.show();

                    } else {

                        ((ListUsersActivity)context).onBackPressed();

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

    private void postLoadData() {
        txtFecha.setText("Mr. Donald Trump");
        txtEstatus.setText("President of United State (2017 - now)");
        txtDetalle.setText("+001 2345 6789");
        txtProductos.setText("donald.trump@donaldtrump.com");

    }

    public void resetLoader(View view) {

        txtFecha.resetLoader();
        txtEstatus.resetLoader();
        txtDetalle.resetLoader();
        txtProductos.resetLoader();

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
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            postLoadData();
        }
    }




}
