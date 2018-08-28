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
        rlyPanel=this.view.findViewById(R.id.rlPanel);
        btnPlus=this.view.findViewById(R.id.btnPlus);
        btnMinus= this.view.findViewById(R.id.btnMinus);
        backGround=this.view.findViewById(R.id.bgrImage);
        txtDesc=this.view.findViewById(R.id.txtDesc);
        txtPrice=this.view.findViewById(R.id.txtPrice);
        txtCounter=this.view.findViewById(R.id.txtCounter);
        addButton=this.view.findViewById(R.id.addButton);
        btnPlus.setOnClickListener(this);
        btnMinus.setOnClickListener(this);
        addButton.setOnClickListener(this);
        addButton.setOnResultEndListener(addFinishListener);
        Log.e("#######",""+productItem);
        switch (productItem){
            case 0:

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        backGround.setBackgroundResource(R.drawable.cbd);
                        txtDesc.setText("xxxxxxxxxxx");
                        txtPrice.setText("600.00");
                    }
                });
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
                product.id=1;
                product.name="Muffin";
                product.price=25;
                break;
            case 2:
                backGround.setBackgroundResource(R.drawable.cookies);
                txtDesc.setText("");
                txtPrice.setText("25.00");
                product=new Product();
                product.id=2;
                product.name="Galletas";
                product.price=25;
                break;
            case 3:
                backGround.setBackgroundResource(R.drawable.bud);
                txtDesc.setText("");
                txtPrice.setText("100.00");
                product=new Product();
                product.id=3;
                product.name="Cogoyos";
                product.price=100;
                break;
            case 4:
                backGround.setBackgroundResource(R.drawable.pomada);
                txtDesc.setText("");
                txtPrice.setText("120.00");
                product=new Product();
                product.id=4;
                product.name="Pomada";
                product.price=120;
                break;
        }
        counter=checkProductList(product.id);
        txtCounter.setText("" + counter);
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
    private int checkProductList(int id){
        int res=0;
        for (Product product: ((MarketActivity)context).productList  ) {
            if(product.id==id){
                res=product.count;

            }
        }

        return res;
    }
}
