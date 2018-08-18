package com.libre.registro.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.libre.registro.R;
import com.libre.registro.ui.MainActivity;
import com.libre.registro.ui.adapters.SignaturePanelAdapter;
import com.unstoppable.submitbuttonview.SubmitButton;

public class SignatureRegister extends Fragment {
    private View view;
    private Context context;
    private TextView txtFirma;
    private SubmitButton btnSiguienteSignature;
    private SignaturePanelAdapter firmaPanel;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        super.onSaveInstanceState(savedInstance);
        setRetainInstance(true);
        context=getActivity();
        this.view = inflater.inflate(R.layout.signature_fragment, container, false);
        txtFirma= (TextView)this.view.findViewById(R.id.txtFirma);
        btnSiguienteSignature=(SubmitButton)this.view.findViewById(R.id.btnSiguienteSignature);
        btnSiguienteSignature.setOnClickListener(siguienteListenerSignature);
        RelativeLayout rlPanel = (RelativeLayout)this.view.findViewById(R.id.rlPanel);
        firmaPanel = new SignaturePanelAdapter(context,txtFirma);
        rlPanel.addView(firmaPanel);
        return  this.view;
    }

    View.OnClickListener siguienteListenerSignature=new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            ((MainActivity) context).paginaSiguiente(4);
        }

    };
}
