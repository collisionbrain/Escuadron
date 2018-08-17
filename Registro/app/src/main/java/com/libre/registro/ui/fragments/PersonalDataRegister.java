package com.libre.registro.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.libre.registro.R;

public class PersonalDataRegister  extends Fragment {
    private View view;
    private Context context;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        super.onSaveInstanceState(savedInstance);
        setRetainInstance(true);
        this.view = inflater.inflate(R.layout.personal_data_fragment,container,false);
        return  this.view;
    }
}
