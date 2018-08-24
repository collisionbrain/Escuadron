package com.libre.registro.ui.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.libre.registro.R;
import com.libre.registro.ui.MainActivity;
import com.unstoppable.submitbuttonview.SubmitButton;

public class DigitalCodeRegister extends Fragment implements  View.OnClickListener  {
    private View view;
    private Context context;
    private  SubmitButton btnTermina;
    private ImageView  imgCode;
    private Bitmap code;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        super.onSaveInstanceState(savedInstance);
        setRetainInstance(true);
        context=getContext();
        this.view = inflater.inflate(R.layout.digital_code_fragment,container,false);
        btnTermina=(SubmitButton)this.view.findViewById(R.id.btnTerminar);
        imgCode=(ImageView) this.view.findViewById(R.id.imgCode);
        btnTermina.setOnClickListener(this);
        ((MainActivity) context).saveToServer();
        return  this.view;
    }
    @Override
    public void onResume(){
        super.onResume();


    }


    @Override
    public void onClick(View v) {
        btnTermina.setOnResultEndListener(finishListenerSignature);
        btnTermina.doResult(true);
    }
    SubmitButton.OnResultEndListener finishListenerSignature=new SubmitButton.OnResultEndListener() {
        @Override
        public void onResultEnd() {
            ((MainActivity) context).registerUser();
            code=((MainActivity) context).generateCode();
            imgCode.setImageBitmap(code);
            btnTermina.setVisibility(View.GONE);
        }
    };
}
