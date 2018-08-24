package com.libre.registro.ui.fragments;

import android.app.Fragment;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.libre.registro.R;
import com.libre.registro.ui.MainActivity;
import com.libre.registro.ui.MarketActivity;
import com.libre.registro.ui.pojos.Product;
import com.unstoppable.submitbuttonview.SubmitButton;

/**
 * Created by hugo on 20/08/18.
 */

public class DetailFragment extends Fragment   implements View.OnClickListener{
    private View view;
    private Context context;
    private Button btnPlus,btnMinus;
    private RelativeLayout rlyPanel;
    private ImageView backGround;
    private TextView txtDesc,txtPrice,txtCounter;
    private SubmitButton addButton;
    private Product product;
    private int counter=0;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        super.onSaveInstanceState(savedInstance);
        setRetainInstance(true);
        context=getActivity();
        int productItem = this.getArguments().getInt("productItem");
        this.view = inflater.inflate(R.layout.detail_fragment,container,false);
        rlyPanel=(RelativeLayout) this.view.findViewById(R.id.rlPanel);
        btnPlus=(Button) this.view.findViewById(R.id.btnPlus);
        btnMinus=(Button) this.view.findViewById(R.id.btnMinus);
        backGround=(ImageView) this.view.findViewById(R.id.bgrImage);
        txtDesc=(TextView) this.view.findViewById(R.id.txtDesc);
        txtPrice=(TextView) this.view.findViewById(R.id.txtPrice);
        txtCounter=(TextView) this.view.findViewById(R.id.txtCounter);
        addButton=(SubmitButton) this.view.findViewById(R.id.addButton);
        btnPlus.setOnClickListener(this);
        btnMinus.setOnClickListener(this);
        Log.e("#######",""+productItem);
        switch (productItem){
            case 0:
                backGround.setBackgroundResource(R.drawable.cbd);
                txtDesc.setText("");
                txtPrice.setText("600.00");
                product=new Product();
                product.id=0;
                product.name="CBD";
                product.price=600;
                break;
            case 1:
                backGround.setBackgroundResource(R.drawable.muffin);
                txtDesc.setText("");
                txtPrice.setText("25.00");
                product=new Product();
                product.id=0;
                product.name="Muffin";
                product.price=25;
                break;
            case 2:
                backGround.setBackgroundResource(R.drawable.cookies);
                txtDesc.setText("");
                txtPrice.setText("25.00");
                product=new Product();
                product.id=0;
                product.name="Galletas";
                product.price=25;
                break;
            case 3:
                backGround.setBackgroundResource(R.drawable.bud);
                txtDesc.setText("");
                txtPrice.setText("100.00");
                product=new Product();
                product.id=0;
                product.name="Cogoyos";
                product.price=100;
                break;
            case 4:
                backGround.setBackgroundResource(R.drawable.pomada);
                txtDesc.setText("");
                txtPrice.setText("120.00");
                product=new Product();
                product.id=0;
                product.name="Pomada";
                product.price=120;
                break;
        }

        return  this.view;
    }
    @Override
    public void onResume(){
        super.onResume();

    }
    @Override
    public void onClick(View v) {
       switch (v.getId()){

           case  R.id.btnMinus :
               Log.e("########3","MiNUS");
               if(counter>1) {
                   counter = counter - 1;
                   txtCounter.setText("" + counter);
               }else{
                   counter = 0;
                   txtCounter.setText("" + counter);
               }
               break;
           case  R.id.btnPlus :
               Log.e("########3","MAS");
               counter=counter+1;

               txtCounter.setText(""+counter);
               break;

           case  R.id.addButton :
               product.count=counter;
               ((MarketActivity)context).addProduct(product);

               break;
       }

    }
}
