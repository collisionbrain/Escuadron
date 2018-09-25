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
import com.google.firebase.database.ValueEventListener;
import com.libre.escuadronpromotor.R;
import com.libre.escuadronpromotor.ui.adapters.SignaturePanelAdapter;
import com.libre.escuadronpromotor.ui.pojos.Order;
import com.unstoppable.submitbuttonview.SubmitButton;

public class OrderFragment extends Fragment implements  View.OnClickListener  {
    private View view;
    private Context context;
    private TextView txtFirma;
    private SubmitButton btnConfirmOrder;
    private SignaturePanelAdapter firmaPanel;
    private  String client_key;
    private DatabaseReference mDatabase;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        super.onSaveInstanceState(savedInstance);
        setRetainInstance(true);
        context=getActivity();
        client_key = getArguments().getString("id");
        this.view = inflater.inflate(R.layout.order_fragment, container, false);
        txtFirma=this.view.findViewById(R.id.txtTotal);
        txtFirma.setText(client_key);
        btnConfirmOrder=this.view.findViewById(R.id.btnSiguienteSignature);
        btnConfirmOrder.setOnClickListener(this);
        btnConfirmOrder.setOnResultEndListener(finishOrder);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("pedidos").child(client_key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Order order = dataSnapshot.getValue(Order.class);

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
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




        }
    };



}
