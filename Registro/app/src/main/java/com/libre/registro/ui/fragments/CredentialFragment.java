package com.libre.registro.ui.fragments;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;

import com.jaredrummler.materialspinner.MaterialSpinner;
import com.libre.registro.R;
import com.libre.registro.ui.MainActivity;
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


        return this.view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.imgFrontId:
                ((MainActivity) context).takePicture(0);
                break;
            case R.id.imgBackId:
                ((MainActivity) context).takePicture(1);
                break;
            case R.id.btnSiguienteCronic:
                ((MainActivity) context).newMember.condition=condition;
                btnSiguienteCamera.setOnResultEndListener(finishListenerCamera);
                btnSiguienteCamera.doResult(true);
                break;
        }


    } ;
    SubmitButton.OnResultEndListener finishListenerCamera = new SubmitButton.OnResultEndListener() {
        @Override
        public void onResultEnd() {
            ((MainActivity) context).paginaSiguiente(2);

            btnSiguienteCamera.reset();
        }
    };

    public void setFrontImage(Bitmap bitmap){
        imageViewFront.setImageBitmap(bitmap);
    }

    public void setBackImage(Bitmap bitmap){
        imageViewBack.setImageBitmap(bitmap);
    }
}