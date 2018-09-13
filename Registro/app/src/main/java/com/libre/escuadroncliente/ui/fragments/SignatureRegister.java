package com.libre.escuadroncliente.ui.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.libre.escuadroncliente.R;
import com.libre.escuadroncliente.ui.RegisterActivity;
import com.libre.escuadroncliente.ui.adapters.SignaturePanelAdapter;
import com.libre.escuadroncliente.ui.util.Data;
import com.unstoppable.submitbuttonview.SubmitButton;

public class SignatureRegister extends Fragment implements  View.OnClickListener  {
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
        txtFirma=this.view.findViewById(R.id.txtFirma);
        btnSiguienteSignature=this.view.findViewById(R.id.btnSiguienteSignature);
        btnSiguienteSignature.setOnClickListener(this);
        btnSiguienteSignature.setOnResultEndListener(finishListenerSignature);
        RelativeLayout rlPanel = this.view.findViewById(R.id.rlPanel);
        firmaPanel = new SignaturePanelAdapter(context,txtFirma);
        rlPanel.addView(firmaPanel);
        return  this.view;
    }


    @Override
    public void onClick(View v) {
        btnSiguienteSignature.doResult(true);
    }
    SubmitButton.OnResultEndListener finishListenerSignature=new SubmitButton.OnResultEndListener() {
        @Override
        public void onResultEnd() {
            Bitmap bmpSignature =firmaPanel.getBitmap();

            ((RegisterActivity) context).newMember.signature=Data.bitmapToBase64(bmpSignature);
            ((RegisterActivity) context).paginaSiguiente(6);


        }
    };


}
