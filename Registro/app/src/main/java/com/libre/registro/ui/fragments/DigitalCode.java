package com.libre.registro.ui.fragments;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.libre.registro.R;
import com.libre.registro.ui.MainActivity;
import com.libre.registro.ui.MarketActivity;
import com.unstoppable.submitbuttonview.SubmitButton;

import java.io.File;

public class DigitalCode extends Fragment implements  View.OnClickListener  {
    private View view;
    private Context context;
    private  SubmitButton btnTermina;
    private ImageView  imgCode;
    private Bitmap code;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        super.onSaveInstanceState(savedInstance);
        setRetainInstance(true);
        context=getActivity();
        this.view = inflater.inflate(R.layout.digital_code,container,false);
        btnTermina=this.view.findViewById(R.id.btnTerminar);
        imgCode=this.view.findViewById(R.id.imgCode);

        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/Escuadron/");
        File imgFile = new File (myDir, "MyCode.jpg");
        if(imgFile.exists()){
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            imgCode.setImageBitmap(myBitmap);

        };
        btnTermina.setOnClickListener(this);

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

            ((MarketActivity) context).closeCodeFragment();

        }
    };
}
