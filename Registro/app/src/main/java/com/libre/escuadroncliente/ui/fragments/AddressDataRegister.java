package com.libre.escuadroncliente.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.libre.escuadroncliente.R;
import com.libre.escuadroncliente.ui.RegisterActivity;
import com.libre.escuadroncliente.ui.pojos.Address;
import com.unstoppable.submitbuttonview.SubmitButton;

/**
 * Created by hugo on 17/08/18.
 */

public class AddressDataRegister extends Fragment implements  View.OnClickListener {
    private View view;
    private Context context;
    private SubmitButton btnSiguienteAddress;
    private boolean registerAddressSuccess;
    private EditText edtCalle,edtNumExt,edtNumInt,edtCol,edtMun,edtEst,edtCp;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        super.onSaveInstanceState(savedInstance);
        setRetainInstance(true);
        context = getContext();
        this.view = inflater.inflate(R.layout.address_fragment, container, false);
        btnSiguienteAddress =this.view.findViewById(R.id.btnSiguienteAddress);
        edtCalle=this.view.findViewById(R.id.edtCalle);
        edtNumExt=this.view.findViewById(R.id.edtNumExt);
        edtNumInt=this.view.findViewById(R.id.edtNumInt);
        edtCp=this.view.findViewById(R.id.edtCpostal);
        edtCol=this.view.findViewById(R.id.edtCol);
        edtMun=this.view.findViewById(R.id.edtMun);
        edtEst=this.view.findViewById(R.id.edtEst);
        btnSiguienteAddress.setOnClickListener(this);
        btnSiguienteAddress.setOnResultEndListener(finishListenerAddress);

        return this.view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnSiguienteAddress:
                if(validateForm()) {

                    Address address=new Address();
                    address.street=edtCalle.getText().toString();
                    address.numExt=edtNumExt.getText().toString();
                    address.numInt=edtNumInt.getText().toString();
                    address.postal=edtCp.getText().toString();
                    address.col=edtCol.getText().toString();
                    address.mun=edtMun.getText().toString();
                    address.est=edtEst.getText().toString();
                    ((RegisterActivity) context).newMember.address=address;

                    btnSiguienteAddress.doResult(true);


                }
                break;
        }


    }
    private boolean validateForm() {
        if (TextUtils.isEmpty(edtCalle.getText().toString())) {
            edtCalle.requestFocus();
            btnSiguienteAddress.reset();
            registerAddressSuccess=false;
            setErrorMessageForm("Ingresa el nombre de tu Calle");
            return false;
        } else if (TextUtils.isEmpty(edtNumExt.getText().toString())) {
            edtNumExt.requestFocus();
            btnSiguienteAddress.reset();
            registerAddressSuccess=false;
            setErrorMessageForm("Ingresa un Numero");
            return false;
        } else if (TextUtils.isEmpty(edtCol.getText().toString())) {
            edtCol .requestFocus();
            btnSiguienteAddress.reset();
            registerAddressSuccess=false;
            setErrorMessageForm("Ingresa tu Colonia");
            return false;
        }  else if (TextUtils.isEmpty(edtCp.getText().toString())) {
            edtCp .requestFocus();
            btnSiguienteAddress.reset();
            registerAddressSuccess=false;
            setErrorMessageForm("Ingresa tu Codigo Postal");
            return false;
        }  else if (TextUtils.isEmpty(edtMun.getText().toString())) {
            edtMun .requestFocus();
            btnSiguienteAddress.reset();
            registerAddressSuccess=false;
            setErrorMessageForm("Ingresa  Alcaldia  ó Municipio");
            return false;
        }  else if (TextUtils.isEmpty(edtEst.getText().toString())) {
            edtEst .requestFocus();
            btnSiguienteAddress.reset();
            registerAddressSuccess=false;
            setErrorMessageForm("Ingresa  Estado");
            return false;
        } else {
            //save to object
            registerAddressSuccess=true;
            return true;
        }
    }
    public void setErrorMessageForm(String message){
        ((RegisterActivity) context).showError(message);
    }
    SubmitButton.OnResultEndListener finishListenerAddress = new SubmitButton.OnResultEndListener() {
        @Override
        public void onResultEnd() {
            ((RegisterActivity) context).paginaSiguiente(2);
            btnSiguienteAddress.reset();
        }
    };


}