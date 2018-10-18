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
import android.widget.TextView;

import com.libre.escuadroncliente.R;
import com.libre.escuadroncliente.ui.MarketActivity;
import com.libre.escuadroncliente.ui.pojos.Quiz;
import com.libre.escuadroncliente.ui.storage.PreferencesStorage;
import com.libre.escuadroncliente.ui.util.Data;
import com.unstoppable.submitbuttonview.SubmitButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by hugo on 17/08/18.
 */

public class PayFragment extends Fragment implements  View.OnClickListener {
    private View view;

    private Context context;
    private SubmitButton btnSiguienteTicket;
    private ImageView imgTicket;
    private TextView totalToPay,txtAccount,txtBank;
    private PreferencesStorage prefs;
    private String count_one,count_tow,count_three,count_four;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        super.onSaveInstanceState(savedInstance);
        setRetainInstance(true);
        context = getActivity();
        prefs=new PreferencesStorage(context);
        count_one=prefs.loadData("PAY_ACCOUNT_ONE");
        count_tow=prefs.loadData("PAY_ACCOUNT_TWO");
        count_three=prefs.loadData("PAY_ACCOUNT_THREE");
        count_four=prefs.loadData("PAY_ACCOUNT_FOUR");



        this.view = inflater.inflate(R.layout.pay_fragment, container, false);
        btnSiguienteTicket =this.view.findViewById(R.id.btnSiguienteTicket);
        totalToPay=this.view.findViewById(R.id.totalToPay);
        imgTicket=this.view.findViewById(R.id.imgTicket);
        txtAccount=this.view.findViewById(R.id.numAccount);
        txtBank=this.view.findViewById(R.id.bank);
        String b64=((MarketActivity)context).order.ticket;
        String total=((MarketActivity)context).toPayProducto()+".00";
        if(b64!=null   ){
            if(b64.length()>0) {
                this.setFrontImage();
            }
        }
        txtAccount.setText(total);
        totalToPay.setText("");
        txtBank.setText("");

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