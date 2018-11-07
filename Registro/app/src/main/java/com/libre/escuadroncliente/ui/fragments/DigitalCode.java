package com.libre.escuadroncliente.ui.fragments;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.libre.escuadroncliente.R;
import com.libre.escuadroncliente.ui.RegisterActivity;
import com.libre.escuadroncliente.ui.MarketActivity;
import com.unstoppable.submitbuttonview.SubmitButton;

import java.io.File;

public class DigitalCode extends Fragment implements  View.OnClickListener  {
    private View view;
    private Context context;
    private  SubmitButton btnTermina;
    private ImageView  imgCode;
    private Bitmap code;
    private long startMillis,count;
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
        imgCode.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int eventaction = event.getAction();
                if (eventaction == MotionEvent.ACTION_DOWN) {

                    //get system current milliseconds
                    long time= System.currentTimeMillis();


                    //if it is the first time, or if it has been more than 3 seconds since the first tap ( so it is like a new try), we reset everything
                    if (startMillis==0 || (time-startMillis> 3000) ) {
                        startMillis=time;
                        count=1;
                    }
                    //it is not the first, and it has been  less than 3 seconds since the first
                    else{ //  time-startMillis< 3000
                        count++;
                    }

                    if (count==5) {
                        ((MarketActivity) context).update();
                    }
                    return true;
                }
                return false;
            }
        });
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
