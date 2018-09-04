package com.libre.registro.ui.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import com.libre.registro.R;
import com.libre.registro.ui.MainActivity;
import com.libre.registro.ui.util.Network;
import com.unstoppable.submitbuttonview.SubmitButton;

public class PersonalDataRegister  extends Fragment implements  View.OnClickListener  {
    private View view;
    private SubmitButton btnSiguiente;
    private EditText edtName, edtMail,edtCelphone;
    private Context context;
    private int netStatus;
    private boolean registerPersonalSuccess=true;
    private CheckBox checkWhats;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        super.onSaveInstanceState(savedInstance);
        setRetainInstance(true);
        context=getContext();
        this.view = inflater.inflate(R.layout.personal_data_fragment,container,false);
        btnSiguiente=this.view.findViewById(R.id.btnSiguiente);
        edtName=this.view.findViewById(R.id.edtNombre);
        edtMail=this.view.findViewById(R.id.edtCorreo);
        edtCelphone=this.view.findViewById(R.id.edtCelular);
        checkWhats=this.view.findViewById(R.id.checkWats);


        btnSiguiente.setOnClickListener(this);
        btnSiguiente.setOnResultEndListener(finishListener);
        return  this.view;
    }
    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onClick(View v) {

        netStatus= Network.getConnectivityStatus(context);
        switch (v.getId()) {

            case R.id.btnSiguiente:
                    if(validateForm()) {

                        ((MainActivity) context).newMember.name=edtName.getText().toString();
                        ((MainActivity) context).newMember.mail=edtMail.getText().toString();
                        ((MainActivity) context).newMember.phone=edtCelphone.getText().toString();
                        ((MainActivity) context).newMember.whats=checkWhats.isChecked();

                        btnSiguiente.doResult(true);


                    }


        }
    }
    SubmitButton.OnResultEndListener finishListener=new SubmitButton.OnResultEndListener() {
        @Override
        public void onResultEnd() {

            if(registerPersonalSuccess) {
                ((MainActivity) context).paginaSiguiente(1);
                btnSiguiente.reset();
            }
        }
    };

    private boolean validateForm() {
        if (TextUtils.isEmpty(edtMail.getText().toString())) {
            edtMail.requestFocus();
            btnSiguiente.reset();
            registerPersonalSuccess=false;
            setErrorMessageForm("Ingresa un Correo electronico");
            return false;
        } else if (TextUtils.isEmpty(edtName.getText().toString())) {
            edtName.requestFocus();
            btnSiguiente.reset();
            registerPersonalSuccess=false;
            setErrorMessageForm("Ingresa un Nombre completo");
            return false;
        } else if (TextUtils.isEmpty(edtCelphone.getText().toString())) {
            edtCelphone .requestFocus();
            btnSiguiente.reset();
            registerPersonalSuccess=false;
            setErrorMessageForm("Ingresa un Telefono de contacto");
            return false;
        } else {
            //save to object
            registerPersonalSuccess=true;
            return true;
        }
    }
    public void setErrorMessageForm(String message){
        ((MainActivity) context).showError(message);
    }
}
