package com.libre.escuadroncliente.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.libre.escuadroncliente.R;
import com.libre.escuadroncliente.ui.RegisterActivity;
import com.unstoppable.submitbuttonview.SubmitButton;

import java.util.Calendar;

public class HealtDataRegister  extends Fragment implements  View.OnClickListener  {
    private View view;
    private Context context;
    private SubmitButton btnSiguienteHealt;
    private  EditText edtFechaNacimiento,edtPeso;
    private CheckBox chckCronic,chckLudic;
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
        chckLudic=this.view.findViewById(R.id.checkHomoepatic);
        chckLudic.setOnCheckedChangeListener(listenerLudic);
        chckCronic.setOnCheckedChangeListener(listenerCronic);

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

            String inDate=edtFechaNacimiento.getText().toString();
            if(inDate.length()>=6){
                String [] date=edtFechaNacimiento.getText().toString().split("/");
                int y=Integer.valueOf(date[2]);
                if(y <= 2001)  {
                    edtFechaNacimiento.setBackgroundResource(R.drawable.editext_line);
                }
                else
                {
                    setErrorMessageForm("Necesitas ser Mayor de Edad");
                    edtFechaNacimiento.setBackgroundResource(R.drawable.editext_line_error);
                }
            }


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
                    ((RegisterActivity) context).newMember.birthday=edtFechaNacimiento.getText().toString();
                    ((RegisterActivity) context).newMember.weigth=Integer.parseInt(edtPeso.getText().toString());
                    ((RegisterActivity) context).newMember.suffering=chckCronic.isChecked();
                    ((RegisterActivity) context).newMember.ludic=chckLudic.isChecked();

                    if(radioButtonF.isChecked()){
                        gender=2;
                    }
                    if(radioButtonM.isChecked()){
                        gender=1;
                    }
                    ((RegisterActivity) context).newMember.gender=gender;

                    btnSiguienteHealt.setOnResultEndListener(finishListenerHealt);
                    btnSiguienteHealt.doResult(true);
                    registerHealtSuccess=true;

                }

        }
    }
    SubmitButton.OnResultEndListener finishListenerHealt=new SubmitButton.OnResultEndListener() {
        @Override
        public void onResultEnd() {
            ((RegisterActivity) context).paginaSiguiente(2);

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
        ((RegisterActivity) context).showError(message);
    }

    CompoundButton.OnCheckedChangeListener listenerLudic=new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            ((RegisterActivity) context).newMember.ludic=isChecked;
        }
    };

    CompoundButton.OnCheckedChangeListener listenerCronic=new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            ((RegisterActivity) context).newMember.suffering=isChecked;


        }
    };
}
