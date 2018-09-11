package com.libre.escuadronpromotor.ui.fragments;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.libre.escuadronpromotor.R;


/**
 * Created by hugo on 8/09/18.
 */

public class DialogUploadFragment extends DialogFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_upload, container, false);
        int style , theme;
        style = DialogFragment.STYLE_NO_TITLE;
        theme = android.R.style.Theme_Holo_Light;
        setStyle(style, theme);
        return v;
    }
}
