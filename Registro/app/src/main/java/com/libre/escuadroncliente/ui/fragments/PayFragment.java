package com.libre.escuadroncliente.ui.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.libre.escuadroncliente.R;
import com.libre.escuadroncliente.ui.MarketActivity;
import com.unstoppable.submitbuttonview.SubmitButton;

/**
 * Created by hugo on 17/08/18.
 */

public class PayFragment extends Fragment implements  View.OnClickListener {
    private View view;

    private Context context;
    private SubmitButton btnSiguienteTicket;
    private ImageView imageViewFront;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        super.onSaveInstanceState(savedInstance);
        setRetainInstance(true);
        context = getActivity();
        this.view = inflater.inflate(R.layout.pay_fragment, container, false);
        btnSiguienteTicket =this.view.findViewById(R.id.btnSiguienteTicket);
        imageViewFront=this.view.findViewById(R.id.imgFrontId);
        btnSiguienteTicket.setOnClickListener(this);
        imageViewFront.setOnClickListener(this);
        return this.view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.imgFrontId:
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

            btnSiguienteTicket.reset();
        }
    };

    public void setFrontImage(Bitmap bitmap){
        imageViewFront.setImageBitmap(bitmap);
    }



}