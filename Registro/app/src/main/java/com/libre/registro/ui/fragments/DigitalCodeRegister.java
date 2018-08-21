package com.libre.registro.ui.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.WriterException;
import com.libre.registro.R;
import com.libre.registro.ui.MainActivity;
import com.libre.registro.ui.pojos.Member;
import com.unstoppable.submitbuttonview.SubmitButton;

import java.util.HashMap;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

import static android.R.attr.bitmap;
import static android.content.ContentValues.TAG;

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
        code=((MainActivity) context).generateCode();
        btnTermina.setOnClickListener(this);

        return  this.view;
    }
    @Override
    public void onResume(){
        super.onResume();
        imgCode.setImageBitmap(code);

    }


    @Override
    public void onClick(View v) {
        btnTermina.setOnResultEndListener(finishListenerSignature);
        btnTermina.doResult(true);
    }
    SubmitButton.OnResultEndListener finishListenerSignature=new SubmitButton.OnResultEndListener() {
        @Override
        public void onResultEnd() {
            ((MainActivity) context).saveToServer();
            btnTermina.setVisibility(View.GONE);
        }
    };
}
