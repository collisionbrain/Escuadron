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

public class SignatureRegister extends Fragment {
    private View view;
    private Context context;
    private TextView txtFirma;
    private Button btnSiguiente;
    private SignaturePanelAdapter firmaPanel;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        super.onSaveInstanceState(savedInstance);
        setRetainInstance(true);
        context=getActivity();
        this.view = inflater.inflate(R.layout.signature_fragment, container, false);
        txtFirma= (TextView)this.view.findViewById(R.id.txtFirma);
        btnSiguiente=(Button)this.view.findViewById(R.id.btnSiguiente);
        btnSiguiente.setOnClickListener(siguienteListener);
        RelativeLayout rlPanel = (RelativeLayout)this.view.findViewById(R.id.rlPanel);
        firmaPanel = new SignaturePanelAdapter(context,txtFirma);
        rlPanel.addView(firmaPanel);
        return  this.view;
    }

    View.OnClickListener siguienteListener=new View.OnClickListener(){
        @Override
        public void onClick(View v) {

        }

    };
}
