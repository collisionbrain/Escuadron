package com.libre.escuadronpromotor.ui.fragments;

import android.content.Context;
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
import com.libre.escuadronpromotor.ui.pojos.Member;
import com.libre.escuadronpromotor.ui.pojos.Order;
import com.unstoppable.submitbuttonview.SubmitButton;

public class OrderFragment extends Fragment implements  View.OnClickListener  {
    private View view;
    private Context context;
    private TextView txtFirma;
    private SubmitButton btnConfirmOrder;
    private SignaturePanelAdapter firmaPanel;
    private  Order order_local;
    private  String client_key;
    private  DatabaseReference reference;
    private DotProgressBar progress;

    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        super.onSaveInstanceState(savedInstance);
        setRetainInstance(true);
        context=getActivity();
        client_key = getArguments().getString("id");
        this.view = inflater.inflate(R.layout.order_fragment, container, false);
        txtFirma=this.view.findViewById(R.id.txtTotal);
        progress=this.view.findViewById(R.id.progress);
        btnConfirmOrder=this.view.findViewById(R.id.btnSiguienteSignature);
        btnConfirmOrder.setOnClickListener(this);
        btnConfirmOrder.setOnResultEndListener(finishOrder);

         reference = database.getReference("registro");
        Query query = reference.child("pedidos").child(client_key);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                     order_local = dataSnapshot.getValue(Order.class);
                    txtFirma.setText("$" +order_local.total);
                    progress.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                databaseError.getMessage().toString();
            }
        });
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

                    } else {

                        ((ListUsersActivity)context).onBackPressed();

                    }
                }
            });


        }
    };



}
