package com.libre.registro.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import com.libre.registro.R;
import com.libre.registro.ui.MainActivity;
import com.unstoppable.submitbuttonview.SubmitButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class HealtDataRegister  extends Fragment implements  View.OnClickListener  {
    private View view;
    private Context context;
    private SubmitButton btnSiguienteHealt;
    private  EditText edtFechaNacimiento,edtPeso;
    private CheckBox chckCronic;
    private  boolean registerHealtSuccess;
    private int gender;
    private RadioButton radioButtonM,radioButtonF;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        super.onSaveInstanceState(savedInstance);
        setRetainInstance(true);
        context=getContext();
        this.view = inflater.inflate(R.layout.healt_data_fragment,container,false);
        btnSiguienteHealt=this.view.findViewById(R.id.btnSiguienteHealt);
        edtFechaNacimiento=this.view.findViewById(R.id.edtFecha);
        edtPeso=this.view.findViewById(R.id.edtPeso);
        chckCronic=this.view.findViewById(R.id.checkCronic);
        edtFechaNacimiento.addTextChangedListener(txtWatcherListener);
        btnSiguienteHealt.setOnClickListener(this);
        radioButtonM=this.view.findViewById(R.id.rdMas);
        radioButtonF=this.view.findViewById(R.id.rdFem);
        return  this.view;
    }
    TextWatcher txtWatcherListener = new TextWatcher(){
        private String current = "";
        private String ddmmyyyy = "DDMMAAAA";
        private Calendar cal = Calendar.getInstance();
        @Override
        public void afterTextChanged(Editable arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onTextChanged(CharSequence s, int a, int b, int c) {
            if (!s.toString().equals(current)) {
                String clean = s.toString().replaceAll("[^\\d.]", "");
                String cleanC = current.replaceAll("[^\\d.]", "");

                int cl = clean.length();
                int sel = cl;
                for (int i = 2; i <= cl && i < 6; i += 2) {
                    sel++;
                }
                //Fix for pressing delete next to a forward slash
                if (clean.equals(cleanC)) sel--;

                if (clean.length() < 8){
                    clean = clean + ddmmyyyy.substring(clean.length());
                }else{
                    //This part makes sure that when we finish entering numbers
                    //the date is correct, fixing it otherwise
                    int day  = Integer.parseInt(clean.substring(0,2));
                    int mon  = Integer.parseInt(clean.substring(2,4));
                    int year = Integer.parseInt(clean.substring(4,8));

                    if(mon > 12) mon = 12;
                    cal.set(Calendar.MONTH, mon-1);
                    year = (year<1900)?1900:(year>2100)?2100:year;
                    cal.set(Calendar.YEAR, year);
                    // ^ first set year for the line below to work correctly
                    //with leap years - otherwise, date e.g. 29/02/2012
                    //would be automatically corrected to 28/02/2012

                    day = (day > cal.getActualMaximum(Calendar.DATE))? cal.getActualMaximum(Calendar.DATE):day;
                    clean = String.format("%02d%02d%02d",day, mon, year);
                }

                clean = String.format("%s/%s/%s", clean.substring(0, 2),
                        clean.substring(2, 4),
                        clean.substring(4, 8));

                sel = sel < 0 ? 0 : sel;
                current = clean;
                edtFechaNacimiento.setText(current);
                edtFechaNacimiento.setSelection(sel < current.length() ? sel : current.length());
            }


        }};
    @Override
    public void onClick(View v) {


        switch (v.getId()) {

            case R.id.btnSiguienteHealt:

                if(validateForm()) {
                    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                    try {
                        Date date = format.parse(edtFechaNacimiento.getText().toString());
                        ((MainActivity) context).newMember.birthday=date;
                        System.out.println(date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    ((MainActivity) context).newMember.weigth=Integer.parseInt(edtPeso.getText().toString());
                    ((MainActivity) context).newMember.suffering=chckCronic.isChecked();

                    if(radioButtonF.isChecked()){
                        gender=2;
                    }
                    if(radioButtonM.isChecked()){
                        gender=1;
                    }
                    ((MainActivity) context).newMember.gender=gender;

                    btnSiguienteHealt.setOnResultEndListener(finishListenerHealt);
                    btnSiguienteHealt.doResult(true);
                    registerHealtSuccess=true;

                }

        }
    }
    SubmitButton.OnResultEndListener finishListenerHealt=new SubmitButton.OnResultEndListener() {
        @Override
        public void onResultEnd() {
            if(chckCronic.isChecked() ){
                ((MainActivity) context).paginaSiguiente(4);
            }else{

                ((MainActivity) context).paginaSiguiente(3);
            }

            btnSiguienteHealt.reset();
        }
    };

    private boolean validateForm() {
        if (TextUtils.isEmpty(edtFechaNacimiento.getText().toString())) {
            edtFechaNacimiento.requestFocus();
            btnSiguienteHealt.reset();
            registerHealtSuccess=false;
            setErrorMessageForm("Indica tu Fecha de Nacimiento");
            return false;
        } else if (TextUtils.isEmpty(edtPeso.getText().toString())) {
            edtPeso.requestFocus();
            btnSiguienteHealt.reset();
            registerHealtSuccess=false;
            setErrorMessageForm("Indica tu Peso");
            return false;
        }else if (!radioButtonM.isChecked() && !radioButtonF.isChecked()) {
            btnSiguienteHealt.reset();
            registerHealtSuccess=false;
            setErrorMessageForm("Indica tu Género");
            return false;
        } else {

            //save to object
            return true;
        }
    }
    public void setErrorMessageForm(String message){
        ((MainActivity) context).showError(message);
    }
}
