package com.libre.escuadroncliente.ui.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.libre.escuadroncliente.R;
import com.libre.escuadroncliente.ui.MarketActivity;
import com.libre.escuadroncliente.ui.util.Data;
import com.unstoppable.submitbuttonview.SubmitButton;

/**
 * Created by hugo on 17/08/18.
 */

public class PayFragment extends Fragment implements  View.OnClickListener {
    private View view;

    private Context context;
    private SubmitButton btnSiguienteTicket;
    private ImageView imgTicket;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        super.onSaveInstanceState(savedInstance);
        setRetainInstance(true);
        context = getActivity();
        this.view = inflater.inflate(R.layout.pay_fragment, container, false);
        btnSiguienteTicket =this.view.findViewById(R.id.btnSiguienteTicket);
        imgTicket=this.view.findViewById(R.id.imgTicket);
        String b64=((MarketActivity)context).order.ticket;
        if(b64!=null   ){


            if(b64.length()>0) {
                this.setFrontImage();
            }
        }
        btnSiguienteTicket.setOnClickListener(this);
        imgTicket.setOnClickListener(this);
        return this.view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.imgTicket:
                ((MarketActivity)context).takePhotoTicket();
                break;
            case R.id.btnSiguienteTicket:
                btnSiguienteTicket.setOnResultEndListener(finishListenerTicket);
                btnSiguienteTicket.doResult(true);
                break;
        }


    } ;
    SubmitButton.OnResultEndListener finishListenerTicket = new SubmitButton.OnResultEndListener() {
        @Override
        public void onResultEnd() {

            ((MarketActivity)context).onBackPressed();
            btnSiguienteTicket.reset();
        }
    };

    public void setFrontImage(){
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.add_photo_green);
        imgTicket.setImageBitmap(bitmap);
    }



}