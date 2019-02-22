package escuadron_cannabico.registroseguimiento.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.libre.escuadroncliente.R;
import com.libre.escuadroncliente.ui.RegisterActivity;
import com.unstoppable.submitbuttonview.SubmitButton;

public class DigitalCodeRegister extends Fragment implements  View.OnClickListener  {
    private View view;
    private Context context;
    private  SubmitButton btnTerminar,btnRegistrar;
    private ImageView  imgCode;
    private Bitmap code;
    private TextView txtCode;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        super.onSaveInstanceState(savedInstance);
        setRetainInstance(true);
        context=getContext();
        this.view = inflater.inflate(R.layout.digital_code_fragment,container,false);
        btnTerminar=this.view.findViewById(R.id.btnTerminar);
        btnRegistrar=this.view.findViewById(R.id.btnRegistrar);
        imgCode=this.view.findViewById(R.id.imgCode);
        txtCode=this.view.findViewById(R.id.txtCode);
        btnTerminar.setOnClickListener(this);
        btnRegistrar.setOnClickListener(this);
        //((MainActivity) context).saveToServer();
        return  this.view;
    }
    @Override
    public void onResume(){
        super.onResume();


    }


    public void setCode(Bitmap bitmap){
        code=bitmap;
        btnRegistrar.doResult(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnTerminar:
                btnTerminar.doResult(true);
                break;
            case R.id.btnRegistrar:

                btnRegistrar.setOnResultEndListener(finishListenerRegister);
                ((RegisterActivity) context).registerUser();
                break;
        }


    }

    SubmitButton.OnResultEndListener finishListenerRegister=new SubmitButton.OnResultEndListener() {
        @Override
        public void onResultEnd() {

            imgCode.setImageBitmap(code);
            txtCode.setVisibility(View.VISIBLE);
            btnRegistrar.setVisibility(View.GONE);
            btnTerminar.setVisibility(View.VISIBLE);
            btnTerminar.setOnResultEndListener(finishListener);

        }
    };
    SubmitButton.OnResultEndListener finishListener=new SubmitButton.OnResultEndListener() {
        @Override
        public void onResultEnd() {
            ((RegisterActivity)context).launchMarket();


        }
    };
    public void setErrorButton(){
        btnRegistrar.reset();
    }
}
