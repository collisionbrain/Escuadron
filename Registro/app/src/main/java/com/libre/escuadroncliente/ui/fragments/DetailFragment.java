package com.libre.escuadroncliente.ui.fragments;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
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

import com.libre.escuadroncliente.R;
import com.libre.escuadroncliente.ui.MarketActivity;
import com.libre.escuadroncliente.ui.pojos.CartOrder;
import com.libre.escuadroncliente.ui.pojos.Product;
import com.libre.escuadroncliente.ui.util.Data;
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
    private TextView txtDesc,txtPrice,txtDetail,txtTecnical,txtCounter;
    private SubmitButton addButton;
    private Product product;
    private int counter=0;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        super.onSaveInstanceState(savedInstance);
        setRetainInstance(true);
        context=getActivity();
        product = (Product)this.getArguments().getSerializable("product");
        this.view = inflater.inflate(R.layout.detail_fragment,container,false);
        rlyPanel=this.view.findViewById(R.id.rlPanel);
        btnPlus=this.view.findViewById(R.id.btnPlus);
        btnMinus= this.view.findViewById(R.id.btnMinus);
        backGround=this.view.findViewById(R.id.bgrImage);
        txtDesc=this.view.findViewById(R.id.txtDesc);
        //txtDetail=this.view.findViewById(R.id.txtDetail);
        txtTecnical=this.view.findViewById(R.id.txtTecnical);
        txtPrice=this.view.findViewById(R.id.txtPrice);
        txtCounter=this.view.findViewById(R.id.txtCounter);
        addButton=this.view.findViewById(R.id.addButton);
        btnPlus.setOnClickListener(this);
        btnMinus.setOnClickListener(this);
        addButton.setOnClickListener(this);
        addButton.setOnResultEndListener(addFinishListener);
        Log.e("#######",""+product.name);
        Bitmap bitmap=Data.base64ToBitmap(product.image);
        Drawable background = new BitmapDrawable(getResources(), bitmap);
        backGround.setBackground(background);
        txtDesc.setText(product.name);
        txtPrice.setText(product.price);
        counter=checkProductList(product.id);
        txtCounter.setText("" + counter);
        txtDetail.setText(product.presentation);
        txtTecnical.setText(product.ingredients.toString().replace("|","\n"));
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
               counter=counter+1;
               txtCounter.setText(""+counter);
               break;

           case  R.id.addButton :
               product.count=Integer.parseInt(txtCounter.getText().toString());
               if(product.count>0){
                   ((MarketActivity)context).addProduct(product);
                   counter=0;
               }
               addButton.doResult(true);

               break;
       }

    }

    SubmitButton.OnResultEndListener addFinishListener=new SubmitButton.OnResultEndListener() {
        @Override
        public void onResultEnd() {
            ((MarketActivity)context).onBackPressed();
        }
    };
    private int checkProductList(String id){
        int res=0;
        for (CartOrder product: ((MarketActivity)context).productList  ) {
            if(product.id.equals(id)){
                res=product.count;

            }
        }

        return res;
    }
}
