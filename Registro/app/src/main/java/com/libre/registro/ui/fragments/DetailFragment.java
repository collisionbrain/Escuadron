package com.libre.registro.ui.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.libre.registro.R;
import com.libre.registro.ui.MainActivity;
import com.unstoppable.submitbuttonview.SubmitButton;

/**
 * Created by hugo on 20/08/18.
 */

public class DetailFragment extends Fragment   implements View.OnClickListener{
    private View view;
    private Context context;
    RelativeLayout btnPlus,btnMinus;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        super.onSaveInstanceState(savedInstance);
        setRetainInstance(true);
        context=getActivity();
        int productItem = this.getArguments().getInt("productItem");
        this.view = inflater.inflate(R.layout.detail_fragment,container,false);
        btnPlus=(RelativeLayout) this.view.findViewById(R.id.rlyPlus);
        btnMinus=(RelativeLayout) this.view.findViewById(R.id.rlyMinus);
        btnPlus.setOnClickListener(this);
        btnMinus.setOnClickListener(this);
        Log.e("#######",""+productItem);
        return  this.view;
    }
    @Override
    public void onResume(){
        super.onResume();

    }
    @Override
    public void onClick(View v) {
       switch (v.getId()){

           case  R.id.rlyMinus :
               Log.e("########3","MENUS");
               break;
           case  R.id.rlyPlus :
               Log.e("########3","MAS");
               break;
       }

    }
}
