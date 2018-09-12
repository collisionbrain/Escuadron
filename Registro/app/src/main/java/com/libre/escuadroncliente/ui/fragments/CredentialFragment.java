package com.libre.escuadroncliente.ui.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.libre.escuadroncliente.R;
import com.libre.escuadroncliente.ui.RegisterActivity;
import com.unstoppable.submitbuttonview.SubmitButton;

import java.util.List;

/**
 * Created by hugo on 17/08/18.
 */

public class CredentialFragment extends Fragment implements  View.OnClickListener {
    private View view;
    List<String> listaPadecimientos;
    private String condition;
    private Context context;
    private SubmitButton btnSiguienteCamera;
    private ImageView imageViewFront,imageViewBack;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        super.onSaveInstanceState(savedInstance);
        setRetainInstance(true);
        context = getContext();
        this.view = inflater.inflate(R.layout.credential_fragment, container, false);
        btnSiguienteCamera =this.view.findViewById(R.id.btnSiguienteCamera);
        imageViewFront=this.view.findViewById(R.id.imgFrontId);
        imageViewBack=this.view.findViewById(R.id.imgBackId);
        btnSiguienteCamera.setOnClickListener(this);
        imageViewFront.setOnClickListener(this);
        imageViewBack.setOnClickListener(this);


        return this.view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.imgFrontId:
                ((RegisterActivity) context).takePicture(0);
                break;
            case R.id.imgBackId:
                ((RegisterActivity) context).takePicture(1);
                break;
            case R.id.btnSiguienteCamera:
                btnSiguienteCamera.setOnResultEndListener(finishListenerCamera);

                btnSiguienteCamera.doResult(true);
                break;
        }


    } ;
    SubmitButton.OnResultEndListener finishListenerCamera = new SubmitButton.OnResultEndListener() {
        @Override
        public void onResultEnd() {
            ((RegisterActivity) context).paginaSiguiente(6);
            btnSiguienteCamera.reset();
        }
    };

    public void setFrontImage(Bitmap bitmap){

        imageViewFront.setImageBitmap(bitmap);
        imageViewBack.setVisibility(View.VISIBLE);
    }

    public void setBackImage(Bitmap bitmap){

        imageViewBack.setImageBitmap(bitmap);

    }
}