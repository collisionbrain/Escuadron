package com.libre.registro.ui.fragments;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;

import com.jaredrummler.materialspinner.MaterialSpinner;
import com.libre.registro.R;
import com.libre.registro.ui.MainActivity;
import com.unstoppable.submitbuttonview.SubmitButton;
import java.util.List;

/**
 * Created by hugo on 17/08/18.
 */

public class CronicSuffering extends Fragment implements  View.OnClickListener {
    private View view;
    List<String> listaPadecimientos;
    private String condition;
    private Context context;
    private SubmitButton btnSiguienteCronic;
    private Resources resources;
    private EditText edtOtro;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        super.onSaveInstanceState(savedInstance);
        setRetainInstance(true);
        context = getContext();
        this.view = inflater.inflate(R.layout.pcronic_data_fragment, container, false);
        btnSiguienteCronic = (SubmitButton) this.view.findViewById(R.id.btnSiguienteCronic);
        edtOtro= (EditText) this.view.findViewById(R.id.edtOtro);
        resources = getResources();
        MaterialSpinner spinner = (MaterialSpinner) this.view.findViewById(R.id.spinner);
        spinner.setItems("Seleciona tu padecimiento","Diabetes", "Asma", "Estres", "Neuropatias", "Artritis","Otro");
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
               Log.e("###########", "Clicked " + item);
                if(item.equals("Otro")){
                    edtOtro.setVisibility(View.VISIBLE);
                }else{
                    condition=item;
                }
            }
        });
        btnSiguienteCronic.setOnClickListener(this);

        return this.view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnSiguienteCronic:
                ((MainActivity) context).newMember.condition=condition;
                ((MainActivity) context).newMember.extra=edtOtro.getText().toString();
                btnSiguienteCronic.setOnResultEndListener(finishListenerCronic);
                btnSiguienteCronic.doResult(true);
        }


    } ;
    SubmitButton.OnResultEndListener finishListenerCronic = new SubmitButton.OnResultEndListener() {
        @Override
        public void onResultEnd() {
            ((MainActivity) context).paginaSiguiente(2);

            btnSiguienteCronic.reset();
        }
    };

    AdapterView.OnItemSelectedListener itemSelected = new AdapterView.OnItemSelectedListener() {
        public void onNothingSelected(AdapterView<?> parent) {

        }

        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            String seleted = listaPadecimientos.get(position);
            Log.e("##########", "" + seleted);

        }

        ;

        public void setErrorMessage(String message) {
            ((MainActivity) context).messageError.setText(message);
            ((MainActivity) context).dialogError.show();
        }
    };
}