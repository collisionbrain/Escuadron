package com.libre.escuadroncliente.ui.fragments;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;

import com.jaredrummler.materialspinner.MaterialSpinner;
import com.libre.escuadroncliente.R;
import com.libre.escuadroncliente.ui.RegisterActivity;
import com.unstoppable.submitbuttonview.SubmitButton;

import java.util.List;

/**
 * Created by hugo on 17/08/18.
 */

public class AdressFragment extends Fragment implements  View.OnClickListener {
    private View view;
    List<String> listaEstados;
    List<String> listaMunicipios;
    private EditText edtCodigo;
    private Context context;
    private SubmitButton btnSiguienteRegion;
    private Resources resources;
    private MaterialSpinner spinnerEstados,spinnerMunicipios;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        super.onSaveInstanceState(savedInstance);
        setRetainInstance(true);
        context = getContext();
        this.view = inflater.inflate(R.layout.pcronic_data_fragment, container, false);
        btnSiguienteRegion =this.view.findViewById(R.id.btnSiguienteCronic);
        edtCodigo=this.view.findViewById(R.id.edtCodigo);
        resources = getResources();
        spinnerEstados = this.view.findViewById(R.id.spinnerEstados);
        spinnerMunicipios= this.view.findViewById(R.id.spinnerMunicipios);


/*
        spinner.setItems("Padecimiento Cronico","Cancer","Parkinson","Diabetes", "Asma", "Neuropatias", "Artritis","Otro");
        spinnerLudic.setItems("Tratamiento Homeopatico","Estres", "Ansiedad", "Depresion","Otro");
*/
        spinnerEstados.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                Log.e("###########", "Clicked " + item);

            }
        });
        spinnerMunicipios.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
               Log.e("###########", "Clicked " + item);

            }
        });
        btnSiguienteRegion.setOnClickListener(this);

        return this.view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnSiguienteRegion:

                btnSiguienteRegion.setOnResultEndListener(finishListenerCronic);
                btnSiguienteRegion.doResult(true);
        }


    } ;
    SubmitButton.OnResultEndListener finishListenerCronic = new SubmitButton.OnResultEndListener() {
        @Override
        public void onResultEnd() {
            ((RegisterActivity) context).paginaSiguiente(3);

            btnSiguienteRegion.reset();
        }
    };

    AdapterView.OnItemSelectedListener itemSelected = new AdapterView.OnItemSelectedListener() {
        public void onNothingSelected(AdapterView<?> parent) {

        }

        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


        }


    };

    public void setErrorMessage(String message) {
        ((RegisterActivity) context).messageError.setText(message);
        ((RegisterActivity) context).dialogError.show();
    }

}