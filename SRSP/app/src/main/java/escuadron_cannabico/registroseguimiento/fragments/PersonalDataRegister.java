package escuadron_cannabico.registroseguimiento.fragments;


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
import android.widget.EditText;
import android.widget.TextView;

import com.libre.escuadroncliente.R;
import com.libre.escuadroncliente.ui.RegisterActivity;
import com.libre.escuadroncliente.ui.util.Network;
import com.unstoppable.submitbuttonview.SubmitButton;

<<<<<<<HEAD

public class PersonalDataRegister  extends Fragment implements  View.OnClickListener ,EditText.OnFocusChangeListener {
=======
import java.util.regex.Pattern;

public class PersonalDataRegister  extends Fragment implements  View.OnClickListener  {
>>>>>>> cd2e9baa86862e10bfb9b43cda77ad615eaa79a1
    private View view;
    private SubmitButton btnSiguiente;
    private EditText edtName, edtMail,edtCelphone;
    private Context context;
    private int netStatus;
    private boolean registerPersonalSuccess=true;
    private CheckBox checkWhats;
    private TextView txtAviso,txtCuenta;
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        super.onSaveInstanceState(savedInstance);
        setRetainInstance(true);
        context=getContext();
        this.view = inflater.inflate(R.layout.personal_data_fragment,container,false);
        btnSiguiente=this.view.findViewById(R.id.btnSiguiente);
        edtName=this.view.findViewById(R.id.edtNombre);
        edtName.setOnFocusChangeListener(this);
        edtMail=this.view.findViewById(R.id.edtCorreo);
        edtMail.setOnFocusChangeListener(this);
        edtCelphone=this.view.findViewById(R.id.edtCelular);
        edtCelphone.setOnFocusChangeListener(this);
        checkWhats=this.view.findViewById(R.id.checkWats);
        txtAviso=this.view.findViewById(R.id.txtAviso);
        txtCuenta=this.view.findViewById(R.id.txtCuenta);
        txtAviso.setOnClickListener(this);
        txtCuenta.setOnClickListener(this);
        btnSiguiente.setOnClickListener(this);
        btnSiguiente.setOnResultEndListener(finishListener);


        edtMail .addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {

                String email= edtMail.getText().toString();
                if (email.matches(emailPattern) && s.length() > 0)
                {
                    edtMail.setBackgroundResource(R.drawable.editext_line);
                }
                else
                {
                    edtMail.setBackgroundResource(R.drawable.editext_line_error);
                }
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // other stuffs
            }
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // other stuffs
            }
        });
        edtCelphone .addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {

                String phone= edtCelphone.getText().toString();
                if (isValidPhone(phone))
                {
                    edtCelphone.setBackgroundResource(R.drawable.editext_line);
                }
                else
                {
                    edtMail.setBackgroundResource(R.drawable.editext_line_error);
                }
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // other stuffs
            }
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // other stuffs
            }
        });
        return  this.view;
    }
    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onClick(View v) {

        netStatus= Network.getConnectivityStatus(context);
        switch (v.getId()) {

            case R.id.btnSiguiente:
                    if(validateForm()) {

                        ((RegisterActivity) context).newMember.name=edtName.getText().toString();

                        ((RegisterActivity) context).newMember.mail=edtMail.getText().toString();
                        ((RegisterActivity) context).newMember.phone=edtCelphone.getText().toString();
                        ((RegisterActivity) context).newMember.whats=checkWhats.isChecked();

                        btnSiguiente.doResult(true);


                    }
                    break;
            case R.id.txtAviso:

                ((RegisterActivity) context).newMember.privacy=true;
                ((RegisterActivity) context).showPrivacy();

                break;
            case R.id.txtCuenta:
                ((RegisterActivity) context).startLogin();
                break;

        }
    }
    SubmitButton.OnResultEndListener finishListener=new SubmitButton.OnResultEndListener() {
        @Override
        public void onResultEnd() {

            if(registerPersonalSuccess) {
                ((RegisterActivity) context).paginaSiguiente(1);
                btnSiguiente.reset();
            }
        }
    };

    private boolean validateForm() {
        if (TextUtils.isEmpty(edtName.getText().toString())) {
            edtName.requestFocus();
            btnSiguiente.reset();
            registerPersonalSuccess=false;
            setErrorMessageForm("Ingresa tu Nombre completo");
            return false;
        } else if (TextUtils.isEmpty(edtMail.getText().toString())) {
            edtMail.requestFocus();
            btnSiguiente.reset();
            registerPersonalSuccess=false;
            setErrorMessageForm("Ingresa un Correo electronico");
            return false;
        } else  if (TextUtils.isEmpty(edtCelphone.getText().toString())) {
            edtCelphone .requestFocus();
            btnSiguiente.reset();
            registerPersonalSuccess=false;
            setErrorMessageForm("Ingresa un Teléfono de contacto");
            return false;
        } else if (!((RegisterActivity) context).newMember.privacy) {
            edtCelphone .requestFocus();
            btnSiguiente.reset();
            registerPersonalSuccess=false;
            setErrorMessageForm("Debes Leer nuestro Aviso de Privacidad");
            return false;
        } else {
            //save to object
            registerPersonalSuccess=true;
            return true;
        }
    }
    public void setErrorMessageForm(String message){
        ((RegisterActivity) context).showError(message);
    }

<<<<<<< HEAD
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()){

            case R.id.edtNombre:
                break;
            case R.id.edtCorreo:
                break;
            case R.id.edtCelular:
                break;
        }

=======
    private boolean isValidPhone(String phone)
    {
        boolean check=false;
        if(!Pattern.matches("[a-zA-Z]+", phone))
        {
            if(phone.length() < 6 || phone.length() > 10)
            {
                check = false;

            }
            else
            {
                check = true;

            }
        }
        else
        {
            check=false;
        }
        return check;
>>>>>>> cd2e9baa86862e10bfb9b43cda77ad615eaa79a1
    }
}
